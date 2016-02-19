# Spring Angular2 Component based

An example project using Spring (component-based) and Angular 2.

## Dependencies

- [Java SDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Postgres](http://www.postgresql.org/download/)
- [Redis](http://redis.io/download/)

## Setup

1. Install postgres: `brew install postgres`
1. Follow instructions to start postgres now and on restart
1. Install redis: `brew install postgres`
1. Follow instructions to start redis now and on restart
1. Create a postgres database super user admin with password 'admin': `createuser admin --superuser --password`
1. Create postgres databases: `createdb spring-ng2-example && createdb spring-ng-example-test`
1. `cp applications/example-api/src/resources/application.yml.example deployment/example-api/src/resources/application.yml`

## Running

1. `gradle bootRun`
1. Navigate to `http://localhost:8080/`

## Running tests

1. `./gradlew :applications/example-api:migrateTestDatabase`
1. `./gradlew clean test`

## Deploying to Cloud Foundry

1. Create a postgres service
1. Create a redis service
1. Modify `manifest.yml` with correct postgres service name, environment variable values
1. `cf push`
