package ru.verstache.gabella.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.verstache.gabella.dto.MatchDto;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
public class KafkaConsumerServiceTest {

    @Mock
    private MatchService matchService;

    @InjectMocks
    private KafkaConsumerService uut;

    @Test
    void shouldInvokeSaving() {
        MatchDto matchDto = mock(MatchDto.class);
        uut.listen(matchDto);
        Mockito.verify(matchService, times(1)).saveResult(matchDto);
    }
}
