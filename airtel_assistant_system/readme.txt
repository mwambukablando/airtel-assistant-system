

the system name is : airtel assistant system


the main goal of our system is:
To develop a reliable, user-friendly, and fully offline Windows-based inventory management system that ensures 
accurate tracking, accountability, and reporting of all end-user equipment.

Airtel Asset Management System

System Overview

The Airtel Asset Management System is a Java-based enterprise application designed to automate the tracking, assignment, 
and auditing of corporate hardware. It utilizes a Three-Tier Architecture (Controller, Service, Repository) to ensure modularity and data security.

Default Credentials

To access the system, use the following administrative credentials:

Username: 24RP04662

Password: 24RP08537

Core Functionality & Logic Flow
1. User Authentication
The system starts with a login interface. It validates the username and password against the users table in the MariaDB database.
Successful authentication grants access to the dashboard based on the user's assigned role.

2. Asset Inventory Management
The system maintains a real-time record of all hardware in the assets table.
Each asset is tracked by a unique ID and a status (e.g., Available, Assigned, or Under Maintenance).

3. Secure Asset Assignment
The assignment process follows a strict validation sequence to prevent data conflicts:

Identification: The administrator inputs the Asset ID and the Employee Name.

Availability Check:
The system queries the database to verify if the asset status is currently "Available." 
If the asset is already assigned, the system blocks the request.

Transaction Execution: If available, the system performs a synchronized update:

Creates a new record in the assignments table.

Changes the asset status to "Assigned" in the assets table.

Integrity Guarantee: The system uses SQL transactions (setAutoCommit(false)), meaning if the assignment record fails, the asset status will not change, preventing data mismatch.

4. Audit & Accountability
Every successful assignment triggers an automatic entry into the audit_logs. 
This ensures a permanent, unchangeable history of which employee received which asset and when the action took place.

5. Reporting
The system generates reports by querying the database to aggregate asset distributions.
 This allows management to see current inventory levels and employee hardware allocations at a glance.

Technical Requirements
Language: Java (Swing for GUI)

Database: MariaDB

Architecture: MVC (Model-View-Controller) pattern

Database Driver: MySQL Connector/J