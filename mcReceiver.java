import java.net.*;
import java.nio.*;
import java.io.*;
import java.util.Scanner;
import javax.script.*;

public class MulticastReceiver extends Thread {
    protected MulticastSocket socket = null;
    protected byte[] buf = new byte[256];

    private DatagramSocket socket_;
    private byte[] buf_;

    private String message;
    private String thisMaq = "1"; // um valor diferente para cada maquina

    public void mcRun(String add) throws IOException {
        socket = new MulticastSocket(4446);
        // ip --> "224.0.0.0"
        InetAddress group = InetAddress.getByName(add);
        socket.joinGroup(group);
        
        Boolean test = true;

        while (true) {
            System.out.println("Aguardando...");
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println(received);

            if (received.length() > 1) {
                message = received;

                buf = thisMaq.getBytes();
                DatagramPacket pack = new DatagramPacket(buf_, buf_.length, group, 4446);

                socket_ = new DatagramSocket();
                socket_.send(pack);
                socket_.close();
            } else {
                DatagramPacket p = new DatagramPacket(buf, buf.length);

                socket.receive(p);
                String r = new String(p.getData(), 0, p.getLength());
                
                if(Integer.parseInt(thisMaq) > Integer.parseInt(r)) {test = false;}
                continue;
            }

            if(test){
                try {
                    ScriptEngineManager factory = new ScriptEngineManager();
                    ScriptEngine engine = factory.getEngineByName("JavaScript");
                    Object obj = engine.eval(message);

                    buf_ = (thisMaq + ": " + obj.toString()).getBytes();
                    DatagramPacket packet_ = new DatagramPacket(buf_, buf_.length, group, 4447);

                    socket_ = new DatagramSocket();
                    socket_.send(packet_);
                    socket_.close();

                    System.out.println(obj);
                } catch (Exception e) {
                    System.out.println("ERROR!");
                }
            }

            if("end".equals(received)){
                break;
            }
        }
        
        socket.leaveGroup(group);
        socket.close();
    }

   public static void main(String[] args) throws IOException {
       MulticastReceiver mr=new MulticastReceiver();
       Scanner scan = new Scanner(System.in);
       System.out.print("Informe o IP do grupo multicast: ");
       String add = scan.nextLine();
       //String add = "224.0.0.0";
       mr.mcRun(add);
   }
}