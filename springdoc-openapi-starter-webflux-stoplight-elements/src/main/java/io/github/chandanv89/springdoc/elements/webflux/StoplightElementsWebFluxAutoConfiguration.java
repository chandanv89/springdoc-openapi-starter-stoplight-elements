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

package io.github.chandanv89.springdoc.elements.webflux;

import io.github.chandanv89.springdoc.elements.StoplightElementsConfigProperties;
import io.github.chandanv89.springdoc.elements.StoplightElementsIndexTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * Spring Boot auto-configuration for the Stoplight Elements API documentation UI
 * in WebFlux applications.
 *
 * <p>This configuration is activated when:
 * <ul>
 *   <li>The application is a reactive web application</li>
 *   <li>The property {@code springdoc.stoplight-elements.enabled} is {@code true} (default)</li>
 * </ul>
 *
 * <p>It registers the following beans:
 * <ul>
 *   <li>{@link StoplightElementsIndexTransformer} — renders the HTML template with configuration</li>
 *   <li>{@link RouterFunction} — reactive route serving the Elements HTML page</li>
 *   <li>{@link StoplightElementsWebFluxResourceConfigurer} — serves the bundled Elements
 *       JS/CSS as static resources</li>
 * </ul>
 *
 * @author Chandan Veerabhadrappa (@chandanv89)
 */
@AutoConfiguration
@ConditionalOnWebApplication(type = Type.REACTIVE)
@ConditionalOnProperty(name = "springdoc.stoplight-elements.enabled", matchIfMissing = true)
@EnableConfigurationProperties(StoplightElementsConfigProperties.class)
public class StoplightElementsWebFluxAutoConfiguration {

    private static final Logger log =
            LoggerFactory.getLogger(StoplightElementsWebFluxAutoConfiguration.class);

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
            log.info("Stoplight Elements UI (WebFlux) is enabled at endpoint `{}`",
                    properties.getPath());
        }
        return new StoplightElementsIndexTransformer(properties);
    }

    /**
     * Creates the reactive {@link RouterFunction} that serves the Elements page.
     *
     * @param indexTransformer the transformer that renders the HTML
     * @param properties the Stoplight Elements configuration properties
     * @return the router function bean
     */
    @Bean
    @ConditionalOnMissingBean(name = "stoplightElementsRouterFunction")
    public RouterFunction<ServerResponse> stoplightElementsRouterFunction(
            final StoplightElementsIndexTransformer indexTransformer,
            final StoplightElementsConfigProperties properties) {
        return new StoplightElementsWebFluxRouterConfig(indexTransformer, properties.getPath())
                .stoplightElementsRouterFunction();
    }

    /**
     * Creates the {@link StoplightElementsWebFluxResourceConfigurer} bean that registers
     * static resource handlers.
     *
     * @return a new {@link StoplightElementsWebFluxResourceConfigurer} instance
     */
    @Bean
    @ConditionalOnMissingBean
    public StoplightElementsWebFluxResourceConfigurer stoplightElementsWebFluxResourceConfigurer() {
        return new StoplightElementsWebFluxResourceConfigurer();
    }
}
