# Gallera – Virtual Gallery Creator

Live Site: https://gallerycreator.onrender.com/
Video Overview: https://www.youtube.com/watch?v=s0eSRpWx_oQ

Course: CST-451 / CST-452 Senior Capstone
Developer: Francisco Gonzalez

## Overview

Gallera is a web application built to let users create their own online photo galleries. The goal was to make something simple but useful, where anyone can register, upload photos, organize them into galleries, and interact with other users through comments. The app includes full user authentication, secure file uploads, and a clean UI using Thymeleaf + Bootstrap. The project was designed as my capstone to show everything I learned across the Software Development program.

## Technology Stack

Gallera was built using:

Java 17

Spring Boot (Web, Security, Validation)

PostgreSQL (hosted on Render)

JPA / Hibernate for database ORM

Thymeleaf for server-side rendering

Bootstrap 5 for styling

Cloudinary for image hosting and file uploads

GitHub for version control

Render for deployment

I chose this stack because I already had experience with Spring Boot and Thymeleaf, and the tools worked well for a project that needed both authentication and file handling.

## Project Features

Gallera supports the following main features:

User Authentication

  Users can register, log in, and log out
  
  Passwords are hashed using Spring Security
  
  Only authenticated users can create/edit galleries
  
  Guests can still view public galleries

Gallery Creation + Management

  Create a gallery with a title + description
  
  Edit and delete your own galleries
  
  Each gallery shows its photos and comments
  
  Photo Uploads

  Upload images to Cloudinary
  
  Replace or edit existing photos
  
  Delete your own photos
  
  Photos are tied to the gallery that owns them

Comment System

  Users can comment on photos
  
  Users can delete their own comments
  
  Gallery owners can delete any comment in their gallery
  
  All comments show the username of the commenter

User Restrictions

  Only the user who created a gallery can modify it
  
  Only the commenter can delete their comment (unless gallery owner)
  
  Guests can browse but not modify anything

## System Architecture

The project uses a pretty standard Spring MVC setup:

Controller  →  Service  →  Repository  →  Database
          →  Thymeleaf Views (HTML)

Main Components

Controllers: Handle all routes for users, galleries, photos, and comments

Services: Business logic, validation, ownership checks

Repositories: JPA interfaces for interacting with the DB

Models: User, Gallery, Photo, Comment

## Architecture Diagram
<img width="322" height="564" alt="image" src="https://github.com/user-attachments/assets/03065e37-8975-48fe-aad4-e3774b7a7dbf" />

## UML Diagram
<img width="946" height="658" alt="image" src="https://github.com/user-attachments/assets/34b2f5e9-7286-44ba-8e29-e4d8cf07406d" />

## Screenshots
<img width="1913" height="900" alt="image" src="https://github.com/user-attachments/assets/1ad42124-d67d-426e-9589-d4c7bb0ea534" />
<img width="1919" height="912" alt="image" src="https://github.com/user-attachments/assets/a0c948f3-eb8b-4db0-bac3-5349a52f6789" />
<img width="1916" height="896" alt="image" src="https://github.com/user-attachments/assets/c9783934-19ad-4a83-9404-1d8815959a37" />
<img width="1914" height="900" alt="image" src="https://github.com/user-attachments/assets/5d13a502-60a5-4236-a93d-666a554314fd" />
<img width="1914" height="900" alt="image" src="https://github.com/user-attachments/assets/27eb7d7e-2567-438c-bfbd-7b1a204d63b4" />

## Deployment (Render)

  The app is deployed on Render using:
  
  PostgreSQL instance
  
  Web service running the Spring Boot jar
  
  Environment variables for Cloudinary + DB
  
  To redeploy, just push to main and Render rebuilds.

## Approach to Implementation

The process was basically:

  Set up user authentication first
  
  Build gallery + photo models
  
  Implement file uploads using Cloudinary
  
  Add comment system
  
  Style everything with Bootstrap
  
  Add security rules for ownership
  
  Connect to Render PostgreSQL
  
  Move all files from the previous PhotoApp project
  
  Fix bugs and test each page manually
  
  A lot of things were built in small pieces and tested before moving on (kinda like mini sprints).

## Challenges + How They Were Solved

Some of the main issues that came up:

Foreign key errors

  Solved by adjusting entity mappings and adding cascade rules.

Security restrictions not working

  Fixed by adding @PreAuthorize checks and manually verifying user ownership.

File uploads breaking

  Cloudinary needed correct environment variables and secure URLs.

Render database connection failures

  Fixed by updating JDBC URLs and enabling SSL.

Overall it took patience because debugging sometimes felt slow, but everything came together.

## Christian Worldview Integration

Throughout this project I've came across some difficult times working on this app. It lead to frustration and anger but I kept reminding myself that everything will work out in the end as it is all in God's plan. I also had some personal stuff happening throughout the time making this app and going into prayer helped me stay on track and not get anxious about this project.

## Future Improvements

If there was more time, I’d want to:

Add likes on photos

Add user-to-user messaging

Build a better photo editor

Add categories or tags for galleries

Make the UI more modern with React or Tailwind

Add email notifications




