# Virality Engine (Spring Boot + Redis + PostgreSQL)


### Overview

Virality Engine is a backend system designed to simulate how social media platforms handle high traffic, bot interaction and notification spam control.

This project focuses on:

- Handling concurrent bot traffic safely
- Using Redis for atomic operations and rate limiting
- Maintaining data integrity with PostgreSQL
- Implementing smart notification batching

### Tech Stack

- Java 17
- Spring Boot
- PostgreSQL (Persistent storage)
- Redis (In-memory processing)
- Docker (Containerized setup)

# Features


### 1. Post & Comment System

- Users can create posts
- Users and bots can comment
- Support nested comments with depth validation

### 2. Virality & Score (Real-Time)

Each interaction updates score instantly in Redis:

| Interaction | Score |
| --- | --- |
| Human Like | +20 |
| Human Comment | +50 |
| Bot Comment | +1 |

Implemented using Redis atomic operations:

```java
INCR post:{id}:virality_score
```

### 3. Guardrails (Spam Protection)

**Bot Reply Limit (Horizontal Cap)**

- Maximum 100 bot comments per post
- Enforced using Redis atomic counter:

```java
post:{id}:bot_count
```

**Bot Cooldown**

- A bot cannot repeatedly interact with the same user within 10 minutes
- Implemented using:

```java
SETNX cooldown:bot_{botId}:human_{userId}
```

### 4. Notification Engine (Smart Batching)

**Throttler**

- First bot interaction → immediate notification
- Subsequent interactions → stored in Redis list

```java
user:{id}:pending_notifs
```

**Scheduler (CRON Sweeper)**

- Runs every few mintues
- Aggregates notifications
- Log summary:

```java
Summarized Push Notification: Bot X and N others interacted with your posts
```

## Concurrency Handling

System is tested against:

- 200 concurrent bot requests
- Ensures exactly 100 comments are saved per posts
- Uses Redis atomic operations to prevent race conditions

# Architecture


This project follows a **layered monolithic architecture with Redis-backed real-time processing**.

It is not a microservices architecture. The application is a single Spring Boot backend, but responsibilities are separated into layers and services.

**Architecture Type:** Layered Monolithic + Redis Gatekeeper Pattern

## Thread Safety Approach for Atomic Locks

Redis-based atomic locks to protect the system from race conditions, especially when many bots interact with the same post at the same time.

The application does not use Java in-memory variables like `HashMap`, `static`, counters, or synchronized blocks. Instead, all counters and locks are stored in Redis.

### Horizontal Cap: Bot Reply Limit

To limit a post to a maximum of 100 bot replies, the system uses Redis atomic increment:

```
post:{postId}:bot_count
```

When bot tries to comment, the service executes:
```
Long count = redisTemplate.opsForValue().increment(botCountKey);

if (count > 100) {
    throw new TooManyBotRepliesException("Bot reply limit exceeded max 100 allowed.");
}
```

Redis `INCR` is atomic, meaning even if 200 bot requests arrive at the same millisecond, Redis processes each increment one by one. This guarantees that only the first 100 bot comments are allowed and the remaining requests are rejected with HTTP 429.

### Cooldown Lock

To prevent the same bot from interacting with the same human repeatedly within 10 minutes, the system uses Redis `SETNX` behavior through `SetIfAbsent`.
```
cooldown:bot_{botId}:human_{humanId}
```
```
Boolean success = redisTemplate.opsForValue()
        .setIfAbsent(cooldownKey, "1", 600, TimeUnit.SECONDS);

if (Boolean.FALSE.equals(success)) {
    throw new BotCoolDownException("Bot already interacted with this user recently");
}
```

`SetIfAbsent` is atomic. If two requests from the same bot arrive at the same time, only one request can create the cooldown key. The other request sees that the key already exists and is rejected.

### Why This is Thread-Safe

The system avoids the unsafe pattern:
```
Read value → Check value → Update value
```
because this can fail under concurrent requests.

Instead, it uses Redis atomic operations:
```
INCR
SETNX with TTL
```
These operations are executed atomically inside Redis, so race conditions are avoided even when multiple applications threads handle requests at the same time.

### Data Integrity

Redis guardrails are checked before saving the comment in PostgreSQL.

```
Request
  ↓
Redis Guardrails
  ↓
If allowed → Save comment in PostgreSQL
If rejected → No database write
```
This ensures PostgreSQL store only valid content while Redis acts as the real-time gatekeeper.


## API Testing

see [TESTING.md](docs/api-testing/TESTING.md) for step by step testing instructions.

Postman collection is available in [docs/api-testing/](docs/api-testing).