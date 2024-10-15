package managers.commands;

import data.Flat;
import managers.CollectionManager;
import managers.DBManager;
import system.Request;

import java.sql.SQLException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Класс AddCommand реализует интерфейс Command и отвечает за добавление новой квартиры в коллекцию.
 */
public class Add implements Command {
    private static ReadWriteLock lock = new ReentrantReadWriteLock();
    /**
     *  Метод для выполнения команды insert
     * @param request аргумент
     */

    @Override
    public String execute(Request request) throws SQLException {
        lock.writeLock().lock();
        try {
            Flat flat = request.getFlat();
            CollectionManager.add(flat);
            DBManager.addFlat(flat, request);
            lock.writeLock().unlock();
            return "квартира успешно добавлен";
        } catch (SQLException e) {
            lock.writeLock().unlock();
            return e.getMessage();
        }
    }

    /**
     * Метод getName возвращает имя команды "add".
     * @return имя команды "add"
     */
    @Override
    public String getName() {
        return "add";
    }

    /**
     * Метод getDescription возвращает описание команды "Добавить новый элемент в коллекцию".
     * @return описание команды "Добавить новый элемент в коллекцию"
     */
    @Override
    public String getDescription() {
        return "Добавить новый элемент в коллекцию";
    }
}


