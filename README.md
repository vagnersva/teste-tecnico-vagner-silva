# Teste técnico Backend - Vagner Silva

## Pré-requisitos
- Docker e Docker Compose
- Java 17
- Maven

## Instruções

### 1. Clonar o repositório
```bash
git clone https://github.com/vagnersva/teste-tecnico-vagner-silva.git
cd teste-tecnico-vagner-silva
```

### 2. Subir o banco de dados
```bash
docker compose up -d
```

### 3. Rodar a aplicação
```bash
mvn quarkus:dev
```

### 4. Acessar a documentação (Swagger UI)
#### Após iniciar a aplicação, acesse a URL abaixo para visualizar a documentação e testar os endpoints:

```bash
http://localhost:8080/q/swagger-ui/
```

### 4. Testando os Endpoints
#### Um arquivo para importação no Postman está disponível no repositório (postman_collection.json).
Basta importar esse arquivo no Postman para testar os endpoints da aplicação.
