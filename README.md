# Modak challenge: Rate-Limited Notification Service

### Challenge Description:
```
We have a Notification system that sends out email notifications of various types (status update, daily news, project invitations, etc). We need to protect recipients from getting too many emails, either due to system errors or due to abuse, so let’s limit the number of emails sent to them by implementing a rate-limited version of NotificationService.

The system must reject requests that are over the limit.

Some sample notification types and rate limit rules, e.g.:

- Status: not more than 2 per minute for each recipient
- News: not more than 1 per day for each recipient
- Marketing: not more than 3 per hour for each recipient
- Etc. these are just samples, the system might have several rate limit rules!
```

### Getting Started

In order to start this project, first you need:

- [Docker](https://docs.docker.com/get-docker/)
- [Docker-Compose](https://docs.docker.com/compose/install/)

clone this repository and then navigate into it

```
git clone https://github.com/nicolasm91/notification-service.git
cd notification-service
```

Then use the following command to start the application within a Docker-compose network

```
docker-compose up -d
```

There's a POSTMAN collection inside the project directory that you can import to start manually testing the application.

When done, you can drop the entire network, volumes and containers using the following command:

```
docker-compose down --volumes --rmi all
```

