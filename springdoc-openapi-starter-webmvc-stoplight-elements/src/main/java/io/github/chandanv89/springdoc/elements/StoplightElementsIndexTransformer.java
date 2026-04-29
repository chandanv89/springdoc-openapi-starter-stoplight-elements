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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transforms the Stoplight Elements HTML template by replacing placeholders with configured values.
 *
 * <p>The template uses the following placeholders:
 * <ul>
 *   <li>{@code {{title}}} — the page title</li>
 *   <li>{@code {{specUrl}}} — the OpenAPI spec URL</li>
 *   <li>{@code {{options}}} — Elements HTML attributes (layout, router, hideTryIt, etc.)</li>
 *   <li>{@code {{faviconUrl}}} — the URL to the favicon shown in browser tabs</li>
 *   <li>{@code {{elementsJsUrl}}} — the URL to the Elements web component JS file</li>
 *   <li>{@code {{elementsCssUrl}}} — the URL to the Elements CSS file</li>
 * </ul>
 *
 * @author Chandan Veerabhadrappa (@chandanv89)
 */
public class StoplightElementsIndexTransformer {

    private static final Logger log = LoggerFactory.getLogger(StoplightElementsIndexTransformer.class);

    private static final String TEMPLATE_PATH =
            "META-INF/resources/stoplight-elements/stoplight-elements.html";
    private static final String BUNDLED_JS_PATH =
            "/webjars/stoplight-elements/web-components.min.js";
    private static final String BUNDLED_CSS_PATH =
            "/webjars/stoplight-elements/styles.min.css";
    private static final String BUNDLED_FAVICON_PATH =
            "/stoplight-elements/favicon.ico";

    private final StoplightElementsConfigProperties properties;
    private final String template;

    /**
     * Creates a new transformer, loading the HTML template from the classpath.
     *
     * @param properties the Stoplight Elements configuration properties
     */
    public StoplightElementsIndexTransformer(final StoplightElementsConfigProperties properties) {
        this.properties = properties;
        this.template = loadTemplate();
    }

    /**
     * Renders the Stoplight Elements HTML page by substituting all placeholders in the template.
     *
     * @return the fully rendered HTML string
     */
    public String render() {
        return render("");
    }

    /**
     * Renders the Stoplight Elements HTML page by substituting all placeholders in the template
     * and prefixing bundled asset URLs with the provided servlet context path.
     *
     * @param contextPath the servlet context path (for example {@code /example-app})
     * @return the fully rendered HTML string
     */
    public String render(final String contextPath) {
        final var normalizedContextPath = normalizeContextPath(contextPath);
        final var jsUrl = properties.isUseCdn()
                ? properties.getCdnJsUrl()
                : prefixWithContextPath(normalizedContextPath, BUNDLED_JS_PATH);
        final var cssUrl = properties.isUseCdn()
                ? properties.getCdnCssUrl()
                : prefixWithContextPath(normalizedContextPath, BUNDLED_CSS_PATH);
        final var faviconUrl = prefixWithContextPath(normalizedContextPath, BUNDLED_FAVICON_PATH);
        final var optionsStr = properties.buildElementsAttributeString();
        final var normalizedSpecUrl = prefixWithContextPath(
                normalizedContextPath, properties.getSpecUrl());

        return template
                .replace("{{title}}", escapeHtml(properties.getTitle()))
                .replace("{{specUrl}}", escapeHtml(normalizedSpecUrl))
                .replace("{{options}}", optionsStr)
                .replace("{{faviconUrl}}", escapeHtml(faviconUrl))
                .replace("{{elementsJsUrl}}", escapeHtml(jsUrl))
                .replace("{{elementsCssUrl}}", escapeHtml(cssUrl));
    }

    private static String normalizeContextPath(final String contextPath) {
        if (contextPath == null) {
            return "";
        }

        var normalized = contextPath.trim();
        if (normalized.isEmpty() || "/".equals(normalized)) {
            return "";
        }
        if (!normalized.startsWith("/")) {
            normalized = "/" + normalized;
        }
        if (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }

    private static String prefixWithContextPath(final String contextPath, final String assetPath) {
        return contextPath.isEmpty() || assetPath.startsWith(contextPath)
                ? assetPath : contextPath + assetPath;
    }

    private String loadTemplate() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(TEMPLATE_PATH)) {
            if (is == null) {
                log.error("Stoplight Elements HTML template not found at classpath:{}", TEMPLATE_PATH);
                throw new IllegalStateException(
                        "Stoplight Elements HTML template not found at classpath:" + TEMPLATE_PATH);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load Stoplight Elements HTML template", e);
        }
    }

    private static String escapeHtml(final String input) {
        if (input == null) {
            return "";
        }
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
