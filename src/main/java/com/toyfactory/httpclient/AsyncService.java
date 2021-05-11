package com.toyfactory.httpclient;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AsyncService {

    private final RestTemplate restTemplateWithPool;

    private final RestTemplate simpleRestTemplate;

    private final RestTemplate restTemplateWithDefaultPool;

    public static ExecutorService executorService = Executors.newFixedThreadPool(300);

    public void runJobs(int count) {

        Runnable runnableTask = () -> {
            try {

                log.debug("runnableTask start");

                String url = "http://localhost:8080/delay/10";

                ResponseEntity<String> response = restTemplateWithPool.getForEntity(url, String.class);

                log.debug("runnableTask response:{}", response.toString());

            } catch (Exception ex) {
                log.error("[runnableTask] exception:" + ex.getMessage());
            }
        };

        for (int i = 0 ; i <= count ; i++) {
            executorService.execute(runnableTask);
        }
    }

    public void runSimpleJobs(int count) {

        Runnable runnableTask = () -> {
            try {

                log.debug("runnableTask start");

                String url = "http://localhost:8080/delay/10";

                ResponseEntity<String> response = simpleRestTemplate.getForEntity(url, String.class);

                log.debug("runnableTask response:{}", response.toString());

            } catch (Exception ex) {
                log.error("[runnableTask] exception:" + ex.getMessage());
            }
        };

        for (int i = 0 ; i <= count ; i++) {
            executorService.execute(runnableTask);
        }
    }

    public void runSelfJobs(int count) {
        Runnable runnableTask = () -> {
            try {

                log.debug("runnableTask start");

                String url = "http://localhost:8888/test/webclient/10";

                ResponseEntity<String> response = simpleRestTemplate.getForEntity(url, String.class);

                log.debug("runnableTask response:{}", response.toString());

            } catch (Exception ex) {
                log.error("[runnableTask] exception:" + ex.getMessage());
            }
        };

        for (int i = 0 ; i <= count ; i++) {
            executorService.execute(runnableTask);
        }
    }
}
