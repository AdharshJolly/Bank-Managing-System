import java.util.Scanner;

import com.talenciaglobal.gdb.controller.AccountController;
import com.talenciaglobal.gdb.repository.AccountRepository;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AccountRepository repository = new AccountRepository();
        AccountController controller = new AccountController(scanner, repository);

        boolean running = true;
        while (running) {
            System.out.println("\n===== GlobalDigitalBank =====");
            System.out.println("  1. Create Account");
            System.out.println("  2. Activate Account");
            System.out.println("  3. Deposit");
            System.out.println("  4. Withdraw");
            System.out.println("  5. Transfer");
            System.out.println("  6. View Account");
            System.out.println("  7. List All Accounts");
            System.out.println("  8. Close Account");
            System.out.println("  9. Apply Interest (Savings only)");
            System.out.println("  0. Exit");
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
                    case "6" -> {
                        System.out.print("Account Number: ");
                        long id = Long.parseLong(scanner.nextLine().trim());
                        repository.findById(id).ifPresentOrElse(
                                controller::display,
                                () -> System.out.println("Account not found."));
                    }
                    case "7" -> controller.listAll();
                    case "8" -> controller.closeAccount();
                    case "9" -> controller.applyInterest();
                    case "0" -> running = false;
                    default -> System.out.println("Invalid option. Please choose 0-8.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        System.out.println("Goodbye!");
        scanner.close();
    }
}
