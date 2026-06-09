# Software Requirements Specification (SRS)

## Coffee Shop Management System (Enterprise Edition)

## 1. Introduction

### 1.1 Purpose

The purpose of this Software Requirements Specification (SRS) is to provide a comprehensive, detailed, and unequivocal description of the Coffee Shop Management System. This document serves as the definitive source of truth for all stakeholders, software engineers, and Quality Assurance (QA) personnel regarding the system's functional capabilities, operational constraints, and non-functional guarantees.

### 1.2 Scope

The system is an Point of Sale (POS) and internal management platform architected specifically for physical coffee shop operations. The core modules encompass:

- **Order & Checkout Lifecycle:** End-to-end order creation, modification, dynamic tax/discount calculations, and multi-modal secure payment processing.
- **Production Workflow:** Real-time, event-driven synchronization of orders to Kitchen/Barista Display Systems (KDS).
- **Inventory Control:** Automated, recipe-driven stock deduction to ensure precise inventory parity.
- **Floor & Human Resources Management:** Dynamic table assignment, role-based access control, and shift operations management.

_Exclusions:_ The system currently excludes automated payroll generation, advanced corporate general ledger accounting, and consumer-facing mobile applications.

### 1.3 Glossary & Definitions

| Term             | Definition                                                                                                                                                        |
| :--------------- | :---------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **POS**          | Point of Sale; the hardware/software ecosystem where retail transactions are executed.                                                                            |
| **KDS**          | Kitchen Display System; digital screens used by production staff to track orders.                                                                                 |
| **Product**      | A finalized, sellable item presented to the customer (e.g., "Vanilla Latte").                                                                                     |
| **Ingredient**   | A raw material tracked perpetually in the inventory (e.g., "Whole Milk", "Coffee Beans").                                                                         |
| **Recipe**       | The exact quantitative mapping of Ingredients required to synthesize one unit of a Product.                                                                       |
| **Tokenization** | The cryptographic process of substituting sensitive payment data (e.g., credit card PANs) with non-sensitive equivalents (tokens) for secure transit and storage. |

---

## 2. System Overview

### 2.1 User Characteristics (Actors)

- **Customer:** The initiator of orders. Profiles may be anonymous or fully registered for loyalty tracking and digital receipt delivery.
- **Cashier:** The primary operator of the POS terminal, responsible for order intake, customer interaction, and payment processing.
- **Barista / Chef:** The production staff who monitor the KDS, fulfill product recipes, and trigger order status updates.
- **Waiter:** Floor staff responsible for table management, order delivery, and state updates.
- **Store Manager:** The administrative authority holding privileges for inventory auditing, HR oversight, and financial reporting.
- **Supplier:** An external entity (or integrated B2B interface) supplying inventory replenishment.

### 2.2 Operating Environment

- **Data Persistence:** The system utilizes MongoDB for transactional, inventory, and catalog data storage, implemented via the Repository Pattern.
- **User Interface:** The system utilizes an interactive Command-Line Interface (CLI) in Java to simulate and execute all operational workflows.
- **Localization:** The system utilizes Java ResourceBundles to provide full Internationalization (i18n) support, offering dynamic toggling between English and Spanish.

### 2.3 Design & Implementation Constraints

- **Architectural Pattern:** The implementation MUST adhere strictly to the Model-View-Controller (MVC) or Clean Architecture principles, ensuring absolute decoupling via specialized Domain Managers (`OrderManager`, `InventoryManager`, `HRManager`, `FloorManager`).
- **Financial Data Types:** All monetary and quantitative calculations MUST utilize arbitrary-precision decimal types (e.g., `BigDecimal`). The use of floating-point primitives (`double`, `float`) for currency is strictly prohibited to prevent rounding anomalies.
- **Security Standard:** Raw Credit Card numbers are never processed or stored; electronic transactions are simulated using opaque Payment Tokens.
- **Data Integrity:** Database operations must ensure consistency and reliability when managing order payments, KDS status updates, and recipe-driven ingredient deduction.

---

### 3. Functional Requirements

### 3.1 Order Management & Customization

| ID        | Title                   | Description                                                                                                                                     | Priority |
| :-------- | :---------------------- | :---------------------------------------------------------------------------------------------------------------------------------------------- | :------- |
| **FR-01** | **Order Creation**      | The Cashier must be able to initiate new orders, add/remove `OrderItem` entities, apply customizations (`Modifier`), and link to a Customer.     | Critical |
| **FR-02** | **Dynamic Calculation** | The system must calculate the Subtotal, apply conditional discounts (Strategy pattern), and compute exact taxes to formulate the final Total.   | Critical |
| **FR-03** | **Combos & Modifiers**  | Support composite products (`Combo`) and item modifiers (`Modifier`) with custom recipes that dynamically affect total prices and inventory.   | High     |

**Acceptance Criteria (FR-01, FR-02 & FR-03):**
- Order totals update dynamically upon modifier addition or discount strategy changes.
- Combo prices are computed polimorficamente via the composite product price definition.
- Modifiers add additional prices and trigger ingredient deduction based on their custom recipe.

### 3.2 Payment Processing & Invoicing

