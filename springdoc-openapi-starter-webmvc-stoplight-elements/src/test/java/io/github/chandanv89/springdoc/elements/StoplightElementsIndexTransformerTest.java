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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class StoplightElementsIndexTransformerTest {

    @Test
    void render_replacesTitle() {
        var props = new StoplightElementsConfigProperties();
        props.setTitle("My Custom API Docs");
        var transformer = new StoplightElementsIndexTransformer(props);

        var html = transformer.render();
        assertThat(html).contains("<title>My Custom API Docs</title>");
    }

    @Test
    void render_replacesSpecUrl() {
        var props = new StoplightElementsConfigProperties();
        props.setSpecUrl("/custom/api-docs");
        var transformer = new StoplightElementsIndexTransformer(props);

        var html = transformer.render();
        assertThat(html).contains("apiDescriptionUrl=\"/custom/api-docs\"");
    }

    @Test
    void render_usesBundledAssetsByDefault() {
        var props = new StoplightElementsConfigProperties();
        var transformer = new StoplightElementsIndexTransformer(props);

        var html = transformer.render();
        assertThat(html).contains("src=\"/webjars/stoplight-elements/web-components.min.js\"");
        assertThat(html).contains("href=\"/webjars/stoplight-elements/styles.min.css\"");
        assertThat(html).contains("href=\"/stoplight-elements/favicon.ico\"");
    }

    @Test
    void render_usesCdnUrlsWhenEnabled() {
        var props = new StoplightElementsConfigProperties();
        props.setUseCdn(true);
        props.setCdnJsUrl("https://cdn.example.com/elements.js");
        props.setCdnCssUrl("https://cdn.example.com/elements.css");
        var transformer = new StoplightElementsIndexTransformer(props);

        var html = transformer.render();
        assertThat(html).contains("src=\"https://cdn.example.com/elements.js\"");
        assertThat(html).contains("href=\"https://cdn.example.com/elements.css\"");
        assertThat(html).doesNotContain("src=\"/webjars/stoplight-elements/");
    }

    @Test
    void render_withContextPath_prefixesBundledAssetUrls() {
        var props = new StoplightElementsConfigProperties();
        var transformer = new StoplightElementsIndexTransformer(props);

        var html = transformer.render("/example-app");
        assertThat(html).contains("src=\"/example-app/webjars/stoplight-elements/web-components.min.js\"");
        assertThat(html).contains("href=\"/example-app/webjars/stoplight-elements/styles.min.css\"");
        assertThat(html).contains("href=\"/example-app/stoplight-elements/favicon.ico\"");
        assertThat(html).contains("apiDescriptionUrl=\"/example-app/v3/api-docs\"");
    }

    @Test
    void render_includesElementsAttributes() {
        var props = new StoplightElementsConfigProperties();
        props.setHideTryIt(true);
        props.setHideExport(true);
        props.setLayout("stacked");
        var transformer = new StoplightElementsIndexTransformer(props);

        var html = transformer.render();
        assertThat(html).contains("hideTryIt=\"true\"");
        assertThat(html).contains("hideExport=\"true\"");
        assertThat(html).contains("layout=\"stacked\"");
    }

    @Test
    void render_includesDefaultLayoutAndRouter() {
        var props = new StoplightElementsConfigProperties();
        var transformer = new StoplightElementsIndexTransformer(props);

        var html = transformer.render();
        assertThat(html).contains("layout=\"sidebar\"");
        assertThat(html).contains("router=\"hash\"");
    }

    @Test
    void render_producesValidHtml() {
        var props = new StoplightElementsConfigProperties();
        var transformer = new StoplightElementsIndexTransformer(props);

        var html = transformer.render();
        assertThat(html).startsWith("<!DOCTYPE html>");
        assertThat(html).contains("<html lang=\"en\">");
        assertThat(html).contains("</html>");
        assertThat(html).contains("<elements-api");
        assertThat(html).contains("</elements-api>");
        assertThat(html).contains("</body>");
    }

    @Test
    void render_defaultValues_producesExpectedOutput() {
        var props = new StoplightElementsConfigProperties();
        var transformer = new StoplightElementsIndexTransformer(props);

        var html = transformer.render();
        assertThat(html).contains("<title>API Documentation</title>");
        assertThat(html).contains("apiDescriptionUrl=\"/v3/api-docs\"");
        assertThat(html).contains("src=\"/webjars/stoplight-elements/web-components.min.js\"");
        assertThat(html).contains("href=\"/webjars/stoplight-elements/styles.min.css\"");
        assertThat(html).contains("href=\"/stoplight-elements/favicon.ico\"");
        assertThat(html).contains("layout=\"sidebar\"");
        assertThat(html).contains("router=\"hash\"");
    }

    @Test
    void render_includesLogoAttribute() {
        var props = new StoplightElementsConfigProperties();
        props.setLogo("https://example.com/logo.png");
        var transformer = new StoplightElementsIndexTransformer(props);

        var html = transformer.render();
        assertThat(html).contains("logo=\"https://example.com/logo.png\"");
    }

    @Test
    void render_includesTryItCorsProxy() {
        var props = new StoplightElementsConfigProperties();
        props.setTryItCorsProxy("https://cors.example.com");
        var transformer = new StoplightElementsIndexTransformer(props);

        var html = transformer.render();
        assertThat(html).contains("tryItCorsProxy=\"https://cors.example.com\"");
    }
}
