# pip install bs4
import requests

from bs4 import BeautifulSoup
from urllib.parse import urlparse

def get_visible_text_from_url(url):
    try:
        headers = {
            "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"
        }
        response = requests.get(url, headers=headers, timeout=10)
        response.raise_for_status()  # Lança erro se status != 200

        soup = BeautifulSoup(response.text, 'html.parser')

        # Remove scripts e estilos
        for tag in soup(['script', 'style', 'noscript']):
            tag.decompose()

        # Extrai texto visível
        text = soup.get_text(separator=' ', strip=True)

        # Remove espaços excessivos
        clean_text = ' '.join(text.split())

        return clean_text

    except requests.exceptions.RequestException as e:
        return f"[Erro ao acessar a URL]: {e}"
    except Exception as e:
        return f"[Erro inesperado]: {e}"

if __name__ == '__main__':
    url = input("Insira a URL para análise: ")
    
    if not url.startswith("http"):
        url = "https://" + url

    domain = urlparse(url).netloc
    print(f"\n🔍 Fazendo scraping do site: {domain}\n")

    content = get_visible_text_from_url(url)

    print("📝 Conteúdo extraído:\n")
    print(content[:1500])  # Mostra os primeiros 1500 caracteres

    # Aqui você pode adicionar sua lógica de IA ou blacklist
