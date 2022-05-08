import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;

public class ClientRecevier extends Thread {

	private final DatagramSocket ServerDatagramSocket;

	public ClientRecevier(DatagramSocket server) {
		this.ServerDatagramSocket = server;
		

	}
	
	
	public long bytesToLong(byte[] bytes) {
	    ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
	    buffer.put(bytes);
	    buffer.flip();
	    return buffer.getLong();
	}
	
	Client a = new Client(); 
	

	@Override
	public void run() {
		while (true) {
			try {
				
			
		//	byte[] Senderbuffer = new byte[1008];
				
				
			byte[] Receiverbuffer = new byte[1024];

			DatagramPacket receive = new DatagramPacket(Receiverbuffer, Receiverbuffer.length);
			ServerDatagramSocket.receive(receive);
			
			if(receive.getLength()==3) {
				var signal = new String(receive.getData(), 0, 3, StandardCharsets.UTF_8); 
				if(signal.equals("ACK")) {
					System.out.println("ACK datagram");
					Client.registerAck();
				//	System.out.print("ACK");
					continue;
				} else if(signal.equals("ALV"))  {
					
					byte[] ALV = new byte[3]; 
					String alv = "ALV";
					ALV = alv.getBytes();
					DatagramPacket send = new DatagramPacket(ALV, ALV.length, receive.getAddress(), receive.getPort());
					ServerDatagramSocket.send(send);
					
					
					
					
					
				}
			}
			
			
			
			
			
			InetAddress Address = receive.getAddress();
			int port = receive.getPort();
			byte [] iii = new byte[1024]; 
			byte [] mess = new byte[1008];
			byte [] header = new byte[16];
			byte [] check = new byte[4]; 
			byte [] numberOfPackets = new byte[4];
			byte [] id = new byte[4];
			byte [] currentPacket = new byte[4];
			iii = receive.getData();
			
			
			
			
			
			
			
			
			mess = Arrays.copyOfRange(iii, 16, 1024);
			header = Arrays.copyOfRange(iii, 0, 16);
			id = Arrays.copyOfRange(header, 0, 4);
			check = Arrays.copyOfRange(header, 4, 8);
			currentPacket = Arrays.copyOfRange(header, 8, 12);
			numberOfPackets = Arrays.copyOfRange(header, 12, 16);
			
			// for (int i = 0; i<header.length; i++) {
			// System.out.println("hiif " + header[i] + " num: " + i);
			// }

			// System.out.println("Header length " + header.length);
			// System.out.println("checksum length " + check.length);
			// System.out.println("numberOfPac length " + numberOfPackets.length);

			// System.out.println("The checksum is: " + q.bytesToLong(check));
			// System.out.println("the total Number of Packets is: " +
			// q.ByteToInt(numberOfPackets));
			// System.out.println("last element of the header: " + header[header.length
			// -1]);

			// System.out.println("recevied packet length: " + iii.length);

			String clientmessage = new String(mess);
			// System.out.println("+----------------------------------------NEW-MESSAGE-------------------------------------+");
			System.out.print("\n >" + clientmessage + "\n");
			
			// System.out.println("the total Number of Packets is: " +
			// q.ByteToInt(numberOfPackets));

			// if (ack.getCheckSum()== q.bytesToLong(check)){

		//	System.out.println("Recevied checksum value: " + bytesToLong(check) + " it's the same checksum expected");

			// }

			// System.out.println(ack.getCheckSum());

		}catch (IOException ex){
			ex.printStackTrace(); 
			
		}

	}
}
}
