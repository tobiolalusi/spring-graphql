/*
 * Copyright 2002-2021 the original author or authors.
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

package org.springframework.graphql.boot;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.graphql.execution.GraphQlSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link GraphQlAutoConfiguration}
 */
class GraphQlAutoConfigurationTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(GraphQlAutoConfiguration.class));

	@Test
	void shouldFailWhenSchemaFileIsMissing() {
		this.contextRunner.run((context) -> {
			assertThat(context).hasFailed();
			assertThat(context).getFailure().getRootCause().hasMessage("'schemaResource(s)' not provided");
		});
	}

	@Test
	void shouldCreateBuilderWithSdl() {
		this.contextRunner.withPropertyValues("spring.graphql.schema.location:classpath:books/*.graphqls")
				.run((context) -> assertThat(context).hasSingleBean(GraphQlSource.class));
	}

	@Test
	void shouldUseProgrammaticallyDefinedBuilder() {
		this.contextRunner.withPropertyValues("spring.graphql.schema.location:classpath:books/*.graphqls")
				.withUserConfiguration(CustomGraphQlBuilderConfiguration.class).run((context) -> {
					assertThat(context).hasBean("customGraphQlSourceBuilder");
					assertThat(context).hasSingleBean(GraphQlSource.Builder.class);
				});
	}

	@Configuration(proxyBeanMethods = false)
	static class CustomGraphQlBuilderConfiguration {

		@Bean
		GraphQlSource.Builder customGraphQlSourceBuilder() {
			List<Resource> schemaResources = new ArrayList<>();
			schemaResources.add(new ClassPathResource("books/schema.graphqls"));
			schemaResources.add(new ClassPathResource("books/type.graphqls"));
			return GraphQlSource.builder().schemaResources(schemaResources);
		}

	}

}
