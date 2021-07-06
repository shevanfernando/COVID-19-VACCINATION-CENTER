import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * @author Shevan
 * @created 06/07/2021 - 12:38
 * @project COVID-19 VACCINATION CENTER PROGRAM
 * @file ServiceCenterExample
 */
class BoothInfo {
    public Integer boothNo;
    public VaccinationRequested vaccine;

    public BoothInfo(Integer boothNo, VaccinationRequested vaccine) {
        this.boothNo = boothNo;
        this.vaccine = vaccine;
    }
}

public class ServiceCenterExample {

    private static final String WARNING = "\033[0;31m- WARNING : (vaccine stock has reached to minimum level.)\u001B[0m";

    private static void initialise(String[] ServiceCenter, Integer[] Vaccines) {
        for (int x = 0; x < ServiceCenter.length; x++) {
            ServiceCenter[x] = "e";
            Vaccines[x] = 150;
        }
        System.out.println("\033[1;32minitialise\u001B[0m");
    }

    private static void _viewAllBooth(String condition, String[] ServiceCenter) {
        if (condition.equals("Empty")) {
            for (int x = 0; x < ServiceCenter.length; x++) {
                if (ServiceCenter[x].equals("e")) System.out.printf("booth %d is empty%n", x);
            }
        } else {
            for (int x = 0; x < ServiceCenter.length; x++) {
                System.out.printf("booth %d %s%n", x, ServiceCenter[x]);
            }
        }
    }

    private static BoothInfo _getVaccinationType(BufferedReader br) throws IOException {
        System.out.println("\n+---- MENU ----+\n" + "1: AstraZeneca\n" + "2: Sinopharm\n" + "3: Pfizer\n" + "+--------------+\n");
        System.out.println("Enter Vaccine Type : ");
        int choice = Integer.parseInt(br.readLine());
        int boothNum;

        while (true) {
            switch (choice) {
                case 1: {
                    System.out.println("Enter booth number (0-1) or 6 to stop:");
                    boothNum = Integer.parseInt(br.readLine());
                    return new BoothInfo(boothNum, VaccinationRequested.AstraZeneca);
                }
                case 2: {
                    System.out.println("Enter booth number (2-3) or 6 to stop:");
                    boothNum = Integer.parseInt(br.readLine());
                    return new BoothInfo(boothNum, VaccinationRequested.Sinopharm);
                }
                case 3: {
                    System.out.println("Enter booth number (4-5) or 6 to stop:");
                    boothNum = Integer.parseInt(br.readLine());
                    return new BoothInfo(boothNum, VaccinationRequested.Pfizer);
                }
                default: {
                    System.out.println("Your choice is invalid! \nPlease Try Again....");
                    break;
                }
            }
        }
    }

    private static void _addPatient(BufferedReader br, String[] ServiceCenter, Integer[] Vaccines) throws IOException {
        String firstName;
        String surname;
        BoothInfo boothInfo = _getVaccinationType(br);
        Integer boothNum = boothInfo.boothNo;
        if (boothNum < 6) {
            if (ServiceCenter[boothNum].equals("e")) {
                System.out.printf("Enter customer name for booth %d: %n", boothNum);
                System.out.println("First Name : ");
                firstName = br.readLine();
                System.out.println("Surname : ");
                surname = br.readLine();
                ServiceCenter[boothNum] = String.format("%s %s", firstName.toUpperCase(), surname.toUpperCase());
                Vaccines[boothNum] = Vaccines[boothNum] - 1;
                System.out.println("Customer booth allocation successfully.");
                System.out.println("\n+---- PATIENT DETAILS ----+");
                System.out.println("Patient Full Name: " + firstName + " " + surname);
                System.out.println("Patient Allocated Booth No: " + boothNum);
                System.out.println("Patient Request Vaccine Name: " + boothInfo.vaccine);
                System.out.println("+-------------------------+\n");
            } else {
                System.out.printf("booth %d is already booked.%n", boothNum);
            }
        } else if (boothNum == 6) {
            System.out.println("Stopped patients adding process.\n");
        }
    }

