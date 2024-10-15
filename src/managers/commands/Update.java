package managers.commands;


import managers.CollectionManager;
import managers.DBManager;
import system.Request;

import java.sql.SQLException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * Реализация команды для обновления элемента коллекции по его id.
 */
public class Update implements Command {

    private static ReadWriteLock lock = new ReentrantReadWriteLock();
    /**
     *  Метод для выполнения команды update_id
     * @param request аргумент
     */
    @Override
    public String execute(Request request) throws SQLException {
        lock.writeLock().lock();
        CollectionManager.updateCommand(request);
        String line = DBManager.updateFlat(request);
        lock.writeLock().unlock();
        return line;
    }

    /**
     * @return название команды "update"
     */
    @Override
    public String getName() {
        return "update";
    }

    /**
     * @return описание команды "обновить значение элемента коллекции, id которого равен заданному"
     */
    @Override
    public String getDescription() {
        return "обновить значение элемента коллекции, id которого равен заданному";
    }
}
