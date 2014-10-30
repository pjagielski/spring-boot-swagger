Spring-boot-swagger
===================
Dead simple [Swagger](https://github.com/wordnik/swagger-ui) config for [Spring Boot](https://github.com/spring-projects/spring-boot).

[![Build Status](https://travis-ci.org/pjagielski/spring-boot-swagger.svg?branch=master)](https://travis-ci.org/pjagielski/spring-boot-swagger)

Motivation
==========
Spring Boot is one of the hottest JVM frameworks in the microservice area. The only missing part for me is integration with Swagger — de facto standard for documenting REST services. There is a great [swagger-springmvc](https://github.com/martypitt/swagger-springmvc) library for Spring MVC, but its configuration seems to be too verbose for microservices. This project provides sensible defaults for swagger-springmvc which let you expose Swagger docs in just a few lines of code.

Usage
=====
Spring-boot-swagger is available for download from the Maven Central repository.

Maven:
```xml
<dependency>
    <groupId>com.github.pjagielski.spring-boot-swagger</groupId>
    <artifactId>spring-boot-swagger</artifactId>
    <version>0.1</version>
</dependency>
```

Grab:
```groovy
@Grab('com.github.pjagielski.spring-boot-swagger:spring-boot-swagger:0.1')
```

Examples
========
Sample `app.groovy`:
```groovy
@Grab('org.springframework.boot:spring-boot-starter-actuator:1.0.2.RELEASE')
@Grab('org.springframework:spring-web:4.0.3.RELEASE')
@Grab('com.github.pjagielski.spring-boot-swagger:spring-boot-swagger:0.1')

import com.mangofactory.swagger.configuration.SpringSwaggerConfig
import com.wordnik.swagger.annotations.ApiOperation
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import springboot.swagger.SpringBootSwaggerConfig

import static org.springframework.web.bind.annotation.RequestMethod.GET

@RestController
class SampleController {

    @RequestMapping(value = "/messages", method = GET)
    @ApiOperation("hello world")
    Message hello() {
		new Message(text: "Hello world!")
    }
}

class Message {
    String text
}

@Configuration
@Import(SpringSwaggerConfig)
class SwaggerConfig extends SpringBootSwaggerConfig {

    @Override
    protected List<String> getIncludePatterns() {
        ['/messages.*']
    }

    @Override
    protected String getSwaggerGroup() {
        'messages'
    }
}
```
