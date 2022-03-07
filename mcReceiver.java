import java.net.*;
import java.nio.*;
import java.io.*;
import java.util.Scanner;

public class MulticastReceiver extends Thread {
    protected MulticastSocket socket = null;
    protected byte[] buf = new byte[256];

    public void mcRun(String ipA) throws IOException {
        socket = new MulticastSocket(4446);
        // ip --> "224.0.0.0"
        InetAddress group = InetAddress.getByName(ipA);
        socket.joinGroup(group);
        while (true) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            String received = new String(packet.getData(), 0, packet.getLength());
            try {
                ScriptEngineManager factory = new ScriptEngineManager();
                ScriptEngine engine = factory.getEngineByName("JavaScript");
                System.out.println (obj);
            }
            catch(Exception e) {
                System.out.println("ERROR!");
            }
            if ("end".equals(received)) {
                break;
            }
        }
        socket.leaveGroup(group);
        socket.close();
    }

   public static void main(String[] args) throws IOException {
       MulticastReceiver mr=new MulticastReceiver();
       System.out.println("Informe o IP: ");
       String ipAddr = scan.nextLine();
       mr.mcRun(ipAddr);
   }
}