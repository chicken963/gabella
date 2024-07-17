# Say hello to Gabella - game stats app

## Generator
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

## Manager

As all the match history is accumulated, one can get statistics from different prospects.
For this purpose the following endpoint groups are available:

### Servers
* `/servers/info` - general info about all the servers
* `/servers/${server-id}/info` - general info about the specific server
* `/servers/${server-id}/stats` - statistics for a specific server

### Players
* `/players/${player-nick}/stats` - statistics for a specific player

### Reports
* `/reports/recent-matches/${n}` - info about last `n` played matches
* `/reports/popular-servers/${n}` - info about top `n` popular servers
* `/reports/best-players/${n}` - info about top `n` players