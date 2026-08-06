# 💳 Payment Integration System

<p align="center">

Enterprise-grade Payment Integration System built using **Spring Boot Microservices**, **Spring Cloud**, **Stripe API**, and **AWS**.

Designed for **High Availability**, **Scalability**, and **Fault Tolerance** using modern cloud-native architecture.

</p>

---

<p align="center">

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.3-brightgreen)
![Spring Cloud](https://img.shields.io/badge/Spring_Cloud-2024.0.1-blue)
![Stripe](https://img.shields.io/badge/Stripe-Payment-purple)
![MySQL](https://img.shields.io/badge/MySQL-8-blue)
![AWS](https://img.shields.io/badge/AWS-Cloud-orange)
![License](https://img.shields.io/badge/License-MIT-green)

</p>

---

# 🚀 Overview

This project demonstrates how an enterprise payment platform is designed using a **Microservices Architecture**.

The application integrates with **Stripe Checkout**, processes payments securely, validates requests, updates transaction status using webhooks, and communicates between services using **OpenFeign**.

The architecture follows industry best practices such as:

- Microservices Architecture
- API Gateway
- Service Discovery
- Centralized Configuration
- Circuit Breaker Pattern
- Idempotency
- Webhook-driven Payment Processing
- Agile/Scrum Development

---

# 🏗️ System Architecture

```text
                         Client
                            │
                            ▼
                  API Gateway (8080)
               Authentication + Routing
                            │
        ┌───────────────────┴───────────────────┐
        │                                       │
        ▼                                       ▼
Payment Validation Service          Payment Processing Service
        │                                       │
        │                                       ▼
        │                           OpenFeign + Circuit Breaker
        │                                       │
        └──────────────► Stripe Provider Service ◄──────────────┐
                                │                               │
                                ▼                               │
                         Stripe Payment API                     │
                                │                               │
                           Stripe Webhook                       │
                                │                               │
                                └────────► Update Transaction ◄─┘


Infrastructure

• Eureka Server
• Config Server
• AWS RDS
• AWS EC2
```

---

# ✨ Features

## ✅ Payment Validation

- Request Validation
- Enum Validation
- Swagger Documentation
- Global Exception Handling

---

## ✅ Payment Processing

- Transaction Management
- Idempotency
- Pagination
- MySQL Persistence
- OpenFeign Communication
- Circuit Breaker

---

## ✅ Stripe Integration

- Stripe Checkout Session
- Stripe Webhooks
- Metadata Support
- Payment Verification
- Refund API
- Signature Verification
- Automatic Transaction Status Update

---

## ✅ Platform Services

- Eureka Service Registry
- Config Server
- API Gateway
- Dynamic Routing

---

# 🔄 Payment Flow

```text
Client
   │
   ▼
Initiate Payment
   │
   ▼
Payment Validation
   │
   ▼
Payment Processing
   │
   ▼
Stripe Checkout
   │
   ▼
Customer Payment
   │
   ▼
Stripe Webhook
   │
   ▼
Update Transaction
   │
   ▼
SUCCESS
```

---

# 📂 Microservices

| Service | Port | Status |
|----------|------|--------|
| API Gateway | 8080 | ✅ |
| Eureka Server | 8761 | ✅ |
| Config Server | 8888 | ✅ |
| Payment Validation | 8081 | ✅ |
| Payment Processing | 8082 | ✅ |
| Stripe Provider | 8083 | ✅ |
| Auth Service | 8084 | 🚧 |
| Notification Service | 8085 | 📅 |

---

# 🛠 Tech Stack

## Backend

- Java 17
- Spring Boot
- Spring Cloud
- Spring Data JPA
- Spring Security
- Spring Cloud Gateway
- OpenFeign
- Resilience4j

---

## Payment

- Stripe Checkout
- Stripe Webhooks
- Stripe Refund API

---

## Database

- MySQL 8

---

## DevOps

- AWS EC2
- AWS RDS
- AWS ECR
- Docker *(Planned)*
- GitHub Actions *(Planned)*

---

## Testing

- JUnit 5
- Mockito
- MockMvc

---

# 📊 Project Progress

| Sprint | Status |
|----------|--------|
| Infrastructure | ✅ |
| Validation | ✅ |
| Platform Services | ✅ |
| Payment Processing | ✅ |
| Stripe Integration | ✅ |
| Security | 🚧 |
| Redis Cache | 📅 |
| Docker | 📅 |
| CI/CD | 📅 |
| Performance Testing | 📅 |

---

# 🎯 Design Patterns Used

- Circuit Breaker
- Builder Pattern
- Factory Pattern
- Dependency Injection
- Service Discovery
- API Gateway
- Repository Pattern
- DTO Pattern

---

# 📈 Performance Goals

| Metric | Target |
|----------|----------|
| Concurrent Users | 10,000+ |
| Availability | 99.9% |
| Response Time | <100ms |
| Payment Success | 99%+ |

---

# 🚀 Getting Started

## Prerequisites

- Java 17
- Maven
- MySQL
- Stripe Account
- ngrok

---

## Start Services

```text
1. Eureka Server

2. Config Server

3. Payment Processing

4. Payment Validation

5. Stripe Provider

6. API Gateway
```

---

## Stripe Configuration

```properties
stripe.api.key=YOUR_SECRET_KEY

stripe.webhook.secret=YOUR_WEBHOOK_SECRET

stripe.success.url=http://localhost:8080/payment/success

stripe.cancel.url=http://localhost:8080/payment/cancel
```

---

## Test Card

```
4242 4242 4242 4242

Expiry : 12/29

CVV : 123
```

---

# 📷 Screenshots

> Add screenshots here

- Architecture Diagram

- Swagger

- Stripe Checkout

- Payment Success

- Eureka Dashboard

- Config Server

---

# 🔮 Future Enhancements

- JWT Authentication
- Redis Cache
- ActiveMQ
- Docker
- Kubernetes
- GitHub Actions
- AWS Auto Scaling
- Notification Service
- Monitoring
- Distributed Tracing

---

# 👨‍💻 Author

**Ashish Wagh**

Java Backend Developer

GitHub

https://github.com/ashishw116

---

⭐ If you like this project, don't forget to star the repository.
