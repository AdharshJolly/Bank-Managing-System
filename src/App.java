import java.util.Scanner;

import com.talenciaglobal.gdb.controller.AccountController;
import com.talenciaglobal.gdb.model.Account;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AccountController controller = new AccountController(scanner);

        System.out.println("=== GlobalDigitalBank ===");
        Account account = controller.create();

        System.out.println("\nAccount created. Activating...");
        account.activateAccount();

        controller.display(account);
        scanner.close();
    }
}
