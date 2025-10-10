package se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.infrastructure.adapter.http;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.domain.model.Developer;
import se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.domain.model.Language;
import se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.domain.model.Repo;
import se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.domain.model.SpokenLanguage;
import se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.domain.port.TrendingPort;

import java.util.List;

@Service
public class PythonTrendingClient implements TrendingPort {

    private final WebClient web;

    public PythonTrendingClient(
            @Value("${trending.api.base-url:http://127.0.0.1:8000}") String baseUrl,
            WebClient.Builder webClientBuilder
    ) {
        this.web = webClientBuilder.baseUrl(baseUrl).build();
    }

    @Override
    public List<Repo> fetchRepos(String language, String since, String spokenLanguage) {
        var uriSpec = web.get().uri(uri -> {
            var b = uri.path("/repos")
                    .queryParam("since", since);
            if (language != null) b.queryParam("language", language);
            if (spokenLanguage != null) b.queryParam("spoken_language", spokenLanguage);
            return b.build();
        }).accept(MediaType.APPLICATION_JSON);

        var response = uriSpec.retrieve()
                .bodyToMono(RepoEnvelope.class)
                .block();
        return response.items().stream().map(PythonTrendingClient::toRepo).toList();
    }

    @Override
    public List<Developer> fetchDevelopers(String language, String since) {
        var response = web.get().uri(uri -> {
            var b = uri.path("/developers").queryParam("since", since);
            if (language != null) b.queryParam("language", language);
            return b.build();
        }).retrieve().bodyToMono(DevEnvelope.class).block();

        return response.items().stream().map(PythonTrendingClient::toDeveloper).toList();
    }

    @Override
    public List<Language> listLanguages() {
        return web.get().uri("/languages").retrieve()
                .bodyToFlux(LanguageDto.class)
                .map(d -> new Language(d.param, d.name))
                .collectList().block();
    }

    @Override
    public List<SpokenLanguage> listSpokenLanguages() {
        return web.get().uri("/spoken-languages").retrieve()
                .bodyToFlux(SpokenLangDto.class)
                .map(d -> new SpokenLanguage(d.code, d.name))
                .collectList().block();
    }

    // === DTOs matching FastAPI JSON ===
    private record RepoEnvelope(int count, List<RepoDto> items) {}
    private record DevEnvelope(int count, List<DevDto> items) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record RepoDto(
            String fullname, String url, String description,
            String language,
            Integer stars, Integer forks,
            @JsonProperty("currentPeriodStars") Integer currentPeriodStars,
            @JsonProperty("builtBy") List<BuiltByDto> builtBy
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record BuiltByDto(String username, String href, String avatar) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record DevDto(
            String username, String name, String url,
            @JsonProperty("repo") RepoMini repo
    ) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        private static record RepoMini(String name, String url, String description) {}
    }

    private record LanguageDto(String param, String name) {}
    private record SpokenLangDto(String code, List<String> name) {}

    // === mappers ===
    private static Repo toRepo(RepoDto d) {
        List<String> builtBy = (d.builtBy == null) ? List.of() : d.builtBy.stream().map(BuiltByDto::username).toList();
        return new Repo(d.fullname, d.url, d.description, d.language, d.stars, d.forks, d.currentPeriodStars, builtBy);
        // adjust mapping if your FastAPI returns slightly different fields
    }

    private static Developer toDeveloper(DevDto d) {
        String rn = d.repo != null ? d.repo.name() : null;
        String ru = d.repo != null ? d.repo.url() : null;
        String rd = d.repo != null ? d.repo.description() : null;
        return new Developer(d.username, d.name, d.url, rn, ru, rd);
    }
}
