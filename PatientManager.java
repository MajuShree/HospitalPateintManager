package hospital;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * PatientManager handles all data operations on the patient collection.
 * MODULE-1: Collections - ArrayList, Iterator, Comparators, Algorithms (Ch. 20)
 */
public class PatientManager {

    // MODULE-1: Storing User Defined Classes in Collections using ArrayList<Patient>
    private ArrayList<Patient> patientList;
    private int nextId;

    public PatientManager() {
        patientList = new ArrayList<>();
        nextId = 1001;
        loadSampleData();
    }

    /**
     * Load initial sample patients for demonstration
     */
    private void loadSampleData() {
        addPatient("Rajesh Kumar", 65, "Hypertension");
        addPatient("Priya Sharma", 34, "Diabetes");
        addPatient("Anand Verma", 72, "Arthritis");
        addPatient("Sunita Patel", 58, "Asthma");
        addPatient("Mohan Das", 81, "Cardiac Issues");
        addPatient("Kavitha Nair", 45, "Migraine");
        addPatient("Suresh Reddy", 63, "Kidney Stones");
        addPatient("Lakshmi Rao", 29, "Fever");
        addPatient("Vijay Singh", 77, "Parkinson's");
        addPatient("Deepa Menon", 52, "Thyroid");
    }

    /**
     * Add a new patient to the collection
     */
    public Patient addPatient(String name, int age, String disease) {
        Patient p = new Patient(nextId++, name, age, disease);
        patientList.add(p);
        return p;
    }

    /**
     * Remove patient by ID
     * MODULE-1: Accessing a Collection Via an Iterator (Ch. 20)
     */
    public boolean removePatient(int patientId) {
        Iterator<Patient> it = patientList.iterator();
        while (it.hasNext()) {
            Patient p = it.next();
            if (p.getPatientId() == patientId) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * Update existing patient details
     */
    public boolean updatePatient(int patientId, String name, int age, String disease) {
        for (Patient p : patientList) {
            if (p.getPatientId() == patientId) {
                p.setName(name);
                p.setAge(age);
                p.setDisease(disease);
                return true;
            }
        }
        return false;
    }

    /**
     * Search patients by name (partial match)
     * MODULE-2: Searching Strings - indexOf(), contains() (Ch. 18)
     */
    public ArrayList<Patient> searchByName(String keyword) {
        ArrayList<Patient> result = new ArrayList<>();
        for (Patient p : patientList) {
            if (p.getName().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(p);
            }
        }
        return result;
    }

    /**
     * Search patients by disease (partial match)
     */
    public ArrayList<Patient> searchByDisease(String keyword) {
        ArrayList<Patient> result = new ArrayList<>();
        for (Patient p : patientList) {
            if (p.getDisease().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(p);
            }
        }
        return result;
    }

    /**
     * Get patients above age 60 (Senior Patients)
     * MODULE-1: Collection Algorithms filter (Ch. 20)
     */
    public ArrayList<Patient> getPatientsAbove60() {
        ArrayList<Patient> seniors = new ArrayList<>();
        for (Patient p : patientList) {
            if (p.getAge() > 60) {
                seniors.add(p);
            }
        }
        return seniors;
    }

    /**
     * Get all patients sorted by age
     * MODULE-1: Collection Algorithms - Collections.sort() with Comparator (Ch. 20)
     */
    public ArrayList<Patient> getSortedByAge(boolean ascending) {
        ArrayList<Patient> sorted = new ArrayList<>(patientList);
        if (ascending) {
            Collections.sort(sorted, new PatientComparators.SortByAge());
        } else {
            Collections.sort(sorted, new PatientComparators.SortByAgeDescending());
        }
        return sorted;
    }

    /**
     * Get all patients sorted by name
     * MODULE-1: Comparators (Ch. 20)
     */
    public ArrayList<Patient> getSortedByName() {
        ArrayList<Patient> sorted = new ArrayList<>(patientList);
        Collections.sort(sorted, new PatientComparators.SortByName());
        return sorted;
    }

    /**
     * Get all patients sorted by ID (default view)
     */
    public ArrayList<Patient> getSortedById() {
        ArrayList<Patient> sorted = new ArrayList<>(patientList);
        Collections.sort(sorted, new PatientComparators.SortByID());
        return sorted;
    }

    /**
     * Find patient by ID
     */
    public Patient findById(int patientId) {
        for (Patient p : patientList) {
            if (p.getPatientId() == patientId) {
                return p;
            }
        }
        return null;
    }

    public ArrayList<Patient> getAllPatients() {
        return new ArrayList<>(patientList);
    }

    public int getTotalCount() {
        return patientList.size();
    }

    public long getSeniorCount() {
        return patientList.stream().filter(p -> p.getAge() > 60).count();
    }

    public double getAverageAge() {
        if (patientList.isEmpty()) return 0;
        return patientList.stream().mapToInt(Patient::getAge).average().orElse(0);
    }
}
