package ai.bitflow.bitwise.wallet.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)  
		          .select()                                  
		          .apis(RequestHandlerSelectors.any())
		          .paths(PathSelectors.ant("/api/v1/**"))
		          .build()
		          .apiInfo(apiInfo());
	}

	@Bean
	public UiConfiguration uiConfig() {
		return UiConfigurationBuilder.builder()
				.docExpansion(DocExpansion.LIST) // or DocExpansion.NONE or DocExpansion.FULL
				.build();
	}

	private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Bitwise Wallet API")
                .description("API documentation for Bitwise Wallet")
                .version("1.0.0")
                .build();
    }
}
