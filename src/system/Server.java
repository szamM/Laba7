package system;

import exceptions.UnknownCommandException;
import managers.CommandManager;
import managers.DBManager;
import managers.tasks.GetTask;
import managers.tasks.ProcessTask;
import managers.tasks.SendTask;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static managers.DBManager.connect;

public class Server {
    private static InetAddress address;
    static int port;
    private final ForkJoinPool pool = new ForkJoinPool();
    private final ExecutorService sendPool = Executors.newFixedThreadPool(10); // Fixed thread pool
    private static final ReadWriteLock lock = new ReentrantReadWriteLock();

    public void listen() throws SQLException {
        connect();
        DBManager.ReadDB();
        while (true) {
            // многопоточное приянтие данных
            GetTask getTask = new GetTask();
            Request request = pool.invoke(getTask);

            // многопоточная обработка полученного запроса
            ProcessTask processTask = new ProcessTask(request);
            String message = pool.invoke(processTask);
            request.setMessage(message);

            // многопоточная отправка ответа
            SendTask sendTask = new SendTask(address, port, request);
            sendPool.execute(sendTask);
        }
    }

    public static void setAddress(InetAddress address1) {
        address = address1;
    }

    public static void setPort(int port1) {
        port = port1;
    }

}