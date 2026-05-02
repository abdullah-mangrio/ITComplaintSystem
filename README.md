# IT Equipment Complaint Management System
> A fully-functional Java Swing desktop application for managing IT equipment complaints in a university environment — built from SRS → UML Design → Working Implementation.

**Course:** Software Design & Analysis (SDA) | FAST NUCES Peshawar  
**Stack:** Java 8+, Java Swing, OOP Design Patterns  

---

## 🚀 Quick Start

```bash
# Option 1: Run JAR directly (no setup needed)
java -jar ITComplaintSystem.jar

# Option 2: Compile from source
javac -d out $(find src -name "*.java")
java -cp out com.itcms.Main
```

**Demo Accounts**

| Username | Password   | Role        |
|----------|------------|-------------|
| admin    | admin123   | Admin       |
| ali      | ali123     | IT Staff    |
| kamran   | kamran123  | Technician  |
| sara     | sara123    | User        |

---

## ✨ Features

### 👤 User
- Register / Login with role-based access
- Submit IT equipment complaints (type, description, location, image)
- Auto-generated complaint ticket ID
- Track complaint status in real-time
- Submit feedback with rating on resolved complaints

### 🛠️ IT Staff
- View and manage all submitted complaints
- Assign complaints to technicians
- Mark complaints as resolved

### 🔧 Technician
- View assigned complaints
- Update repair status and add repair notes

### 🛡️ Admin
- Manage user accounts
- Delete invalid complaints
- Generate complaint reports with stats

---

## 🏗️ Architecture

This project follows a complete **Design → Implementation** pipeline:

```
SRS Document (Requirements)
        ↓
UML Design (Use Case + Class Diagrams)
        ↓
Java Implementation (Class Diagram → Code)
        ↓
Swing GUI (Role-Based Dashboards)
```

### Design Patterns Used
- **Singleton** — `DataStore` as centralized in-memory database
- **Inheritance** — `Admin`, `ITStaff`, `Technician` all extend `User`
- **Abstract Class** — `BaseDashboard` shared by all role dashboards
- **Association / Aggregation** — Complaint linked to User, Equipment, Technician

### Package Structure

```
com.itcms/
├── model/         → User, Admin, ITStaff, Technician, Complaint, Equipment, Feedback, Notification, Report
├── service/       → DataStore.java (Singleton in-memory DB)
├── ui/            → LoginFrame, RegisterFrame, BaseDashboard, role-specific Dashboards
└── util/          → UITheme.java (dark theme, fonts, reusable components)
```

---

## 📋 Requirements Coverage

All 20 functional requirements from the SRS are implemented:

| FR | Description | Status |
|----|-------------|--------|
| FR-01/02 | User Registration & Login | ✅ |
| FR-03–07 | Report IT problems (type, desc, image, location) | ✅ |
| FR-08 | Auto-generate complaint ticket ID | ✅ |
| FR-09 | In-memory data storage (DataStore singleton) | ✅ |
| FR-10/11 | View and track complaint status | ✅ |
| FR-12/13 | IT Staff views and assigns complaints | ✅ |
| FR-14–16 | Technician views, updates status, adds notes | ✅ |
| FR-17 | IT Staff marks complaints resolved | ✅ |
| FR-18 | User submits feedback with rating | ✅ |
| FR-19/20 | Admin manages users and generates reports | ✅ |

---

## 🖥️ GUI Screens

1. **Login** — Role detection, register link
2. **Registration** — New user account with validation
3. **User Dashboard** — Submit complaints, track status, give feedback
4. **IT Staff Dashboard** — View all complaints, assign technicians, mark resolved
5. **Technician Dashboard** — View assigned tasks, update status and notes
6. **Admin Dashboard** — Full stats, user management, complaint reports

---

## 👨‍💻 Team

| Name | Role |
|------|------|
| **Abdullah Mangrio** (24P-0622) | Backend Architecture, OOP Design, DataStore, UML-to-Code Mapping, System Integration |
| **Muhammad Musif** (24P-0680) | Frontend UI, Swing Components, Dashboard Layouts |
| **Muhammad Atif Khan** (24P-0540) | Requirements & SRS, UML Diagrams, Testing |

---

## 📄 License
Academic project — FAST NUCES Peshawar, 2026.
