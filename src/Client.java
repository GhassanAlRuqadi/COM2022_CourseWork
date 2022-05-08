import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;
import java.util.zip.CRC32;
import java.nio.ByteBuffer;
import java.math.BigInteger;
import java.util.*;

public class Client {
	
	static int SendingPort = 4545;

	int numberOfPackets = 0;
	long SentChecksum;

	public Client() {
		super();
	}
	
	private static int _waitingForAcks = 0;

    public static synchronized void registerAck() {
        if (isWaitingForAck()) _waitingForAcks--;
    }

    public static synchronized void requestAck() {
        _waitingForAcks++;
    }

    public static synchronized boolean isWaitingForAck() {
        return _waitingForAcks > 0;
    }
	
	

	public void storeCheckSum(long sss) {

		this.SentChecksum = sss;

	}

	public long getCheckSum() {

		return SentChecksum;
	}

	public void increaseNumberOfPackets() {

		this.numberOfPackets += 1;

	}

	public int numberOfPackets() {

		return this.numberOfPackets;
	}

	public int checksum(byte[] data) {
		CRC32 crc = new CRC32();
		crc.update(data);
		return (int) crc.getValue();
	}
	
	
	public int longToInt(long i) {
	     return Long.valueOf(i).intValue();
	}

	public byte[] convertToByteArray(long value) {

		byte[] bytes = new byte[8];
		ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
		buffer.putLong(value);
		return buffer.array();

	}

	public byte[] intToArray(int i) {
		BigInteger bigInt = BigInteger.valueOf(i);
		return bigInt.toByteArray();
	}

	public int ByteToInt(byte[] intBytes) {
		int s = new BigInteger(intBytes).intValue();
		return s;

	}

	public long bytesToLong(byte[] bytes) {
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.put(bytes);
		buffer.flip();
		return buffer.getLong();
	}

	public void SendAndReceive(int port) {

	}

	public static void main(String[] args) throws SocketException, IOException {

		Client a = new Client();

		//InetAddress Address = InetAddress.getByName("localhost");
		 InetAddress Address = InetAddress.getByName("10.77.12.22");
		// InetAddress Address = InetAddress.getByName("10.77.115.174");
		Scanner S = new Scanner(System.in);
		DatagramSocket clientDatagramSocket = new DatagramSocket();
		byte[] Senderbuffer1 = new byte[256];
		byte[] Receiverbuffer1 = new byte[256];

		System.out.println("Enter a message...");
		
		byte[] NEW = new byte[3]; 
		String ne = "NEW";
		NEW = ne.getBytes();
		System.out.println("Sending NEW segnal..");
		DatagramPacket send = new DatagramPacket(NEW, NEW.length, Address, SendingPort);
		clientDatagramSocket.send(send);
		
		
		
		byte[] alvv = new byte[3]; 
		DatagramPacket receive = new DatagramPacket(alvv, alvv.length);
		clientDatagramSocket.receive(receive);
		System.out.println("Received " + new String(alvv));
		
		
		byte[] ALV = new byte[3]; 
		String alv = "ALV";
		ALV = alv.getBytes();
		System.out.println("Sending ALV segnal..");
		DatagramPacket senn = new DatagramPacket(ALV, ALV.length, Address, SendingPort);
		clientDatagramSocket.send(senn);
		
		
		
		
		
//		String name = S.nextLine();
//		Senderbuffer1 = name.getBytes();
//		DatagramPacket send = new DatagramPacket(Senderbuffer1, Senderbuffer1.length, Address, 9992);
//		clientDatagramSocket.send(send);
//		a.increaseNumberOfPackets();
//
//		DatagramPacket rrr = new DatagramPacket(Receiverbuffer1, Receiverbuffer1.length);
//		clientDatagramSocket.receive(rrr);
//		String clientname = new String(rrr.getData());
//		a.increaseNumberOfPackets();
//		System.out.print("\nOk " + name + " you can send messages to " + clientname + " now!\n");
		// System.out.println(Address);
		// System.out.println("Packets: " + a.numberOfPackets());

		

		
		ClientRecevier ca = new ClientRecevier(clientDatagramSocket);
		ca.start();
		
		int currentPackett = 0;
		
		while (true) {
			byte[] Senderbuffer = new byte[1008];
			
			
			Random rand = new Random();
			
			int id = rand.nextInt(1000); 
			
			
			

		//	System.out.print("\n" + name + " :");
			
			String clientmessage = S.nextLine();
//			Senderbuffer = clientmessage.getBytes();
			System.arraycopy(clientmessage.getBytes(), 0, Senderbuffer, 0, clientmessage.getBytes().length);

		//	checksum = a.convertToByteArray(a.checksum(clientmessage));
			
		 
			
	//		int checksumInt = (int)a.checksum(clientmessage);
	//		int checksumInt= Long.valueOf(a.checksum(clientmessage)).intValue();
	//		int checksumInt = Integer.parseInt(String.valueOf(a.checksum(clientmessage)));
			int checksumInt = (int) a.checksum(Senderbuffer);
			
			

			// a.storeCheckSum(a.checksum(clientmessage));

			a.increaseNumberOfPackets();

			// System.out.println("checksum: " + a.checksum(clientmessage));
			
			
			
			ByteBuffer headd = ByteBuffer.allocate(16);
			
			headd.putInt(id);
			
			
			headd.putInt((int) a.checksum(Senderbuffer));
			
			
			headd.putInt(currentPackett++);
			headd.putInt(a.numberOfPackets());
			
			
			
			
			/*
			
			System.arraycopy(Id, 0, temp1, 0, Id.length);
			System.arraycopy(checksum, 0, temp1, Id.length, checksum.length);
			
			
			
			System.arraycopy(currentPacket, 0, temp2, 0, currentPacket.length);
			System.arraycopy(numberOfPac, 0, temp2, currentPacket.length, numberOfPac.length);
			
			
			

			System.arraycopy(temp1, 0, udpHeader, 0, temp1.length);
			System.arraycopy(temp2, 0, udpHeader, temp1.length, temp2.length);

			// udpHeader = (new String(checksum, "l1") + new String(numberOfPac,
			// "l1")).getBytes("l1");
			
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        outputStream.write(Id);
	        outputStream.write(checksum);
	        outputStream.write(currentPacket);
	        outputStream.write(numberOfPac);
	        
	        
	        head = outputStream.toByteArray();
	        
	        
	        
	        
	        
	        
	        
*/
			
			
			
			
			
			
			
			
			
			byte[] newSenderbuffer = new byte[1024];

			System.arraycopy(headd.array(), 0, newSenderbuffer, 0, headd.array().length);
			System.arraycopy(Senderbuffer, 0, newSenderbuffer, headd.array().length, Senderbuffer.length);
			
			
			
			
			
			
			
			
			
			
			
			

			// newSenderbuffer = (new String(udpHeader, "l1") + new String(Senderbuffer,
			// "l1")).getBytes("l1");

			/*
			 * 
			 * for(int i = 0; i<numberOfPac.length; i++) { System.out.println("packets : " +
			 * numberOfPac[i]); }
			 * 
			 * for (int i = 0; i<udpHeader.length; i++) { System.out.println("hiif " +
			 * udpHeader[i] + " num: " + i); }
			 */

			// System.out.println("Header length " + udpHeader.length);
			// System.out.println("checksum length " + checksum.length);
			// System.out.println("numberOfPac length " + numberOfPac.length);

			// System.out.println("Recevied length " + newSenderbuffer.length);
			
		//	for (int i = 0; i<headd.array().length; i++) { System.out.println("hiif " +
			//		headd.array()[i] + " num: " + i); }
			
			System.out.println("\n");
			
			
		//	System.out.println(checksumInt);
		//	System.out.println(a.checksum(clientmessage));
			
			

			
			DatagramPacket sen = new DatagramPacket(newSenderbuffer, newSenderbuffer.length, Address, SendingPort);
			clientDatagramSocket.send(sen);
			
			requestAck();
			
			System.out.println("wait for Ack");
			while (Client.isWaitingForAck()) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ignored) {}
            }
			
