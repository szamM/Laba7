package managers.commands;

import managers.UserManager;
import system.Request;

public class Create {

    public static String execute(Request request) throws Exception {
        return UserManager.createUser(request);
    }

}
