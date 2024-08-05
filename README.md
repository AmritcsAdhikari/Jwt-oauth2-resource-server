# JWT-Oauth2-resource-server
The goal of this project is to leverage Spring's inbuilt security feature to secure a spring boot application using JWT.


### How to run ?
1. Clone this project
2. Run `docker compose up -d` to start the MySQL database
3. Run `mvn spring-boot:run` to start the app


### How to test ?
1. Register a User `POST http://localhost:8080/api/auth/register`
    ```json
    {
      "username": "john",
      "password": "pass",
      "email": "john@dev.com"
    }
    ```
2. Login `POST http://localhost:8080/api/auth/login`
    ```json
    {
      "username": "john",
      "password": "pass"
    }
    ```
    This will return a JWT in the response body.


3. Access the protected resource `GET http://localhost:8080/api/hello` with token in the Authorization header.
    ```curl
    curl --location 'http://localhost:8080/api/hello' \
    --header 'Authorization: Bearer my.jwt.token' \
    --data ''
    ```

### RSA Public & Private Keys
```shell
    # create rsa key pair
openssl genrsa -out keypair.pem 2048

# extract public key
openssl rsa -in keypair.pem -pubout -out public.pem

# create private key in PKCS#8 format
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem
```


###  References
 - https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html
 - https://www.danvega.dev/blog/spring-security-jwt
 - https://github.com/SaiUpadhyayula/spring-security-jwt-demo/