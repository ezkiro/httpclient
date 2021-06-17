package com.toyfactory.httpclient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.ForkJoinPool;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final RestTemplate simpleRestTemplate;
    private final RestTemplate restTemplateWithPool;

    private final AsyncService asyncService;

    @GetMapping("/single/{delay}")
    public String testSingle(@PathVariable int delay) {
        log.debug("testSingle start");

        String url = "http://localhost:8080/delay/" + delay;

        ResponseEntity<String> response = simpleRestTemplate.getForEntity(url, String.class);

        log.debug("testSingle response:{}", response.toString());

        return response.getBody();
    }

    @GetMapping("/pool/{count}")
    public String testPool(@PathVariable int count) {
        String url = "http://www.test99.co.kr:8081/";

        ResponseEntity<String> response = simpleRestTemplate.getForEntity(url, String.class);

        log.debug("testPool[{}] response:{}", count, response.toString());
        return response.getBody();
    }

    @GetMapping("/multi/{count}")
    public String testMulti(@PathVariable int count) {
        asyncService.runJobs(count);
        return "OK";
    }

    @GetMapping("/simple/multi/{count}")
    public String testSimpleMulti(@PathVariable int count) {
        asyncService.runSimpleJobs(count);
        return "OK";
    }

    @GetMapping("/deferred/{delay}")
    public DeferredResult<String> testDeferredResult(@PathVariable int delay) {

        log.debug("[testDeferredResult] delay:{}", delay);

        DeferredResult<String> output = new DeferredResult<>();

        ForkJoinPool.commonPool().submit(() -> {
            String url = "http://localhost:8080/delay/" + delay;
            ResponseEntity<String> response = simpleRestTemplate.getForEntity(url, String.class);
            log.debug("[testDeferredResult] external response:{}", response.toString());
            output.setResult(response.getBody());
        });

        log.debug("[testDeferredResult] done");
        return output;
    }

    @GetMapping("/self/multi/{count}")
    public String testSelfMulti(@PathVariable int count) {
        asyncService.runSelfJobs(count);
        return "testSelfMulti OK";
    }

    //https://www.baeldung.com/spring-webclient-resttemplate
    @GetMapping("/webclient/{delay}")
    public Mono<String> testAsyncWithWebClient(@PathVariable int delay) {

        log.debug("[testAsyncWithWebClient] delay:{}", delay);
        String testUrl = "http://localhost:8080/delay/" + delay;

        Mono<String> result = WebClient.create()
                .get()
                .uri(testUrl)
                .retrieve()
                .bodyToMono(String.class);

        result.subscribe(res -> {
            log.debug("[testAsyncWithWebClient] webclient call done!");
        });

        log.debug("[testAsyncWithWebClient] done");

        return result;
    }
}
