package com.jacek.atipera_task;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@SpringBootApplication
public class AtiperaTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(AtiperaTaskApplication.class, args);
	}

	@Bean
	RestClient githubRestClient(RestClient.Builder builder,
								@Value("${github.api.base-url}") String baseUrl) {
		return builder.baseUrl(baseUrl).build();
	}
}
