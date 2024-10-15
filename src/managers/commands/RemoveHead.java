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
public class RemoveHead implements Command {

    /**
     * Выполняет команду для вывода и удаления первого элемента коллекции.
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
            String line = flat.toString();
            request.setFlat(flat);
            request.setPassword(flat.getId().toString());
            DBManager.removeFlat(request);
            return line + "\n" + "Элемент удален";
        }
    }

    /**
     * @return название команды "remove head"
     */
    @Override
    public String getName() {
        return "remove head";
    }

    /**
     * @return описание команды "вывести первый элемент коллекции и удалить его"
     */
    @Override
    public String getDescription() {
        return "вывести первый элемент коллекции и удалить его";
    }
}

