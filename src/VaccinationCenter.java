import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Shevan
 * @created 21/07/2021 - 10:31
 * @project COVID-19 VACCINATION CENTER PROGRAM
 * @file VaccinationCenter
 */
public class VaccinationCenter {
    private static Booth[] booths;
    private static List<Patient> patients;
    private static List<Patient> waitingPatients;
    private static final String WARNING = "\033[0;33m- WARNING : (vaccine stock has reached to minimum level.)\u001B[0m";

    private static void initialise() {
        booths = new Booth[6];
        patients = new ArrayList<>();
        waitingPatients = new LinkedList<>();
        String vaccination = "";
        for (int x = 0; x < booths.length; x++) {
            switch (x) {
                case 0:
                case 1: {
                    vaccination = VaccinationType.AstraZeneca.toString();
                    break;
                }
                case 2:
                case 3: {
                    vaccination = VaccinationType.Sinopharm.toString();
                    break;
                }
                case 4:
                case 5: {
                    vaccination = VaccinationType.Pfizer.toString();
                    break;
                }
            }
            booths[x] = new Booth(x, vaccination, null, 150, false);
        }
        System.out.println("\033[1;32mProgramm initialising...\u001B[0m");
    }

    private static void _viewAllBooth(String condition) {
        if (condition.equals("Empty")) {
            for (Booth booth : booths) {
                if (!booth.getBooked()) System.out.printf("booth %d is empty%n", booth.getBoothNo());
            }
        } else {
            for (Booth booth : booths) {
                System.out.printf("booth: %d, is booked: %s%n", booth.getBoothNo(), booth.getBooked() ? "Yes" : "No");
            }
        }
    }

    private static Patient _getPatientDetails(BufferedReader br, VaccinationType vaccinationType) throws IOException {
        Patient patient = new Patient();
        System.out.println("+--- ENTER PATIENT DETAILS ---+");
        System.out.println("First Name : ");
        patient.setFirstName(br.readLine().toUpperCase());
        System.out.println("Surname : ");
        patient.setSurname(br.readLine().toUpperCase());
        System.out.println("Age : ");
        patient.setAge(Integer.parseInt(br.readLine()));
        System.out.println("City : ");
        patient.setCity(br.readLine());
        System.out.println("NIC : ");
        patient.setNic(br.readLine());
        patient.setVaccinationRequested(vaccinationType);
        return patient;
    }

    private static void _addPatient(BufferedReader br) throws IOException {
        while (true) {
            System.out.println("\n+---- MENU ----+\n" +
                    "1: AstraZeneca\n" +
                    "2: Sinopharm\n" +
                    "3: Pfizer\n" +
                    "4: Exit\n" +
                    "+--------------+\n");
            System.out.println("Enter Vaccine Type : ");
            int choice = Integer.parseInt(br.readLine());
            if (choice < 4) {
                String vaccineName = VaccinationType.values()[choice - 1].toString();
                System.out.printf("You select %s vaccine, Find the empty booth for you...%n", vaccineName);
                for (Booth booth : booths) {
                    if (booth.getVaccineName().equals(vaccineName)) {
                        if (!booth.getBooked()) {
                            patients.add(_getPatientDetails(br, VaccinationType.values()[choice - 1]));
                            booth.setPatient(patients.get(patients.size() - 1));
                            booth.setBooked(true);
                            booth.setVaccines(booth.getVaccines() - 1);
                            System.out.println("Customer booth allocation successfully!");
                            System.out.println(booth);
                            break;
                        } else if (booth.getBoothNo() % 2 != 0) {
                            System.out.printf("All the booth depends on %s are booked!%n", VaccinationType.values()[choice - 1]);
                            System.out.println("We can put you on the waiting list...\n" + "Do you agree with that?: \n" + "Y: Yes\n" + "N: No");
                            switch (br.readLine().toUpperCase()) {
                                case "Y": {
                                    patients.add(_getPatientDetails(br, VaccinationType.values()[choice - 1]));
                                    waitingPatients.add(patients.get(patients.size() - 1));
                                    System.out.println("Your name add to waiting list...");
                                    break;
                                }
                                case "N": {
                                    System.out.println("Ok!");
                                    break;
                                }
                                default: {
                                    System.out.println("Wrong selection..");
                                    break;
                                }
                            }
                        }
                    }
                }
                break;
            } else if (choice == 4) {
                System.out.println("Exit...\n");
            } else {
                System.out.println("\033[0;31mPYour choice is invalid! \\nPlease Try Again....\u001B[0m");
            }
        }
    }

