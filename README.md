# Say hello to Gabella - game stats app

## Assessment task:
Напишите сервер статистики (microservice1) и ее генератор(microservice2) для многопользовательской игры-шутера. Матчи этой игры проходят на разных серверах, и задача сервера статистики — строить общую картину по результатам матчей со всех серверов.
Сервер должен представлять собой standalone-приложение, реализующее описанный ниже RESTful API.

Общая схема работы такая: игровые сервера (ms2) присылают в кафку  результаты каждого завершенного матча. Сервер статистики аккумулирует разную статистику по результатам матчей и отдает её по запросам (статистика по серверу, статистика по игроку, топ игроков и т.д.).
API

API сервера статистики состоит из следующих методов:

`/servers/<endpoint>/info` GET

`/servers/<endpoint>/matches/<timestamp>` GET

`/servers/info` GET

`/servers/<endpoint>/stats` GET

`/players/<name>/stats` GET

`/reports/recent-matches[/<count>]` GET

`/reports/best-players[/<count>]` GET

`/reports/popular-servers[/<count>]` GET

## Solution
The solution consists of two microservices - `Generator` and `Manager`
### Generator
Emulates the ending of a brand-new match. To emulate new match ending, one should send the `POST` request to `${generator-base-url}/generate` with the following request body schema:

```json
{
    "server": {
        "name": "string"
    },
    "startedAt": "2024-07-17T08:37:32.640Z",
    "finishedAt": "2024-07-17T08:37:32.640Z",
    "participants": [
        {
            "nick": "string"
        }
    ],
    "winners": [
        {
            "nick": "string",
            "points": 0
        }
    ]
}
```

### Manager

As all the match history is accumulated, one can get statistics from different prospects.
For this purpose the following endpoint groups are available:

#### Servers
* `/servers/info` - general info about all the servers
* `/servers/${server-id}/info` - general info about the specific server
* `/servers/${server-id}/stats` - statistics for a specific server
* `/servers/${server-id}/matches/${date}` - statistics for all matches on a server finished at the specific day

#### Players
* `/players/${player-nick}/stats` - statistics for a specific player

#### Reports
* `/reports/recent-matches/${n}` - info about last `n` played matches
* `/reports/popular-servers/${n}` - info about top `n` popular servers
* `/reports/best-players/${n}` - info about top `n` players