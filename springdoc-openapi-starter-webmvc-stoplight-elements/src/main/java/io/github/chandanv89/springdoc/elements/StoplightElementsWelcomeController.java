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

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller that serves the Stoplight Elements API documentation HTML page.
 *
 * <p>The endpoint path is configurable via {@code springdoc.stoplight-elements.path}
 * (default: {@code /api-docs}). The rendered HTML is produced by
 * {@link StoplightElementsIndexTransformer} using the configured properties.
 * This controller is hidden from the OpenAPI specification.</p>
 *
 * @author Chandan Veerabhadrappa (@chandanv89)
 */
@Hidden
@Controller
public class StoplightElementsWelcomeController {

    private final StoplightElementsIndexTransformer indexTransformer;

    /**
     * Creates a new controller with the given index transformer.
     *
     * @param indexTransformer the transformer that renders the Elements HTML
     */
    public StoplightElementsWelcomeController(final StoplightElementsIndexTransformer indexTransformer) {
        this.indexTransformer = indexTransformer;
    }

    /**
     * Serves the Stoplight Elements API documentation page.
     *
     * @param request the incoming HTTP request (used to resolve servlet context path)
     * @return the rendered Elements HTML page as a string
     */
    @GetMapping(value = "${springdoc.stoplight-elements.path:/api-docs}", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String elements(final HttpServletRequest request) {
        return indexTransformer.render(request.getContextPath());
    }
}
