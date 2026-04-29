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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the Stoplight Elements API documentation UI.
 *
 * <p>All properties are bound under the {@code springdoc.stoplight-elements} prefix.
 * Elements-native options are converted to HTML attributes on the {@code <elements-api>} element.</p>
 *
 * @author Chandan Veerabhadrappa (@chandanv89)
 */
@ConfigurationProperties(prefix = "springdoc.stoplight-elements")
public class StoplightElementsConfigProperties {

    /** Whether the Stoplight Elements UI endpoint is enabled. */
    private boolean enabled = true;

    /** The URL path where the Stoplight Elements UI is served. */
    private String path = "/api-docs";

    /** The URL of the OpenAPI specification to render. */
    private String specUrl = "/v3/api-docs";

    /** The HTML page title for the Stoplight Elements documentation page. */
    private String title = "API Documentation";

    /**
     * Whether to load the Elements JS and CSS from a CDN instead of the bundled version.
     * Set to {@code true} for CDN mode; {@code false} (default) uses the bundled assets.
     */
    private boolean useCdn = false;

    /** The CDN URL for the Stoplight Elements web component JS file. Used only when {@code useCdn} is {@code true}. */
    private String cdnJsUrl = "https://unpkg.com/@stoplight/elements/web-components.min.js";

    /** The CDN URL for the Stoplight Elements CSS file. Used only when {@code useCdn} is {@code true}. */
    private String cdnCssUrl = "https://unpkg.com/@stoplight/elements/styles.min.css";

    /**
     * The layout mode for Stoplight Elements. Options:
     * <ul>
     *   <li>{@code sidebar} — (default) three-column design with a resizable sidebar</li>
     *   <li>{@code responsive} — like sidebar, but collapses to a drawer on small screens</li>
     *   <li>{@code stacked} — single-column layout</li>
     * </ul>
     */
    private String layout = "sidebar";

    /**
     * Determines how navigation should work. Options:
     * <ul>
     *   <li>{@code hash} — (default) uses the hash portion of the URL</li>
     *   <li>{@code history} — uses the HTML5 history API</li>
     *   <li>{@code memory} — keeps history in memory only</li>
     *   <li>{@code static} — renders using a static router</li>
     * </ul>
     */
    private String router = "hash";

    /** When {@code true}, filters out any content marked as internal with {@code x-internal}. */
    private Boolean hideInternal;

    /** When {@code true}, hides the Try It feature completely. */
    private Boolean hideTryIt;

    /** When {@code true}, hides the Try It panel while still displaying the Request Sample. */
    private Boolean hideTryItPanel;

    /** When {@code true}, hides the schemas in the Table of Contents (sidebar layout only). */
    private Boolean hideSchemas;

    /** When {@code true}, hides the Export button on the overview section. */
    private Boolean hideExport;

    /** URL of a CORS proxy used to send requests via the Try It feature. */
    private String tryItCorsProxy;

    /**
     * Credential policy for the Try It feature. Options:
     * {@code omit} (default), {@code include}, {@code same-origin}.
     */
    private String tryItCredentialsPolicy;

    /** URL to an image displayed as a small square logo next to the title, above the table of contents. */
    private String logo;

    /**
     * Helps when using {@code router: history} but docs are in a subdirectory
     * (e.g. {@code https://example.com/docs/api}).
     */
    private String basePath;

    /**
     * Builds a map of non-null Stoplight Elements options as camelCase HTML attribute names to their values.
     *
     * @return a map of Elements HTML attributes
     */
    public Map<String, String> buildElementsAttributes() {
        final var attributes = new LinkedHashMap<String, String>();
        addIfNotNull(attributes, "layout", layout);
        addIfNotNull(attributes, "router", router);
        addIfNotNull(attributes, "hideInternal", hideInternal);
        addIfNotNull(attributes, "hideTryIt", hideTryIt);
        addIfNotNull(attributes, "hideTryItPanel", hideTryItPanel);
        addIfNotNull(attributes, "hideSchemas", hideSchemas);
        addIfNotNull(attributes, "hideExport", hideExport);
        addIfNotNull(attributes, "tryItCorsProxy", tryItCorsProxy);
        addIfNotNull(attributes, "tryItCredentialsPolicy", tryItCredentialsPolicy);
        addIfNotNull(attributes, "logo", logo);
        addIfNotNull(attributes, "basePath", basePath);
        return attributes;
    }

    /**
     * Converts the Elements options into an HTML attribute string for the {@code <elements-api>} element.
     *
     * @return a string of HTML attributes, e.g. {@code layout="sidebar" router="hash" hideTryIt="true"}
     */
    public String buildElementsAttributeString() {
        final var attributes = buildElementsAttributes();
        if (attributes.isEmpty()) {
            return "";
        }
        final var joiner = new StringJoiner(" ");
        attributes.forEach((key, value) ->
                joiner.add(key + "=\"" + value + "\""));
        return joiner.toString();
    }

