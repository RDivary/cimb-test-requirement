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

## Run

A step for running this service

```
java -jar target/application.jar
```

## Running The Tests

Explain how to run the automated tests for this system

```
mvn clean test
```

You can see test result at `http://localhost:63342/cimb-test-requirement/target/site/jacoco/index.html`