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

package org.springframework.graphql;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.idl.RuntimeWiring;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.graphql.execution.DataFetcherExceptionResolver;
import org.springframework.graphql.execution.GraphQlSource;

/**
 * Utility methods for GraphQL tests.
 */
public abstract class GraphQlTestUtils {

	public static GraphQL initGraphQl(String schemaContent, String typeName, String fieldName, DataFetcher<?> fetcher) {

		return initGraphQlSource(Collections.singletonList(schemaContent), typeName, fieldName, fetcher).build()
				.graphQl();
	}

	public static GraphQL initGraphQl(List<String> schemas, String typeName, String fieldName, DataFetcher<?> fetcher) {

		return initGraphQlSource(schemas, typeName, fieldName, fetcher).build().graphQl();
	}

	public static GraphQL initGraphQl(String schemaContent, String typeName, String fieldName, DataFetcher<?> fetcher,
			DataFetcherExceptionResolver... resolvers) {

		return initGraphQlSource(Collections.singletonList(schemaContent), typeName, fieldName, fetcher)
				.exceptionResolvers(Arrays.asList(resolvers)).build().graphQl();
	}

	public static GraphQlSource.Builder initGraphQlSource(List<String> schemas, String typeName, String fieldName,
			DataFetcher<?> fetcher) {

		RuntimeWiring wiring = RuntimeWiring.newRuntimeWiring()
				.type(typeName, (builder) -> builder.dataFetcher(fieldName, fetcher)).build();

		List<Resource> resources = new ArrayList<>();
		for (String schemaContent : schemas) {
			resources.add(new ByteArrayResource(schemaContent.getBytes(StandardCharsets.UTF_8)));
		}
		return GraphQlSource.builder().schemaResources(resources).runtimeWiring(wiring);
	}

}
