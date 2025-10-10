package se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.infrastructure.adapter.http;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.domain.model.Developer;
import se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.domain.model.Language;
import se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.domain.model.Repo;
import se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.domain.model.SpokenLanguage;
import se.bahram.ai.mcp.gh_trending_mcp_server_with_java.usecases.trending.domain.port.TrendingPort;

import java.net.URI;
import java.util.List;

@Service
public class PythonTrendingClient implements TrendingPort {

    private final RestTemplate rest;
    private final String baseUrl;

    public PythonTrendingClient(RestTemplate rest,
                                @Value("${trending.api.base-url:http://127.0.0.1:8000}") String baseUrl) {
        this.rest = rest;
        this.baseUrl = baseUrl;
    }

    @Override
    public List<Repo> fetchRepos(String language, String since, String spokenLanguage) {
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/repos")
                .queryParam("since", since)
                .queryParams(optional("language", language))
                .queryParams(optional("spoken_language", spokenLanguage))
                .build(true).toUri();

        RepoEnvelope env = get(uri, RepoEnvelope.class);
        return env.items().stream().map(PythonTrendingClient::toRepo).toList();
    }

    @Override
    public List<Developer> fetchDevelopers(String language, String since) {
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/developers")
                .queryParam("since", since)
                .queryParams(optional("language", language))
                .build(true).toUri();

        DevEnvelope env = get(uri, DevEnvelope.class);
        return env.items().stream().map(PythonTrendingClient::toDeveloper).toList();
    }

    @Override
    public List<Language> listLanguages() {
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl).path("/languages").build(true).toUri();
        LanguageDto[] arr = get(uri, LanguageDto[].class);
        return List.of(arr).stream().map(d -> new Language(d.param, d.name)).toList();
    }

    @Override
    public List<SpokenLanguage> listSpokenLanguages() {
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl).path("/spoken-languages").build(true).toUri();
        SpokenLangDto[] arr = get(uri, SpokenLangDto[].class);
        return List.of(arr).stream().map(d -> new SpokenLanguage(d.code, d.name)).toList();
    }

    // ------- helpers -------

    private <T> T get(URI uri, Class<T> type) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            ResponseEntity<T> resp = rest.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), type);
            if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
                return resp.getBody();
            }
            throw new IllegalStateException("Unexpected response: " + resp.getStatusCode());
        } catch (RestClientException e) {
            throw new IllegalStateException("HTTP call failed: " + uri, e);
        }
    }

    private static LinkedMultiValueMap<String, String> optional(String k, String v) {
        var m = new LinkedMultiValueMap<String, String>();
        if (v != null && !v.isBlank()) m.add(k, v);
        return m;
    }

    // ------- DTOs ------
    private record RepoEnvelope(int count, List<RepoDto> items) {}
    private record DevEnvelope(int count, List<DevDto> items) {}
    private record LanguageDto(String param, String name) {}
    private record SpokenLangDto(String code, List<String> name) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record RepoDto(
            String fullname, String url, String description, String language,
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

    // ------- mappers -------
    private static Repo toRepo(RepoDto d) {
        List<String> builtBy = (d.builtBy == null) ? List.of()
                : d.builtBy.stream().map(BuiltByDto::username).toList();
        return new Repo(d.fullname, d.url, d.description, d.language, d.stars, d.forks, d.currentPeriodStars, builtBy);
    }

    private static Developer toDeveloper(DevDto d) {
        String rn = (d.repo != null) ? d.repo.name() : null;
        String ru = (d.repo != null) ? d.repo.url() : null;
        String rd = (d.repo != null) ? d.repo.description() : null;
        return new Developer(d.username, d.name, d.url, rn, ru, rd);
    }
}