    private static void _removePatient(BufferedReader br, String[] ServiceCenter) throws IOException {
        int boothNum;
        System.out.println("Enter checkout patient booth");
        boothNum = Integer.parseInt(br.readLine());
        if (!ServiceCenter[boothNum].equals("e")) {
            System.out.printf("%s patient checkout successful.%n", ServiceCenter[boothNum]);
            System.out.println("Thank You.\n");
            ServiceCenter[boothNum] = "e";
        } else {
            System.out.println("Your selected booth is empty...");
        }
    }

    private static void _viewRemainingVaccine(Integer[] Vaccines) {
        for (int i = 0; i < Vaccines.length; i++) {
            int v = Vaccines[i];
            System.out.printf("booth no: %d, remaining vaccines: %d %s%n", i, v, v <= 20 ? WARNING : ".");
        }
        System.out.println("+---------------------+\n");
    }

    private static void _addVaccines(BufferedReader br, Integer[] Vaccines) throws IOException {
        int boothNum;
        System.out.println("Enter booth number (0-5) or 6 to stop:");
        boothNum = Integer.parseInt(br.readLine());
        if (boothNum < 6) {
            System.out.println("Enter amount of vaccines:");
            Vaccines[boothNum] = Integer.parseInt(br.readLine());
        } else if (boothNum == 6) {
            System.out.println("Stopped vaccines adding process.");
        }
    }

    private static void _checkVaccinesStock(Integer[] Vaccines) {
        boolean tableStart = true;
        boolean tableEnd = false;
        for (int i = 0; i < Vaccines.length; i++) {
            int v = Vaccines[i];
            if (v <= 20) {
                if (tableStart)
                    System.out.println("\n+---- CHECK VACCINE STOCK ----+\n");

                System.out.printf("booth no: %d,  remaining vaccines: %d %s%n", i, v, WARNING);
                tableStart = false;
                tableEnd = true;
            }
        }
        System.out.println(tableEnd ? "+-----------------------------+\n" : "");
    }

    public static void main(String[] args) throws IOException {
        String choice;
        String[] ServiceCenter = new String[6];
        Integer[] Vaccines = new Integer[6];
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        initialise(ServiceCenter, Vaccines);
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
                    _viewAllBooth("", ServiceCenter);
                    break;
                }
                case "101":
                case "VEB": {
                    System.out.println("+--- VIEW ALL EMPTY BOOTHS ---+");
                    _viewAllBooth("Empty", ServiceCenter);
                    break;
                }
                case "102":
                case "APB": {
                    System.out.println("+--- ADD PATIENT TO A BOOTH ---+");
                    _addPatient(br, ServiceCenter, Vaccines);
                    break;
                }
                case "103":
                case "RPB": {
                    System.out.println("+--- REMOVE PATIENT FROM A BOOTH ---+");
                    _removePatient(br, ServiceCenter);
                    break;
                }
                case "104":
                case "VPS": {
                    System.out.println("+--- VIEW PATIENTS SORTED IN ALPHABETICAL ORDER ---+");
                    Arrays.sort(ServiceCenter);
                    System.out.println(Arrays.toString(ServiceCenter));
                    break;
                }
                case "105":
                case "SPD": {
                    System.out.println("+--- STORE PROGRAM DATA INTO FILE ---+");
                    break;
                }
                case "106":
                case "LPD": {
                    System.out.println("+--- LOAD PROGRAM DATA FROM FILE ---+");
                    break;
                }
                case "107":
                case "VRV": {
                    System.out.println("+--- VIEW REMAINING VACCINATIONS ---+");
                    _viewRemainingVaccine(Vaccines);
                    vrvIsNotWent = false;
                    break;
                }
                case "108":
                case "AVS": {
                    System.out.println("+--- ADD VACCINATIONS TO STOCK ---+");
                    _addVaccines(br, Vaccines);
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

            if (vrvIsNotWent)
                _checkVaccinesStock(Vaccines);
        }
    }
}
