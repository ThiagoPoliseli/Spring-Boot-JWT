Este é um guia passo a passo para testar a aplicação Spring Boot `ProjetoJavaApplication`, que implementa autenticação e autorização baseadas em JWT com endpoints para registro, login e acesso protegido. A aplicação roda localmente em `http://localhost:8080`. Siga as instruções abaixo para testar todas as funcionalidades.

---

## Pré-requisitos
- **Java**: Certifique-se de ter o JDK 17 ou superior instalado.
- **Maven**: Instale o Maven para compilar e executar o projeto.
- **Postman**: Instale o Postman para enviar requisições HTTP.
- **Aplicação Rodando**: Execute o projeto localmente usando sua IDE (ex.: IntelliJ ou Eclipse) ou com o comando `mvn spring-boot:run` no terminal.
---
## Passo a Passo para Testar Tudo

### Passo 1: Testar o Registro de Usuário (`/api/auth/register`)
1. **Abra o Postman**:
   - Selecione o método **POST**.
   - Insira a URL: `http://localhost:8080/api/auth/register`.

2. **Configure o Corpo da Requisição**:
   - Vá para a aba **Body**.
   - Selecione **raw** e **JSON**.
   - Insira o seguinte JSON:
     ```json
     {
       "name": "Usuario Teste",
       "email": "user@teste.com",
       "password": "password",
       "role": "USER"
     }
     ```

3. **Envie a Requisição**:
   - Clique em **Send**.
   - **Resultado Esperado**: Status **200 OK** com um token JWT no corpo da resposta (ex.: `eyJhb...`).

4. **Verifique o Token**:
   - Copie o token retornado.
   - Acesse [jwt.io](https://jwt.io), cole o token na seção "Encoded" e confirme:
     - `sub`: Deve ser `user@teste.com`.
     - `role`: Deve ser `USER`.

5. **Nota**: Se o email já existir, use um novo email (ex.: `user2@teste.com`) e tente novamente.

---

### Passo 2: Para Testar o Login (`/api/auth/login`)
1. **Abra o Postman**:
   - Selecione o método **POST**.
   - Insira a URL: `http://localhost:8080/api/auth/login`.

2. **Configure o Corpo da Requisição**:
   - Vá para a aba **Body**.
   - Selecione **raw** e **JSON**.
   - Insira o seguinte JSON com os dados do usuário registrado:
     ```json
     {
       "email": "user@teste.com",
       "password": "password"
     }
     ```

3. **Envie a Requisição**:
   - Clique em **Send**.
   - **Resultado Esperado**: Status **200 OK** com um novo token JWT no corpo da resposta(No terminal).

4. **Verifique o Token**:
   - Copie o token e decodifique em [jwt.io](https://jwt.io).
   - Confirme que `sub` e `role` correspondem aos dados do usuário.

---

### Passo 3: Testar Acesso a Endpoint Protegido para Usuário (`/api/user/**`)
1. **Abra o Postman**:
   - Selecione o método **GET**.
   - Insira a URL: `http://localhost:8080/api/user/profile`.

2. **Adicione o Token no Cabeçalho**:
   - Vá para a aba **Headers**.
   - Adicione uma nova chave:
     - **Key**: `Authorization`
     - **Value**: `Bearer <seu-token>` (substitua `<seu-token>` pelo token obtido no Passo 1 ou 2).

3. **Envie a Requisição**:
   - Clique em **Send**.
   - **Resultado Esperado**: Status **200 OK** se o endpoint estiver implementado e o role `USER` for autorizado. Caso contrário, **404 Not Found**.

4. **Verifique o Log**:
   - No console da aplicação, confira mensagens como `Processing request: /api/user/profile, Token: <token>`.
---

### Passo 4: Testar Acesso a Endpoint Protegido para Admin (`/api/admin/**`)
1. **Abra o Postman**:
   - Selecione o método **GET**.
   - Insira a URL: `http://localhost:8080/api/admin/users`.

2. **Adicione o Token no Cabeçalho**:
   - Vá para a aba **Headers**.
   - Adicione:
     - **Key**: `Authorization`
     - **Value**: `Bearer <seu-token>` (use o token de um usuário com role `ADMIN`).

3. **Envie a Requisição**:
   - Clique em **Send**.
   - **Resultado Esperado**: 
     - **200 OK** com token de um usuário `ADMIN`.
     - **403 Forbidden** com token de um usuário `USER`.

4. **Registre um Admin**:
   - Repita o Passo 1 com:
     ```json
     {
       "name": "Admin Teste",
       "email": "admin@teste.com",
       "password": "adminpass",
       "role": "ADMIN"
     }
     ```
   - Use o token gerado para testar.
---

## Resultados Esperados
- **Passo 1**: Registro bem-sucedido com token válido.
- **Passo 2**: Login bem-sucedido com novo token.
- **Passo 3**: Acesso permitido para endpoints de usuário com token válido.
- **Passo 4**: Acesso permitido para endpoints de admin com token de admin, negado com token de usuário.
- **Passo 5**: Falha de autenticação sem token ou com token inválido.
