## Description

This project shows how to use different enterprise libraries together in the Spring framework based project.

This project is a RESTful service implemented in Spring Web MVC.

The service is divided in several layers:
  
  * REST API (Spring Web MVC)
  * Services Layer (some functionality use RxJava)
  * Data Access Layer (Spring Data JPA, Hibernate as ORM)
  * Database system (H2 was used for testing, and MySql or Postgres may be used in production) 

The documentation for REST API can be find [here](https://github.com/Chicker/spring-rest-loan/wiki/REST-API-Doc).

## Development

This is a gradle-based project.

### Running

Using gradle plugin `gretty` allows run our web application 
with jetty (or another servlet container) runner. Gradle task for this:

```
./gradlew [appRun | jettyRun]
```

## Testing

The service layer is tested with JUnit and Mockito.

REST API is tested with Spring Mvc Test Framework.

All the tests use H2 database system.

## Deployment

To generate `war` file execute following command:

```
./gradlew war
```

The `war` file will be placed in `/build/libs/` folder.
