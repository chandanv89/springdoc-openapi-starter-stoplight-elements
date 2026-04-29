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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class StoplightElementsConfigPropertiesTest {

    @Test
    void defaultValues() {
        var props = new StoplightElementsConfigProperties();
        assertTrue(props.isEnabled());
        assertEquals("/api-docs", props.getPath());
        assertEquals("/v3/api-docs", props.getSpecUrl());
        assertEquals("API Documentation", props.getTitle());
        assertFalse(props.isUseCdn());
        assertEquals("https://unpkg.com/@stoplight/elements/web-components.min.js", props.getCdnJsUrl());
        assertEquals("https://unpkg.com/@stoplight/elements/styles.min.css", props.getCdnCssUrl());
        assertEquals("sidebar", props.getLayout());
        assertEquals("hash", props.getRouter());
        assertNull(props.getHideInternal());
        assertNull(props.getHideTryIt());
        assertNull(props.getHideTryItPanel());
        assertNull(props.getHideSchemas());
        assertNull(props.getHideExport());
        assertNull(props.getTryItCorsProxy());
        assertNull(props.getTryItCredentialsPolicy());
        assertNull(props.getLogo());
        assertNull(props.getBasePath());
    }

    @Test
    void buildElementsAttributes_includesLayoutAndRouterByDefault() {
        var props = new StoplightElementsConfigProperties();
        var attributes = props.buildElementsAttributes();
        assertEquals(2, attributes.size());
        assertEquals("sidebar", attributes.get("layout"));
        assertEquals("hash", attributes.get("router"));
    }

    @Test
    void buildElementsAttributes_includesExplicitOptions() {
        var props = new StoplightElementsConfigProperties();
        props.setHideTryIt(true);
        props.setHideExport(true);
        props.setLayout("stacked");
        props.setRouter("history");

        var attributes = props.buildElementsAttributes();
        assertEquals(4, attributes.size());
        assertEquals("stacked", attributes.get("layout"));
        assertEquals("history", attributes.get("router"));
        assertEquals("true", attributes.get("hideTryIt"));
        assertEquals("true", attributes.get("hideExport"));
    }

    @Test
    void buildElementsAttributes_includesAllOptions() {
        var props = new StoplightElementsConfigProperties();
        props.setLayout("responsive");
        props.setRouter("memory");
        props.setHideInternal(true);
        props.setHideTryIt(true);
        props.setHideTryItPanel(false);
        props.setHideSchemas(true);
        props.setHideExport(true);
        props.setTryItCorsProxy("https://cors.example.com");
        props.setTryItCredentialsPolicy("include");
        props.setLogo("https://example.com/logo.png");
        props.setBasePath("/docs/api");

        var attributes = props.buildElementsAttributes();
        assertEquals(11, attributes.size());
        assertEquals("responsive", attributes.get("layout"));
        assertEquals("memory", attributes.get("router"));
        assertEquals("true", attributes.get("hideInternal"));
        assertEquals("true", attributes.get("hideTryIt"));
        assertEquals("false", attributes.get("hideTryItPanel"));
        assertEquals("true", attributes.get("hideSchemas"));
        assertEquals("true", attributes.get("hideExport"));
        assertEquals("https://cors.example.com", attributes.get("tryItCorsProxy"));
        assertEquals("include", attributes.get("tryItCredentialsPolicy"));
        assertEquals("https://example.com/logo.png", attributes.get("logo"));
        assertEquals("/docs/api", attributes.get("basePath"));
    }

    @Test
    void buildElementsAttributeString_formatsCorrectly() {
        var props = new StoplightElementsConfigProperties();
        props.setHideTryIt(true);
        props.setHideExport(true);

        var result = props.buildElementsAttributeString();
        assertTrue(result.contains("layout=\"sidebar\""));
        assertTrue(result.contains("router=\"hash\""));
        assertTrue(result.contains("hideTryIt=\"true\""));
        assertTrue(result.contains("hideExport=\"true\""));
    }

    @Test
    void buildElementsAttributeString_defaultsOnly() {
        var props = new StoplightElementsConfigProperties();
        var result = props.buildElementsAttributeString();
        assertEquals("layout=\"sidebar\" router=\"hash\"", result);
    }

    @ParameterizedTest
    @CsvSource({
            "/custom-docs, /custom-docs",
            "/api/elements, /api/elements"
    })
    void setPath_updatesValue(String input, String expected) {
        var props = new StoplightElementsConfigProperties();
        props.setPath(input);
        assertEquals(expected, props.getPath());
    }

    @Test
    void cdnUrls_canBeOverridden() {
        var props = new StoplightElementsConfigProperties();
        props.setCdnJsUrl("https://cdn.example.com/elements.js");
        props.setCdnCssUrl("https://cdn.example.com/elements.css");
        assertEquals("https://cdn.example.com/elements.js", props.getCdnJsUrl());
        assertEquals("https://cdn.example.com/elements.css", props.getCdnCssUrl());
    }
}
