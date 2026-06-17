# 📋 System Requirements
*Coffee Shop Management System*

This document details the specific Functional Requirements (FR) and Non-Functional Requirements (NFR) that the development team must fulfill to build the system.

## Functional Requirements (FR)

### Core POS & Ordering
*   **FR01 - User Authentication:** The system must allow employees to log in securely using a unique username and password.
*   **FR02 - Role Authorization:** The system must restrict access to specific modules based on the user's role (Cashier, Barista, Manager).
*   **FR03 - Cash Session Management:** The system must require a cashier to open a "Cash Register Session" with a starting cash float balance before processing any transactions.
*   **FR04 - Order Creation & Modifiers:** The system must allow cashiers to create orders and add predefined modifiers (e.g., Extra Syrup, Milk alternatives) to items, automatically adjusting the price and recipe components.
*   **FR05 - Price & Tax Calculation:** The system must automatically calculate the subtotal, specific taxes (VAT), discounts, and total price of an order.

### Payments & Invoicing
*   **FR06 - Split Payments:** The system must allow a single invoice to be paid using multiple payment methods concurrently (e.g., $10 Cash, $5 Card).
*   **FR07 - Fiscal Invoicing:** Upon successful payment, the system must generate a formal fiscal invoice separate from the preparation order, containing tax breakdowns and customer data.
*   **FR08 - Refunds & Voids:** The system must allow authorized users (Managers) to void an invoice or process a refund, generating a reverse adjustment transaction in both the financial ledger and the inventory Kardex.
*   **FR09 - Cash Reconciliation (Arqueo):** The system must allow cashiers to close their register session at the end of a shift, prompting them to declare physical cash and calculating any over/short discrepancies against the expected system cash.

### Inventory & Supply Chain
*   **FR10 - Recipe Deduction:** When an order is completed, the system must automatically subtract the exact quantities of raw ingredients from the inventory based on the product's recipe AND any selected modifiers.
*   **FR11 - Unit Conversion:** The system must automatically convert supplier bulk units (e.g., Kilograms) into preparation recipe units (e.g., Grams) when registering stock entries.
*   **FR12 - Inventory Audit (Kardex):** The system must record EVERY change in inventory stock (sales deductions, deliveries, spoilage) as an immutable transaction record indicating the user, timestamp, quantity, unit, and type of movement.
*   **FR13 - Inventory Alerts:** The system must display a visual warning to the Manager when an ingredient's stock falls below its predefined minimum threshold.
*   **FR14 - Purchase Orders:** The system must allow the Manager to generate, save, and track digital purchase orders sent to registered suppliers.
*   **FR15 - Product Management:** The system must allow the Manager to CRUD menu items, categories, recipes, and product modifiers.

### Preparation & CRM
*   **FR16 - Order Queue (KDS):** The system must display a real-time chronological queue of pending preparation orders on the Barista interface.
*   **FR17 - Order Status:** The system must allow preparation staff to change the status of an order (Pending -> Preparing -> Ready).
*   **FR18 - Customer Registration:** The system must allow cashiers to register new customers by capturing their name, phone number, and email.
*   **FR19 - Loyalty Engine:** The system must automatically assign loyalty points to registered customers based on their purchase amount and allow the redemption of those points.
*   **FR20 - Reporting:** The system must generate exportable sales, cash discrepancy, and inventory kardex reports.

## Non-Functional Requirements (NFR)

*   **NFR01 - Performance:** The POS interface must process and confirm an order in less than 2 seconds.
*   **NFR02 - Usability:** The POS and KDS interfaces must be touch-friendly and optimized for tablet screens.
*   **NFR03 - Data Integrity (Immutability):** Financial invoices and Kardex inventory transactions must be strictly immutable once created. Corrections must be handled via formal cancellation or adjustment transactions.
*   **NFR04 - Security:** Passwords must be encrypted using strong hashing algorithms (e.g., bcrypt).
*   **NFR05 - Availability:** The system must target 99.9% uptime during operating hours.
*   **NFR06 - Concurrency:** The system must handle concurrent inventory deductions securely to prevent race conditions during high sales volume.
*   **NFR07 - Backup:** The database must automatically perform a full backup every 24 hours.