    private static void _manageWaitingPatients(Integer boothNo) {
        for (Patient p : waitingPatients) {
            if (p.getVaccinationRequested().toString().equals(booths[boothNo].getVaccineName())) {
                booths[boothNo].setPatient(p);
                booths[boothNo].setBooked(true);
                booths[boothNo].setVaccines(booths[boothNo].getVaccines() - 1);
            }
        }
    }

    private static void _removePatient(BufferedReader br) throws IOException {
        int boothNum;
        System.out.println("Enter checkout patient booth no : ");
        boothNum = Integer.parseInt(br.readLine());
        try {
            if (booths[boothNum].getBooked()) {
                System.out.printf("%s patient checkout successful.%n", booths[boothNum].getPatient().getPatientName());
                System.out.println("Thank You.\n");
                patients.remove(booths[boothNum].getPatient());
                booths[boothNum].setBooked(false);
                _manageWaitingPatients(boothNum);
            } else {
                System.out.println("Your selected booth is empty...");
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Given booth no is invalid!");
        }
    }

    private static void _viewRemainingVaccine() {
        for (Booth booth : booths) {
            int vaccines = booth.getVaccines();
            System.out.printf("booth no: %d, remaining vaccines: %d %s%n", booth.getBoothNo(), vaccines, vaccines <= 20 ? WARNING : ".");
        }
        System.out.println("+---------------------+\n");
    }

    private static void _addVaccines(BufferedReader br) throws IOException {
        int boothNum;
        System.out.println("Enter booth number (0-5) or 6 to stop:");
        boothNum = Integer.parseInt(br.readLine());
        if (boothNum < 6) {
            System.out.println("Enter amount of vaccines:");
            booths[boothNum].setVaccines(Integer.parseInt(br.readLine()));
            System.out.println("Vaccines add process successful!");
        } else if (boothNum == 6) {
            System.out.println("Stopped vaccines adding process.");
        }
    }

    private static void _checkVaccinesStock() {
        boolean tableStart = true;
        boolean tableEnd = false;
        for (Booth booth : booths) {
            int v = booth.getVaccines();
            if (v <= 20) {
                if (tableStart)
                    System.out.println("\n+---- CHECK VACCINE STOCK ----+\n");

                System.out.printf("booth no: %d,  remaining vaccines: %d %s%n", booth.getBoothNo(), v, WARNING);
                tableStart = false;
                tableEnd = true;
            }
        }
        System.out.println(tableEnd ? "+-----------------------------+\n" : "");
    }

    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        initialise();
        String choice;

        try {
            while (true) {
                boolean vrvIsNotWent = true;
                System.out.println("+----------------------- MENU ------------------------+\n" +
                        "100 or VVB: View all Vaccination Booths\n" +
                        "101 or VEB: View all Empty Booths\n" +
                        "102 or APB: Add Patient to a Booth\n" +
                        "103 or RPB: Remove Patient from a Booth\n" +
                        "104 or VPS: View Patients Sorted in alphabetical order\n" +
                        "105 or SPD: Store Program Data into file\n" +
                        "106 or LPD: Load Program Data from file\n" +
                        "107 or VRV: View Remaining Vaccinations\n" +
                        "108 or AVS: Add Vaccinations to the Stock\n" +
                        "999 or EXT: Exit the Program\n" +
                        "+-----------------------------------------------------+\n");
                choice = br.readLine();

                switch (choice) {
                    case "100":
                    case "VVB": {
                        System.out.println("+--- VIEW ALL VACCINATION BOOTHS ---+");
                        _viewAllBooth("");
                        break;
                    }
                    case "101":
                    case "VEB": {
                        System.out.println("+--- VIEW ALL EMPTY BOOTHS ---+");
                        _viewAllBooth("Empty");
                        break;
                    }
                    case "102":
                    case "APB": {
                        System.out.println("+--- ADD PATIENT TO A BOOTH ---+");
                        _addPatient(br);
                        break;
                    }
                    case "103":
                    case "RPB": {
                        System.out.println("+--- REMOVE PATIENT FROM A BOOTH ---+");
                        _removePatient(br);
                        break;
                    }
                    case "104":
                    case "VPS": {
                        System.out.println("+--- VIEW PATIENTS SORTED IN ALPHABETICAL ORDER ---+");
                        Collections.sort(patients);
                        for (Patient p : patients) {
                            System.out.println(p.getPatientName());
                        }
                        break;
                    }
                    case "105":
                    case "SPD": {
                        System.out.println("+--- STORE PROGRAM DATA INTO FILE ---+");
                        String filePath = String.format("VAC_%s.txt", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_hh_mm")));
                        try {
                            File create = new File(filePath);
                            if (create.createNewFile()) {
                                FileWriter writer = new FileWriter(filePath);

                                for (Booth booth : booths) {
                                    writer.write(booth.toStore());
                                }
                                writer.close();
                                System.out.println("Successfully wrote to the file.");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case "106":
                    case "LPD": {
                        System.out.println("+--- LOAD PROGRAM DATA FROM FILE ---+");
                        System.out.println("Enter file path: ");
                        String filePath = br.readLine();

                        try {
                            File file = new File(filePath);
                            Scanner b = new Scanner(file);

                            while (b.hasNextLine()) {
                                String data = b.nextLine();
                                int boothNo;
                                if (!data.isEmpty()) {
                                    if (data.substring(0, data.indexOf('{')).equalsIgnoreCase("Booth")) {
                                        data = data.substring(data.indexOf('{') + 1, data.lastIndexOf('}'));
                                        boothNo = Integer.parseInt(data.substring(data.lastIndexOf("boothNo=") + 8, data.indexOf(',')));
                                        if (!booths[boothNo].getBooked()) {
                                            Patient tmpPat = null;

                                            if (!data.substring(data.lastIndexOf("patient=") + 8).split(",")[0].equalsIgnoreCase("null")) {
                                                String dataPt = data.substring(data.lastIndexOf('{') + 1, data.indexOf(",}"));
                                                tmpPat = new Patient();
                                                String[] tmpp = dataPt.split(", ");
                                                for (String t : tmpp) {
                                                    String[] tmpd = t.split("=");

                                                    if (tmpd[0].equalsIgnoreCase("firstName"))
                                                        tmpPat.setFirstName(tmpd[1]);

                                                    if (tmpd[0].equalsIgnoreCase("surname"))
                                                        tmpPat.setSurname(tmpd[1]);

                                                    if (tmpd[0].equalsIgnoreCase("age"))
                                                        tmpPat.setAge(Integer.parseInt(tmpd[1]));

                                                    if (tmpd[0].equalsIgnoreCase("city"))
                                                        tmpPat.setCity(tmpd[1]);

                                                    if (tmpd[0].equalsIgnoreCase("nic"))
                                                        tmpPat.setNic(tmpd[1]);

                                                    if (tmpd[0].equalsIgnoreCase("vaccinationType"))
                                                        tmpPat.setVaccinationRequested(VaccinationType.valueOf(tmpd[1]));
                                                }
                                            }

                                            if (Objects.nonNull(tmpPat)) {
                                                patients.add(tmpPat);
                                                booths[boothNo].setPatient(tmpPat);
                                            }

                                            data = data.substring(0, data.indexOf(" patient")).concat(data.substring(data.indexOf("},") + 2));

                                            String[] split = data.split("\\s");

                                            for (String s : split) {
                                                String[] tmp = s.split("=");
                                                String d = tmp[1].substring(0, tmp[1].lastIndexOf(','));
                                                if (tmp[0].equalsIgnoreCase("Vaccines")) {
                                                    booths[boothNo].setVaccines(Integer.parseInt(d));
                                                }

                                                if (tmp[0].equalsIgnoreCase("booked") && Objects.nonNull(tmpPat)) {
                                                    booths[boothNo].setBooked(Boolean.parseBoolean(d));
                                                } else {
                                                    System.out.print(Boolean.parseBoolean(d) ? "\033[0;33mWARNING : Without patient details, booth can't set as booked!\033[0m\n" : "");
                                                }
                                            }
                                        } else {
                                            System.out.println("\033[0;33mWARNING : Can't save data to Booth No: " + boothNo + ", it's already booked...\033[0m");
                                            break;
                                        }
                                    }
                                }

                                if (!b.hasNextLine())
                                    System.out.println("Data Load and Save successful...");
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case "107":
                    case "VRV": {
                        System.out.println("+--- VIEW REMAINING VACCINATIONS ---+");
                        _viewRemainingVaccine();
                        vrvIsNotWent = false;
                        break;
                    }
                    case "108":
                    case "AVS": {
                        System.out.println("+--- ADD VACCINATIONS TO STOCK ---+");
                        _addVaccines(br);
                        break;
                    }
                    case "999":
                    case "EXT": {
                        System.out.println("Exit the Program");
                        System.exit(0);
                        break;
                    }
                    default: {
                        System.out.println("Your choice is invalid! \nPlease Try Again....");
                        break;
                    }
                }

                if (vrvIsNotWent) {
                    _checkVaccinesStock();
                }
            }
        } catch (
                Exception ex) {
            ex.printStackTrace();
        }
    }
}
