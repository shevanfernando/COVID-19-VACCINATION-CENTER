import java.util.Objects;

/**
 * @author Shevan
 * @created 21/07/2021 - 10:28
 * @project COVID-19 VACCINATION CENTER PROGRAM
 * @file Booth
 */
public class Booth {
    private final Integer boothNo;
    private final String vaccineName;
    private Patient patient;
    private Integer Vaccines;
    private Boolean booked;

    public Booth(Integer boothNo, String vaccineName, Patient patient, Integer vaccines, Boolean booked) {
        this.boothNo = boothNo;
        this.vaccineName = vaccineName;
        this.patient = patient;
        Vaccines = vaccines;
        this.booked = booked;
    }

    public Integer getBoothNo() {
        return boothNo;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Integer getVaccines() {
        return Vaccines;
    }

    public void setVaccines(Integer vaccines) {
        Vaccines = vaccines;
    }

    public Boolean getBooked() {
        return booked;
    }

    public void setBooked(Boolean booked) {
        this.booked = booked;
    }

    public String toStore() {
        return "Booth{" +
                "boothNo=" + boothNo +
                ", vaccineName=" + vaccineName +
                ", patient=" + (Objects.nonNull(patient) ? patient.toStore() : null) +
                ", Vaccines=" + Vaccines +
                ", booked=" + booked +
                ",}\n";
    }

    @Override
    public String toString() {
        return "\n+---- BOOTH DETAILS ----+" +
                "\nBoothNo = " + boothNo +
                "\nVaccineName = " + vaccineName +
                "\nRemaining Vaccines = " + Vaccines +
                "\nBooked Status = " + booked +
                "\nCurrent Patient = " + patient +
                "\n+-------------------------+\n";
    }
}
