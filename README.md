spring-boot-swagger
===================

Dead simple Swagger config for Spring Boot

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
    public Message hello() {
		new Message(text: "Hello world!")
	}
}

class Message {
    String text
}

@Configuration
@Import(SpringSwaggerConfig)
public class SwaggerConfig extends SpringBootSwaggerConfig {

    @Override
    protected List<String> getIncludePatterns() {
        return ['/messages.*']
    }

    @Override
    protected String getSwaggerGroup() {
        return 'messages'
    }

}
```
[![Build Status](https://travis-ci.org/pjagielski/spring-boot-swagger.svg?branch=master)](https://travis-ci.org/pjagielski/spring-boot-swagger)
