package ru.verstache.gabella.service;

import ru.verstache.gabella.dto.MatchDto;

import java.util.List;

public interface MatchService {

    void saveResult(MatchDto matchDto);

    List<MatchDto> findLastMatches(Integer amount);
}
