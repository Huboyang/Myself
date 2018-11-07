package com.hby.myselfproject.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * Swagger2配置类
 * **/
@Configuration
@EnableSwagger2
public class SwaggerConfig {

      @Bean
      public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
//            .apis(RequestHandlerSelectors.basePackage("com.hby.myselfproject.controller"))
            .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
            .paths(PathSelectors.any())
            .build();
      }

      private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("胡博洋的swagger-ui文档")
            .description("更多功能待实现")
            .termsOfServiceUrl("http://www.baidu.com")
            .contact("胡博洋")
            .version("1.0")
            .build();
      }

}
