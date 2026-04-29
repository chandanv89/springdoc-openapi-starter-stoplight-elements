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

import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * Configures WebFlux resource handlers to serve the bundled Stoplight Elements JS and CSS files.
 *
 * <p>Maps requests for {@code /webjars/stoplight-elements/**} to the classpath location
 * {@code META-INF/resources/webjars/stoplight-elements/} where the bundled assets are stored.</p>
 *
 * @author Chandan Veerabhadrappa (@chandanv89)
 */
public class StoplightElementsWebFluxResourceConfigurer implements WebFluxConfigurer {

    /**
     * {@inheritDoc}
     *
     * <p>Adds a resource handler for the bundled Stoplight Elements web component assets.</p>
     */
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/stoplight-elements/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/stoplight-elements/");
    }
}
