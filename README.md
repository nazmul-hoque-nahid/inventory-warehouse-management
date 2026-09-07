# 📦 Inventory & Warehouse Management System

A backend-focused **Inventory & Warehouse Management System** built with **Spring Boot** and **MySQL**, providing secure REST APIs for managing products, categories, suppliers, warehouses, inventory, stock movements, stock transfers, and purchase orders.

The application is containerized using **Docker** and can be deployed to cloud infrastructure. A **GitHub Actions CI/CD pipeline** automatically builds and tests the project, creates a Docker image, and pushes the image to **Docker Hub**.

---

## 🌐 Live Demo

[![Swagger API](https://img.shields.io/badge/Swagger-API-green?logo=swagger)](https://warehouse.blueglacier-6fbbfd1c.eastasia.azurecontainerapps.io/swagger-ui/index.html)

---

## 🚀 Features

### 🔐 Authentication & Authorization

* User registration and login
* JWT-based authentication
* Stateless authentication using Spring Security
* Role-based authorization
* Protected REST endpoints
* Custom authentication and access-denied handling
* User account status validation

Supported roles include:

* `ADMIN`
* `INVENTORY_MANAGER`
* `WAREHOUSE_MANAGER`
* `WAREHOUSE_STAFF`

---

### 👤 User Management

* Create and manage users
* View user information
* Activate users
* Suspend users
* Change user roles
* Search users
* Validate user account status before inventory operations

---

### 📦 Product Management

* Create and manage products
* Unique SKU for products
* Search products
* View product information
* Associate products with categories
* Track products across multiple warehouses

---

### 🏷️ Category Management

* Create categories
* Update categories
* View categories
* Delete categories
* Associate products with categories

---

### 🚚 Supplier Management

* Create and manage suppliers
* Store supplier contact information
* Search suppliers
* Associate suppliers with purchase orders

---

### 🏭 Warehouse Management

* Create and manage warehouses
* Track warehouse status
* Activate warehouses
* Temporarily close warehouses
* Permanently close warehouses
* Search warehouses

Warehouse statuses:

```text
ACTIVE
TEMPORARILY_CLOSED
PERMANENTLY_CLOSED
```

Inventory operations are restricted when a warehouse is not active.

---

### 📊 Inventory Management

* Maintain product inventory per warehouse
* Track available quantity
* Track reserved quantity
* Prevent invalid stock operations
* Search and filter inventory
* Maintain inventory consistency during concurrent operations

Available quantity is calculated using:

```text
Available Quantity = Quantity - Reserved Quantity
```

---

### 🔄 Stock Movement Management

The system records stock operations through stock movement records.

Supported movement types can include:

```text
IN
OUT
TRANSFER
```

Each movement records information such as:

* Product
* Warehouse
* Quantity
* User who performed the operation
* Related stock transfer when applicable

This provides an audit trail of inventory changes.

---

### 🔁 Stock Transfer

The system supports transferring stock between warehouses.

Features include:

* Transfer products between warehouses
* Validate source warehouse
* Validate destination warehouse
* Prevent source and destination from being the same
* Validate warehouse status
* Check available inventory
* Reserve inventory during transfers
* Track the user performing the transfer
* Create stock movement records
* Support transfer items

Example flow:

```text
Source Warehouse
       │
       │ Reserve Stock
       ▼
   Stock Transfer
       │
       ▼
Destination Warehouse
       │
       ▼
Update Inventory
```

The system prevents transfers when available stock is insufficient.

---

### 🛒 Purchase Order Management

The system supports creating and managing purchase orders from suppliers.

Features include:

* Create purchase orders
* Add multiple products to an order
* Associate orders with suppliers
* Associate orders with warehouses
* Track the user who created the order
* Receive purchase orders
* Cancel purchase orders
* Automatically update inventory when an order is received
* Create stock movement records for received stock

Purchase order statuses:

```text
CREATED
RECEIVED
CANCELLED
```

When a purchase order is received:

```text
Purchase Order
      │
      ▼
Receive Order
      │
      ▼
Increase Inventory
      │
      ▼
Create Stock Movement
```

---

## 🛡️ Error Handling & Validation

The application provides centralized error handling and request validation.

Features include:

* Global exception handling
* Custom exceptions
* Structured API error responses
* Bean validation
* Resource existence validation
* Quantity validation
* Warehouse status validation
* User status validation
* Insufficient stock validation
* Duplicate resource validation

---

## 🔎 Search & Filtering

The application provides search and filtering functionality for resources such as:

* Products
* Categories
* Suppliers
* Warehouses
* Users
* Inventory
* Stock movements
* Stock transfers
* Purchase orders

Dynamic filtering is implemented using **Spring Data JPA Specifications** where appropriate.

---

# 🏗️ Architecture

The project follows a layered Spring Boot architecture:

```text
Client
   │
   ▼
REST Controllers
   │
   ▼
Services
   │
   ▼
Repositories
   │
   ▼
JPA / Hibernate
   │
   ▼
MySQL
```

### Security Flow

```text
Client
   │
   │ Authorization: Bearer <JWT>
   ▼
JWT Authentication Filter
   │
   ▼
Spring Security
   │
   ▼
Controller
   │
   ▼
Service
```

---

# 🛠️ Technologies

| Technology        | Purpose                        |
| ----------------- | ------------------------------ |
| Java              | Programming language           |
| Spring Boot       | Backend framework              |
| Spring Security   | Authentication & authorization |
| JWT               | Stateless authentication       |
| Spring Data JPA   | Data access                    |
| Hibernate         | ORM                            |
| MySQL             | Relational database            |
| Maven             | Build & dependency management  |
| Docker            | Application containerization   |
| GitHub Actions    | CI/CD                          |
| Docker Hub        | Container image registry       |
| Microsoft Azure   | Cloud deployment               |
| OpenAPI / Swagger | API documentation              |

---

# 📂 Project Structure

```text
src/main/java/
└── ...
    ├── config/
    │   ├── SecurityConfig
    │   ├── CorsConfig
    │   └── OpenApiConfig
    │
    ├── controller/
    │   ├── AuthController
    │   ├── UserController
    │   ├── ProductController
    │   ├── CategoryController
    │   ├── SupplierController
    │   ├── WarehouseController
    │   ├── InventoryController
    │   ├── StockMovementController
    │   ├── StockTransferController
    │   └── PurchaseOrderController
    │
    ├── service/
    │   ├── AuthService
    │   ├── UserService
    │   ├── ProductService
    │   ├── CategoryService
    │   ├── SupplierService
    │   ├── WarehouseService
    │   ├── InventoryService
    │   ├── StockMovementService
    │   ├── StockTransferService
    │   └── PurchaseOrderService
    │
    ├── repository/
    │
    ├── entity/
    │
    ├── dto/
    │   ├── request/
    │   └── response/
    │
    ├── security/
    │
    ├── exception/
    │
    └── specification/
```

---

# 🔑 API Endpoints

## Authentication

```text
POST /api/auth/register
POST /api/auth/login
PUT  /api/auth/change-password
```

---

## Users

```text
GET /api/users
GET /api/users/{id}
GET /api/users/search

PUT /api/users/{id}/activate
PUT /api/users/{id}/suspend
PUT /api/users/{id}/change-role
PUT /api/users/{id}/update
```

---

## Products

```text
POST   /api/products
GET    /api/products
GET    /api/products/{id}
GET    /api/products/search
PUT    /api/products/{id}
DELETE /api/products/{id}
```

---

## Categories

```text
POST   /api/categories
GET    /api/categories
GET    /api/categories/{id}
PUT    /api/categories/{id}
DELETE /api/categories/{id}
```

---

## Suppliers

```text
POST   /api/suppliers
GET    /api/suppliers
GET    /api/suppliers/{id}
GET    /api/suppliers/search
PUT    /api/suppliers/{id}
DELETE /api/suppliers/{id}
```

---

## Warehouses

```text
POST   /api/warehouses
GET    /api/warehouses
GET    /api/warehouses/{id}
GET    /api/warehouses/search
PUT    /api/warehouses/{id}
DELETE /api/warehouses/{id}
```

---

## Inventory

```text
POST /api/inventory
GET  /api/inventory
GET  /api/inventory/{id}
GET  /api/inventory/search
PUT  /api/inventory/{id}
```

---

## Stock Movements

```text
GET /api/stock-movements
GET /api/stock-movements/{id}
GET /api/stock-movements/search
```

Stock movements can be filtered by attributes such as:

```text
Product
Warehouse
Movement Type
User
Date Range
```

---

## Stock Transfers

```text
POST /api/stock-transfers
GET  /api/stock-transfers
GET  /api/stock-transfers/{id}
GET  /api/stock-transfers/search
```

---

## Purchase Orders

```text
POST  /api/purchase-orders
GET   /api/purchase-orders
GET   /api/purchase-orders/{id}

PATCH /api/purchase-orders/{id}/receive
PATCH /api/purchase-orders/{id}/cancel
```

> Adjust endpoint names above if your controllers use slightly different mappings.

---

# 🐳 Docker

The Spring Boot application is packaged as a Docker image.

### Pull the image

Make sure Docker is installed and running:

```bash
docker pull nazmulhoque416/warehouse-inventory-management:latest
```

### Run the application

```bash
docker run -d \
  --name=warehouse-inventory-management \
  --network=network_name \
  -e DB_URL=jdbc:mysql://database_link:3306/database_name \
  -e DB_PASSWORD=db_password \
  -e DB_USERNAME=db_username \
  -p 8080:8080 \
  nazmulhoque416/warehouse-inventory-management:latest
```

Replace the following values with your environment:

```text
network_name
database_link
database_name
db_username
db_password
```

For example, when using Docker Compose, the MySQL service/container hostname can be used as the database host:

```text
jdbc:mysql://mysql:3306/warehouse
```

---

## 🐳 Docker Architecture

```text
Spring Boot Application
        │
        ▼
      Docker
        │
        ▼
   Docker Image
        │
        ▼
    Docker Hub
        │
        ▼
      Azure
        │
        ▼
   MySQL Database
```

---

# ⚙️ CI/CD with GitHub Actions

The project uses **GitHub Actions** to automate the build, test, and Docker image publishing process.

```text
Developer pushes code
        │
        ▼
   GitHub Repository
        │
        ▼
 GitHub Actions
        │
        ├── Checkout source code
        │
        ├── Setup Java
        │
        ├── Build project
        │
        ├── Run tests
        │
        ├── Build Docker image
        │
        └── Push image to Docker Hub
                    │
                    ▼
                 Docker Hub
                    │
                    ▼
                  Azure
```

This allows a new Docker image to be automatically produced when changes are pushed to the configured branch.

---

# ☁️ Cloud Deployment

The application is designed to run as a containerized Spring Boot application in the cloud.

### Cloud Architecture

```text
                    ┌─────────────────────┐
                    │   GitHub Repository  │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │  GitHub Actions CI  │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │     Docker Hub      │
                    │   Container Image   │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │   Azure Container   │
                    │     Application     │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │     MySQL Database  │
                    │   Cloud Database    │
                    └─────────────────────┘
```

### Deployment Components

* **Application:** Spring Boot
* **Containerization:** Docker
* **Container Registry:** Docker Hub
* **Cloud Platform:** Microsoft Azure
* **Database:** MySQL
* **CI/CD:** GitHub Actions

---

# 🗄️ Database

The application uses MySQL as its relational database.

Main entities include:

```text
User
Product
Category
Supplier
Warehouse
Inventory
StockMovement
StockTransfer
StockTransferItem
PurchaseOrder
PurchaseOrderItem
```

### Important Relationships

```text
Category
    │
    ▼
  Product
    │
    ▼
Inventory ─────── Warehouse


Supplier
    │
    ▼
PurchaseOrder
    │
    ▼
PurchaseOrderItem
    │
    ▼
Product


Warehouse ─────── StockTransfer ─────── Warehouse
                       │
                       ▼
                  Transfer Items
                       │
                       ▼
                    Product


Warehouse ─────── StockMovement
Product ───────── StockMovement
User ──────────── StockMovement
```

---

# 🔐 Security

Spring Security and JWT are used to secure the REST API.

### Authentication Flow

```text
1. User logs in
       ↓
2. Server authenticates credentials
       ↓
3. Server generates JWT
       ↓
4. Client stores JWT
       ↓
5. Client sends JWT with requests
       ↓
6. JWT Filter validates token
       ↓
7. Spring Security checks roles
       ↓
8. Request reaches controller
       ↓
9. Service performs business operation
```

Example request:

```http
Authorization: Bearer <JWT_TOKEN>
```

Role-based access control is implemented using Spring Security.

---

# 🔒 Inventory Consistency & Concurrency

Inventory operations require careful handling because multiple users may attempt to modify the same stock simultaneously.

The application uses **JPA optimistic locking** where appropriate to help maintain inventory consistency during concurrent stock operations.

The inventory model also tracks:

```text
Quantity
Reserved Quantity
Available Quantity
```

Where:

```text
Available Quantity =
Quantity - Reserved Quantity
```

Before a stock operation is performed, the system validates that sufficient available inventory exists.

Example:

```text
Current Quantity = 100
Reserved Quantity = 20

Available Quantity = 100 - 20
                   = 80
```

A transfer requesting more than 80 available units is rejected.

---

# ▶️ Running Locally

## Prerequisites

* Java 26
* Maven
* MySQL
* Docker (optional)

---

## 1. Clone the Repository

```bash
git clone https://github.com/nazmul-hoque-nahid/warehouse-inventory-management.git
```

---

## 2. Configure MySQL

Create a MySQL database and configure the application.

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/warehouse
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

Do not commit real database credentials to GitHub.

---

## 3. Build the Project

```bash
mvn clean package
```

---

## 4. Run the Application

```bash
mvn spring-boot:run
```

The API will normally be available at:

```text
http://localhost:8080
```

---

# 📖 API Documentation

The project includes **OpenAPI / Swagger** configuration for API documentation.

When the application is running, open the configured Swagger UI endpoint to explore and test the REST APIs.

Swagger provides interactive documentation for:

* Authentication
* Users
* Products
* Categories
* Suppliers
* Warehouses
* Inventory
* Stock movements
* Stock transfers
* Purchase orders

---

# 🧪 Testing

The project can be built and tested using Maven:

```bash
mvn clean test
```

The same build and test process is integrated into the GitHub Actions workflow.

---

# 🔄 Development & Deployment Workflow

```text
        Developer
            │
            ▼
       Write Code
            │
            ▼
     Push to GitHub
            │
            ▼
   GitHub Actions
            │
       ┌────┴────┐
       ▼         ▼
    Build       Test
       │         │
       └────┬────┘
            ▼
     Build Docker Image
            │
            ▼
       Docker Hub
            │
            ▼
      Cloud Deployment
            │
            ▼
     MySQL Database
```

---

# 🎯 What I Learned

Through this project, I practiced:

* Building RESTful APIs with Spring Boot
* Designing layered backend architecture
* Spring Data JPA and Hibernate
* Entity relationships
* DTO-based API design
* Spring Security
* JWT authentication
* Role-based authorization
* Global exception handling
* Request validation
* Search and filtering using JPA Specifications
* Inventory management
* Warehouse management
* Stock movement tracking
* Stock transfer workflows
* Purchase order management
* Database transaction management
* Inventory consistency
* JPA optimistic locking
* Concurrent stock operation handling
* Docker containerization
* GitHub Actions CI/CD
* Docker image publishing
* Cloud deployment
* Connecting a containerized application to a MySQL database

---

# 👨‍💻 Author

**NAZMUL HOQUE**

Software Engineering Student | Backend Developer

### Technologies

```text
Java
Spring Boot
Spring Security
JWT
Spring Data JPA
Hibernate
MySQL
Docker
GitHub Actions
Azure
Swagger / OpenAPI
```
