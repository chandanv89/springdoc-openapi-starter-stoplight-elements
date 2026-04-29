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

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

/**
 * Spring Boot Actuator endpoint that exposes the Stoplight Elements UI on the management port.
 *
 * <p>When {@code springdoc.stoplight-elements.actuator.enabled=true} and a separate management port
 * is configured (e.g. {@code management.server.port=9090}), the Elements UI becomes available at
 * {@code http://localhost:9090/actuator/stoplight-elements}.</p>
 *
 * <p>This is useful for exposing API documentation on the management port while keeping
 * it hidden from the main application port.</p>
 *
 * @author Chandan Veerabhadrappa (@chandanv89)
 */
@Endpoint(id = "stoplight-elements")
public class StoplightElementsActuatorEndpoint {

    private final StoplightElementsIndexTransformer indexTransformer;

    /**
     * Creates a new actuator endpoint.
     *
     * @param indexTransformer the transformer that renders the Elements HTML
     */
    public StoplightElementsActuatorEndpoint(final StoplightElementsIndexTransformer indexTransformer) {
        this.indexTransformer = indexTransformer;
    }

    /**
     * Returns the rendered Stoplight Elements HTML page.
     *
     * @return the Elements HTML as a string
     */
    @ReadOperation(produces = "text/html")
    public String elements() {
        return indexTransformer.render();
    }
}
