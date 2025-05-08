# Escolhe uma imagem base com Python
FROM python:3.10-slim

# Evita prompts de instalação interativa
ENV DEBIAN_FRONTEND=noninteractive

# Instala dependências do sistema
RUN apt-get update && apt-get install -y \
    build-essential \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Cria diretório da aplicação
WORKDIR /app

# Copia os arquivos da aplicação para o container
COPY . /app

# Instala dependências do Python (inclui PyTorch com suporte CPU)
RUN pip install --no-cache-dir \
    torch==2.2.2+cpu \
    torchvision==0.17.2+cpu \
    torchaudio==2.2.2+cpu \
    -f https://download.pytorch.org/whl/torch_stable.html \
    flask \
    scikit-learn \
    beautifulsoup4 \
    requests

# Expõe a porta da API Flask
EXPOSE 5001

# Comando para rodar a API Flask
CMD ["python", "app.py"]
