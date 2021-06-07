package EBS_Proj.EBS_Proj;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {
    public List<ClientHandler> subscribers;
    private int port;

    public Server(int port) {
        this.port = port;
        subscribers = new ArrayList<>();
    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            Socket sock = null;

            try {
                sock = serverSocket.accept();

                DataInputStream dis = new DataInputStream(sock.getInputStream());
                DataOutputStream dos = new DataOutputStream(sock.getOutputStream());

                ClientHandler t = new ClientHandler(sock, dis, dos);
                t.start();
                subscribers.add(t);
            } catch (Exception e) {
                try {
                    sock.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    }
}

class ClientHandler extends Thread {
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;
    String subscription;

    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    public String getSubscription() {
        return this.subscription;
    }

    public void notifySingle(WeatherModel weather) throws IOException {
        dos.writeUTF(weather.toString());
    }

    public void notifyWindow(String metaPublication) throws IOException {
        dos.writeUTF(metaPublication);
    }

    @Override
    public void run() {
        try {
            subscription = dis.readUTF();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}