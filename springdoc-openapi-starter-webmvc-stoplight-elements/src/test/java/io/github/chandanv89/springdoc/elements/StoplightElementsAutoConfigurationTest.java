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
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;

class StoplightElementsAutoConfigurationTest {

    private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(StoplightElementsAutoConfiguration.class));

    @Test
    void autoConfiguration_registersAllBeans() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(StoplightElementsConfigProperties.class);
            assertThat(context).hasSingleBean(StoplightElementsIndexTransformer.class);
            assertThat(context).hasSingleBean(StoplightElementsWelcomeController.class);
            assertThat(context).hasSingleBean(StoplightElementsWebMvcConfigurer.class);
        });
    }

    @Test
    void autoConfiguration_disabled_whenPropertySetToFalse() {
        contextRunner
                .withPropertyValues("springdoc.stoplight-elements.enabled=false")
                .run(context -> {
                    assertThat(context).doesNotHaveBean(StoplightElementsIndexTransformer.class);
                    assertThat(context).doesNotHaveBean(StoplightElementsWelcomeController.class);
                    assertThat(context).doesNotHaveBean(StoplightElementsWebMvcConfigurer.class);
                });
    }

    @Test
    void autoConfiguration_bindsCustomProperties() {
        contextRunner
                .withPropertyValues(
                        "springdoc.stoplight-elements.path=/docs",
                        "springdoc.stoplight-elements.spec-url=/custom/api-docs",
                        "springdoc.stoplight-elements.title=My API",
                        "springdoc.stoplight-elements.use-cdn=true",
                        "springdoc.stoplight-elements.layout=stacked",
                        "springdoc.stoplight-elements.router=history",
                        "springdoc.stoplight-elements.hide-try-it=true",
                        "springdoc.stoplight-elements.hide-export=true"
                )
                .run(context -> {
                    var props = context.getBean(StoplightElementsConfigProperties.class);
                    assertThat(props.getPath()).isEqualTo("/docs");
                    assertThat(props.getSpecUrl()).isEqualTo("/custom/api-docs");
                    assertThat(props.getTitle()).isEqualTo("My API");
                    assertThat(props.isUseCdn()).isTrue();
                    assertThat(props.getLayout()).isEqualTo("stacked");
                    assertThat(props.getRouter()).isEqualTo("history");
                    assertThat(props.getHideTryIt()).isTrue();
                    assertThat(props.getHideExport()).isTrue();
                });
    }

    @Test
    void autoConfiguration_respectsConditionalOnMissingBean() {
        contextRunner
                .withBean(StoplightElementsIndexTransformer.class,
                        () -> new StoplightElementsIndexTransformer(new StoplightElementsConfigProperties()))
                .run(context ->
                        assertThat(context).hasSingleBean(StoplightElementsIndexTransformer.class));
    }
}
