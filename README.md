# atividade-4-ti2
Uso de ambiente de computação em nuvem e IA, utilizando um modelo de classificação e PostgreSQL. Modelo de Inteligência
Artificial por predição (Machine Learning) para encontrar domínios com conteúdos pornográficos por Web Scrapping em domínios e classificação!

## Arquitetura do Projeto:
```txt
userManager/
├── Dockerfile
├── pom.xml
├── predict_api.py
├── db.properties
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── app/
│       │           ├── Main.java
│       │           ├── controller/
│       │           │   └── RotaController.java
│       │           ├── dao/
│       │           │   └── UsuarioDAO.java
│       │           └── model/
│       │               └── Usuario.java
│       ├── resources/
│       │   ├── static/
│       │   │   ├── css/
│       │   │   │   └── style.css
│       │   │   └── js/
│       │   │       └── background.js
│       │   └── templates/
│       │       └── index.html
├── target/        ← (gerado automaticamente pelo Maven)
```
## Tecnologias do Projeto:
* HTML, CSS e Java Script (Front-end)
* Java, Maven, Spark (Back-end)
* Postgresql (Banco de Dados Relacional)
* Pytorch, TensorBoard, Matplotlib (Inteligência Artificial)
* Beautiful Soup (lib de python para Web Scrapping)
* API RESTFUL (API para Crud e IA em python)

## Comandos de Acesso local
Para as configurações padrões faça os seguintes passos:

```bash
# Baixa o PyTorch no dispositivo
pip install torch torchvision torchaudio --index-url https://download.pytorch.org/whl/cpu

# 2 opcao: ao inves de instalar no dispositivo baixe localmente na imagem Docker criada (menos custoso)
docker build -t flask-pytorch-app .
docker run -p 5001:5001 flask-pytorch-app

# testa o treinamento da IA em um site com conteudo adulto
curl -X POST http://localhost:5001/predict -H "Content-Type: application/json" -d '{"url": "http://xvideos.com"}'
```
Ou use minha automatização dando **chmod +x arquivo.sh** na sua máquina Linux ou MacOS:
```bash
git clone https://github.com/rainbowcrack/ShellKitty
```

## Exemplos de Acesso Público