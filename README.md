# 🏥 Hospital Patient Record Manager

A desktop application built with **Java Swing** for managing hospital patient records. Features a clean **white & light blue** themed UI with tabbed navigation, full CRUD operations, sorting, searching, and a dedicated senior patient view.

---

## 📸 Features at a Glance

| Tab | What it does |
|-----|-------------|
| 📋 All Patients | View the complete patient list in a colour-coded table |
| ➕ Add Patient | Register a new patient with name, age, and diagnosis |
| ✏️ Edit / Delete | Load a patient by ID, update details or remove the record |
| 🔍 Search | Search patients by name or disease keyword |
| 📊 Sort & View | Sort the table by ID, Age (↑↓), or Name A–Z |
| 👴 Seniors (60+) | Dedicated view highlighting patients above age 60 |

---

## 🖥️ Tech Stack

- **Language:** Java (JDK 8+)
- **UI Framework:** Java Swing
- **Data Structure:** `ArrayList<Patient>` (Java Collections Framework)
- **Sorting:** Custom `Comparator` implementations
- **IDE:** Any Java IDE (VS Code, IntelliJ IDEA, Eclipse)

---

## 📁 Project Structure

```
HospitalPatientManager/
│
├── src/
│   ├── HospitalApp.java          # Main Swing GUI — all tabs, event handling
│   ├── Patient.java              # Patient model class with toString()
│   ├── PatientComparators.java   # Comparators: SortByAge, SortByName, SortByID
│   └── PatientManager.java       # Business logic: CRUD, search, sort, filter
│
├── out/                          # Compiled .class files (auto-generated)
├── compile_and_run.sh            # Linux/macOS build script
└── README.md
```

---

## ⚙️ Getting Started

### Prerequisites
- Java JDK **8 or higher** installed
- Verify with:
  ```bash
  java -version
  javac -version
  ```

### Run on Windows (PowerShell)
```powershell
# Navigate to project folder
cd "path\to\HospitalPatientManager"

# Create output directory
mkdir out

# Compile
javac -d out src/Patient.java src/PatientComparators.java src/PatientManager.java src/HospitalApp.java

# Run
java -cp out hospital.HospitalApp
```


> **Note:** If your `.java` files are in the root folder (not inside `src/`), use `javac -d out *.java` instead.



## 📌 Key Classes

### `Patient.java`
Represents a single patient record.
```java
Patient p = new Patient(1001, "Rajesh Kumar", 65, "Hypertension");
System.out.println(p); // Patient[ID=1001, Name=Rajesh Kumar, Age=65, Disease=Hypertension]
```

### `PatientComparators.java`
Four comparator implementations for flexible sorting.
```java
Collections.sort(list, new PatientComparators.SortByAge());
Collections.sort(list, new PatientComparators.SortByName());
```

### `PatientManager.java`
Handles all data operations — add, remove, update, search, sort, and filter.
```java
manager.addPatient("Anjali Sharma", 52, "Diabetes");
manager.getPatientsAbove60();           // Returns ArrayList of senior patients
manager.searchByDisease("Cardiac");     // Partial match search


## 🎨 UI Design

- **Background:** Very light blue `#EBF5FF`
- **Cards & Table rows:** Pure white
- **Header:** White with blue bottom border and stat pills
- **Table header:** Light blue `#DBE4FF`
- **Alternate rows:** Soft sky blue `#F0F8FF`
- **Senior rows:** Light amber highlight
- **Buttons:** Custom-painted (cross-platform compatible, not overridden by Windows LAF)



##  Known Notes

- The app uses the **cross-platform (Metal) Look & Feel** to ensure buttons and colours render correctly on all operating systems including Windows.
- All 10 sample patients are pre-loaded on startup for demonstration purposes.



## 📄 License

This project is intended for **educational purposes** as part of a Java programming course assignment.

---

## 👩‍💻 Author

**Majushree**  
Java Programming — Modules 1, 2 & 3  
*Hospital Patient Record Manager — Swing Project*
