package ru.verstache.gabella.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.verstache.gabella.dto.MatchDto;
import ru.verstache.gabella.service.KafkaProducerService;

@RestController
@RequiredArgsConstructor
public class GeneratorController {

    private final KafkaProducerService kafkaProducerService;

    @Operation(summary = "Emulate new match result")
    @PostMapping("/generate")
    public ResponseEntity<Void> sendMessage(@RequestBody MatchDto match) {
        kafkaProducerService.sendMessage(match);
        return ResponseEntity.ok().build();
    }
}
