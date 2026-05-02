# IT Equipment Complaint Management System
### SDA Assignment — From Design to Implementation

**Course:** Software Design & Analysis  
**Group:** Abdullah Mangrio (24P-0622), Muhammad Musif (24P-0680), Muhammad Atif Khan (24P-0540)  
**Submitted To:** Engr. Muhammad Umer Haroon

---

## Overview

A fully-functional Java Swing desktop application implementing the IT Equipment Complaint Management System previously designed in the SRS document.

---

## How to Run

**Requirements:** Java 8+ (JDK or JRE)

```bash
# Option 1: Run the JAR directly
java -jar ITComplaintSystem.jar

# Option 2: Compile and run from source
javac -d out $(find src -name "*.java")
java -cp out com.itcms.Main
```

---

## Demo Accounts

| Username | Password   | Role        |
|----------|------------|-------------|
| admin    | admin123   | Admin       |
| ali      | ali123     | IT Staff    |
| kamran   | kamran123  | Technician  |
| bilal    | bilal123   | Technician  |
| sara     | sara123    | User        |
| usman    | usman123   | User        |

---

## Features Implemented

### Use Cases Covered (from SRS)
- ✅ FR-01/FR-02: User Registration & Login (with role-based access)
- ✅ FR-03/FR-04/FR-05: Report IT equipment problems (with equipment type, description)
- ✅ FR-06: Upload image path for problem
- ✅ FR-07: Select location (lab/office)
- ✅ FR-08: Auto-generate complaint ticket ID
- ✅ FR-09: In-memory data storage (DataStore singleton)
- ✅ FR-10/FR-11: View and track complaint status
- ✅ FR-12: IT Staff views all complaints
- ✅ FR-13: IT Staff assigns complaints to technicians
- ✅ FR-14/FR-15/FR-16: Technician views, updates status & adds repair notes
- ✅ FR-17: IT Staff marks complaints as resolved
- ✅ FR-18: Users give feedback (rating + comments) on resolved complaints
- ✅ FR-19: Admin manages/views user accounts
- ✅ FR-20: Admin generates complaint reports

---

## Architecture (Design to Code Mapping)

### Classes (from Class Diagram)
```
com.itcms.model/
├── User.java          — Base user class with login(), register()
├── Admin.java         — extends User: generateReport(), deleteInvalidComplaint()
├── ITStaff.java       — extends User: assignComplaint(), markResolved()
├── Technician.java    — extends User: updateRepairStatus(), addRepairNotes()
├── Complaint.java     — generateTicketId(), addRepairNotes()
├── Equipment.java     — verifyCompany(), updateStatus()
├── Report.java        — generateReport(), exportReport()
├── Feedback.java      — submitFeedback()
└── Notification.java  — sendNotification(), markAsRead()

com.itcms.service/
└── DataStore.java     — Singleton in-memory database (authenticate, CRUD)

com.itcms.util/
└── UITheme.java       — Dark theme, colors, fonts, reusable UI components

com.itcms.ui/
├── LoginFrame.java    — Login screen with demo account hints
├── RegisterFrame.java — New user registration
├── BaseDashboard.java — Abstract base with sidebar navigation
├── UserDashboard.java — Submit/track complaints, notifications, feedback
├── ITStaffDashboard.java — View all, assign technicians, mark resolved
├── TechnicianDashboard.java — View assigned, update status, add notes
└── AdminDashboard.java — User management, reports, delete complaints
```

### Relationships Implemented
- **Inheritance**: Admin, ITStaff, Technician all `extend User`
- **Association**: Complaint → User (reporter), Complaint → Equipment, Complaint → Technician
- **Aggregation**: DataStore manages all entities
- **Dependency**: Dashboards depend on DataStore

---

## GUI Screens

1. **Login Screen** — Username/password login, register new account link
2. **Registration Screen** — New user account creation with validation
3. **User Dashboard** — Stats, submit complaint form, my complaints table, feedback form
4. **IT Staff Dashboard** — All complaints table, assign technician dialog, mark resolved
5. **Technician Dashboard** — Assigned complaints, update status + repair notes
6. **Admin Dashboard** — Full stats grid, manage all complaints (delete), user table, generated reports

---

## LLM Experience (Critical Analysis)

**Tool Used:** Claude (Anthropic)

**What went well:**
- LLM accurately mapped the class diagram attributes and methods to Java
- Generated consistent inheritance hierarchy (User → Admin/ITStaff/Technician)
- Produced working Swing GUI with cohesive dark theme and reusable components
- Correctly implemented all 20 functional requirements from the SRS

**Limitations observed:**
- LLM initially introduced a naming conflict (inner class `DataStore` shadowing the service class) — required manual correction
- LLM cannot run/test GUI code itself, so visual layout issues require manual testing
- Generated code follows one consistent style — customization still requires developer judgment

**Conclusion:** LLM assistance significantly accelerated implementation (hours vs. days), but critical review of generated code remains essential. The developer must understand the design deeply to verify correctness of the mapping from UML to code.
