# BabyNest

A full-stack e-commerce application that integrates a **React** frontend with a secure **JWT**-protected backend. The app provides product listing, user registration, shopping cart functionality, and an admin dashboard for managing users, reviews, and products.

## Table of Contents

1. [Overview](#overview)  
2. [Key Features](#key-features)  
3. [Tech Stack](#tech-stack)  
4. [Getting Started](#getting-started)  
5. [Project Structure](#project-structure)  
6. [Changelog / Highlights](#changelog--highlights)  
7. [Additional Details](#additional-details)  
8. [Contributing](#contributing)  
9. [License](#license)

---

## Overview

This application was developed as part of an academic assignment. It showcases a secure and user-friendly e-commerce platform. The **frontend** is built in **React**, leveraging features such as **React Router**, **DataTables**, and custom components for product filtering, cart management, and user registration. The **backend** handles entities, authentication, and role-based access control using **JWT** and **BCrypt**-based password hashing.

---

## Key Features

1. **Product Management**  
   - Display products in a **DataTable** with sorting, filtering, and pagination.  
   - Archive/unarchive products to manage availability.

2. **Shopping Cart**  
   - Create, read, update, and delete items in a user’s cart.  
   - A `QuantityInput` component allows users to easily adjust item quantities.

3. **User Authentication & Authorization**  
   - **JWT**-based login and session management.  
   - **ProtectedRoute** component in React to guard sensitive pages.  
   - Role-based access control for admin and standard users.

4. **Admin Dashboard**  
   - Manage users, reviews, and products in one unified interface.  
   - Approve or reject user reviews, suspend or activate users.

5. **Registration & Validation**  
   - User registration form with styling and form validation.  
   - Strong password constraints enforced with **regex** (min. 8 characters, uppercase, lowercase, symbol, number).  
   - **BCrypt** hashing and salting for password security.

6. **Email Notifications**  
   - Automated emails for user status changes (suspension/activation).  
   - Notifications if a product in a user’s cart is archived, or a review is approved.

7. **Address Management**  
   - Full **CRUD** operations for user addresses.

---

## Tech Stack

- **Frontend**:  
  - React, React Router, DataTables.net, jQuery, HTML/CSS  
- **Backend**:  
  - SpringBoot 
  - JWT for authentication  
  - BCrypt for password hashing  
  - CORS configuration  
- **Database**:  
  - MySQL

---

## Getting Started

1. **Clone the Repository**  
    git clone https://github.com/eoinhasty/BabyNest.git

2. **Install Dependencies** (Frontend & Backend)  
   - **Frontend**:  
        cd frontend  
        npm install  
        npm run build

   - **Backend** (example for Spring Boot):  
        cd backend  
        mvn clean install  
        mvn spring-boot:run

3. **Configure Environment**  
   - Update environment variables (e.g., database credentials, JWT secret) as needed.

4. **Run the Application**  
   - Start the backend server.  
   - Start or serve the frontend build.  
   - Open your browser at `http://localhost:3000` (or whichever port you configured).

---

## Changelog / Highlights

- **Week 1 (Nov 7 - Nov 12, 2024)**  
  - Added React project and integrated **React Router**.  
  - Updated entity classes with JSON references to avoid circular serialization.  
  - Replaced `AllProducts` view with a **DataTable** for better UI/UX.

- **Week 2 (Nov 16 - Nov 21, 2024)**  
  - Introduced a filtering form (by price/category) with associated CSS.  
  - Installed and configured **DataTables.net** and **jQuery**.  
  - Implemented a **NavBar** component and a styled registration form.  
  - Added a product carousel for enhanced user experience.

- **Week 3 (Nov 22 - Nov 26, 2024)**  
  - Integrated **JWT** authentication and configured **CORS**.  
  - Set up **BCrypt** for password encoding.  
  - Secured backend/frontend endpoints with JWT tokens.  
  - Implemented a `ProtectedRoute` to lock down sensitive areas.

- **Week 4 (Nov 27 - Dec 1, 2024)**  
  - Improved product image handling and error management on the cart page.  
  - Developed a `ShoppingCartController` with **CRUD** operations.  
  - Created an **Admin Dashboard** for user, review, and product management.  
  - Added archiving/unarchiving features and a robust password validation scheme.  
  - Introduced emailing for status changes, product archiving, and review approval.

---

## Additional Details

- **Emailing**: Automated notifications to customers when their account status changes, or when products in their cart are archived.  
- **Address Management**: Full CRUD for user addresses.  
- **Admin Functions**: User role checks, product archiving, review approvals, user suspension/reactivation.  
- **Password Scheme**:  
  - **BCrypt** hashing and salting.  
  - **Regex** enforces:  
    - **Minimum 8 characters**  
    - **At least 1 uppercase letter**  
    - **At least 1 lowercase letter**  
    - **At least 1 symbol**  
    - **At least 1 number**

---

## Contributing

1. **Fork** this repository.  
2. **Create** a new branch for your feature or bug fix.  
3. **Commit** your changes.  
4. **Push** to your fork and **submit** a pull request.

---

## License

This project is licensed under the MIT License
