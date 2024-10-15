package managers.commands;

import data.Flat;
import exceptions.ArrayDeqIsEmpty;
import managers.CollectionManager;
import managers.DBManager;
import system.Request;

import java.sql.SQLException;

/**
 * Реализация команды для удаления первого элемента коллекции.
 */
public class RemoveFirst implements Command {

    /**
     * Выполняет команду для удаления первого элемента коллекции.
     * @param request аргументы команды (не используются)
     * @throws ArrayDeqIsEmpty если коллекция пуста
     */
    @Override
    public String execute(Request request) throws ArrayDeqIsEmpty, SQLException {
        if (CollectionManager.getArrayDeque().size() == 0){
            throw new ArrayDeqIsEmpty("Коллекция пуста");
        }
        else{
            Flat flat = CollectionManager.getArrayDeque().getFirst();
            request.setFlat(flat);
            request.setPassword(flat.getId().toString());
            DBManager.removeFlat(request);
            return "Элемент удален";
        }
    }

    /**
     * @return название команды "Remove first"
     */
    @Override
    public String getName() {
        return "Remove first";
    }

    /**
     * @return описание команды "удалить первый элемент из коллекции"
     */
    @Override
    public String getDescription() {
        return "удалить первый элемент из коллекции";
    }
}

