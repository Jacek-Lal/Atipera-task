package com.jacek.atipera_task;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
final class GithubClient {

    private final RestClient restClient;

    GithubClient (RestClient githubRestClient){
        this.restClient = githubRestClient;
    }

    List<GithubRepo> getUserRepos(String username){
        try {
            List<GithubRepo> response = restClient.get()
                    .uri("/users/{username}/repos", username)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<GithubRepo>>(){});

            return response != null ? response : List.of();
        } catch (HttpClientErrorException.NotFound e){
            throw new UserNotFoundException(username, e);
        }
    }

    List<GithubBranch> getRepoBranches(String username, String repoName){
        return restClient.get()
                .uri("/repos/{username}/{repoName}/branches", username, repoName)
                .retrieve()
                .body(new ParameterizedTypeReference<List<GithubBranch>>(){});
    }

    record GithubRepo(String name, Owner owner, boolean fork){
        record Owner(String login){}
    }

    record GithubBranch(String name, Commit commit){
        record Commit(String sha){}
    }
}
