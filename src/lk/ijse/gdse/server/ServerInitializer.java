package lk.ijse.gdse.server;

import lk.ijse.gdse.server.controller.LocalSocketManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerInitializer {

    public static List<LocalSocketManager> localSocketManagerList = new ArrayList<>();

    public static void main(String[] args) {

        ServerSocket serverSocket;
        Socket socket;
        try {

            serverSocket = new ServerSocket(3002);

            while (!serverSocket.isClosed()) {

                System.out.println("Waiting");
                socket = serverSocket.accept();
                System.out.println("accept");

                LocalSocketManager localSocketManager = new LocalSocketManager(socket, localSocketManagerList);
                localSocketManagerList.add(localSocketManager);

                Thread thread =new Thread(localSocketManager);
                System.out.println("Thread start");
                thread.start();
            }
            //socket.close();
            serverSocket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
