package de.cdp.tg.analitics.api;

import de.cdp.tg.analitics.model.SessionInsight;
import de.cdp.tg.analitics.service.CdpTigerGraphService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/v1/insights")
@RequiredArgsConstructor
public class CdpTigerGraphController {

    private final CdpTigerGraphService cdpTigerGraphService;

    @GetMapping
    public List<SessionInsight> getAllUserInsights() {
        List<SessionInsight> allInsights = cdpTigerGraphService.getAllSessionInsights();
        return allInsights;
    }

    @GetMapping("/{userId}")
    public SessionInsight getSessionInsightByUserId(@PathVariable String userId) {
        return cdpTigerGraphService.getSessionInsightByUserId(userId);
    }

    @GetMapping("/details/{userData}")
    public List<String> getUserData(@PathVariable Integer userId) throws InterruptedException, ExecutionException, TimeoutException {
        return cdpTigerGraphService.getUserInsightData(userId);
    }
}
