package com.springboot.productclient.service;

import com.springboot.productclient.data.MetaDataEvent;
import com.springboot.productclient.exception.MyCustomExceptionServerIssue;
import com.springboot.productclient.exception.ProductNotFoundException;
import com.springboot.productclient.exception.RetryExhaustedException;
import com.springboot.productclient.model.Product;
import com.springboot.productclient.repo.MetaEventDataRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ProductClientService {

    private Logger log = LoggerFactory.getLogger(ProductClientService.class);

    @Autowired
    private WebClient webClient;

    @Autowired
    private MetaEventDataRepo metaDataEventRepository;

    public Product getProductByName(String name) {
        AtomicReference<MetaDataEvent> event = new AtomicReference<>();
       return  webClient.get()
                .uri("api/v1/product/name/{name}",name)
                .retrieve()
                .bodyToMono(Product.class)
                .log()
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(5))
                        .doAfterRetry(retrySignal -> {
                            log.info("Retried " + retrySignal.totalRetries());
                            if(!Objects.isNull(event.get())){
                                var metaEvent = event.get();
                                metaEvent.setRetryCount(Integer.valueOf(String.valueOf(retrySignal.totalRetries())));
                                updateMetaDataEvent(metaEvent);
                            }
                        }).doBeforeRetry(retrySignal -> {
                            if(retrySignal.totalRetries() == 0)
                                    event.set(createMetaDataEvent(name));
                        })
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal)
                                -> new RetryExhaustedException("Retry exhausted")))
               .doOnSuccess(succ -> log.info("suceess.. {} ", succ))
               .doOnError(RetryExhaustedException.class, (msg) -> {
                   System.out.println("Message :: " + msg);
                   // here update the DB
                   //create an event
                   event.get().setRetryCount(3);
                   updateMetaDataEvent(event.get());
               })
               .block();


    }

    private MetaDataEvent createMetaDataEvent(String name) {
        var event =  metaDataEventRepository.save(MetaDataEvent.builder().productName(name).retryCount(0).updatedTime(LocalDateTime.now()).build());
        return event;
    }

    private MetaDataEvent updateMetaDataEvent(MetaDataEvent event) {
        return metaDataEventRepository.save(event);
    }
}
