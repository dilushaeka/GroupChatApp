package lk.dil.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.sound.sampled.Port;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Client {
    private String name;

    private int port;

    private ServerSocket serverSocket;

    private Socket accept;

    private DataInputStream dataInputStream;

    private DataOutputStream dataOutputStream;

    private String message="";

    public Client(int port){
        this.port=port;
    }

    public void  acceptConnection() throws IOException {
        serverSocket=new ServerSocket(port);
        accept=serverSocket.accept();

    }

    public void setInputOutput() throws IOException {
        this.dataInputStream= new DataInputStream(accept.getInputStream());
        this.dataOutputStream = new DataOutputStream(accept.getOutputStream());
    }

}
