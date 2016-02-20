# Spring Angular2 Component based

An example project using Spring (component-based) and Angular 2.

## Dependencies

- [Java SDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Postgres](http://www.postgresql.org/download/)

## Setup

1. Install postgres: `brew install postgres`
1. Follow instructions to start postgres now and on restart
1. Create a postgres database super user admin with password 'admin': `createuser admin --superuser --password`
1. Create postgres databases: `createdb spring-ng2-example && createdb spring-ng-example-test`
1. `cp applications/example-api/src/resources/application.yml.example deployment/example-api/src/resources/application.yml`

## Running

1. `gradle bootRun`
1. Navigate to `http://localhost:8080/` 
1. Check out `http://localhost:8080/swagger-ui.html` to see all functionality of the backend.

## Front end development

The front end build has a few node tasks that will help when developing locally. To get started
run the `npmInstall` gradle task followed by the `run` gradle task. Run will compile, setup watchers
for ts and scss files, and open a browser with browser-sync enabled.

## Running tests

1. `./gradlew :applications/example-api:migrateTestDatabase`
1. `./gradlew clean test`
