package ru.verstache.gabella.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.verstache.gabella.dto.MatchDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final MatchService matchService;

    @KafkaListener(topics = "${spring.kafka.topic-name}", groupId = "${spring.kafka.group-id}")
    public void listen(MatchDto matchDto) {
        matchService.saveResult(matchDto);
        log.info("Saved new match to statistics");
    }
}
