import java.net.*;
import java.io.*;
import java.nio.*;
import java.util.Scanner;

public class MulticastPublisher {
    private DatagramSocket socket;
    private InetAddress group;
    private byte[] buf;

    public void multicast(String multicastMessage, String ipA) throws IOException {
        socket = new DatagramSocket();
        // ip --> "224.0.0.0"
        group = InetAddress.getByName(ipA);
        buf = multicastMessage.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 4446);
        socket.send(packet);
        socket.close();
    }

    public static void main(String[] args) throws IOException {
       MulticastPublisher mp = new MulticastPublisher();
       Scanner scan = new Scanner(System.in);
       System.out.println("Informe o IP: ");
       String ipAddr = scan.nextLine();
       System.out.println("Escreva uma expressao: ");
       String op = scan.nextLine();
       mp.multicast(op, ipAddr);
   }
}