package system;

import data.Flat;
import java.io.Serial;
import java.io.Serializable;
import java.sql.SQLException;

public class Request implements Serializable {

    @Serial
    private static final long serialVersionUID = 5760575944040770153L;
    private String message = null;
    private Flat flat = null;
    private String password = null;
    private String login = null;

    /**
     *  Метод для создания запроса
     * @param message сообщение
     * @param flat человек
     */
    public Request(String message, Flat flat) {
        this.message = message;
        this.flat = flat;
    }

    /**
     *  Метод для создания запроса
     * @param message сообщение
     */
    public Request(String message) {
        this.message = message;
    }

    /**
     *  Метод для создания запроса
     * @param message сообщение
     * @param flat человек
     * @param password пароль
     * @param login логин
     */
    public Request(String message, Flat flat, String password, String login) {
        this.message = message;
        this.flat = flat;
        this.password = password;
        this.login = login;
    }


    /**
     *  Метод для получения сообщения
     * @return Сообщение
     */

    public String getMessage() {
        return message;
    }

    /**
     *  Метод для установки сообщения
     * @param message   сообщение
     */
    public void setMessage(String message) {
        this.message = message;
    }

    public Flat getFlat() {
        return flat;
    }
    public void setFlat(Flat flat) {
        this.flat = flat;
    }
    /**
     *  Метод для получения пароля
     * @return логин
     */
    public String getLogin() {
        return login;
    }

    /**
     *  Метод для установки логина
     * @param login логин
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     *  Метод для получения логина
     * @return Пароль
     */
    public String getPassword() {
        return password;
    }

    /**
     *  Метод для установки пароля
     * @param password пароль
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
