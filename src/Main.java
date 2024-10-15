import exceptions.UnknownCommandException;
import managers.CollectionManager;
import managers.CommandManager;
import system.Server;

import java.io.IOException;
import java.net.SocketException;
import java.util.Objects;

public class Main {
    public static void main(String[] args)  {
        try {
            new CommandManager();
            Server server = new Server();
            server.listen();
        } catch (Exception e){
            System.out.println("Возникла ошибка: " + e.getMessage());
        }
    }
}
