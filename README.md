# Prince Urban Knot — Backend

A production-deployed REST API for a men's neckties e-commerce platform. Built with Spring Boot, secured with JWT authentication, and backed by a cloud-hosted MySQL database on Aiven.

**Live API:** [prince-urban-knot-backend.onrender.com](https://prince-urban-knot-backend.onrender.com)  
**Frontend Repo:** [prince-urban-knot-frontend](https://github.com/jagdeep2001kainth/prince-urban-knot-frontend)

---

## Tech Stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot 4.0 |
| Language | Java 17 |
| Security | Spring Security + JWT (jjwt) |
| ORM | Spring Data JPA / Hibernate |
| Database | MySQL 8 (Aiven cloud-hosted) |
| Build | Maven |
| Containerization | Docker |
| Deployment | Render (Dockerized) |

---

## Features

- **JWT authentication** — stateless auth with role claims (`USER`, `ADMIN`) embedded in tokens; verified on every request via a custom `JwtFilter`
- **Role-based access control** — Spring Security `SecurityFilterChain` enforces `hasAuthority("ADMIN")` on all write operations; public GET endpoints require no auth
- **Product catalog** — full CRUD for products, category filtering via `?category=` query param, single product lookup by ID
- **Multi-image product support** — each product stores up to 3 Cloudinary image URLs via JPA `@ElementCollection` (`product_images` table), with a legacy `imageUrl` field kept for backward compatibility
- **Bulk upload** — `/api/products/bulk-v3` endpoint accepts a JSON array of products with `imageUrls` list, auto-populates legacy `imageUrl` from first entry
- **Cart & Orders** — add/remove cart items, place orders, view order history per authenticated user
- **Health endpoint** — `/health` returns live status; pinged every 5 minutes by cron-job.org to prevent Render free-tier cold starts

---

## API Endpoints

### Auth
| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/api/auth/register` | Public | Register new user |
| POST | `/api/auth/login` | Public | Login, returns JWT |

### Products
| Method | Endpoint | Auth | Description |
|---|---|---|---|
| GET | `/api/products` | Public | All products (optional `?category=`) |
| GET | `/api/products/{id}` | Public | Single product |
| GET | `/api/products/category/{category}` | Public | Products by category |
| POST | `/api/products` | ADMIN | Create single product |
| POST | `/api/products/bulk-v3` | Authenticated | Bulk create with multi-image support |
| PUT | `/api/products/{id}` | ADMIN | Update product |
| DELETE | `/api/products/{id}` | ADMIN | Delete product |

### Cart
| Method | Endpoint | Auth | Description |
|---|---|---|---|
| GET | `/api/cart` | Authenticated | Get cart items |
| POST | `/api/cart/add` | Authenticated | Add item to cart |
| DELETE | `/api/cart/remove/{id}` | Authenticated | Remove cart item |

### Orders
| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/api/orders/place` | Authenticated | Place order from cart |
| GET | `/api/orders` | Authenticated | Get order history |

### System
| Method | Endpoint | Auth | Description |
|---|---|---|---|
| GET | `/health` | Public | Health check |

---

## Project Structure

```
src/main/java/com/jagdeep/princeurbanknot/
├── config/
│   ├── SecurityConfig.java     # Spring Security filter chain, route auth rules
│   ├── JwtUtil.java            # Token generation and claim extraction
│   ├── JwtFilter.java          # Per-request JWT validation, sets SecurityContext
│   └── CorsConfig.java         # CORS for Vercel frontend origin
├── controller/
│   ├── AuthController.java         # Register + login endpoints
│   ├── ProductController.java      # Product CRUD + category filter
│   ├── ProductBulkV3Controller.java # Multi-image bulk upload
│   ├── CartController.java
│   ├── OrderController.java
│   └── HealthController.java
├── model/
│   ├── Product.java    # @ElementCollection for imageUrls, @PrePersist for createdAt
│   ├── User.java       # Role enum (USER, ADMIN)
│   ├── Cart.java
│   ├── CartItem.java
│   └── Order.java
├── repository/
│   ├── ProductRepository.java  # findByCategory() custom query
│   └── ...
└── service/
    ├── ProductService.java
    ├── UserService.java
    └── ...
```

---

## Architecture Decisions

**Stateless JWT authentication**  
No session storage server-side. Each request carries a self-contained JWT signed with HMAC-SHA256. The `JwtFilter` extracts the `role` claim and sets a `UsernamePasswordAuthenticationToken` with the appropriate `GrantedAuthority` on every request, which Spring Security's authorization layer then evaluates against route-level rules.

**`@ElementCollection` for multiple images**  
Rather than creating a full `ProductImage` entity with its own repository, `@ElementCollection` with `FetchType.EAGER` stores image URLs in a joined `product_images` table with an `image_order` column. This keeps the `Product` aggregate clean while supporting ordered multi-image display on the frontend.

**Legacy `imageUrl` field preserved**  
The original single `imageUrl` column is kept alongside `imageUrls` for backward compatibility. `@PrePersist` auto-populates it from `imageUrls.get(0)` so existing frontend code (product cards, category grids) continues working without modification.

**Versioned bulk upload endpoints**  
Rather than patching the original `/bulk` endpoint through repeated debugging cycles, new isolated endpoints (`/bulk-v2`, `/bulk-v3`) were created incrementally. This preserved working behavior at each stage and made rollback trivial.

**Environment-based configuration**  
All secrets (DB credentials, JWT secret) are injected via Render environment variables at runtime. `application-prod.properties` contains only `${PLACEHOLDER}` references — no credentials ever committed to the repository.

---

## Database Schema

```sql
products          -- id, name, description (TEXT), price, image_url, category, stock, created_at
product_images    -- id, product_id (FK), image_url, image_order
users             -- id, name, email, password (bcrypt), role, created_at
carts             -- id, user_id (FK)
cart_items        -- id, cart_id (FK), product_id (FK), quantity
orders            -- id, user_id (FK), total, created_at
order_items       -- id, order_id (FK), product_id (FK), quantity, price
```

---

## Local Development

```bash
# Clone
git clone https://github.com/jagdeep2001kainth/prince-urban-knot-backend
cd prince-urban-knot-backend

# Set up local properties
cp src/main/resources/application-prod.properties src/main/resources/application-dev.properties
# Fill in your local MySQL credentials in application-dev.properties

# Run
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

---

## Environment Variables

| Variable | Description |
|---|---|
| `DB_URL` | JDBC connection string (e.g. `jdbc:mysql://host:port/db?sslMode=REQUIRED`) |
| `DB_USERNAME` | Database username |
| `DB_PASSWORD` | Database password |
| `JWT_SECRET` | HMAC-SHA256 signing key (base64, min 64 chars recommended) |
| `JWT_EXPIRATION` | Token TTL in milliseconds (e.g. `86400000` = 24h) |

---

## Deployment

Deployed on **Render** as a Dockerized web service. Docker build uses a multi-stage Maven + Eclipse Temurin 17 JRE image. Auto-deploys on push to `main`. A cron-job.org monitor pings `/health` every 5 minutes to prevent free-tier instance spin-down.