import java.util.ArrayList;
import com.google.gson.Gson;
import java.io.*;
import java.util.Scanner;

public class LogIn {
    static Scanner sc = new Scanner(System.in);
//    static File file = new File("test.json");
    static File file = new File("accounts.json");
    static ArrayList<Account> accounts = new ArrayList<>();

    static boolean loggedIn = false;
    static int currentAccount = -1;

    public static void main(String[] args) {
        readJson(file);

        System.out.println("~~~Cottage Kore~~~\nType 'i' to see all command keys");
        String input = "";
        while (!input.equals("x")) {
            int result;
            System.out.print("\nPlease type a valid command key: ");
            input = sc.next().toLowerCase();

            //noinspection EnhancedSwitchMigration
            switch (input) {
                case "i":
                case "'i'":
                    System.out.println("> i : get all command keys");
                    System.out.println("> w : get welcome message");
                    if (!loggedIn) {
                        System.out.println("> l : login");
                        System.out.println("> c : create account");
                    }
                    if (loggedIn) System.out.println("> o : logout");
                    System.out.println("> s : get login status");
                    System.out.println("> x : exit program");
                    break;
                case "w":
                    System.out.println("---Welcome---");
                    if (loggedIn) {
                        System.out.print("Hi, " + accounts.get(currentAccount).getName().getFirst());
                        System.out.println("! Welcome to Cottage Kore!");
                    } else {
                        System.out.println("Hi! Welcome to Cottage Kore!");
                    }
                    break;
                case "c":
                    System.out.println("---Create Account---");
                    result = createAccount();
                    if (result == -1) {
                        System.out.println("Account already exists. Please login instead.");
                    } else if (result == 0) {
                        System.out.println("Account created successfully!");
                    } else {
                        System.out.println("uh oh. something went wrong.");
                    }
                    break;
                case "l":
                    System.out.println("---Login---");
                    result = login();
                    if (result == -1) {
                        System.out.println("Account does not exist. Please create one before attempting to login.");
                    } else if (result == 0) {
                        System.out.println("Login successful!");
                    } else if (result == 1) {
                        System.out.println("Already logged in.");
                    } else {
                        if (result != -2) System.out.println("uh oh. something went wrong.");
                    }
                    break;
                case "o":
                    System.out.println("---Logout---");
                    result = logout();
                    if (result == -1) {
                        System.out.println("Already not logged in.");
                    } else if (result == 0) {
                        System.out.println("Logged out.");
                    } else {
                        System.out.println("uh oh. something went wrong.");
                    }
                    break;
                case "s" :
                    System.out.println("---Login Status---");
                    if (currentAccount == -1) {
                        System.out.println("Not currently logged in.");
                    } else {
                        String user = accounts.get(currentAccount).getEmail();
                        System.out.println("Currently logged in as: " + user);
                    }
                    break;
                case "x":
                    System.out.println("---Exit---");
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Unknown key.");
            }
        }
    }

    private static void readJson(File file) {
        try {
            Scanner reader = new Scanner(file);
            Gson gson = new Gson();

            while (reader.hasNextLine()) {
                String line = reader.nextLine();

                if (!line.equals("[") && !line.equals("]")) {
                    if (line.charAt(line.length()-1) == ',') {
                        line = line.substring(0, line.length()-1);
                    }
                    accounts.add(gson.fromJson(line, Account.class));
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static int createAccount() {
        System.out.print("Email Address: ");
        String email = sc.next().toLowerCase();
        if (accountExists(email) != -1) {
            return -1;
        }

        System.out.print("First Name: ");
        String fName = sc.next();

        System.out.print("Middle Name (type '-' to skip): ");
        String mName = sc.next();

        System.out.print("Last Name: ");
        String lName = sc.next();

        String password;
        boolean valid;
        do {
            System.out.print("\nPassword is case sensitive and must be 8 to 25 characters and cannot include spaces.");
            System.out.print(" Password: ");
            password = sc.next();
            valid = password.length() >= 8 && password.length() <= 25;
        } while (!valid);

        Account acc;
        if (!mName.equals("-") && !mName.equals("'='")) {
            acc = new Account(new Name(fName, mName, lName), email, password);
        } else {
            acc = new Account(new Name(fName, lName), email, password);
        }
        accounts.add(acc);
        sendToFile();

        login(accounts.size()-1);
        loggedIn = true;
        return 0;
    }

    private static int accountExists(String email) {
        for (int i=0; i < accounts.size(); i++) {
            if (accounts.get(i).getEmail().equals(email)) return i;
        }
        return -1;
    }

    private static int login() {
        if (loggedIn) return 1;

        System.out.print("Email Address: ");
        String email = sc.next();

        currentAccount = accountExists(email);
        if (currentAccount == -1) return -1;

        boolean done = false;
        do {
            System.out.print("Password: ");
            String password = sc.next().toLowerCase();

            if (password.equals(accounts.get(currentAccount).getPassword())) {
                loggedIn = true;
                done = true;
            } else {
                System.out.println("\nIncorrect Password. Try again? ('y' or 'n')");
                String answer = sc.next();
                if (!(answer.equals("y") || answer.equals("'y"))) {
                    currentAccount = -1;
                    return -2;
                }
            }
        } while (!done);
        return 0;
    }

    private static void login(int index) {
        if (index < accounts.size()) {
            loggedIn = true;
            currentAccount = index;
        }
    }

    private static int logout() {
        if (loggedIn) {
            loggedIn = false;
            currentAccount = -1;
            return 0;
        }
        return -1;
    }

    private static void sendToFile() {
        try {
            Gson gson = new Gson();
            FileWriter writer = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(writer);
            int len = accounts.size() + 2;

            for (int i=0; i < len; i++) {
                if (i == 0) {
                    out.write("[");
                } else if (i == len-1) {
                    out.newLine();
                    out.write("]");

                } else {
                    out.newLine();
                    if (i == len-2) {
                        out.write(gson.toJson(accounts.get(i-1)));
                    } else {
                        out.write(gson.toJson(accounts.get(i - 1)) + ",");
                    }
                }
            }
            out.close();
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }
}
