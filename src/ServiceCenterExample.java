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
public class ServiceCenterExample {

    private static void initialise(String[] ServiceCenter, Integer[] Vaccines) {
        for (int x = 0; x < ServiceCenter.length; x++) {
            ServiceCenter[x] = "e";
            Vaccines[x] = 150;
        }
        System.out.println("initialise");
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

    private static void _addPatient(BufferedReader br, String[] ServiceCenter, Integer[] Vaccines) throws IOException {
        String customerName;
        int boothNum;
        System.out.println("Enter booth number (0-5) or 6 to stop:");
        boothNum = Integer.parseInt(br.readLine());
        if (boothNum < 6) {
            if (ServiceCenter[boothNum].equals("e")) {
                System.out.printf("Enter customer name for booth %d :%n", boothNum);
                customerName = br.readLine();
                ServiceCenter[boothNum] = customerName.toUpperCase();
                Vaccines[boothNum] = Vaccines[boothNum] - 1;
                System.out.println("Customer booth allocation successfully.");
            } else {
                System.out.printf("booth %d is already booked.%n", boothNum);
            }
        } else if (boothNum == 6) {
            System.out.println("Stopped patients adding process.");
        }
    }

    private static void _removePatient(BufferedReader br, String[] ServiceCenter) throws IOException {
        int boothNum;
        System.out.println("Enter checkout patient booth");
        boothNum = Integer.parseInt(br.readLine());
        System.out.printf("%s patient checkout successful.%n", ServiceCenter[boothNum]);
        ServiceCenter[boothNum] = "e";
    }

    private static void _viewRemainingVaccine(Integer[] Vaccines) {
        for (int i = 0; i < Vaccines.length; i++) {
            int v = Vaccines[i];
            String warning = "";
            if (v <= 20) {
                warning = "- WARNING : (vaccine stock has reached to minimum level.)";
            }
            System.out.printf("booth no: %d, remaining vaccines: %d %s%n", i, v, warning);
        }
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
        for (int i = 0; i < Vaccines.length; i++) {
            int v = Vaccines[i];
            if (v <= 20) {
                System.out.printf("booth no : %d,  remaining vaccines: %d - WARNING : (vaccine stock has reached to minimum level.)%n", i, v);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String choice;
        String[] ServiceCenter = new String[6];
        Integer[] Vaccines = new Integer[6];
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        initialise(ServiceCenter, Vaccines);

        while (true) {
            System.out.println("-- Menu ---\n" +
                    "100 or VVB: View all Vaccination Booths\n" +
                    "101 or VEB: View all Empty Booths\n" +
                    "102 or APB: Add Patient to a Booth\n" +
                    "103 or RPB: Remove Patient from a Booth\n" +
                    "104 or VPS: View Patients Sorted in alphabetical order\n" +
                    "105 or SPD: Store Program Data into file\n" +
                    "106 or LPD: Load Program Data from file\n" +
                    "107 or VRV: View Remaining Vaccinations\n" +
                    "108 or AVS: Add Vaccinations to the Stock\n" +
                    "999 or EXT: Exit the Program\n");
            choice = br.readLine();

            switch (choice) {
                case "100":
                case "VVB": {
                    System.out.println("--- View all Vaccination Booths ---");
                    _viewAllBooth("", ServiceCenter);
                    break;
                }
                case "101":
                case "VEB": {
                    System.out.println("--- View all Empty Booths ---");
                    _viewAllBooth("Empty", ServiceCenter);
                    break;
                }
                case "102":
                case "APB": {
                    System.out.println("--- Add Patient to a Booth ---");
                    _addPatient(br, ServiceCenter, Vaccines);
                    break;
                }
                case "103":
                case "RPB": {
                    System.out.println("--- Remove Patient from a Booth ---");
                    _removePatient(br, ServiceCenter);
                    break;
                }
                case "104":
                case "VPS": {
                    System.out.println("--- View Patients Sorted in alphabetical order ---");
                    Arrays.sort(ServiceCenter);
                    System.out.println(Arrays.toString(ServiceCenter));
                    break;
                }
                case "105":
                case "SPD": {
                    System.out.println("--- Store Program Data into file ---");
                    break;
                }
                case "106":
                case "LPD": {
                    System.out.println("--- Load Program Data from file ---");
                    break;
                }
                case "107":
                case "VRV": {
                    System.out.println("--- View Remaining Vaccinations ---");
                    _viewRemainingVaccine(Vaccines);
                    break;
                }
                case "108":
                case "AVS": {
                    System.out.println("--- Add Vaccinations to the Stock ---");
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
                    System.out.println("Your choice is invalid!");
                    break;
                }
            }

            _checkVaccinesStock(Vaccines);
        }
    }
}
