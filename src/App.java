import com.talenciaglobal.gdb.controller.AccountController;
import com.talenciaglobal.gdb.model.Account;
import com.talenciaglobal.gdb.model.Privilege;

public class App {
    public static void main(String[] args) throws Exception {

        Account a = new Account();
        a.setName("Adharsh");
        a.setBalance(100000);
        a.setAccountNumber(1);
        a.setPrivilege(Privilege.Platinum);

        AccountController controller = new AccountController();
        controller.display(a);
    }
}
