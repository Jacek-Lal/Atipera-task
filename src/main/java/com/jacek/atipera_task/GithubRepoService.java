package com.jacek.atipera_task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
final class GithubRepoService {

    private final GithubClient githubClient;

    GithubRepoService(GithubClient githubClient){
        this.githubClient = githubClient;
    }

    List<RepoResponse> getUserRepos(String username) {
        List<GithubClient.GithubRepo> response = githubClient.getUserRepos(username);

        List<RepoResponse> repos = new ArrayList<>();

        for(GithubClient.GithubRepo repo : response){
            if (repo.fork()) continue;

            List<RepoResponse.BranchResponse> branches = githubClient
                    .getRepoBranches(repo.owner().login(), repo.name())
                    .stream()
                    .map(b -> new RepoResponse.BranchResponse(b.name(), b.commit().sha()))
                    .toList();

            repos.add(new RepoResponse(repo.name(), repo.owner().login(), branches));
        }

        return repos;
    }
}
