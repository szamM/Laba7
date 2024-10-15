package managers.commands;

import managers.DBManager;
import system.Request;

import java.sql.SQLException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Реализация команды для удаления элемента коллекции по его id.
 */
public class Remove implements Command {

    private static ReadWriteLock lock = new ReentrantReadWriteLock();
    /**
     *  Метод для выполнения команды remove_key
     * @param request аргумент
     */
    @Override
    public String execute(Request request) throws SQLException {
        lock.writeLock().lock();
        String line = DBManager.removeFlat(request);
        lock.writeLock().unlock();
        return line;

    }


    /**
     * @return название команды "remove_by_id"
     */
    @Override
    public String getName() {
        return "remove_by_id";
    }

    /**
     * @return описание команды "удалить элемент из коллекции по его id"
     */
    @Override
    public String getDescription() {
        return "удалить элемент из коллекции по его id";
    }
}

