package com.jacek.atipera_task;

import java.util.List;

record RepoResponse(String repository_name,
                           String owner_login,
                           List<BranchResponse> branches) {

    record BranchResponse(String name, String last_commit_sha){}
}

