# pip install flask torch scikit-learn bs4 requests

# ---------- Bibliotecas ----------
import torch
import torch.nn as nn
import torch.optim as optim
from flask import Flask, request, jsonify
from sklearn.feature_extraction.text import CountVectorizer
from bs4 import BeautifulSoup
import requests
from urllib.parse import urlparse

# ---------- Web Scraping (Beautiful Soup) ----------
def get_visible_text_from_url(url):
    try:
        headers = {"User-Agent": "Mozilla/5.0"}
        response = requests.get(url, headers=headers, timeout=10)
        response.raise_for_status()
        soup = BeautifulSoup(response.text, 'html.parser')
        for tag in soup(['script', 'style', 'noscript']):
            tag.decompose()
        text = soup.get_text(separator=' ', strip=True)
        return ' '.join(text.split())
    except Exception as e:
        return f"[Erro]: {e}"

# ---------- Mini Dataset ----------
texts = [
    # Conteudo +18 (valor 1)
    "livecam",
    "sites de conteúdo adulto para maiores de 18 anos",
    "mulheres solteiras esperando por você agora",
    "XVideos",
    "XVIDEOS",
    "pornhub",
    "Gay Porn",
    "Porn",
    "Porno",
    "NOVINHA",
    "Teen videos",

    # Conteudo livre (valor 0)
    "como fazer brigadeiro de colher",
    "programação para iniciantes",
    "tecnologia e tendências em inteligência artificial",
    "notícias sobre a política internacional atual",
    "dicas de jardinagem para iniciantes",
    "roteiro de viagem pelo sul do Brasil",
    "notícias de famosos e celebridades",
    "mercado financeiro hoje",
    "hábitos para alcançar o shape ideal",
    "guia completo do whey protein",
    "resultados de futebol ao vivo",

]
labels = [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
          0, 0, 0, 0, 0, 0, 0, 0, 0, 0,]  # 1: suspeito, 0: normal

# ---------- Vetorizacao ----------
vectorizer = CountVectorizer(binary=True)
X = vectorizer.fit_transform(texts).toarray()
y = torch.tensor(labels, dtype=torch.float32).view(-1, 1)

# ---------- Modelo ----------
class SimpleClassifier(nn.Module):
    def __init__(self, input_dim):
        super().__init__()
        self.fc = nn.Sequential(
            nn.Linear(input_dim, 16),
            nn.ReLU(),
            nn.Linear(16, 1),
            nn.Sigmoid()
        )

    def forward(self, x):
        return self.fc(x)

model = SimpleClassifier(X.shape[1])
criterion = nn.BCELoss()
optimizer = optim.Adam(model.parameters(), lr=0.01)

# ---------- Treinamento (nos) ----------
X_tensor = torch.tensor(X, dtype=torch.float32)
for epoch in range(100):
    optimizer.zero_grad()
    outputs = model(X_tensor)
    loss = criterion(outputs, y)
    loss.backward()
    optimizer.step()

# ---------- Inferencia (predicao) ----------
def predict_risk(text):
    x = vectorizer.transform([text]).toarray()
    x_tensor = torch.tensor(x, dtype=torch.float32)
    with torch.no_grad():
        prob = model(x_tensor).item()
    return prob

# ---------- Flask API (torna um servico) ----------
app = Flask(__name__)

@app.route('/predict', methods=['POST'])
def predict():
    data = request.get_json()
    url = data.get("url")

    if not url:
        return jsonify({"error": "Campo 'url' ausente"}), 400

    if not url.startswith("http"):
        url = "https://" + url

    print(f"Analisando URL: {urlparse(url).netloc}")
    content = get_visible_text_from_url(url)

    if content.startswith("[Erro]"):
        return jsonify({"error": content}), 500

    risk_score = predict_risk(content)
    return jsonify({
        "url": url,
        "risco": round(risk_score, 3)
    })

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5001)
