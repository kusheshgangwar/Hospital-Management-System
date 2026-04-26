# 🏥 Hospital Management System
## Java Swing + MySQL | College Project

---

## 📁 Project Structure
```
HospitalManagement/
├── database.sql                          ← Pehle yeh run karo MySQL mein
├── src/
│   └── hospital/
│       ├── HospitalManagement.java       ← MAIN CLASS (Run karo yahan se)
│       ├── db/
│       │   └── DatabaseConnection.java   ← DB Connection settings
│       ├── model/
│       │   ├── Patient.java
│       │   ├── Doctor.java
│       │   ├── Appointment.java
│       │   ├── MedicalRecord.java
│       │   └── Bill.java
│       ├── dao/
│       │   ├── PatientDAO.java
│       │   ├── DoctorDAO.java
│       │   ├── AppointmentDAO.java
│       │   ├── MedicalRecordDAO.java
│       │   └── BillingDAO.java
│       └── gui/
│           ├── MainFrame.java
│           ├── PatientPanel.java
│           ├── AppointmentPanel.java
│           ├── MedicalRecordPanel.java
│           └── BillingPanel.java
```

---

## ⚙️ Setup Steps (Step by Step)

### Step 1: MySQL Setup
1. MySQL Workbench ya Command Line kholo
2. `database.sql` file run karo:
   ```sql
   SOURCE /path/to/database.sql;
   ```
   Ya copy-paste karke run karo

### Step 2: MySQL Connector Download
- Download karo: https://dev.mysql.com/downloads/connector/j/
- `mysql-connector-j-X.X.X.jar` download hoga

### Step 3: Password Set Karo
`DatabaseConnection.java` mein apna MySQL password daalo:
```java
private static final String PASSWORD = "your_password";  // ← yahan daalo
```

### Step 4: IntelliJ IDEA / Eclipse Setup

**IntelliJ IDEA:**
1. File → New Project → yeh folder open karo
2. File → Project Structure → Libraries → + → mysql-connector jar add karo
3. `HospitalManagement.java` pe Right Click → Run

**Eclipse:**
1. File → Import → Existing Projects
2. Project pe Right Click → Build Path → Add External JARs → connector jar add karo
3. `HospitalManagement.java` pe Right Click → Run As → Java Application

---

## ✨ Features

| Feature | Description |
|---------|-------------|
| 👤 Patient Registration | Add, Edit, Delete, Search patients |
| 📅 Doctor Appointment | Book appointments, update status |
| 🩺 Medical Records | Diagnosis & prescription save karo |
| 💰 Billing System | Bill generate karo, payment track karo |

---

## 🛠 Technologies Used
- **Language:** Java (JDK 8+)
- **GUI:** Java Swing
- **Database:** MySQL
- **Connectivity:** JDBC (MySQL Connector/J)
- **Pattern:** DAO (Data Access Object) Pattern

---

## 💡 Tips for College Presentation
1. Database design explain karo (ER Diagram)
2. DAO Pattern ka concept batao
3. JDBC connection flow dikhao
4. OOP concepts highlight karo (Encapsulation in Models)

---

*Developed with ❤️ using Java Swing + MySQL*
