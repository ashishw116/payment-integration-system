# AWS Setup Documentation

## EC2 Instance Details
- Type: t2.micro (Free Tier)
- OS: Amazon Linux 2023
- Region: ap-south-1 (Mumbai)
- IP: 15.206.73.162

## Installed Software
- Docker ✅
- Docker Compose v5.1.0 ✅

## Security Group Ports
| Port | Service          |
|------|-----------------|
| 22   | SSH (My IP only)|
| 8081 | Validation Svc  |
| 8082 | Processing Svc  |
| 8083 | Stripe Svc      |
| 80   | HTTP            |