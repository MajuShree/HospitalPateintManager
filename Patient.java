package hospital;

/**
 * Patient class representing a hospital patient record.
 * MODULE-2: Demonstrates String handling, toString override
 * MODULE-1: Used in Collections with Comparators
 */
public class Patient {
    private int patientId;
    private String name;
    private int age;
    private String disease;

    public Patient(int patientId, String name, int age, String disease) {
        this.patientId = patientId;
        this.name = name;
        this.age = age;
        this.disease = disease;
    }

    // Getters
    public int getPatientId() { return patientId; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getDisease() { return disease; }

    // Setters
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setDisease(String disease) { this.disease = disease; }

    /**
     * MODULE-2: Overriding toString() for String representation
     */
    @Override
    public String toString() {
        return String.format("Patient[ID=%d, Name=%s, Age=%d, Disease=%s]",
                patientId, name, age, disease);
    }
}
