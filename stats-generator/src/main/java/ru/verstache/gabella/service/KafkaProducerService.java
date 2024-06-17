package ru.verstache.gabella.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.verstache.gabella.dto.MatchDto;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topic-name}")
    private String kafkaTopic;

    public void sendMessage(@RequestBody MatchDto match) {
        kafkaTemplate.send(kafkaTopic, match);
    }
}
