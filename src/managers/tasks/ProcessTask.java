package managers.tasks;

import exceptions.UnknownCommandException;
import managers.CommandManager;
import system.Request;

import java.util.concurrent.RecursiveTask;

public class ProcessTask extends RecursiveTask<String> {
    private Request request;

    public ProcessTask(Request request) {
        this.request = request;
    }

    @Override
    protected String compute() {
        try {
            return CommandManager.startExecuting(request);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } catch (UnknownCommandException e) {
            throw new RuntimeException(e);
        }
    }
}
