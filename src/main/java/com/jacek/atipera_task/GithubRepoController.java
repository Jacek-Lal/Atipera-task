package com.jacek.atipera_task;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
final class GithubRepoController {

    private final GithubRepoService githubRepoService;

    GithubRepoController(GithubRepoService githubRepoService){
        this.githubRepoService = githubRepoService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<RepoResponse>> getAllUserRepos(@PathVariable String username){
        return ResponseEntity.ok().body(githubRepoService.getUserRepos(username));
    }
}