			System.out.println("\nReceived ACK");
			
			
		//	System.out.println(_waitingForAcks);

			/*
			 * send checksum value:
			 */

	//		System.out.println("sent checksum value: " + a.bytesToLong(checksum));

			if (clientmessage.equalsIgnoreCase("End")) {
				System.out.println("Connection closed");
				break;
			}
			
			
			// System.out.println("the total Number of Packets is: " +
			// a.ByteToInt(numberOfPac));
			// System.out.println("The checksum is: " + a.bytesToLong(checksum));

			// for (int i = 0; i<udpHeader.length; i++) {
			// System.out.println("hiif " + udpHeader[i] + " num: " + i);
			// }

			/*
			 * 
			 * DatagramPacket receive = new DatagramPacket(Receiverbuffer,
			 * Receiverbuffer.length); clientDatagramSocket.receive(receive);
			 * 
			 * byte [] iii = new byte[1024]; byte [] mess = new byte[1008]; byte [] header =
			 * new byte[16]; byte [] check = new byte[8]; byte [] numberOfPackets = new
			 * byte[1]; iii = receive.getData();
			 * 
			 * 
			 * for(int i = 0; i<iii.length; i++ ) {
			 * 
			 * 
			 * if(i < 15 || i == 15) {
			 * 
			 * header[i] = iii[i];
			 * 
			 * }
			 * 
			 * 
			 * if(i > 15) {
			 * 
			 * mess[i-16] = iii[i];
			 * 
			 * }
			 * 
			 * 
			 * 
			 * }
			 * 
			 * 
			 * 
			 * 
			 * for(int i = 0; i<header.length; i++) {
			 * 
			 * if(i < 8 ) {
			 * 
			 * check[i] = header[i];
			 * 
			 * }
			 * 
			 * if (i == 8) {
			 * 
			 * 
			 * numberOfPackets[i-8] = header[i];
			 * 
			 * }
			 * 
			 * 
			 * 
			 * }
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * String servermessage = new String(mess); a.increaseNumberOfPackets();
			 * 
			 * numberOfPac = a.intToArray(a.numberOfPackets()); //
			 * System.out.println("The checksum is: " + a.bytesToLong(checksum)); //
			 * System.out.println("the total Number of Packets is: " +
			 * a.ByteToInt(numberOfPac));
			 * 
			 * 
			 * // System.out.println(
			 * "+----------------------------------------NEW-MESSAGE-------------------------------------+"
			 * ); System.out.print("\n" + clientname + ": "+ servermessage + "\n"); //
			 * System.out.println("the total Number of Packets is: " +
			 * a.ByteToInt(numberOfPac));
			 * 
			 * // if (ack.getCheckSum() == a.bytesToLong(check)){
			 * 
			 * System.out.println("Recevied checksum value: " + a.bytesToLong(check) +
			 * " it's the same checksum expected");
			 * 
			 * // } // System.out.println(ack.getCheckSum());
			 * 
			 * 
			 * }
			 * 
			 */

		}

		clientDatagramSocket.close();
		S.close();
	}

}
