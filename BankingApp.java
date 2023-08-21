import java.util.Arrays;
import java.util.Scanner;

public class BankingApp {
    private static final Scanner SCANNER = new Scanner(System.in);

    static String CLEAR = "\033[H\033[2J";
    static String BLUE_BOLD_START = "\033[34;1m";
    static String RED_BOLD_START = "\033[31;1m";
    static String GREEN_BOLD_START = "\033[32;1m";
    static String YELLOW_BOLD_START = "\033[33;1m";
    static String RESET = "\033[30;0m";

    static String SUCCESS_MSG = String.format("%s%s%s\n", GREEN_BOLD_START, "%s", RESET);
    static String ERROR_MSG = String.format("%s%s%s\n", RED_BOLD_START, "%s", RESET);
    static String TRY_MSG = String.format("%s%s%s\n", YELLOW_BOLD_START, "%s", RESET);

    static String[][] customer = new String[0][0];

    static String name;
    static int id = customer.length + 1;
    static String accID = String.format("SDB-%05d", id);
    static Double initialAmount;
    static Double depositAmount;
    static Double withdrawAmount;
    static Double newBalance;
    static boolean valid;

    public static void main(String[] args) {

        final String MAIN_MENU = "\u1F4B0 Welcome to Smart Banking App";
        final String OPEN_NEW_ACCOUNT = "Open new Account";
        final String DEPOSIT_MONEY = "Deposit Money";
        final String WITHDRAW_MONEY = "Withdraw Money";
        final String TRANSFER_MONEY = "Transfer Money";
        final String CHECK_ACCOUNT_BALANCE = "Check Account Balance";
        final String DROP_EXISTING_ACCOUNT = "Drop Existing Account";
        final String EXTI = "Exit";

        String screen = MAIN_MENU;

        do {
            final String APP_TITLE = String.format("%s%s%s", BLUE_BOLD_START, screen, RESET);
            System.out.println(CLEAR);
            System.out.println("\t" + APP_TITLE + "\n");
            switch (screen) {
                case MAIN_MENU:
                    System.out.println(" [1]. Open New Account");
                    System.out.println(" [2]. Deposit Money");
                    System.out.println(" [3]. Withdraw Money");
                    System.out.println(" [4]. Transfer Money");
                    System.out.println(" [5]. Check Account Balance");
                    System.out.println(" [6]. Drop Existig Account");
                    System.out.println(" [7]. Exit\n");
                    System.out.print(" Enter an option to continue: ");

                    int option = SCANNER.nextInt();
                    SCANNER.nextLine();

                    switch (option) {
                        case 1:
                            screen = OPEN_NEW_ACCOUNT;
                            break;
                        case 2:
                            screen = DEPOSIT_MONEY;
                            break;
                        case 3:
                            screen = WITHDRAW_MONEY;
                            break;
                        case 4:
                            screen = TRANSFER_MONEY;
                            break;
                        case 5:
                            screen = CHECK_ACCOUNT_BALANCE;
                            break;
                        case 6:
                            screen = DEPOSIT_MONEY;
                            break;
                        case 7:
                            System.out.println(CLEAR);
                            System.exit(0);
                        default:
                            continue;
                    }
                    break;
                case OPEN_NEW_ACCOUNT:
                    System.out.printf("ID: SDB-%05d \n", (customer.length + 1));

                    do {
                        valid = true;
                        System.out.print("Name: ");
                        name = SCANNER.nextLine().strip();
                        if (name.isBlank()) {
                            System.out.printf(ERROR_MSG, String.format("Name can't be empty"));
                            valid = false;
                            continue;
                        }
                        for (int i = 0; i < name.length(); i++) {
                            if (!(Character.isLetter(name.charAt(i)) ||
                                    Character.isSpaceChar(name.charAt(i)))) {
                                System.out.printf(ERROR_MSG, String.format("Invalid Name"));
                                valid = false;
                                break;
                            }
                        }
                    } while (!valid);

                    do {
                        valid = true;
                        System.out.print("Initial deposit: ");
                        initialAmount = SCANNER.nextDouble();
                        SCANNER.nextLine();

                        if (initialAmount < 5000) {
                            System.out.printf(ERROR_MSG, String.format("Insufficient Amount!"));
                            valid = false;
                            continue;
                        }
                    } while (!valid);

                    String[][] tempCustomer = new String[customer.length + 1][3];
                    for (int i = 0; i < customer.length; i++) {
                        tempCustomer[i] = customer[i];
                    }

                    tempCustomer[tempCustomer.length - 1][0] = accID;
                    tempCustomer[tempCustomer.length - 1][1] = name;
                    tempCustomer[tempCustomer.length - 1][2] = initialAmount + "";

                    customer = tempCustomer;
                    id = customer.length + 1;
                    accID = String.format("SDB-%05d", id);

                    System.out.println();
                    System.out.printf(SUCCESS_MSG, String
                            .format("SDB-%05d: %s added successfuly.\n Do you want to add more (Y/n)? ", id - 1, name));
                    if (SCANNER.nextLine().strip().toUpperCase().equals("Y"))
                        continue;
                    screen = MAIN_MENU;
                    break;

                case DEPOSIT_MONEY:

                    String inputID;
                    do {
                        valid = true;
                        System.out.print("Enter Account number: ");
                        inputID = SCANNER.nextLine().strip();
                        valid = validateId(inputID);

                        if (!valid) {
                            System.out.println();
                            System.out.printf(TRY_MSG, String.format("Do you want to try again (Y/n)?: "));
                            if (SCANNER.nextLine().strip().toUpperCase().equals("Y"))
                                continue;
                            else screen = MAIN_MENU;
                            break;
                        } else {
                            int i = 0;
                            while (i < customer.length) {
                                if (inputID.equals(customer[i][0])) {
                                    System.out.printf("Current Balance: Rs %,.2f\n", Double.valueOf(customer[i][2]));
                                    break;
                                }
                                i++;
                            }
                            String deposit = "d";
                            valid = true;
                            System.out.print("\nDeposit Amount: ");
                            depositAmount = SCANNER.nextDouble();
                            SCANNER.nextLine();

                            valid = validateAmount(deposit, depositAmount);

                            if (valid) {
                                newBalance = Double.valueOf(customer[i][2]) + depositAmount;
                                customer[i][2] = newBalance + "";
                                System.out.printf("New Balance: Rs %,.2f", newBalance);
                                System.out.println();
                                System.out.printf(SUCCESS_MSG,
                                        String.format("Deposit completed!. Do you want to try again (Y/n)?: "));
                                if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) continue;
                                    break;
                                
                            } else {
                                System.out.println();
                                System.out.printf(TRY_MSG, String.format("Do you want to try again (Y/n)?: "));
                                if (SCANNER.nextLine().strip().toUpperCase().equals("Y"))
                                    continue;
                                break;
                            }
                        }

                    } while (!valid);
                    screen = MAIN_MENU;
                    break;

                    case WITHDRAW_MONEY:
                    do {
                        valid = true;
                        System.out.print("Enter Account number: ");
                        inputID = SCANNER.nextLine().strip();
                        valid = validateId(inputID);

                        if (!valid) {
                            System.out.println();
                            System.out.printf(TRY_MSG, String.format("Do you want to try again (Y/n)?: "));
                            if (SCANNER.nextLine().strip().toUpperCase().equals("Y"))
                                continue;
                            break;
                        } else {
                            int i = 0;
                            while (i < customer.length) {
                                if (inputID.equals(customer[i][0])) {
                                    System.out.printf("Current Balance: Rs %,.2f\n", Double.valueOf(customer[i][2]));
                                    break;
                                }
                                i++;
                            }
                            String deposit = "w";
                            valid = true;
                            System.out.print("\nWithdraw Amount: ");
                            withdrawAmount = SCANNER.nextDouble();
                            SCANNER.nextLine();

                            valid = validateAmount(deposit, withdrawAmount);

                            if (valid) {
                                newBalance = Double.valueOf(customer[i][2]) - withdrawAmount;
                                customer[i][2] = newBalance + "";
                                System.out.printf("New Balance: Rs %,.2f", newBalance);
                                System.out.println();
                                System.out.printf(SUCCESS_MSG,
                                        String.format("Withdraw completed!. Do you want to try again (Y/n)?: "));
                                if (SCANNER.nextLine().strip().toUpperCase().equals("Y"))
                                    continue;
                                break;
                            } else {
                                System.out.println();
                                System.out.printf(TRY_MSG, String.format("Do you want to try again (Y/n)?: "));
                                if (SCANNER.nextLine().strip().toUpperCase().equals("Y"))
                                    continue;
                                break;
                            }
                        }
                    } while (!valid);
                    screen = MAIN_MENU;
                    break;

                default:
                    break;
            }
        } while (true);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static boolean validateId(String inputID) {

        if (inputID.isBlank()) {
            System.out.printf(ERROR_MSG, String.format("Account number can't be empty"));
            valid = false;
        } else if (!inputID.startsWith("SDB-") || inputID.length() != 9) {
            System.out.printf(ERROR_MSG, String.format("Invalid format"));
            valid = false;
        } else {
            for (int i = 4; i < inputID.length(); i++) {
                if (!Character.isDigit(inputID.charAt(i))) {
                    System.out.printf(ERROR_MSG, "Invalid format");
                    valid = false;
                    break;
                }
            }
            valid = false;
            for (int i = 0; i < customer.length; i++) {
                if (inputID.equals(customer[i][0])) {
                    valid = true;
                    break;
                }
            }
            if (!valid)
                System.out.printf(ERROR_MSG, String.format("Not found!"));
            return valid;
        }
        return valid;
    }

    public static boolean validateAmount(String letter, Double amount) {
        if (letter.equals("d") && amount < 500) {
            System.out.printf(ERROR_MSG, String.format("Insufficient Deposit Amount!"));
            return false;
        } else if (letter.equals("w") && amount < 100) {
            System.out.printf(ERROR_MSG, String.format("Insufficient Withdrawel Amount!"));
            return false;
        } else
            return true;
    }
}