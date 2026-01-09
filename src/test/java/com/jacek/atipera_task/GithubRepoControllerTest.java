package com.jacek.atipera_task;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class GithubRepoControllerTest {

    @RegisterExtension
    static WireMockExtension wiremock = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) {
        registry.add("github.api.base-url", wiremock::baseUrl);
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnNonForkReposWithBranches() throws Exception {
        wiremock.stubFor(get(urlEqualTo("/users/john/repos"))
                .willReturn(okJson("""
            [
              { "name": "non-forked-repo", "fork": false, "owner": { "login": "john" } },
              { "name": "forked-repo", "fork": true, "owner": { "login": "john" } }
            ]
            """)));

        wiremock.stubFor(get(urlEqualTo("/repos/john/non-forked-repo/branches"))
                .willReturn(okJson("""
            [
              { "name": "main", "commit": { "sha": "qwe123" } },
              { "name": "dev",  "commit": { "sha": "asd123" } }
            ]
            """)));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/john/repos"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].repository_name").value("non-forked-repo"))
                .andExpect(jsonPath("$[0].owner_login").value("john"))
                .andExpect(jsonPath("$[0].branches", hasSize(2)))
                .andExpect(jsonPath("$[0].branches[?(@.name=='main')].last_commit_sha").value("qwe123"))
                .andExpect(jsonPath("$[0].branches[?(@.name=='dev')].last_commit_sha").value("asd123"));

        wiremock.verify(1, getRequestedFor(urlEqualTo("/users/john/repos")));
        wiremock.verify(1, getRequestedFor(urlEqualTo("/repos/john/non-forked-repo/branches")));
        wiremock.verify(0, getRequestedFor(urlEqualTo("/repos/john/forked-repo/branches")));
    }

    @Test
    void shouldReturn404InRequiredFormatWhenUserDoesNotExist() throws Exception {
        wiremock.stubFor(get(urlEqualTo("/users/missing-user/repos"))
                .willReturn(aResponse().withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                        {
                            "message": "Not Found",
                            "documentation_url": "https://docs.github.com/rest/repos/repos#list-repositories-for-a-user",
                            "status": "404"
                        }
                        """)
                ));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/missing-user/repos"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("User does not exist"));

        wiremock.verify(1, getRequestedFor(urlEqualTo("/users/missing-user/repos")));
    }
}
