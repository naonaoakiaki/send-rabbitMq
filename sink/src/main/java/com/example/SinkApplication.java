package com.example;

import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by n123362 on 2017/04/15.
 */
@SpringBootApplication
@RestController
@EnableBinding(Sink.class)
public class SinkApplication {

    @Autowired
    Sink sink;

    public static void main(String[] args) {
        SpringApplication.run(SinkApplication.class, args);
    }

    public static class Id {
        public Long id;
    }

//    @StreamListener(Sink.INPUT)
//    public void print(Id id) {
//        System.out.println("Received " + id.id);
//    }
    @StreamListener(Sink.INPUT)
    public void print(Message message) {
        System.out.println("Received " + message.getBody());
    }

}