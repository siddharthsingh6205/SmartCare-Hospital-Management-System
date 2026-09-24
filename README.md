# 🏥 Hospital Appointment Booking System

![System Architecture](https://img.icons8.com/color/96/000000/network.png)  
_A secure, role-based platform for managing hospital appointments with real-time features and admin control._

## 🌟 Key Features

### 👥 Role-Based Access

- **Admin**
  - Register & manage doctors
  - Configure doctor availability (7-day slot)
  - View analytics dashboard
- **Doctor**
  - Set weekly schedule
  - Approve/reject appointments
  - Manage calendar
- **Patient**
  - Browse doctors by specialty
  - Book, cancel, or reschedule appointments
  - Secure online payments
  - Receive confirmations and reminders

### 🔐 Security

- Role-based authorization using JWT
- Encrypted communication
- Temporary password & email activation for onboarding
- Spring Security with protected API endpoints

### 📊 Functionalities

- Real-time slot management
- Admin & Doctor dashboards (Recharts)
- Notifications via email/SMS
- Stripe Payment gateway integration
- Refundable appointment cancellations

## 🛠️ Tech Stack

| Layer       | Technology                              |
| ----------- | --------------------------------------- |
| Frontend    | React.js, Material-UI, Recharts, Day.js |
| Backend     | Spring Boot, Spring Security, JWT       |
| Database    | MySQL 8.0+                              |
| Build Tools | Maven (Backend), npm (Frontend)         |

## 🚀 Getting Started

### 📦 Prerequisites

Ensure the following are installed on your machine:

- Docker
- Node.js v16+ (for local development)
- Java JDK 11+ (for local development)
- Maven (for local development)

### 🐳 Docker Deployment (Recommended)

1. Pull the Docker image from Docker Hub:
   ```bash
   docker pull jenushan264/patientappointmentbooking

### 📁 Clone the Repository

```bash
git clone https://github.com/jeyjenushan/AppointmentBookingApp.git
cd AppointmentBookingApp
```

## Database setup

- CREATE DATABASE hospital_appointments;

## Backend setup

- cd appointmentBackend

## setup the appointment.properties

## Run the backend server:

- mvn spring-boot:run

## Frontend Setup

cd ../appointmentFrontend

## Create a .env file

- REACT_APP_API_BASE_URL=http://localhost:8080/api

## Install dependencies and run the app:

- npm install
- npm start

