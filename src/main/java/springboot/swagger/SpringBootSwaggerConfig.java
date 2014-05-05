package springboot.swagger;

import com.fasterxml.classmate.TypeResolver;
import com.mangofactory.swagger.configuration.JacksonScalaSupport;
import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.configuration.SwaggerGlobalSettings;
import com.mangofactory.swagger.core.SwaggerApiResourceListing;
import com.mangofactory.swagger.core.SwaggerPathProvider;
import com.mangofactory.swagger.models.alternates.AlternateTypeProvider;
import com.mangofactory.swagger.models.alternates.WildcardType;
import com.mangofactory.swagger.scanners.ApiListingReferenceScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.util.List;

import static com.mangofactory.swagger.models.alternates.Alternates.newRule;

@Configuration
public abstract class SpringBootSwaggerConfig implements ServletContextAware {

    @Autowired
    private SpringSwaggerConfig springSwaggerConfig;

    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Bean
    public JacksonScalaSupport jacksonScalaSupport() {
        JacksonScalaSupport jacksonScalaSupport = new JacksonScalaSupport();
        jacksonScalaSupport.setRegisterScalaModule(true);
        return jacksonScalaSupport;
    }

    @Bean
    public SwaggerGlobalSettings swaggerGlobalSettings() {
        SwaggerGlobalSettings swaggerGlobalSettings = new SwaggerGlobalSettings();
        swaggerGlobalSettings.setGlobalResponseMessages(springSwaggerConfig.defaultResponseMessages());
        swaggerGlobalSettings.setIgnorableParameterTypes(springSwaggerConfig.defaultIgnorableParameterTypes());
        AlternateTypeProvider alternateTypeProvider = springSwaggerConfig.defaultAlternateTypeProvider();
        TypeResolver typeResolver = new TypeResolver();
        alternateTypeProvider.addRule(newRule(typeResolver.resolve(ResponseEntity.class),
                typeResolver.resolve(Void.class)));
        alternateTypeProvider.addRule(newRule(typeResolver.resolve(ResponseEntity.class, WildcardType.class),
                typeResolver.resolve(WildcardType.class)));
        alternateTypeProvider.addRule(newRule(typeResolver.resolve(HttpEntity.class, WildcardType.class),
                typeResolver.resolve(WildcardType.class)));
        swaggerGlobalSettings.setAlternateTypeProvider(alternateTypeProvider);
        return swaggerGlobalSettings;
    }

    @Bean
    public SwaggerApiResourceListing swaggerApiResourceListing() {
        SwaggerApiResourceListing swaggerApiResourceListing = new SwaggerApiResourceListing(springSwaggerConfig.swaggerCache(), getSwaggerGroup());
        swaggerApiResourceListing.setSwaggerGlobalSettings(swaggerGlobalSettings());
        swaggerApiResourceListing.setSwaggerPathProvider(pathProvider());
        swaggerApiResourceListing.setApiListingReferenceScanner(apiListingReferenceScanner());
        return swaggerApiResourceListing;
    }

    @Bean
    public ApiListingReferenceScanner apiListingReferenceScanner() {
        ApiListingReferenceScanner apiListingReferenceScanner = new ApiListingReferenceScanner();
        apiListingReferenceScanner.setRequestMappingHandlerMapping(springSwaggerConfig.swaggerRequestMappingHandlerMappings());
        apiListingReferenceScanner.setExcludeAnnotations(springSwaggerConfig.defaultExcludeAnnotations());
        apiListingReferenceScanner.setResourceGroupingStrategy(springSwaggerConfig.defaultResourceGroupingStrategy());
        apiListingReferenceScanner.setSwaggerPathProvider(pathProvider());
        apiListingReferenceScanner.setSwaggerGroup(getSwaggerGroup());
        apiListingReferenceScanner.setIncludePatterns(getIncludePatterns());
        return apiListingReferenceScanner;
    }

    @Bean
    public SwaggerPathProvider pathProvider() {
        return new ServletContextPathProvider(springSwaggerConfig.defaultSwaggerPathProvider(), servletContext);
    }

    protected abstract List<String> getIncludePatterns();

    protected abstract String getSwaggerGroup();

}
