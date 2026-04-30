package hospital;

import java.util.Comparator;

/**
 * PatientComparators class providing Comparator implementations.
 * MODULE-1: The Collection Algorithms - Comparators (Ch. 20)
 */
public class PatientComparators {

    /**
     * Comparator to sort patients by Age (ascending)
     */
    public static class SortByAge implements Comparator<Patient> {
        @Override
        public int compare(Patient p1, Patient p2) {
            return Integer.compare(p1.getAge(), p2.getAge());
        }
    }

    /**
     * Comparator to sort patients by Age (descending)
     */
    public static class SortByAgeDescending implements Comparator<Patient> {
        @Override
        public int compare(Patient p1, Patient p2) {
            return Integer.compare(p2.getAge(), p1.getAge());
        }
    }

    /**
     * Comparator to sort patients by Name (alphabetical)
     * MODULE-2: String Comparison using compareTo()
     */
    public static class SortByName implements Comparator<Patient> {
        @Override
        public int compare(Patient p1, Patient p2) {
            return p1.getName().compareToIgnoreCase(p2.getName());
        }
    }

    /**
     * Comparator to sort patients by Patient ID
     */
    public static class SortByID implements Comparator<Patient> {
        @Override
        public int compare(Patient p1, Patient p2) {
            return Integer.compare(p1.getPatientId(), p2.getPatientId());
        }
    }
}
