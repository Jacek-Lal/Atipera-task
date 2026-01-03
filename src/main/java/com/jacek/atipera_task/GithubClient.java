package com.jacek.atipera_task;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
final class GithubClient {

    private final RestClient restClient;

    GithubClient (RestClient.Builder builder, @Value("${github.api.base-url}") String baseUrl){
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    List<GithubRepo> getUserRepos(String username){
        return restClient.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .body(new ParameterizedTypeReference<List<GithubRepo>>(){});
    }

    record GithubRepo(String name, Owner owner, boolean fork){
        record Owner(String login){}
    }
}
