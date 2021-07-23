/**
 * @author Shevan
 * @created 06/07/2021 - 12:51
 * @project COVID-19 VACCINATION CENTER PROGRAM
 * @file Patient
 */
public class Patient implements Comparable<Patient> {
    private String firstName;
    private String surname;
    private Integer age;
    private String city;
    private String nic;
    private VaccinationType vaccinationType;

    public Patient() {
    }

    public Patient(String firstName, String surname, Integer age, String city, String nic, VaccinationType vaccinationType) {
        this.firstName = firstName;
        this.surname = surname;
        this.age = age;
        this.city = city;
        this.nic = nic;
        this.vaccinationType = vaccinationType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatientName() {
        return String.format("%s %s", this.firstName, this.surname);
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public VaccinationType getVaccinationRequested() {
        return vaccinationType;
    }

    public void setVaccinationRequested(VaccinationType vaccinationType) {
        this.vaccinationType = vaccinationType;
    }

    @Override
    public String toString() {
        return "\n+---- PATIENT DETAILS ----+" +
                "\nName= " + getPatientName() +
                "\nAge= " + age +
                "\nCity= " + city +
                "\nNIC= " + nic +
                "\nVaccination Requested= " + vaccinationType +
                "\n+-------------------------+\n";
    }

    @Override
    public int compareTo(Patient o) {
        return getFirstName().compareTo(o.getFirstName());
    }
}
