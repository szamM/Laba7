package managers.tasks;

import system.Request;
import system.Server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.RecursiveTask;

public class GetTask extends RecursiveTask<Request> {
    private DatagramSocket socket;
    private byte[] buffer = new byte[5000];
    @Override
    protected Request compute() {
        Request request = null;
        try {
            socket = new DatagramSocket(8082);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            System.out.println("Ожидание пакета...");
            socket.receive(packet); // Получение пакета от клиента
            System.out.println("Пакет получен от " + packet.getAddress() + ":" + packet.getPort());
            Server.setAddress(packet.getAddress());
            Server.setPort(packet.getPort());

            // Извлечение данных из пакета
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packet.getData());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            request = (Request) objectInputStream.readObject();
            objectInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }

        return request;
    }
}