| ID        | Title                    | Description                                                                                                       | Priority |
| :-------- | :----------------------- | :---------------------------------------------------------------------------------------------------------------- | :------- |
| **FR-04** | **Multi-Modal Checkout** | Support for Cash, Tokenized Credit Card, and Bank Transfer payments via an abstract `PaymentVisitor` (Visitor).   | Critical |
| **FR-05** | **Invoice Generation**   | Emit a formal sequential `Invoice` containing customer tax ID (RUC/Cedula), email, date, and invoice totals.      | Critical |

**Acceptance Criteria (FR-04 & FR-05):**
- Cash payments calculate and display exact change.
- Invoices are persisted in MongoDB and printed to a text file immediately upon checkout success.

### 3.3 Production, Floor & Shift Operations

| ID        | Title                | Description                                                                                                | Priority |
| :-------- | :------------------- | :--------------------------------------------------------------------------------------------------------- | :------- |
| **FR-06** | **KDS Queue**        | Paid orders manifest on KDS queues. Baristas can toggle statuses: `PENDING` -> `PREPARING` -> `READY`.     | High     |
| **FR-07** | **Table Reservations**| Waiters can reserve tables for customers with specific dates/times using `Reservation` entities.           | Medium   |
| **FR-08** | **Shift Tracking**   | Cashiers and Managers can open and close shifts (`Shift`), verifying initial and final drawer cash.        | High     |

**Acceptance Criteria (FR-06, FR-07 & FR-08):**
- Table states transition smoothly: `FREE` -> `RESERVED` -> `OCCUPIED`.
- Active shifts restrict sales actions to the assigned employee and record till cash drawer reconciliation.

### 3.4 Inventory Control & Supply Chain

| ID        | Title                   | Description                                                                                                                        | Priority |
| :-------- | :---------------------- | :--------------------------------------------------------------------------------------------------------------------------------- | :------- |
| **FR-09** | **Recipe Deduction**    | Transitioning an order to `PREPARING` triggers the `InventoryManager` to deduct the ingredients defined in the recipe.             | Critical |
| **FR-10** | **Purchase Orders**     | Managers can create `PurchaseOrder` sheets. Receiving the order automatically increases ingredient stock in MongoDB.             | Medium   |
| **FR-11** | **Equipment Check**     | Block preparation of products that require machinery (`Equipment`) that is currently set to `MAINTENANCE` or `OFF`.                 | High     |

**Acceptance Criteria (FR-09, FR-10 & FR-11):**
- Low stock alert warning fires on console when an ingredient stock drops below `minimumAlertQuantity`.
- Checkout blocks if ingredient stock is insufficient (Preemptive Block).
- Product preparation is prevented if dependent equipment is not `ON`.

### 3.5 Backoffice & Reports

| ID        | Title                    | Description                                                                                                       | Priority |
| :-------- | :----------------------- | :---------------------------------------------------------------------------------------------------------------- | :------- |
| **FR-12** | **Polymorphic Reports**  | Managers can generate three types of reports: Z-Report (financial), InventoryReport, and SalesReport.             | High     |

**Acceptance Criteria (FR-12):**
- Reports implement the `Report` interface and export to formatted text files in the project workspace.

---

## 4. Non-Functional Requirements (NFRs)

| Category        | ID     | Requirement Description                                                   | Target Metric                           |
| :-------------- | :----- | :------------------------------------------------------------------------ | :-------------------------------------- |
| **Performance** | NFR-01 | Latency for primary POS interactions (item addition, UI navigation).      | < 500ms (95th percentile)               |
| **Scalability** | NFR-02 | Concurrent request handling without service degradation.                  | 50+ simultaneous POS/Tablet clients     |
| **Security**    | NFR-03 | Sensitive data protection regarding Employee access and Customer details. | Encrypted at rest; RBAC enforced        |
| **Reliability** | NFR-04 | System availability during operational business hours (06:00 - 22:00).    | 99.9% Uptime                            |
| **Resilience**  | NFR-05 | POS offline capability for cache-based order intake.                      | Support for localized Cash transactions |

---

## 5. Enterprise Business Rules

- **BR-01 (State Enforcement):** An `Order` object is strictly prohibited from entering the `PREPARING` state until the associated `Payment` completes and the status is finalized as `PAID`.
- **BR-02 (Inventory Integrity):** Ingredient quantities are immutable below zero. Reaching 0 automatically triggers an "Out of Stock" lock on dependent Products. Low stock alerts trigger when quantities reach their custom minimum alert threshold.
- **BR-03 (Financial Accuracy):** No monetary or inventory-weight variable shall be declared as a floating-point primitive. Arbitrary-precision types are mandatory.
- **BR-04 (Price Snapshot Consistency):** Invoiced items must preserve a flat price snapshot (`pricePaidSnapshot`) captured during checkout, isolating historical order subtotals from subsequent catalog price updates.

---

## 6. Design Patterns & Architecture

- **Repository Pattern:** Fully decouples the MongoDB persistence code from the core business logic (Managers).
- **Visitor Pattern:** Used for payment processing to handle different payment types (Cash, Card, Transfer) polymorphically.
- **Model-View-Controller (MVC):** Decouples user interaction (CLI View) from managers (Controllers) and data models.
