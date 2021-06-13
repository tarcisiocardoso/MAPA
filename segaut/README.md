# seguranca-autorizacao

componente de  segurança-autorização container



## Scripts do projeto

Para executar em modo desenvolvimento:

```bash
mvn spring-boot:run
```

## Para executar em modo embarcado:


```bash
mvn spring-boot:run -Pembed
```

Dessa forma o script executa o profile embed como o banco de dados e LDAP embarcados

## Preparar o pacote para deploy

```bash
mvn clean package
```

## Executar dentro do container

```bash
java -Dspring.profiles.active=dev -jar ./target/segaut-0.0.1-SNAPSHOT.jar
```