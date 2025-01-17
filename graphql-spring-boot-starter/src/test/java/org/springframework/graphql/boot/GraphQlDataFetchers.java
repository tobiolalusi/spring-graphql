/*
 * Copyright 2020-2021 the original author or authors.
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

import java.util.Arrays;
import java.util.List;

import graphql.schema.DataFetcher;
import reactor.core.publisher.Flux;

public final class GraphQlDataFetchers {

	private static List<Book> books = Arrays.asList(new Book("book-1", "GraphQL for beginners", 100, "John GraphQL"),
			new Book("book-2", "Harry Potter and the Philosopher's Stone", 223, "Joanne Rowling"),
			new Book("book-3", "Moby Dick", 635, "Moby Dick"), new Book("book-3", "Moby Dick", 635, "Moby Dick"));

	private GraphQlDataFetchers() {

	}

	public static DataFetcher getBookByIdDataFetcher() {
		return (environment) -> books.stream().filter((book) -> book.getId().equals(environment.getArgument("id")))
				.findFirst().orElse(null);
	}

	public static DataFetcher getBooksOnSale() {
		return (environment) -> Flux.fromIterable(books)
				.filter((book) -> book.getPageCount() >= (int) environment.getArgument("minPages"));
	}

}
