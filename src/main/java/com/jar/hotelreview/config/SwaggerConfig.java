package com.jar.hotelreview.config;

import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;

import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@EnableSwagger2
@Configuration
public class SwaggerConfig {
    @Bean
	public Docket swaggerConfiguration(){
		return new Docket(DocumentationType.SWAGGER_2)
		.select()
		.apis(RequestHandlerSelectors.basePackage("com.jar"))
		.build()
        .apiInfo(metaInfo());
	}

    private ApiInfo metaInfo(){
		return new ApiInfo(
			"Hotel Review REST Service",
			"API support to submit reviews for hotels",
			"1.0",
			"Terms of Service", 
			new springfox.documentation.service.Contact(
				"Vedant Somani",
				"https://www.linkedin.com/in/vedant-somani-94614958/",
				"iamvedantsomani@gmail.com"
			), 
			"APACHE-2.0",
			"http://www.apache.org/licenses/LICENSE-2.0",
			new ArrayList<>()
		);
    }
}
