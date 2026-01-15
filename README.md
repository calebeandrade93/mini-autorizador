# Mini Autorizador VR
Este projeto é uma API REST para o gerenciamento de cartões de benefícios, permitindo a criação de cartões, 
consulta de saldo e processamento de transações com validações de regras de negócio.

O projeto é baseado em um cenário real, porém simplificado, que por sua vez, foi proposto como desafio pela VR Benefícios,
uma empresa que oferece soluções que vão além dos benefícios tradicionais, ajudando você a criar uma cultura organizacional ainda mais forte e engajadora! =)


## Tecnologias Utilizadas
 - Java 21 & Spring boot 4.0.1
 - Banco de dados relacional: Mysql
 - Docker
 - Spring Data JPA
 - Spring Security (Basic Auth)
 - Jacoco 
 - Swagger/OpenAPI

## Diferenciais Técnicos
Neste desafio, eu apliquei alguns dos conceitos e boas práticas que adqueri durante minha carreira, que são fundamentais para manter a qualidade e segurança da aplicação:
 - <b>Consistência e Concorrência</b>: Utilização de Pessimistc Locking via Query Nativa, para garantir que duas transações simultâneas não causem inconsistências nos dados durante a transação.
 - <b>Segurança</b>: Implementação de Basic Auth com Spring Security, isolamento de credenciais sensíveis utilizando variáveis de ambiente, e criptografia de senhas com BCrypt para proteger os dados dos usuários.
 - <b>Validação Antecipada</b>: Uso de Bean Validation, para garantir que os dados recebidos pela API estejam corretos antes de chegarem na camada de serviço.
 - <b>Boas Práticas de Código</b>: Utilizei conceitos de Clean Code, SOLID e arquitetura de pastas.
 - <b>Cobertura de Testes</b>: Adicionei o Jacoco ao projeto para gerar o relatório de cobertura de testes.
 - <b>Esteira CI</b>: Configurei uma esteira CI no GitHub Actions que roda testes unitários com relatório JaCoCo disponível para download, e protegi a branch main exigindo PRs, simulando um ambiente corporativo.

## API Endpoints
| Endpoint | Método | Descrição | Resposta Esperada | Erros Possíveis                                                                                     |
|----------|--------|-----------|-------------------|-----------------------------------------------------------------------------------------------------|
| /cartoes | POST | Cria um novo cartão com saldo inicial de R$500.00 | 201 Created com detalhes do cartão | 422 Unprocessable Entity (cartão já existe), 401 Unauthorized                                       |
| /cartoes/{numeroCartao} | GET | Obtém o saldo do cartão especificado | 200 OK com saldo do cartão | 404 Not Found (cartão não existe), 401 Unauthorized                                                 |
| /transacoes | POST | Autoriza uma transação para o cartão especificado | 201 Created com mensagem "OK" | 422 Unprocessable Entity (CARTAO_INEXISTENTE, SENHA_INVALIDA, SALDO_INSUFICIENTE), 401 Unauthorized |

## Execução da aplicação
Para executar a aplicação, siga os passos abaixo:
1. Certifique-se de ter o Docker instalado na sua máquina.
2. Clone este repositório para sua máquina local.
```bash
 git clone https://github.com/calebeandrade93/mini-autorizador.git
```
3. Navegue até o diretório raiz do projeto.
4. Navegue até o diretório 'docker' ```cd ./docker``` 
5. Execute o comando abaixo para buildar e iniciar os containers da aplicação e do banco de dados:
```bash
 docker-compose up --build
   ```
6. aplicação estará disponível em `http://localhost:8080`.
7. (Opcional) Você pode acessar via Swagger UI em `http://localhost:8080/swagger-ui/index.html` para explorar e testar os endpoints da API.
8. (Opcional) Download da Collection para utilizar em um client de preferência(Postman/Insomnia), o arquivo se encontra na pasta 'collection' na raiz do projeto.

## Cobertura de testes
Execute o comando abaixo na raiz do projeto para gerar o relatório de cobertura de testes:
```bash
./mvn clean verify
```
Após a execução, o relatório estará disponível em `target/site/jacoco/index.html`.
