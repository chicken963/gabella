package ru.verstache.gabella.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import ru.verstache.gabella.dto.ServerDto;
import ru.verstache.gabella.exception.ItemNotFoundException;
import ru.verstache.gabella.model.Server;
import ru.verstache.gabella.repository.ServerRepository;
import ru.verstache.gabella.service.ServerService;

import java.time.LocalDateTime;
import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class ServerMapper {

    @Autowired
    private ServerRepository serverRepository;

    public ServerDto toDto(Server server) {
        return new ServerDto(server.getId(), server.getName(), server.getCreatedAt());
    }

    Server toModel(ServerDto dto) {
        return serverRepository.findByName(dto.name())
                .orElseGet(() -> serverRepository.save(generateNewServer(dto.name())));
    }

    private Server generateNewServer(String serverName) {
        return Server.builder()
                .id(UUID.randomUUID())
                .name(serverName)
                .createdAt(LocalDateTime.now())
                .build();
    }


}
