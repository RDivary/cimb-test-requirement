# Test Requirement CIMB

Backend Services for test CIMB

## Getting Started

These instructions will get you a copy of the project up and running on your local machine.

## Prerequisites

What things you need to install the software and how to install them

```
- Maven
- OpenJDK 8
- MySql
```

## Preparation

First you should set application properties with your configuration. 

```
##Database
- spring.datasource.url=your database url
- spring.datasource.username= your database username
- spring.datasource.password= your database password

##JWT
- bezkoder.app.jwtSecret= your jwt secret
- bezkoder.app.jwtExpirationMs= expiration jwt
```

## Installing

A step to get build jar

```
mvn clean install
```

You can see test result at [localhost:63342/cimb-test-requirement/target/site/jacoco/index.html](localhost:63342/cimb-test-requirement/target/site/jacoco/index.html)

## Run

A step for running this service.

```
java -jar target/application.jar
```
_Before running the application, make sure the table in the database already exists_

## Running Swagger
You can access `http://{your-port-service}/swagger-ui/index.html` for view API Documentation using Swagger

## Running The Tests

Explain how to run the automated tests for this system

```
mvn clean test
```

## Flow
_for example, you run application in port localhost:8080_

For the first time, you must create user with 'Admin' role.

### Role Admin
####Authentication
create 'Role Admin' with api http://localhost:8080/api/v1/auth/register-admin 'POST' and with request body:

```
{
  "password": "string",
  "username": "string"
}
```
after that, you must login use api http://localhost:8080/api/v1/auth/login 'POST' with request body:
```
{
  "password": "string",
  "username": "string"
}
```
####Transaction Type
_you must add authorization with type 'Bearer Token' in header with token given during login_

after login, you must create 'Transaction Type' with api http://localhost:8080/api/v1/transaction-type 'POST' with request body:
```
{
  "transactionCode": "string",
  "transactionName": "string"
}
```
if 'Admin' want to edit 'Transaction Type', admin can use api http://localhost:8080/api/v1/transaction-type/{transaction-type-id} 'PUT' with request body:
```
{
  "transactionCode": "string",
  "transactionName": "string"
}
```
if 'Admin' want to delete 'Transaction Type', admin can use api http://localhost:8080/api/v1/transaction-type/{transaction-type-id} 'DELETE'.

if 'Admin' want see 'Transaction Type' by id, admin can use http://localhost:8080/api/v1/transaction-type/{transaction-type-id} 'POST'

if 'Admin' want see all 'Transaction Type', admin can use http://localhost:8080/api/v1/transaction-type 'POST' with parameter:
```
{
  active: [O] boolean
}
```

####Transaction
_you must add authorization with type 'Bearer Token' in header with token given during login_

if 'Admin' want see transaction another user, user can use api http://localhost:8080/api/v1/transaction/user/{user-id} 'GET'.
```
{
  transactionName: [O] string
}
```

if 'Admin' want download transaction another user, user can use api http://localhost:8080/api/v1/transaction/download-report/{user id} 'GET':

####User
_you must add authorization with type 'Bearer Token' in header with token given during login_
if 'Admin' want see another user, user can use api http://localhost:8080/api/v1/user/{user id} 'GET':

___
### Role User
####Authentication
create 'Role User' with api http://localhost:8080/api/v1/auth/register 'POST' and with request body:

```
{
  "password": "string",
  "username": "string"
}
```
after that, you must login use api http://localhost:8080/api/v1/auth/login 'POST' with request body:
```
{
  "password": "string",
  "username": "string"
}
```
####Transaction Type
_you must add authorization with type 'Bearer Token' in header with token given during login_

if 'User' want see 'Transaction Type' by id, user can use http://localhost:8080/api/v1/transaction-type/{transaction-type-id} 'POST'

if 'User' want see all 'Transaction Type', user can use http://localhost:8080/api/v1/transaction-type 'POST'

####Transaction
_you must add authorization with type 'Bearer Token' in header with token given during login_

if 'User' want transaction, user can use http://localhost:8080/api/v1/transaction 'POST' with request body:

```
{
  "amount": Integer,
  "transactionTypeId": Integer
}
```
if 'User' want see 'Transaction' by id, user can use http://localhost:8080/api/v1/transaction/{transaction-id} 'GET'

if 'User' want see all 'Transaction', user can use http://localhost:8080/api/v1/transaction/user 'GET' with parameter:
```
{
  transactionName: [O] string
}
if 'user' want download transaction, user can use api http://localhost:8080/api/v1/transaction/download-report/ 'GET':
```

####User
_you must add authorization with type 'Bearer Token' in header with token given during login_
if 'User' want see detail user, user can use api http://localhost:8080/api/v1/user 'GET':
