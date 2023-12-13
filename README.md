Page Flip Bookstore (Backend)

Page Flip Bookstore is a backend-only Spring Boot application for an online bookstore. It features role based authentication, RESTful API endpoints, PostgreSQL database management with JPA and Hibernate, email notifications, third-party image hosting, and OpenAI integration for personalized book recommendations.

Features

- RESTful API Endpoints: Provides a set of API endpoints for integration with other applications.

- Page Flip Bookstore utilizes Swagger for clear and interactive API documentation. You can explore and test the available API endpoints by navigating to [Swagger UI](http://localhost:8080/swagger-ui.html) after starting the application.

- Security: Secured with Spring Security, utilizing role-based security and JWT tokens.

- Database Management: PostgreSQL database managed with JPA and Hibernate for efficient data storage and retrieval.

- Email Notifications: Sends welcome emails to new users and order confirmations upon successful purchase.

- 3rd Party Image Hosting: Utilizes third-party image hosting for book cover images.

- Open AI Integration: Incorporates OpenAI to provide personalized book recommendations based on the user's purchase history.

User Features - Customers

-   Sign Up: Users can sign up with their email and password.

-   Book Search: Search for books by title, author, and filter by tags and language.

-   Order Management: Place orders, cancel orders, view order status, and access order history.

-   Recommendations: Receive book recommendations based on their order history.

-   Account Actions: Cancel their account if needed.

User Features - Staff

-   Content Management: Staff members can upload, edit, and manage books, authors, and tags.

-   Order Handling: View orders, mark them as completed or cancelled.

-   Account Management: Cancel user accounts if necessary.

Installation

    Clone the Repository:

    bash

git clone https://github.com/your-username/page-flip-bookstore.git
cd page-flip-bookstore

Build and Run:

bash

    ./mvnw clean install
    java -jar target/page-flip-bookstore.jar

    Access the Application:
    Open your browser and navigate to http://localhost:8080

Configuration

    Database: Configure PostgreSQL connection details in application.properties.

    Third-Party Services: Update API keys and endpoints in the configuration files.
