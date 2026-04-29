/*
 * Copyright 2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.chandanv89.springdoc.elements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Boot auto-configuration for the Stoplight Elements API documentation UI.
 *
 * <p>This configuration is activated when:
 * <ul>
 *   <li>The application is a servlet-based web application</li>
 *   <li>The property {@code springdoc.stoplight-elements.enabled} is {@code true} (default)</li>
 * </ul>
 *
 * <p>It registers the following beans:
 * <ul>
 *   <li>{@link StoplightElementsIndexTransformer} — renders the HTML template with configuration</li>
 *   <li>{@link StoplightElementsWelcomeController} — serves the Elements HTML page at the configured path</li>
 *   <li>{@link StoplightElementsWebMvcConfigurer} — serves the bundled Elements JS/CSS as static resources</li>
 *   <li>{@link StoplightElementsActuatorEndpoint} — (optional) exposes Elements on the actuator management port</li>
 * </ul>
 *
 * @author Chandan Veerabhadrappa @chandanv89
 */
@AutoConfiguration
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnProperty(name = "springdoc.stoplight-elements.enabled", matchIfMissing = true)
@EnableConfigurationProperties(StoplightElementsConfigProperties.class)
public class StoplightElementsAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(StoplightElementsAutoConfiguration.class);

    /**
     * Creates the {@link StoplightElementsIndexTransformer} bean that renders the HTML template.
     *
     * @param properties the Stoplight Elements configuration properties
     * @return a new {@link StoplightElementsIndexTransformer} instance
     */
    @Bean
    @ConditionalOnMissingBean
    public StoplightElementsIndexTransformer stoplightElementsIndexTransformer(
            final StoplightElementsConfigProperties properties) {
        if (log.isInfoEnabled()) {
            log.info("Stoplight Elements UI is enabled at endpoint `{}`", properties.getPath());
        }
        return new StoplightElementsIndexTransformer(properties);
    }

    /**
     * Creates the {@link StoplightElementsWelcomeController} bean that serves the Elements page.
     *
     * @param indexTransformer the transformer that renders the HTML
     * @return a new {@link StoplightElementsWelcomeController} instance
     */
    @Bean
    @ConditionalOnMissingBean
    public StoplightElementsWelcomeController stoplightElementsWelcomeController(
            final StoplightElementsIndexTransformer indexTransformer) {
        return new StoplightElementsWelcomeController(indexTransformer);
    }

    /**
     * Creates the {@link StoplightElementsWebMvcConfigurer} bean that registers static resource handlers.
     *
     * @return a new {@link StoplightElementsWebMvcConfigurer} instance
     */
    @Bean
    @ConditionalOnMissingBean
    public StoplightElementsWebMvcConfigurer stoplightElementsWebMvcConfigurer() {
        return new StoplightElementsWebMvcConfigurer();
    }

    /**
     * Nested configuration for Spring Boot Actuator support.
     * Only activated when the Actuator is on the classpath and
     * {@code springdoc.stoplight-elements.actuator.enabled=true}.
     */
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(name = "org.springframework.boot.actuate.endpoint.annotation.Endpoint")
    @ConditionalOnProperty(name = "springdoc.stoplight-elements.actuator.enabled", havingValue = "true")
    static class StoplightElementsActuatorConfiguration {

        /**
         * Creates the {@link StoplightElementsActuatorEndpoint} bean that exposes Elements
         * on the management port.
         *
         * @param indexTransformer the transformer that renders the HTML
         * @return a new {@link StoplightElementsActuatorEndpoint} instance
         */
        @Bean
        @ConditionalOnMissingBean
        public StoplightElementsActuatorEndpoint stoplightElementsActuatorEndpoint(
                final StoplightElementsIndexTransformer indexTransformer) {
            return new StoplightElementsActuatorEndpoint(indexTransformer);
        }
    }
}
