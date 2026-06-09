# Enterprise Features Roster

## Coffee Shop Management System

This document outlines the systematic breakdown of capabilities (Features) engineered into the platform. This matrix is structurally aligned with the Software Requirements Specification (SRS) and forms the foundation for all UML Use Case modeling.

---

### Module 1: Point of Sale (POS) & Order Engine

| Feature ID | Nomenclature               | Technical Description                                                                    |
| :--------- | :------------------------- | :--------------------------------------------------------------------------------------- |
| **F1.1**   | **Interactive Console Flow**| Command-Line Interface (CLI) in Java, designed for rapid order and navigation.           |
| **F1.2**   | **Product Modifiers**      | Modifiers modeled as entities with specific extra price and inventory recipe (`Modifier`). |
| **F1.3**   | **CRM Integration**        | Association of transactions to registered Customer profiles and loyalty tiers (`LoyaltyTier`). |
| **F1.4**   | **Real-Time Financials**   | Synchronous computation of Subtotals, localized Tax (IVA), and Final Totals using BigDecimal. |
| **F1.5**   | **Discount Authorization** | Dynamic application of discounts using the Strategy pattern (`Discount`).               |
| **F1.6**   | **Combo Pricing**          | Support composite products (`Combo`) composed of multiple single products with special pricing. |

### Module 2: Checkout & Payment Gateway

| Feature ID | Nomenclature              | Technical Description                                                                  |
| :--------- | :------------------------ | :------------------------------------------------------------------------------------- |
| **F2.1**   | **Multi-Modal Parsing**   | Algorithmic routing for Cash, Credit/Debit, and Bank Transfer protocols.               |
| **F2.2**   | **Visitor Pattern (Double Dispatch)** | Advanced polymorphic payment resolution, showcasing clean OOP design.       |
| **F2.3**   | **Tender Calculation**    | Automated precision calculation for cash change issuance.                              |
| **F2.4**   | **Simulated Tokenization**| Simulation of secure transactions using opaque Payment Tokens (no real cards stored).   |
| **F2.5**   | **Invoice & Receipt**     | Automatic generation of printed receipts and sequential official invoices (`Invoice`) storing RUC/Cédula. |

### Module 3: Kitchen Display System (KDS)

| Feature ID | Nomenclature            | Technical Description                                                                      |
| :--------- | :---------------------- | :----------------------------------------------------------------------------------------- |
| **F3.1**   | **State Transitions**   | Interactive toggles shifting orders through `PENDING`, `PREPARING`, and `READY` states.    |
| **F3.2**   | **Waiter Notification** | Visual notifications indicating when table-assigned orders transition to the `READY` state. |

### Module 4: Perpetual Inventory Control

| Feature ID | Nomenclature               | Technical Description                                                                    |
| :--------- | :------------------------- | :--------------------------------------------------------------------------------------- |
| **F4.1**   | **Recipe Deduction**       | Algorithmic, micro-measurement subtraction of raw `Ingredients` upon order preparation.  |
| **F4.2**   | **Threshold Alerts**       | Visual alerts in the console when ingredient stock falls beneath pre-defined safety minimums. |
| **F4.3**   | **Preemptive Block**       | Real-time POS interlock preventing the checkout of items lacking required raw materials.  |
| **F4.4**   | **Audit & Reconciliation** | Administrative CLI CRUD interface for adding, editing, and deleting inventory items.     |
| **F4.5**   | **Purchase Orders**        | Procurement flow to register purchase orders (`PurchaseOrder`) and increase stock upon receipt. |

### Module 5: Floor & Human Capital Management

| Feature ID | Nomenclature              | Technical Description                                                                  |
| :--------- | :------------------------ | :------------------------------------------------------------------------------------- |
| **F5.1**   | **Spatial Mapping**       | State-machine tracking of physical table statuses (`FREE`, `OCCUPIED`, `RESERVED`).    |
| **F5.2**   | **Staff Allocation**      | ID-based assignment of Waiters to Tables, and management of Employee shifts.           |
| **F5.3**   | **RBAC Enforcement**      | Employee roles (Cashier, Waiter, Manager) to restrict access to backoffice operations. |
| **F5.4**   | **Table Reservations**    | Reserve tables with date and time using `Reservation` objects.                           |
| **F5.5**   | **Shift Management**      | Shift check-in and check-out tracking for cash drawer reconciliation (`Shift`).        |

### Module 6: Administrative Backoffice

| Feature ID | Nomenclature                 | Technical Description                                                                      |
| :--------- | :--------------------------- | :----------------------------------------------------------------------------------------- |
| **F6.1**   | **Menu Schema Manager**      | ID-based CRUD interfaces for products, including dynamic recipe updates.                   |
| **F6.2**   | **Report Generation**        | Polymorphic generation of Z-Report, InventoryReport, and SalesReport exported to local files. |
| **F6.3**   | **Repository Pattern**       | Total decoupling of MongoDB database access from core business logic (Managers).           |
| **F6.4**   | **Internationalization**     | 100% localization of all UI prompts and system exceptions (English/Spanish) using ResourceBundles. |
| **F6.5**   | **Equipment Control**        | State control of coffee machinery (`Equipment`) ON/OFF/MAINTENANCE to block dependent sales. |
