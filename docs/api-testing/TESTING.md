# Testing Guide

This guide explains how to test the virality Engine project using Docker, PostgreSQL, Redis and Postman.

### 1. Prerequisites

Make sure the following are installed:

- Java 21
- Maven 3.9.9
- Docker
- Postman

### 2. Start PostgreSQL and Redis

From the project root, run:

```java
docker compose up -d
```

Check containers:

```java
docker ps
```

Expected containers:

```java
virality_postgres
virality_redis
```

### 3. Build the Project

```java
./mvnw clean verify
```

### 4. Run the Application

```java
java -jar target/virality-engine-0.0.1-SNAPSHOT.jar
```

Application should start on:

```java
http://localhost:8080
```

## Postman Collection

Download:

[Virality Engine API.postman_collection.json](Virality_Engine_API.postman_collection.json)

# Testing

### Test Create Post

Endpoint:

```java
POST /api/posts
```

Example body:

```java
{
  "authorId": 1,
  "authorType": "USER",
  "content": "Testing virality engine"
}
```

Expected:

```java
201 Created
```

### Test Human Comment

Endpoint:

```java
POST /api/posts/{postId}/comments
```

Example body:

```java
{
  "authorId": 2,
  "authorType": "USER",
  "content": "This is a human comment"
}
```

Expected:

```java
201 Created
```

Redis virality score should increase by 50.

### Test Like Feature

Endpoint:

```java
POST /api/posts/{postId}/like
```

Expected:

```java
200 OK
```

Redis keys:

```java
GET post:{postId}:likes
GET post:{postId}:virality_score
```

Each like increases:

```java
likes +1
virality_score +20
```

### Test Bot comment

Endpoint:

```java
POST /api/posts/{postId}/comments
```

Example body:

```java
{
  "authorId": 101,
  "authorType": "BOT",
  "content": "Bot 101 replied to your post"
}
```

Expected:

```java
201 Created
```

Redis updates:

```java
post:{postId}:bot_count +1
post:{postId}:virality_score +1
```

Note:- when bot reply to a comment then add `parentCommentId` in json body.

Example body:

```java
{
  "authorId": 3,
  "authorType": "USER",
  "content": "I agree",
  "parentCommentId": 1
}
```

### Test Bot Cooldown

Send two bot comments with same bot ID on the same user post within 10 minutes.

Example:

```java
{
  "authorId": 101,
  "authorType": "BOT",
  "content": "Second bot comment"
}
```

Expected second response:

```java
429 Too Many Requests
```

Reason:

```java
Same bot cannot interact with the same human within cooldown period.
```

### Test Bot Reply Limit

Send more than 100 comment to the same post using different bot IDs.

Expected:

```java
First 100 bot comments -> success
101st bot comment -> 429 To Many Requests
```

PostgreSQL check:

```java
docker exec -it virality_postgres psql -U admin -d virality_db
```

Inside Postgres cli we check how many bot comments on a single post:

```java
SELECT COUNT(*) FROM comments WHERE post_id = 1 AND author_type = 'BOT';
```

Expected:

```java
100
```

### Testing Notification Engine

The notification engine works only when a bot interact with a user’s post.

**First Bot interaction**

Expected console log:

```java
Push notification to the user 1: Bot replied to your post
```

**Next Bot Interactions**

Use different bot IDs:

```java
{
  "authorId": 102,
  "authorType": "BOT",
  "content": "Bot 102 replied to your post"
}
```

These should be stored in Redis list:

```java
LRANGE user:1:pending_notifs 0 -1
```

**Scheduler Output**

After the scheduler runs, expected log:

```java
Summarized Push Notification: Bot 102 and 1 others interacted with your posts.
```

After processing, Redis list shoud be deleted.

## Check Redis Data

Open Redis CLI:

```java
docker exec -it virality_redis redis-cli
```

Useful commands:

```java
// 
KEYS *
GET post:{postId}:likes
GET post:{postId}:virality_score
GET post:{postId}:bot_count
TTL cooldown:bot_{botId}:human_{userId}
LRANGE user:{userId}:pending_notifs 0 -1
```

## Check PostgreSQL Data

Open Postgres CLI:

```java
docker exec -it virality_postgres psql -U admin -d virality_db
```

Useful SQL:

```java
// Get all posts
SELECT * FROM posts;

// Get all comments
SELECT * FROM comments;

// count comment on post whose id = 1
SELECT COUNT(*) FROM comments WHERE post_id = 1;

// count comments by bots on a particular post
SELECT COUNT(*) FROM comments WHERE post_id = 1 AND author_type = 'BOT';
```