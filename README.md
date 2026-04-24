# Virality Engine (Spring Boot + Redis + PostgreSQL)


### Overview

Virality Engine is a backend system designed to simulate how social media platforms handle high traffic, bot interaction and notification spam control.

This project focuses on:

- Handling concurrent bot traffic safely
- Using Redis for atomic operations and rate limiting
- Maintaining data integrity with PostgreSQL
- Implementing smart notification batching

### Tech Stack

- Java 21
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

It is not a microservicces architecture. The application is a single Spring Boot backend, but responsibilities are separated into layers and services.

**Architecture Type:** Layered Monolithic + Redis Gatekeeper Pattern

### Project Structure

```java
virality-engine/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/praveen/guardrail/virality_engine/
│   │   │       ├── controller/
│   │   │       │   ├── PostController.java
│   │   │       │   ├── CommentController.java
│   │   │       │   └── LikeController.java
│   │   │       │
│   │   │       ├── dto/
│   │   │       │   ├── PostRequestDTO.java
│   │   │       │   ├── PostResponseDTO.java
│   │   │       │   ├── CommentRequestDTO.java
│   │   │       │   ├── CommentResponseDTO.java
│   │   │       │   └── ErrorResponseDTO.java
│   │   │       │
│   │   │       ├── entity/
│   │   │       │   ├── User.java
│   │   │       │   ├── Bot.java
│   │   │       │   ├── Post.java
│   │   │       │   ├── Comment.java
│   │   │       │   └── AuthorType.java
│   │   │       │
│   │   │       ├── exception/
│   │   │       │   ├── GlobalExceptionHandler.java
│   │   │       │   ├── PostNotFoundException.java
│   │   │       │   ├── CommentNotFoundException.java
│   │   │       │   ├── TooManyBotRepliesException.java
│   │   │       │   ├── BotCoolDownException.java
│   │   │       │   ├── CommentDepthLimitExceededException.java
│   │   │       │   └── CommentPostMismatchException.java
│   │   │       │
│   │   │       ├── mapper/
│   │   │       │   ├── PostMapper.java
│   │   │       │   └── CommentMapper.java
│   │   │       │
│   │   │       ├── repository/
│   │   │       │   ├── UserRepository.java
│   │   │       │   ├── BotRepository.java
│   │   │       │   ├── PostRepository.java
│   │   │       │   └── CommentRepository.java
│   │   │       │
│   │   │       ├── service/
│   │   │       │   ├── PostService.java
│   │   │       │   ├── PostServiceImpl.java
│   │   │       │   ├── CommentService.java
│   │   │       │   ├── CommentServiceImpl.java
│   │   │       │   ├── LikeService.java
│   │   │       │   ├── LikeServiceImpl.java
│   │   │       │   ├── ViralityService.java
│   │   │       │   └── NotificationService.java
│   │   │       │
│   │   │       ├── util/
│   │   │       │   ├── ViralityUtil.java
│   │   │       │   └── InteractionType.java
│   │   │       │
│   │   │       └── ViralityEngineApplication.java
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│
├── docker-compose.yml
├── pom.xml
├── README.md
└── .gitignore
```

## API Testing

see [TESTING.md](docs/api-testing/TESTING.md) for step by step testing instructions.

Postman collection is available in [docs/api-testing/](docs/api-testing).