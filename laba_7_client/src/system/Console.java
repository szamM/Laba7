package system;

import data.Flat;
import data.generators.FlatGenerator;
import data.generators.IdGenerator;

import java.io.InputStream;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Console {
    private Request request;
    private static String filename;
    public static String username;
    public static String password;

    /**
     *  Метод для запуска
     * @param inputStream входной поток
     * @throws Exception исключение
     */
    public void start(InputStream inputStream) throws Exception {
        while (true) {
            Scanner scanner = new Scanner(inputStream);
            Client client = new Client();
            String input;
            boolean status_of_ath = false;
            System.out.println("Добро пожаловать в приложение");
            System.out.println(
                    "если вы новый пользователь, то введите 'create', чтобы создать новый аккаунт\n" +
                            "если вы уже зарегистрированы, то введите 'log_in', чтобы войти в аккаунт"
            );

            while (!status_of_ath) {
                input = scanner.nextLine().split(" ")[0];

                if (input.equals("create")) {
                    System.out.println("создайте имя пользователя");
                    username = scanner.nextLine().split(" ")[0];
                    System.out.println("создайте пароль");
                    password = scanner.nextLine().split(" ")[0];
                    request = new Request("create", null, password, username);

                    System.out.println(client.sendEcho(request));

                } else if (input.equals("log_in")) {
                    System.out.println("напишите имя пользователя");
                    username = scanner.nextLine().split(" ")[0];
                    System.out.println("напишите пароль");
                    password = scanner.nextLine().split(" ")[0];
                    request = new Request("log_in", null, password, username);

                    String res = client.sendEcho(request);
                    if (res == null) {
                        status_of_ath = true;
                        System.out.println("сервер недоступен");
                    } else if (res.equals("true")) {
                        status_of_ath = true;
                        System.out.println("Супер, вы вошли в аккаунт!\n" +
                                "Введите команду 'help', чтобы увидеть все доступные команды");
                    } else {
                        System.out.println("неверный логин или пароль");
                    }
                } else {
                    System.out.println("вы не можете использовать приложение без авторизации");
                }
            }
            while (true) {
                scanner = new Scanner(inputStream);
                try {
                    String[] elements = scanner.nextLine().split(" ");
                    input = elements[0];
                    client = new Client();

                    if (input.equals("update")) {
                        if (elements.length == 1) {
                            System.out.println("введите аргумент после команды");
                        } else {
                            request = new Request(input, null, null, username);
                            String key = elements[1];
                            Flat flat = FlatGenerator.createFlat();
                            request.setFlat(flat);
                            request.setPassword(key);
                            String echo = client.sendEcho(request);
                            System.out.println("Ответ сервера: \n" + echo);
                            client.close();

                        }
                    } else if(input.equals("add")){
                        request = new Request(input, null, null, username);
                        Flat flat = FlatGenerator.createFlat();
                        request.setFlat(flat);
                        String echo = client.sendEcho(request);
                        System.out.println("Ответ сервера: \n" + echo);
                        client.close();
                    }else if (input.equals("remove_by_id") ||input.equals("count_greater_than_view") || input.equals("filter_less_than_living_space")) {
                        if (elements.length == 1) {
                            System.out.println("введите аргумент после команды");
                        } else {
                            request = new Request(input, null, null, username);
                            String key = elements[1];
                            request.setPassword(key);
                            String echo = client.sendEcho(request);
                            System.out.println("Ответ сервера: \n" + echo);
                            client.close();
                        }
                    }else if (input.equals("exit")) {
                        request = new Request(input);
                        System.out.println("пока пока");
                        System.exit(1);
                    // } else if (input.split(" ")[0].equals("execute_script")) {
                    //     request = new Request(input, null, null);
                    //     request.setKey(elements[1]);
                    //     ExecuteScript.execute(request);
                    } else {
                        request = new Request(input, null, null, username);
                        String echo = client.sendEcho(request);
                        System.out.println("Ответ сервера: \n" + echo);
                        client.close();
                    }

                } catch (SocketException e) {
                    System.out.println("SocketException: \n" + e.getMessage());
                } catch (UnknownHostException e) {
                    System.out.println("UnknownHostException: \n" + e.getMessage());
                }
            }

        }
    }

}
