package springboot.swagger;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import static com.jayway.restassured.RestAssured.get;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {
        SpringBootSwaggerTest.SomeApplication.class,
        SpringBootSwaggerTest.SomeController.class,
        SpringBootSwaggerTest.SomeSwaggerConfig.class })
@WebAppConfiguration
@IntegrationTest
public class SpringBootSwaggerTest {

    @Test
    public void shouldReturnApiDocs() {
        get("http://localhost:8080/api-docs")
            .then()
                .body("apis.size()", equalTo(1))
                .body("apis[0].path", equalTo("/test/some-controller"));
    }

    @Test
    public void shouldReturnEndpointDocs() {
        get("http://localhost:8080/api-docs/test/some-controller")
            .then()
                .body("resourcePath", equalTo("/some-controller"))
                .body("apis[0].operations.size()", equalTo(1))
                .body("apis[0].operations[0].method", equalTo("GET"))
                .body("apis[0].operations[0].summary", equalTo("test operation"))
                .body("apis[0].operations[0].parameters.size()", equalTo(0));
    }

    @EnableAutoConfiguration
    public static class SomeApplication {
    }

    @RestController
    @Api("my-controller")
    public static class SomeController {
        @ApiOperation("test operation")
        @RequestMapping(value = "/", method = GET)
        public String home() {
            return "Hello World!";
        }
    }

    @Import(SpringSwaggerConfig.class)
    public static class SomeSwaggerConfig extends SpringBootSwaggerConfig {
        @Override
        protected List<String> getIncludePatterns() {
            return Arrays.asList(".*");
        }

        @Override
        protected String getSwaggerGroup() {
            return "test";
        }
    }
}
