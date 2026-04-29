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
import org.junit.jupiter.api.Test;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ReactiveWebApplicationContextRunner;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.assertj.core.api.Assertions.assertThat;

class StoplightElementsWebFluxAutoConfigurationTest {

    private final ReactiveWebApplicationContextRunner contextRunner =
            new ReactiveWebApplicationContextRunner()
                    .withConfiguration(AutoConfigurations.of(
                            StoplightElementsWebFluxAutoConfiguration.class));

    @Test
    void autoConfiguration_registersAllBeans() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(StoplightElementsConfigProperties.class);
            assertThat(context).hasSingleBean(StoplightElementsIndexTransformer.class);
            assertThat(context).hasSingleBean(RouterFunction.class);
            assertThat(context).hasSingleBean(StoplightElementsWebFluxResourceConfigurer.class);
        });
    }

    @Test
    void autoConfiguration_disabled_whenPropertySetToFalse() {
        contextRunner
                .withPropertyValues("springdoc.stoplight-elements.enabled=false")
                .run(context -> {
                    assertThat(context).doesNotHaveBean(StoplightElementsIndexTransformer.class);
                    assertThat(context).doesNotHaveBean(RouterFunction.class);
                    assertThat(context)
                            .doesNotHaveBean(StoplightElementsWebFluxResourceConfigurer.class);
                });
    }

    @Test
    void autoConfiguration_bindsCustomProperties() {
        contextRunner
                .withPropertyValues(
                        "springdoc.stoplight-elements.path=/docs",
                        "springdoc.stoplight-elements.title=Reactive API",
                        "springdoc.stoplight-elements.layout=stacked",
                        "springdoc.stoplight-elements.router=history"
                )
                .run(context -> {
                    var props = context.getBean(StoplightElementsConfigProperties.class);
                    assertThat(props.getPath()).isEqualTo("/docs");
                    assertThat(props.getTitle()).isEqualTo("Reactive API");
                    assertThat(props.getLayout()).isEqualTo("stacked");
                    assertThat(props.getRouter()).isEqualTo("history");
                });
    }
}
