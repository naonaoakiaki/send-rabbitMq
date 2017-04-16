package com.example;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@SpringBootApplication
@RestController
@EnableBinding(Source.class)
public class SourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SourceApplication.class, args);
    }

    @Autowired
    Source source;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping
    public void sendMq(@RequestBody Id id) {
        System.out.println("id=" + id.id);
        source.output().send(MessageBuilder.withPayload(id).build());
    }

    @PostMapping("/dlq")
    public void sendCustomQueue(@RequestBody CustomBody customBody) {
        System.out.println("id=" + customBody.id + " queue=" + customBody.queue);
        org.springframework.messaging.Message message = MessageBuilder.withPayload(customBody.id).build();
        Id originalId = new Id();
        originalId.id = customBody.id;
//        this.rabbitTemplate.convertAndSend(customBody.queue, "id");

        this.rabbitTemplate.send(customBody.queue, message);
    }

    public static class Id {
        public Long id;
    }

    public static class CustomBody {
        public Long id;
        public String queue;
    }


}

