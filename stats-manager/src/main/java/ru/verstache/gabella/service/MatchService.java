package ru.verstache.gabella.service;

import ru.verstache.gabella.dto.MatchDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface MatchService {

    void saveResult(MatchDto matchDto);

    List<MatchDto> findLastMatches(Integer amount);

    List<MatchDto> findByServerIdAndDate(UUID serverId, LocalDate date);
}
