### Linguagem e banco de dados utilizados

* Java 11
* MySQL na versão "10.4.20-MariaDB"

### Documentação

Documentação da API: http://localhost:8080/api/swagger-ui/
Login no endpoint: http://localhost:8080/api/login

Para realizar login no endpoint acima, é necessário enviar uma requisição POST com email e password no body. Com isto, é devolvivo um token JWT no header "Authorization" do retorno da requisição, e esse token é necessário para os outros endpoints. Exemplo do body:
`{
"email": "admin@example.com",
"password": "123"
}`

Para fins de facilidade na execução da aplicação para avaliação, ao executar o projeto, o banco de dados é automaticamente criado, junto com sua estrutura pelo arquivo schema.sql e dados de 2 usuários inicias pelo arquivo data.sql. Os dados dos usuários são:

Email | Password | Permissões
------------ | -------------
admin@example.com | 123 | Todas requisições`
user@example.com | 123 | Apenas requisições GET

### Instalação

1. Faça o clone desse repositório
2. Importe o projeto na sua IDE de preferência (para desenvolvimento foi utilizado o Intellij IDEA Community 2021.1.3)
3. Inicie o servidor do MySQL. Caso não possua, uma forma prática e rápida para estar fazendo isso é fazendo download do XAMPP, e utilizando o MySQL que vem com ele, não precisa dos outros módulos. O projeto está configurado para utilizar as configurações de acesso padrões do MySQL, então a URL seria "localhost", porta 3306, username "root" e sem senha. Caso seu MySQL esteja configurado diferente, altere os dados no arquivo "application.properties".
4. Rode no seu terminal o comando "mvn clean install" para compilar o projeto. Caso não possua o maven instalado na sua máquina para utilizar o "mvn", faça isso pela sua IDE. Exemplo de como fazer via IntelliJ IDEA:
   ![Step 4 Example](https://i.imgur.com/qGewtDA.jpg)
5. Executa o projeto pela classe "CustomersFavProductsApplication" localizada no pacote "com.rafaelgude.customersfavproducts". Pelo IntelliJ IDEA é possível clicando com o botão direito na classe e optando por "Run", como na imagem abaixo:
   ![Step 5 Example](https://i.imgur.com/P4jtoBO.jpg)
