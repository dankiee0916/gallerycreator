# Gallery Creator Web App

A Spring Boot web application that lets users create galleries, upload images, and comment on photos. Built for my GCU Capstone Project.

## Features
- User registration and login
- Create/edit/delete galleries
- Upload/edit/delete photos
- Comment system with permissions
- Guest viewing mode
- Deployment on Render with Neon PostgreSQL

## Technologies
- Java + Spring Boot
- Spring Security
- Thymeleaf
- PostgreSQL
- Render Deployment

## How to Run
1. Clone the repo  
2. Update `application.properties` with your DB credentials  
3. Run the project in your IDE  
4. Visit `http://localhost:8080`

## Project Structure
- `/controllers` - Web endpoints  
- `/services` - Business logic  
- `/models` - Entities (User, Gallery, Photo, Comment)  
- `/templates` - Thymeleaf UI pages  
- `/uploads` - Stored image files  

## Future Improvements
- Public gallery links
- Search and filtering
- User profiles
- Responsive UI
