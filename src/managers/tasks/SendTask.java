package managers.tasks;

import system.Request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SendTask implements Runnable {
    private InetAddress address;
    private int port;
    private Request request;

    public SendTask(InetAddress address, int port, Request request) {
        this.address = address;
        this.port = port;
        this.request = request;
    }


    @Override
    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(request);
            objectOutputStream.close();
            DatagramPacket sendPacket = new DatagramPacket(byteArrayOutputStream.toByteArray(), byteArrayOutputStream.toByteArray().length, address, port);

            System.out.println("Отправка пакета клиенту " + address + ":" + port);
            socket.send(sendPacket);
            System.out.println("Пакет отправлен");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