    private void addIfNotNull(final Map<String, String> map, final String key, final Object value) {
        if (value != null) {
            map.put(key, value.toString());
        }
    }

    // --- Getters and setters ---

    /** @return whether the Stoplight Elements UI endpoint is enabled */
    public boolean isEnabled() {
        return enabled;
    }

    /** @param enabled whether the Stoplight Elements UI endpoint is enabled */
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    /** @return the URL path where the Stoplight Elements UI is served */
    public String getPath() {
        return path;
    }

    /** @param path the URL path where the Stoplight Elements UI is served */
    public void setPath(final String path) {
        this.path = path;
    }

    /** @return the URL of the OpenAPI specification to render */
    public String getSpecUrl() {
        return specUrl;
    }

    /** @param specUrl the URL of the OpenAPI specification to render */
    public void setSpecUrl(final String specUrl) {
        this.specUrl = specUrl;
    }

    /** @return the HTML page title */
    public String getTitle() {
        return title;
    }

    /** @param title the HTML page title */
    public void setTitle(final String title) {
        this.title = title;
    }

    /** @return whether to load Elements assets from CDN */
    public boolean isUseCdn() {
        return useCdn;
    }

    /** @param useCdn whether to load Elements assets from CDN */
    public void setUseCdn(final boolean useCdn) {
        this.useCdn = useCdn;
    }

    /** @return the CDN URL for the Elements web component JS */
    public String getCdnJsUrl() {
        return cdnJsUrl;
    }

    /** @param cdnJsUrl the CDN URL for the Elements web component JS */
    public void setCdnJsUrl(final String cdnJsUrl) {
        this.cdnJsUrl = cdnJsUrl;
    }

    /** @return the CDN URL for the Elements CSS */
    public String getCdnCssUrl() {
        return cdnCssUrl;
    }

    /** @param cdnCssUrl the CDN URL for the Elements CSS */
    public void setCdnCssUrl(final String cdnCssUrl) {
        this.cdnCssUrl = cdnCssUrl;
    }

    /** @return the layout mode */
    public String getLayout() {
        return layout;
    }

    /** @param layout the layout mode */
    public void setLayout(final String layout) {
        this.layout = layout;
    }

    /** @return the router mode */
    public String getRouter() {
        return router;
    }

    /** @param router the router mode */
    public void setRouter(final String router) {
        this.router = router;
    }

    /** @return whether internal content is hidden */
    public Boolean getHideInternal() {
        return hideInternal;
    }

    /** @param hideInternal whether internal content is hidden */
    public void setHideInternal(final Boolean hideInternal) {
        this.hideInternal = hideInternal;
    }

    /** @return whether Try It is hidden */
    public Boolean getHideTryIt() {
        return hideTryIt;
    }

    /** @param hideTryIt whether Try It is hidden */
    public void setHideTryIt(final Boolean hideTryIt) {
        this.hideTryIt = hideTryIt;
    }

    /** @return whether the Try It panel is hidden */
    public Boolean getHideTryItPanel() {
        return hideTryItPanel;
    }

    /** @param hideTryItPanel whether the Try It panel is hidden */
    public void setHideTryItPanel(final Boolean hideTryItPanel) {
        this.hideTryItPanel = hideTryItPanel;
    }

    /** @return whether schemas are hidden in the sidebar */
    public Boolean getHideSchemas() {
        return hideSchemas;
    }

    /** @param hideSchemas whether schemas are hidden in the sidebar */
    public void setHideSchemas(final Boolean hideSchemas) {
        this.hideSchemas = hideSchemas;
    }

    /** @return whether the export button is hidden */
    public Boolean getHideExport() {
        return hideExport;
    }

    /** @param hideExport whether the export button is hidden */
    public void setHideExport(final Boolean hideExport) {
        this.hideExport = hideExport;
    }

    /** @return the CORS proxy URL for Try It */
    public String getTryItCorsProxy() {
        return tryItCorsProxy;
    }

    /** @param tryItCorsProxy the CORS proxy URL for Try It */
    public void setTryItCorsProxy(final String tryItCorsProxy) {
        this.tryItCorsProxy = tryItCorsProxy;
    }

    /** @return the credentials policy for Try It */
    public String getTryItCredentialsPolicy() {
        return tryItCredentialsPolicy;
    }

    /** @param tryItCredentialsPolicy the credentials policy for Try It */
    public void setTryItCredentialsPolicy(final String tryItCredentialsPolicy) {
        this.tryItCredentialsPolicy = tryItCredentialsPolicy;
    }

    /** @return the logo URL */
    public String getLogo() {
        return logo;
    }

    /** @param logo the logo URL */
    public void setLogo(final String logo) {
        this.logo = logo;
    }

    /** @return the base path for history router */
    public String getBasePath() {
        return basePath;
    }

    /** @param basePath the base path for history router */
    public void setBasePath(final String basePath) {
        this.basePath = basePath;
    }
}
