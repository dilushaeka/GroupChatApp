package lk.ijse.gdse.server.controller;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.List;

public class LocalSocketManager implements Runnable {

    public Socket socket;
    public List<LocalSocketManager> localSocketManagerList;
    public DataInputStream inputStream;
    public DataOutputStream outputStream;
    public String type;

    public LocalSocketManager(Socket socket, List<LocalSocketManager> localSocketManagerList) {

        this.socket = socket;
        this.localSocketManagerList = localSocketManagerList;
    }

    @Override
    public void run() {

        try {

            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());


            while (socket.isConnected()) {

                this.type = inputStream.readUTF();
                if (this.type.equalsIgnoreCase("text")) {

                    sendText();

                }else if(this.type.equalsIgnoreCase("picture")){

                    sendFile();

                }else if(this.type.equalsIgnoreCase("Close")) {

                    System.out.println(localSocketManagerList.size());
                    localSocketManagerList.remove(this);
                    System.out.println(localSocketManagerList.size());
                    this.type = "Text";
                    sendText();
                    Thread.currentThread().isInterrupted();

                }
            }
            inputStream.close();
            outputStream.close();
            socket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendFile() {

        try {

            String userName = inputStream.readUTF();

            byte[] sizeAr = new byte[4];
            inputStream.read(sizeAr);
            int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();

            byte[] imageAr = new byte[size];
            inputStream.read(imageAr);

            for (LocalSocketManager localSocketManager : localSocketManagerList) {

                if(!localSocketManager.equals(this)){

                    localSocketManager.outputStream.writeUTF(type);
                    localSocketManager.outputStream.writeUTF(userName);
                    localSocketManager.outputStream.write(sizeAr);
                    localSocketManager.outputStream.write(imageAr);
                    localSocketManager.outputStream.flush();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void sendText() {

        try {

            String userName = inputStream.readUTF();
            String message = inputStream.readUTF();
            System.out.println(userName);
            System.out.println(message);

            for (LocalSocketManager localSocketManager : localSocketManagerList) {

                if (!localSocketManager.equals(this)) {

                    localSocketManager.outputStream.writeUTF(type);
                    localSocketManager.outputStream.writeUTF(userName);
                    localSocketManager.outputStream.writeUTF(message);
                    localSocketManager.outputStream.flush();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
