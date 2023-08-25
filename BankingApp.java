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
    static int id = 1;
    static String accID = String.format("SDB-%05d", id);
    static Double initialAmount;
    static Double newBalance;
    static Double transferAmount;
    static boolean valid;

    public static void main(String[] args) {

        final String MAIN_MENU = "\u2756 Welcome to Smart Banking App \u2756";
        final String OPEN_NEW_ACCOUNT = "\u2756 Open new Account \u2756";
        final String DEPOSIT_MONEY = "\u2756 Deposit Money \u2756";
        final String WITHDRAW_MONEY = "\u2756 Withdraw Money \u2756";
        final String TRANSFER_MONEY = "\u2756 Transfer Money \u2756";
        final String CHECK_ACCOUNT_BALANCE = "\u2756 Check Account Balance \u2756";
        final String DROP_EXISTING_ACCOUNT = "\u2756 Drop Existing Account \u2756";

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
                            screen = DROP_EXISTING_ACCOUNT;
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
                    id++;
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
                            int i = findAccount(inputID);
                            System.out.printf("Current Balance: Rs %,.2f\n", Double.valueOf(customer[i][2]));
                            valid = true;
                            System.out.print("\nDeposit Amount: ");
                            transferAmount = SCANNER.nextDouble();
                            SCANNER.nextLine();

                            valid = validateAmount("d", transferAmount);

                            if (valid) {
                                newBalance = Double.valueOf(customer[i][2]) + transferAmount;
                                customer[i][2] = newBalance + "";
                                System.out.printf("New Balance: Rs %,.2f", newBalance);
                                System.out.println();
                                System.out.printf(SUCCESS_MSG,
                                        String.format("Deposit completed!. Do you want to try again (Y/n)?: "));
                                if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) {
                                    valid = false;
                                    continue;}
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
                    transferAmount = 0.0;
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
                            int i = findAccount(inputID);
                            System.out.printf("Current Balance: Rs %,.2f\n", Double.valueOf(customer[i][2]));
                            valid = true;
                            System.out.print("\nWithdraw Amount: ");
                            transferAmount = SCANNER.nextDouble();
                            SCANNER.nextLine();

                            valid = validateAmount("w", transferAmount);

                            if (valid) {
                                newBalance = Double.valueOf(customer[i][2]) - transferAmount;
                                if(newBalance < 500) {
                                    System.out.printf(TRY_MSG,String.format("Remaining balance insufficient!. Do you want to try again (Y/n)?: "));
                                    if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) {
                                        valid = false;
                                        continue;}
                                    break;
                                }
                                customer[i][2] = newBalance + "";
                                System.out.printf("New Balance: Rs %,.2f", newBalance);
                                System.out.println();
                                System.out.printf(SUCCESS_MSG,
                                        String.format("Withdraw completed!. Do you want to try again (Y/n)?: "));
                                if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) {
                                    valid = false;
                                    continue;}
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
                    transferAmount = 0.0;
                    screen = MAIN_MENU;
                    break;
                
                case TRANSFER_MONEY:
                    String fromID;
                    String toID;
                    do {
                        /*From id validation */
                        valid = true;
                        int i = 0;
                        int j = 0;
                        System.out.print("Enter From Account number: ");
                        fromID = SCANNER.nextLine().strip();
                        valid = validateId(fromID);
                        if(!valid){
                         System.out.printf(TRY_MSG, String.format("Do you want to try again (Y/n)?: "));
                            if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) continue;
                            break;   
                        }
                        /*To id validation */ 
                        System.out.print("Enter To Account number: ");
                        toID = SCANNER.nextLine().strip();
                        valid = validateId(toID);
                        if(!valid){
                        System.out.printf(TRY_MSG, String.format("Do you want to try again (Y/n)?: "));
                        if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) continue;
                        break;   
                        }  
                        i = findAccount(fromID);
                        j = findAccount(toID);

                        System.out.printf("From Account balance: Rs %,.2f\n",Double.valueOf(customer[i][2]));
                        System.out.printf("From Account balance: Rs %,.2f\n",Double.valueOf(customer[j][2]));
                        
                        System.out.print("Enter amount: ");
                        transferAmount = SCANNER.nextDouble();
                        SCANNER.nextLine();

                        valid = validateAmount("w",transferAmount);

                        if(valid && (Double.valueOf(customer[i][2])-(transferAmount*1.02))>=500){
                           System.out.printf("New From Account Balance: Rs %,.2f\n",Double.valueOf(customer[i][2]) - (transferAmount*1.02));
                           System.out.printf("New To Account Balance: Rs %,.2f\n",Double.valueOf(customer[j][2]) + transferAmount);
                           customer[i][2] = Double.valueOf(customer[i][2]) - (transferAmount*1.02) +"";
                           customer[j][2] = Double.valueOf(customer[j][2]) + transferAmount + ""; 
                           System.out.printf(SUCCESS_MSG,
                                        String.format("Money transfer completed!. Do you want to try again (Y/n)?: "));
                                if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) {
                                    valid = false;
                                    continue;}
                                break;

                        }else {
                           System.out.printf(TRY_MSG, String.format("Insufficient transfer amount. Do you want to try again (Y/n)?: ")); 
                           if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) {
                                    valid = false;
                                    continue;}
                           break; 
                        }
    
                    } while (!valid);
                    screen = MAIN_MENU;
                        break;
                        
                case CHECK_ACCOUNT_BALANCE:
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
                            int i = findAccount(inputID);
                            System.out.printf("Name: %s\n",customer[i][1]);
                            System.out.printf("Current Account Balance: Rs %,.2f\n",Double.valueOf(customer[i][2]));
                            System.out.printf("Available Balance: Rs %,.2f\n",Double.valueOf(customer[i][2])-500.0);
                            System.out.printf(SUCCESS_MSG,
                            String.format("Do you want to try again (Y/n)?: "));
                            if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) {
                                    valid = false;
                                    continue;}
                            break;
                        }

                } while (!valid);
                screen = MAIN_MENU;
                break;

                case DROP_EXISTING_ACCOUNT:
                do {
                    valid = true;
                    System.out.print("Enter Account number: ");
                    inputID = SCANNER.nextLine().strip();
                    valid = validateId(inputID);
                    if(valid){
                       int i = findAccount(inputID);
                       System.out.printf("Name : %s\n",customer[i][1]); 
                       System.out.printf("Account balance: %,.2f\n",Double.valueOf(customer[i][2]));
                       System.out.printf(TRY_MSG,"Do you want to delete (Y/n)?: \n");
                       if(SCANNER.nextLine().strip().toUpperCase().equals("Y")){

                       String accDetails = "".concat(customer[i][0].concat(" : ").concat(customer[i][1]));
                       tempCustomer = new String[customer.length - 1][3];
                            for (int j = 0,k = 0; j < tempCustomer.length; j++,k++) {
                            if(j==i&&k==i){
                                j--; 
                                continue;}
                            tempCustomer[j] = customer[k];
                            }

                        customer = tempCustomer;

                        System.out.printf(SUCCESS_MSG,
                            String.format("%s has been deleted successfully!. Do you want to try again (Y/n)?: ",accDetails));
                            if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) {
                                    valid = false;
                                    continue;}
                            break;

                       } else {
                        valid = false;
                        break;
                       }
                    } else {
                        System.out.println();
                        System.out.printf(TRY_MSG, String.format("Do you want to try again (Y/n)?: "));
                        if (SCANNER.nextLine().strip().toUpperCase().equals("Y"))
                            continue;
                        break;
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

    public static int findAccount(String id){
        int i = 0;
        do {
            if(customer[i][0].equals(id)) return i;
            i++;
        } while (i<customer.length);
        return -1;
    }
}