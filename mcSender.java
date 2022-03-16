import java.net.*;
import java.io.*;
import java.nio.*;
import java.util.Scanner;

public class MulticastPublisher {
    private DatagramSocket socket;
    private InetAddress group;
    private byte[] buf;

    public class MR extends Thread {
        protected MulticastSocket socket_ = null;
        protected byte[] buf_ = new byte[256];
    }

    public void multicast(String multicastMessage, String add) throws IOException {
        socket = new DatagramSocket();
        // ip --> "224.0.0.0"
        group = InetAddress.getByName(add);
        buf = multicastMessage.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 4446);
        socket.send(packet);

        System.out.println("Aguardando...");
        MR mr = new MR();
        mr.socket_ = new MulticastSocket(4447);
        mr.socket_.joinGroup(group);
        DatagramPacket packet_ = new DatagramPacket(mr.buf_, mr.buf_.length, group, 4446);
        mr.socket_.receive(packet_);
        String received = new String(packet_.getData(), 0, packet_.getLength());
        System.out.println(received);

        mr.socket_.leaveGroup(group);
        mr.socket_.close();

        socket.close();
    }

    public static void main(String[] args) throws IOException {
       MulticastPublisher mp = new MulticastPublisher();
       Scanner scan = new Scanner(System.in);
       System.out.print("Informe o IP do grupo multicast: ");
       String add = scan.nextLine();
       //String add = "224.0.0.0";
       System.out.print("Escreva uma expressao: ");
       String op = scan.nextLine();
       mp.multicast(op, add);
   }
}