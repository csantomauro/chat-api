# Chat API

A real-time chat API with rooms and direct messages, built with Java, Spring Boot, and PostgreSQL. Uses WebSocket/STOMP for live messaging and a pub-sub model for typing indicators.

**Stack:** Java 17 · Spring Boot 3.5 · PostgreSQL · Spring Data JPA · WebSocket/STOMP · JUnit 5 / Mockito · Docker

## Getting started

```bash
git clone https://github.com/yourusername/chat-api.git
cd chat-api
docker compose up -d
./mvnw spring-boot:run
```

API available at `http://localhost:8080`, WebSocket endpoint at `/ws`. Run tests with `./mvnw test`.

## Usage examples

**Create users and a room**
```bash
curl -X POST http://localhost:8080/users -H "Content-Type: application/json" -d '{"username": "mario"}'
curl -X POST http://localhost:8080/users -H "Content-Type: application/json" -d '{"username": "luca"}'
curl -X POST http://localhost:8080/rooms -H "Content-Type: application/json" -d '{"name": "general"}'
curl -X POST "http://localhost:8080/rooms/1/join?username=mario"
```

**Get a direct message room between two users**
```bash
curl -X POST "http://localhost:8080/rooms/direct?userA=mario&userB=luca"
```

**Fetch message history**
```bash
curl http://localhost:8080/messages/room/1
```

**Real-time messaging** happens over WebSocket (endpoint `/ws`) rather than plain HTTP, since chat requires a persistent, two-way connection instead of one-off requests. Clients connect once, then send and receive messages instantly on that same connection — no polling needed.

A simple HTML test client is included in `/test-client` to try this out without writing any code: open it in two browser tabs, connect with two different usernames, and exchange messages live.

| Method | Endpoint | Description |
|---|---|---|
| POST | `/users` | Create a user |
| POST | `/rooms` | Create a room |
| POST | `/rooms/{id}/join` | Join a room |
| POST | `/rooms/direct` | Get or create a DM room between two users |
| GET | `/messages/room/{roomId}` | Get message history for a room |
| STOMP | `/app/chat.sendMessage` → `/topic/room/{roomId}` | Send/receive messages in real time |
| STOMP | `/app/chat.typing` → `/topic/room/{roomId}/typing` | Typing indicator |