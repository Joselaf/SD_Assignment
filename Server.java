import java.net.*;
import java.io.*;
import sd23(1).sd

public class Server {
    public static void main(String args[]) throws IOException {
        ServerSocket ss = new ServerSocket(4999);
        Socket s = ss.accept();

        System.out.println("Client connected");

        s.close();
        ss.close();
    }
}
