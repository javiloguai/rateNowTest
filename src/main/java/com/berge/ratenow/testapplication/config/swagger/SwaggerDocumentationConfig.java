package com.berge.ratenow.testapplication.config.swagger;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;


@Configuration
@EnableSwagger2
public class SwaggerDocumentationConfig extends WebMvcConfigurationSupport {
	
	private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
	            "classpath:/META-INF/resources/", "classpath:/resources/",
	            "classpath:/static/", "classpath:/public/"};

    @Value("${application.name}")
    private String applicationName;

    @Value("${application.description}")
    private String applicationDescription;

    @Value("${build.version}")
    private String buildVersion;

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(applicationName)
                .description(applicationDescription)
//              .termsOfServiceUrl("http://www.<>.es/")
//              .license("http://www.<>.es/")
//              .licenseUrl("http://www.<>.es/")
                .version(buildVersion)
                .build();
    }

    @SuppressWarnings("All")
    @Bean
    public Docket customImplementation() {
        //Adding Header
        ParameterBuilder aParameterBuilder = new ParameterBuilder();
        aParameterBuilder.name("Authorization")                 // name of header
                .modelRef(new ModelRef("string"))
                .parameterType("header")               // type - header
                .defaultValue("Bearer XXX")        // based64 of - zone:mypassword
                .required(true)                // for compulsory
                .build();
        java.util.List<Parameter> aParameters = new ArrayList<>();
        aParameters.add(aParameterBuilder.build());             // add parameter
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                //.apis(RequestHandlerSelectors.basePackage("com.berge.ratenow.testapplication.webservice.rest"))
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .globalOperationParameters(aParameters);
        
//        return new Docket(DocumentationType.SWAGGER_2)  
//                .select()                                  
//                .apis(RequestHandlerSelectors.any())              
//                .paths(PathSelectors.any())                          
//                .build();    
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        
        if (!registry.hasMappingForPattern("/**")) {
            registry.addResourceHandler("/**").addResourceLocations(
                    CLASSPATH_RESOURCE_LOCATIONS);
        }
    }


}
