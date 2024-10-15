package managers.commands;

import managers.CollectionManager;
import managers.DBManager;
import system.Request;

import java.sql.SQLException;

/**
 * Класс ClearCommand реализует интерфейс Command и отвечает за очистку коллекции.
 */
public class Clear implements Command {
    /**
     * Метод execute выполняет очистку коллекции.
     * @param request массив аргументов, не используется в этом методе.
     * @throws Exception если возникает ошибка при очистке коллекции.
     */
    @Override
    public String execute(Request request) throws SQLException {
        DBManager.clearUserCollection(request);
        return "коллекция очищена";
    }

    /**
     * Метод getName возвращает имя команды "clear".
     * @return имя команды "clear"
     */
    @Override
    public String getName() {
        return "clear";
    }

    /**
     * Метод getDescription возвращает описание команды "очистить коллекцию".
     * @return описание команды "очистить коллекцию"
     */
    @Override
    public String getDescription() {
        return "очистить коллекцию";
    }
}
