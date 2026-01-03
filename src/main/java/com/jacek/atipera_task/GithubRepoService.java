package com.jacek.atipera_task;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
final class GithubRepoService {

    private final GithubClient githubClient;

    GithubRepoService(GithubClient githubClient){
        this.githubClient = githubClient;
    }

    List<RepoResponse> getUserRepos(String username) {
        List<GithubClient.GithubRepo> response = githubClient.getUserRepos(username);

        return response.stream()
                .filter(repo -> !repo.fork())
                .map(repo -> new RepoResponse(repo.name(), repo.owner().login(), List.of()))
                .toList();
    }
}
