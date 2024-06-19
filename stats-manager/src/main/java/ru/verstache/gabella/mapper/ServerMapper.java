package ru.verstache.gabella.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import ru.verstache.gabella.dto.ServerDto;
import ru.verstache.gabella.model.Server;
import ru.verstache.gabella.service.ServerService;

@Mapper(componentModel = "spring")
public abstract class ServerMapper {

    @Autowired
    private ServerService serverService;

    public ServerDto toDto(Server server) {
        return new ServerDto(server.getId(), server.getName(), server.getCreatedAt());
    }

    Server toModel(ServerDto dto) {
        return serverService.findByName(dto.name());
    }


}
