package springboot.swagger;

import com.mangofactory.swagger.core.SwaggerPathProvider;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletContext;

public class ServletContextPathProvider implements SwaggerPathProvider {

    private final SwaggerPathProvider swaggerPathProvider;

    private final ServletContext servletContext;

    public ServletContextPathProvider(SwaggerPathProvider swaggerPathProvider, ServletContext servletContext) {
        this.swaggerPathProvider = swaggerPathProvider;
        this.servletContext = servletContext;
    }

    @Override
    public String getApiResourcePrefix() {
        return swaggerPathProvider.getApiResourcePrefix();
    }

    public String getAppBasePath() {
        return UriComponentsBuilder
                .fromHttpUrl("http://127.0.0.1:8080")
                .path(servletContext.getContextPath())
                .build()
                .toString();
    }

    @Override
    public String sanitizeRequestMappingPattern(String requestMappingPattern) {
        return swaggerPathProvider.sanitizeRequestMappingPattern(requestMappingPattern);
    }

}
