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
            BankEmployee activeEmp = controller.getActiveEmployee();
            Account activeUser = controller.getActiveUserAccount();
            System.out.println("\n===== GlobalDigitalBank =====");
            System.out.println("  Employee : " + (activeEmp != null
                    ? activeEmp.getEmployeeName() + " [" + activeEmp.getRole() + "]"
                    : "Not logged in"));
            System.out.println("  User     : " + (activeUser != null
                    ? activeUser.getAccountNumber() + " - " + activeUser.getName()
                    : "Not logged in"));
            System.out.println("-----------------------------");
            System.out.println("  E. Employee Login");
            System.out.println("  L. Employee Logout");
            System.out.println("  U. User Login");
            System.out.println("  Q. User Logout");
            System.out.println("-----------------------------");
            System.out.println("  1. Create Account              (employee only)");
            System.out.println("  2. Activate Account            (employee only)");
            System.out.println("  3. Deposit");
            System.out.println("  4. Withdraw");
            System.out.println("  5. Transfer");
            System.out.println("  6. View Account                (employee: any | user: own)");
            System.out.println("  7. List All Accounts           (employee only)");
            System.out.println("  8. Close Account               (employee only)");
            System.out.println("  9. Apply Interest              (Savings only)");
            System.out.println(" 10. View Transaction History    (employee: any | user: own)");
            System.out.println("  0. Exit");
            System.out.print("Choose: ");

            String input = scanner.nextLine().trim().toUpperCase();
            System.out.println();

            try {
                switch (input) {
                    case "E" -> controller.loginEmployee();
                    case "L" -> controller.logoutEmployee();
                    case "U" -> controller.loginUser();
                    case "Q" -> controller.logoutUser();
                    case "1" -> controller.create();
                    case "2" -> controller.activateAccount();
                    case "3" -> controller.deposit();
                    case "4" -> controller.withdraw();
                    case "5" -> controller.transfer();
                    case "6" -> {
                        if (activeEmp != null) {
                            System.out.print("Account Number: ");
                            long id = Long.parseLong(scanner.nextLine().trim());
                            accountRepository.findById(id).ifPresentOrElse(
                                    controller::display,
                                    () -> System.out.println("Account not found."));
                        } else if (activeUser != null) {
                            controller.display(activeUser);
                        } else {
                            throw new IllegalStateException("Access denied. Please log in to view an account.");
                        }
                    }
                    case "7" -> controller.listAll();
                    case "8" -> controller.closeAccount();
                    case "9" -> controller.applyInterest();
                    case "10" -> controller.viewTransactionHistory();
                    case "0" -> running = false;
                    default -> System.out.println("Invalid option.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        System.out.println("Goodbye!");
        scanner.close();
    }
}
