# Food Delivery Backend

A backend application for a Food Delivery Platform built using Spring Boot and MySQL. The application provides APIs for users to browse restaurants and food items, manage their cart, place orders, make payments, manage addresses, and add reviews.

## Features

- User Registration and Authentication
- Role-Based Access Control
- Restaurant/Shop Management
- Food Management
- Cart Management
- Add, Update, View and Remove Cart Items
- Address Management
- Order Placement and Order Tracking
- Online Payment Flow
- Order Status Management
- Food Reviews and Ratings
- API Validation
- Database Transactions
- RESTful APIs

## API Documentation

Interactive API documentation and end-point testing are available via Swagger UI when running the application locally:

Food_Delivery URL: [https://xsparc.up.railway.app/swagger-ui/index.html]

*(Note: Replace `8080` with your custom port if configured in `application.properties`)*

## Technologies Used

- Java
- Spring Boot
- Spring Data JPA
- Spring Security
- JWT Authentication
- MySQL
- Hibernate
- Maven
- Lombok
- REST API

## Main Modules

### User
- Register and login
- Browse shops and food items
- Manage cart
- Manage addresses
- Place orders
- Make payments
- View order history
- Add and manage reviews

### Shop & Food
- Store restaurant information
- Store food items and their prices
- Manage food availability
- Support food discounts and ratings

### Cart
- Add food to cart
- Update food quantity
- View cart
- Remove food from cart

### Order
- Place orders from cart
- View order details
- Track order status
- Calculate total order amount

### Payment
- Process payment for orders
- Generate transaction ID
- Update order status after successful payment

### Review
- Add food reviews and ratings
- View reviews
- Update reviews
- Delete reviews

## Order Flow

```text
User Login
    ↓
Browse Shop
    ↓
Select Food
    ↓
Add to Cart
    ↓
Add Address
    ↓
Place Order
    ↓
Payment
    ↓
Order Confirmed
    ↓
Preparing
    ↓
Out for Delivery
    ↓
Delivered

