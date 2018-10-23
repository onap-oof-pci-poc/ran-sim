/*-
 * ============LICENSE_START=======================================================
 * Ran Simulator Controller
 * ================================================================================
 * Copyright (C) 2018 Wipro Limited.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.ransim.rest.config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.servlet.MultipartConfigElement;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableWebMvc
@EnableSwagger2
@ComponentScan(basePackages = { "org.onap.*", "com.*" })
public class RansimRestConfig extends WebMvcConfigurerAdapter {

    /**
     * init.
     */
    @PostConstruct
    public void init() {
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream("rs.properties")) {
            // load a properties file
            prop.load(input);
        } catch (Exception e) {
            System.out.println("Exception Occured while loading properties file" + e);
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Ran Simulator Controller REST API")
                .description("This API helps to make queries against Ran Simulator Controller").version("3.0").build();
    }

    /**
     * ransimappApi .
     *
     * @return returns api info
     */
    @Bean
    public Docket ransimappApi() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("org.onap.ransim.rest.api")).paths(PathSelectors.any())
                .build().apiInfo(apiInfo());
    }

    /**
     * MultipartConfigElement.
     *
     * @return returns MultipartConfigElement
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        String location = System.getProperty("java.io.tmpdir");
        MultipartConfigElement mp = new MultipartConfigElement(location);
        return mp;
    }
}
