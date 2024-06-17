package ru.verstache.gabella.service;

import ru.verstache.gabella.model.Server;

public interface ServerService {
    Server findByName(String serverName);
}
