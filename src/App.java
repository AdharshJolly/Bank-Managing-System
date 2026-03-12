import java.util.Scanner;

import com.talenciaglobal.gdb.controller.AccountController;
import com.talenciaglobal.gdb.model.Account;
import com.talenciaglobal.gdb.model.BankEmployee;
import com.talenciaglobal.gdb.model.EmployeeRole;
import com.talenciaglobal.gdb.repository.AccountRepository;
import com.talenciaglobal.gdb.repository.EmployeeRepository;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AccountRepository accountRepository = new AccountRepository();
        EmployeeRepository employeeRepository = new EmployeeRepository();

        // Pre-seeded staff
        employeeRepository.save(new BankEmployee("Alice Admin", EmployeeRole.ADMIN, "1111"));
        employeeRepository.save(new BankEmployee("Bob Teller", EmployeeRole.TELLER, "2222"));
        employeeRepository.save(new BankEmployee("Carol Manager", EmployeeRole.MANAGER, "3333"));

        AccountController controller = new AccountController(scanner, accountRepository, employeeRepository);

        boolean running = true;
        while (running) {
            System.out.println("\n===== GlobalDigitalBank =====");
            System.out.println("  1. Employee Portal   (Branch Terminal)");
            System.out.println("  2. Customer Portal   (Self-Service)");
            System.out.println("  0. Exit");
            System.out.print("Choose: ");

            String choice = scanner.nextLine().trim();
            System.out.println();

            switch (choice) {
                case "1" -> employeePortal(scanner, controller);
                case "2" -> customerPortal(scanner, controller);
                case "0" -> running = false;
                default -> System.out.println("Invalid option.");
            }
        }

        System.out.println("Goodbye!");
        scanner.close();
    }

    // ──────────────── Employee Portal ────────────────

    private static void employeePortal(Scanner scanner, AccountController controller) {
        try {
            controller.loginEmployee();
        } catch (IllegalArgumentException e) {
            System.out.println("Login failed: " + e.getMessage());
            return;
        }

        boolean active = true;
        while (active) {
            BankEmployee emp = controller.getActiveEmployee();
            System.out.println("\n===== Employee Portal =====");
            System.out.println("  Logged in: " + emp.getEmployeeName() + " [" + emp.getRole() + "]");
            System.out.println("---------------------------");
            System.out.println("  1. Create Account");
            System.out.println("  2. Activate Account");
            System.out.println("  3. Deposit");
            System.out.println("  4. Withdraw");
            System.out.println("  5. Transfer");
            System.out.println("  6. View Account");
            System.out.println("  7. List All Accounts");
            System.out.println("  8. Close Account");
            System.out.println("  9. Apply Interest (Savings)");
            System.out.println(" 10. View Transaction History");
            System.out.println(" 11. Unlock Customer Account");
            System.out.println("  0. Logout");
            System.out.print("Choose: ");

            String input = scanner.nextLine().trim();
            System.out.println();

            try {
                switch (input) {
                    case "1" -> controller.create();
                    case "2" -> controller.activateAccount();
                    case "3" -> controller.deposit();
                    case "4" -> controller.withdraw();
                    case "5" -> controller.transfer();
                    case "6" -> controller.viewAccount();
                    case "7" -> controller.listAll();
                    case "8" -> controller.closeAccount();
                    case "9" -> controller.applyInterest();
                    case "10" -> controller.viewTransactionHistory();
                    case "11" -> controller.unlockCustomerAccount();
                    case "0" -> {
                        controller.logoutEmployee();
                        active = false;
                    }
                    default -> System.out.println("Invalid option.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // ──────────────── Customer Self-Service Portal ────────────────

    private static void customerPortal(Scanner scanner, AccountController controller) {
        try {
            controller.loginUser();
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Login failed: " + e.getMessage());
            return;
        }

        boolean active = true;
        while (active) {
            Account user = controller.getActiveUserAccount();
            System.out.println("\n===== Customer Self-Service =====");
            System.out.println("  Account: " + user.getAccountNumber() + " — " + user.getName());
            System.out.printf("  Balance: %.2f%n", user.getBalance());
            System.out.println("---------------------------------");
            System.out.println("  1. View My Account");
            System.out.println("  2. Deposit");
            System.out.println("  3. Withdraw");
            System.out.println("  4. Transfer");
            System.out.println("  5. Transaction History");
            System.out.println("  0. Logout");
            System.out.print("Choose: ");

            String input = scanner.nextLine().trim();
            System.out.println();

            try {
                switch (input) {
                    case "1" -> controller.viewOwnAccount();
                    case "2" -> controller.depositOwn();
                    case "3" -> controller.withdrawOwn();
                    case "4" -> controller.transferFromOwn();
                    case "5" -> controller.viewOwnTransactionHistory();
                    case "0" -> {
                        controller.logoutUser();
                        active = false;
                    }
                    default -> System.out.println("Invalid option.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
