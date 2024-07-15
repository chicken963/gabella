package ru.verstache.gabella.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.verstache.gabella.dto.MatchDto;
import ru.verstache.gabella.mapper.MatchMapper;
import ru.verstache.gabella.model.Match;
import ru.verstache.gabella.model.MatchWinner;
import ru.verstache.gabella.repository.MatchRepository;
import ru.verstache.gabella.repository.MatchWinnerRepository;
import ru.verstache.gabella.service.impl.MatchServiceImpl;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;
    @Mock
    private MatchWinnerRepository matchWinnerRepository;
    @Mock
    private MatchMapper matchMapper;
    @Mock
    private PlayerService playerService;

    @InjectMocks
    private MatchServiceImpl uut;

    @Test
    public void shouldSaveMatch_whenValidData() {
        Match match = mockMatch();
        when(matchMapper.toModel(any())).thenReturn(match);
        MatchDto matchDto = Mockito.mock(MatchDto.class);

        uut.saveResult(matchDto);

        Mockito.verify(playerService, times(1)).increaseStatsForParticipants(any());
        Mockito.verify(playerService, times(1)).increaseStatsForWinners(any());
        Mockito.verify(matchRepository, times(1)).save(any());
        Mockito.verify(matchWinnerRepository, times(2)).save(any());
    }

    private Match mockMatch() {
        Match match = Mockito.mock(Match.class);
        when(match.getMatchWinners()).thenReturn(Set.of(
                mock(MatchWinner.class),
                mock(MatchWinner.class)
        ));
        return match;
    }

}
