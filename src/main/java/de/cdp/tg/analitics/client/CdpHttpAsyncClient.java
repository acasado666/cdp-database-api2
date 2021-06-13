package de.cdp.tg.analitics.client;

import de.cdp.tg.analitics.config.ApplicationConfig;
import de.cdp.tg.analitics.model.SessionInsight;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


@Component
@RequiredArgsConstructor
public class CdpHttpAsyncClient {

    private final ApplicationConfig config;

    private static final Logger LOGGER = LoggerFactory.getLogger(CdpHttpAsyncClient.class);


    public List<String> getCdp(Integer userId) throws InterruptedException, ExecutionException, TimeoutException {

        String id = String.valueOf(userId);
        LOGGER.trace("Retrieve data Tigergraph");


        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(getUrlBuilder(id)))
                .setHeader("Content-Type", "application/json")
                .build();

        HttpClient httpAsyncClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(1))
                .build();


        // sendAsync(): Sends the given request asynchronously using this client with the given response body handler.
        CompletableFuture<HttpResponse<String>> asyncResponse = httpAsyncClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        String asyncResultBody = asyncResponse.thenApply(HttpResponse::body).get(config.getGraphDbConnectTimeoutMs(), TimeUnit.MILLISECONDS);
        LOGGER.trace("Connection Succesful");

        return getFormattedStringList(asyncResultBody);
    }

    private List<String> getFormattedStringList(String body) {
        String substring = body.substring(1, body.length() - 1);
        return Arrays.asList(substring.split(",", -1));
    }

    private String getUrlBuilder(String userId) {

        return new StringBuilder()
                .append(config.getGraphDbBaseUrl())
                .append(String.format("?userId=%s", userId))
                .toString();
    }


    public static List<SessionInsight> userSessionListFaker = new ArrayList<>(
            List.of(new SessionInsight("", "", "Higher education","",
                            "e7qSVwW4CoiUzQ16217874", "", "", "education", "",
                            "", "609c9b16-4fae-de40-8398-4be12d511106", "", 1621788384),
                    new SessionInsight("", "Unemployed", "Middle education","",
                            "s8fd21345iWzQ1921734se", "", "", "education", "",
                            "", "6779c9b99-aaa6-de40-8398-4be12d511109", "45", 702588384),
                    new SessionInsight("56000", "Employed", "No education","",
                            "d9rtwW4CoiSe23621we23", "", "", "education", "",
                            "", "888c9b16-56f7-de40-8398-88e12d511888", "", 894788384)));
}
