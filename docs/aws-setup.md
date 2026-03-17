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
# AWS Infrastructure Setup

## AWS Account Details
- **Account ID:** 225523106691
- **Region:** ap-south-1 (Asia Pacific - Mumbai)
- **Environment:** Development

---

## ECR (Elastic Container Registry) Repositories

### Repository Configuration
| Repository Name | URI | Tag Mutability | Scan on Push |
|----------------|-----|----------------|--------------|
| payment-validation-service | `225523106691.dkr.ecr.ap-south-1.amazonaws.com/payment-validation-service` | Mutable | ✅ Enabled |
| payment-processing-service | `225523106691.dkr.ecr.ap-south-1.amazonaws.com/payment-processing-service` | Mutable | ✅ Enabled |
| stripe-provider-service | `225523106691.dkr.ecr.ap-south-1.amazonaws.com/stripe-provider-service` | Mutable | ✅ Enabled |

### Encryption
- **Type:** AES-256 (Industry Standard)
- **Key:** AWS Managed

---

## GitHub Secrets Configuration

The following secrets have been configured in the GitHub repository:

| Secret Name | Description | Example Value |
|------------|-------------|---------------|
| `AWS_ACCOUNT_ID` | AWS Account Number | `225523106691` |
| `AWS_REGION` | AWS Region for ECR | `ap-south-1` |
| `AWS_ACCESS_KEY_ID` | IAM Access Key | `AKIA...` |
| `AWS_SECRET_ACCESS_KEY` | IAM Secret Key | `***` (encrypted) |

---

## Docker Commands

### Login to ECR
```bash
aws ecr get-login-password --region ap-south-1 | docker login --username AWS --password-stdin 225523106691.dkr.ecr.ap-south-1.amazonaws.com
```

### Build and Push Images

#### Validation Service
```bash
docker build -t payment-validation-service:latest ./services/validation
docker tag payment-validation-service:latest 225523106691.dkr.ecr.ap-south-1.amazonaws.com/payment-validation-service:latest
docker push 225523106691.dkr.ecr.ap-south-1.amazonaws.com/payment-validation-service:latest
```

#### Processing Service
```bash
docker build -t payment-processing-service:latest ./services/processing
docker tag payment-processing-service:latest 225523106691.dkr.ecr.ap-south-1.amazonaws.com/payment-processing-service:latest
docker push 225523106691.dkr.ecr.ap-south-1.amazonaws.com/payment-processing-service:latest
```

#### Stripe Provider Service
```bash
docker build -t stripe-provider-service:latest ./services/stripe-provider
docker tag stripe-provider-service:latest 225523106691.dkr.ecr.ap-south-1.amazonaws.com/stripe-provider-service:latest
docker push 225523106691.dkr.ecr.ap-south-1.amazonaws.com/stripe-provider-service:latest
```

---

## Security Notes

1. **Access Keys:** Rotated every 90 days
2. **IAM Permissions:** Least privilege (ECR only)
3. **Image Scanning:** Enabled on all repositories
4. **Encryption:** AES-256 for images at rest

---
