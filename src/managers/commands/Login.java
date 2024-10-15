package managers.commands;

import managers.UserManager;
import system.Request;

public class Login {
    public static String execute(Request request) throws Exception {
        if (UserManager.isAthCorrect(request.getLogin(), request.getPassword())){
            return "true";
        } else {
            return "false";
        }
    }

}
