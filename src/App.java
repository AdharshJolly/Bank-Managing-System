import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.talenciaglobal.gdb.controller.AccountController;
import com.talenciaglobal.gdb.model.Account;
import com.talenciaglobal.gdb.model.BankEmployee;
import com.talenciaglobal.gdb.model.EmployeeRole;
import com.talenciaglobal.gdb.repository.AccountRepository;
import com.talenciaglobal.gdb.repository.AuditLogRepository;
import com.talenciaglobal.gdb.repository.EmployeeRepository;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AccountRepository accountRepository = new AccountRepository();
        EmployeeRepository employeeRepository = new EmployeeRepository();
        AuditLogRepository auditLogRepository = new AuditLogRepository();

        // Pre-seeded staff
        employeeRepository.save(new BankEmployee("Alice Admin", EmployeeRole.ADMIN, "1111"));
        employeeRepository.save(new BankEmployee("Bob Teller", EmployeeRole.TELLER, "2222"));
        employeeRepository.save(new BankEmployee("Carol Manager", EmployeeRole.MANAGER, "3333"));

        AccountController controller = new AccountController(scanner, accountRepository, employeeRepository,
                auditLogRepository);

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
            EmployeeRole role = emp.getRole();
            System.out.println("\n===== Employee Portal =====\n  " + emp.getEmployeeName() + " [" + role + "]");
            System.out.println("---------------------------");

            // Build dynamic menu based on role
            List<String> labels = new ArrayList<>();
            List<Runnable> actions = new ArrayList<>();

            if (role.canCreateAccounts()) {
                labels.add("Create Account");
                actions.add(() -> controller.create());
                labels.add("Activate Account");
                actions.add(controller::activateAccount);
            }
            labels.add("Deposit");
            actions.add(controller::deposit);
            labels.add("Withdraw");
            actions.add(controller::withdraw);
            labels.add("Transfer");
            actions.add(controller::transfer);
            labels.add("View Account");
            actions.add(controller::viewAccount);
            if (role.canCreateAccounts()) {
                labels.add("List All Accounts");
                actions.add(controller::listAll);
                labels.add("Close Account");
                actions.add(controller::closeAccount);
                labels.add("Apply Interest (Savings)");
                actions.add(controller::applyInterest);
            }
            labels.add("View Transaction History");
            actions.add(controller::viewTransactionHistory);
            if (role.canCreateAccounts()) {
                labels.add("Unlock Customer Account");
                actions.add(controller::unlockCustomerAccount);
                labels.add("Change Account Privilege");
                actions.add(controller::changePrivilege);
                labels.add("Batch Apply Interest (All Savings)");
                actions.add(controller::batchApplyInterest);
            }
            if (role.canUnlockEmployees()) {
                labels.add("Unlock Employee Account");
                actions.add(controller::unlockEmployeeAccount);
            }
            if (role.canManageEmployees()) {
                labels.add("Create Employee");
                actions.add(controller::createEmployee);
                labels.add("Deactivate Employee");
                actions.add(controller::deactivateEmployee);
                labels.add("List All Employees");
                actions.add(controller::listAllEmployees);
                labels.add("View Audit Log");
                actions.add(controller::viewAuditLog);
            }

            for (int i = 0; i < labels.size(); i++) {
                System.out.printf("%3d. %s%n", i + 1, labels.get(i));
            }
            System.out.println("  S. Search Customer by Name");
            System.out.println("  0. Logout");
            System.out.print("Choose: ");

            String input = scanner.nextLine().trim();
            System.out.println();

            try {
                if (input.equalsIgnoreCase("S")) {
                    controller.searchByName();
                } else if (input.equals("0")) {
                    controller.logoutEmployee();
                    active = false;
                } else {
                    int choice = Integer.parseInt(input);
                    if (choice < 1 || choice > actions.size()) {
                        System.out.println("Invalid option.");
                    } else {
                        actions.get(choice - 1).run();
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid option.");
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
            System.out.println("  6. Change PIN");
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
                    case "6" -> controller.changeOwnPin();
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
