# Enterprise Coupon & Discount Engine

This is a Spring Boot application designed to evaluate and apply discounts based on complex rules, ensuring correctness under concurrent usage.

## Review Questions Addressed

### 1. How will you design extensible coupon rules?
The system uses the **Strategy Pattern**. The interface `DiscountStrategy` calculates the discount. Implementations like `FlatDiscountStrategy`, `PercentageDiscountStrategy`, and `ConditionalDiscountStrategy` provide specific behaviors. A `DiscountStrategyFactory` injects the correct strategy based on the `CouponType` enum. To add a new rule (e.g., Buy One Get One), we simply add a new `CouponType` and a corresponding `DiscountStrategy` implementation without modifying the existing service logic (Open-Closed Principle).

### 2. How do you ensure a coupon is not reused?
We ensure this through two layers:
- **Business Logic validation**: The `CouponService` checks if `userCouponRepository.findByUserIdAndCouponId` already has a record with `usedFlag = true`.
- **Database constraints**: The `UserCoupon` entity has a `@UniqueConstraint` on `(user_id, coupon_id)`, and `OrderDiscount` has a `@UniqueConstraint` on `(order_id, coupon_id)`. This provides **idempotency**, guaranteeing that even if race conditions bypass the Java-level checks, the DB will reject duplicate applications.

### 3. How to handle concurrency in coupon usage?
Concurrency when decrementing `usage_limit` is handled using **Pessimistic Locking**.
- In `CouponRepository`, we use `@Lock(LockModeType.PESSIMISTIC_WRITE)` when fetching the coupon during checkout (`findByCodeWithLock`). 
- This instructs MySQL to execute `SELECT ... FOR UPDATE`, blocking other transactions from reading or modifying the same row until the current transaction commits. This strictly prevents race conditions from overallocating the usage limit.

### 4. How to avoid N+1 problem in this system?
The N+1 problem commonly occurs when loading a parent entity (like `Order`) and then lazily fetching its collections (like `OrderDiscount`).
To avoid this, we use a custom JPQL query with **JOIN FETCH** in `OrderRepository`:
`SELECT o FROM Order o LEFT JOIN FETCH o.discounts WHERE o.id = :id`
This eagerly fetches the order and its related discounts in a single SQL query, avoiding subsequent queries to the DB.

## Technology Stack
- Java 17
- Spring Boot 3
- Spring Data JPA
- Hibernate Validator
- MySQL
- Maven

## How to run
Update `src/main/resources/application.properties` with your MySQL credentials, then run `CouponEngineApplication.java`.
