package de.cdp.tg.analitics.service;

import de.cdp.tg.analitics.client.CdpHttpAsyncClient;
import de.cdp.tg.analitics.model.SessionInsight;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static de.cdp.tg.analitics.client.CdpHttpAsyncClient.userSessionListFaker;

@Service
@RequiredArgsConstructor
public class CdpTigerGraphService {

    private CdpHttpAsyncClient cdpHttpAsyncClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(CdpTigerGraphService.class);

    @Async("asyncExecutor")
    @Cacheable(cacheNames="customerDataCache", key="#id")
    public List<String> getUserInsightData(Integer id) throws InterruptedException, ExecutionException, TimeoutException {
        return  cdpHttpAsyncClient.getCdp(id);
    }

    public List<SessionInsight> getAllSessionInsights() {
        userSessionListFaker.forEach(SessionInsight -> SessionInsight.setEventReceivedTimestamp((int)Math.random()));
        return userSessionListFaker;
    }

    public SessionInsight getSessionInsightByUserId(String id) {
        String fakeSession = "e7qSVwW4CoiUzQ16217874";
        String fakeId = "609c9b16-4fae-de40-8398-4be12d511106";

        SessionInsight sessionInsight = userSessionListFaker.stream()
                .filter(userInsight -> userInsight.getUser().equals(fakeId)).peek(user -> {
            user.setEventReceivedTimestamp((int) Math.random());
            user.setSessionId(fakeSession);
        }).findAny().orElse(null);
        return sessionInsight;
    }
}
