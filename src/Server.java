import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;
import java.util.zip.CRC32;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.math.BigInteger;
import java.util.*;



class Address {
	public final InetAddress address; 
	public final int port; 
	
	
	public Address(InetAddress address,int port) {
		this.address = address; 
		this.port = port; 
	}
	
}





public class Server {
	
	
	
	public Server() {
		super();
	}
	
	
	
	
	
	
	
	
	
	public int ByteToInt(byte[] intBytes){
		 int s = new BigInteger(intBytes).intValue();
		 return s;
	    
	}
	
	
	public long bytesToLong(byte[] bytes) {
	    ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
	    buffer.put(bytes);
	    buffer.flip();
	    return buffer.getLong();
	}
	
	
	public int checksum(byte[] data) {
		CRC32 crc = new CRC32();
		crc.update(data);
		return (int) crc.getValue();
	}
	
	
	
	public byte[] convertToByteArray(long value) {
	    
	    byte[] bytes = new byte[8];
	    ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
	    buffer.putLong(value);
	    return buffer.array();

	    
	}
	
	public int longToInt(long i) {
	     return Long.valueOf(i).intValue();
	}
	
	
	

	public static void main(String[] args)throws SocketException, IOException {
		
	//	Scanner S = new Scanner(System.in);
		Server q = new Server();
		DatagramSocket ServerDatagramSocket = new DatagramSocket(4545);
//		byte[] Senderbuffer1 = new byte[256]; 
//	    byte[] Receiverbuffer1 = new byte[256];
	    
//	    DatagramPacket re = new DatagramPacket(Receiverbuffer1, Receiverbuffer1.length);
//		ServerDatagramSocket.receive(re);
	//	String clientname = new String(re.getData());
		
	//	System.out.println(clientname + " Wants to message you, what is your name?");
//	    String Servermessag = S.nextLine();
//		Senderbuffer1 = Servermessag.getBytes();
//		InetAddress Add = re.getAddress();
//		int po = re.getPort();
//		DatagramPacket sen = new DatagramPacket(Senderbuffer1, Senderbuffer1.length, Add, po);
//		ServerDatagramSocket.send(sen);
		
		 
		 
		 ArrayList<Address> address = new ArrayList<Address>(); 
		
		while(true) {
			
			
			
			 byte[] Senderbuffer = new byte[1008]; 
			 byte[] Receiverbuffer = new byte[1024];
			
			DatagramPacket receive = new DatagramPacket(Receiverbuffer, Receiverbuffer.length);
			ServerDatagramSocket.receive(receive);
			
			if(receive.getLength()==3) {
				var signal = new String(receive.getData(), 0, 3, StandardCharsets.UTF_8);
				if(signal.equals("NEW")) {
					address.add(new Address(receive.getAddress(), receive.getPort()));
					for(Address a: address) {
						byte[] ALV = new byte[3]; 
						String alv = "ALV";
						ALV = alv.getBytes();
						DatagramPacket send = new DatagramPacket(ALV, ALV.length, a.address, a.port);
						ServerDatagramSocket.send(send);
						
						
					}
					
					address.clear();
					
					
					
					continue; 
					
				}
				else if(signal.equals("ALV")) {
					address.add(new Address(receive.getAddress(), receive.getPort()));
					continue;
				} else if (signal.equals("ACK")) {
					continue;
				}
			}
			
			InetAddress Address = receive.getAddress();
			int port = receive.getPort();
			/*
			boolean hasConnected = false;
			for(Address a: address) {
				if (a.address.equals(Address) && a.port == port) {
					hasConnected = true;
				}
			}
			
			if (!hasConnected) {
				address.add(new Address(Address, port));
			}
			*/
			
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
			
			
			
			String clientmessage = new String(mess).split("\0")[0];
			
			
			
			
			
			int checksumInteger = (int) q.checksum(mess);
			
			System.out.println("Packet id: " + q.ByteToInt(id));
			System.out.println("Expected Checksum: " + checksumInteger);
			System.out.println("The Checksum: " + q.ByteToInt(check));
			System.out.println("Current Packet: " + q.ByteToInt(currentPacket));
			System.out.println("Total Number Of Packets: " + q.ByteToInt(numberOfPackets));
			
			
			
			if( checksumInteger == q.ByteToInt(check)) {
				byte[] ACK = new byte[3]; 
				String ack = "ACK";
				ACK = ack.getBytes();
				DatagramPacket send = new DatagramPacket(ACK, ACK.length, Address, port);
				ServerDatagramSocket.send(send);
				System.out.println("Checksum matched"); 
			} else {
				System.out.println("The checksum is different");
			}
			
			
		//	for (int i = 0; i<header.length; i++) {
		//		System.out.println("hiif " + header[i] + " num: " + i);
		//	}
			
			
		//	System.out.println("Header length " + header.length);
		//	System.out.println("checksum length " + check.length);
		//	System.out.println("numberOfPac length " + numberOfPackets.length);
			
			 
			
		//	System.out.println("The checksum is: " + q.bytesToLong(check));
		//	System.out.println("the total Number of Packets is: " + q.ByteToInt(numberOfPackets));
		//	System.out.println("last element of the header: " + header[header.length -1]);
			
		//	System.out.println("recevied packet length: " + iii.length);
			
			
		//	String clientmessage = new String(mess).split("\0")[0];
		//	System.out.println("+----------------------------------------NEW-MESSAGE-------------------------------------+");
			clientmessage = Address.getHostName() + "|" + port + " :" + clientmessage;
			System.out.print( "\n"  + clientmessage + "\n");
		//	System.out.println("the total Number of Packets is: " + q.ByteToInt(numberOfPackets));
			
       //     if (ack.getCheckSum()== q.bytesToLong(check)){
				
		//		System.out.println("Recevied checksum value: " + q.bytesToLong(check) + " it's the same checksum expected");
				
		//	}
			
        //    System.out.println(ack.getCheckSum());
			
		//	byte[] udpHeader = new byte[16];
		//	byte[] checksum = new byte[8]; 
		//	byte[] numberOfPac = new byte[1];
			
	//		System.out.print("\n" + Servermessag + ": ");
			
			String Servermessage = clientmessage;
			Senderbuffer = Servermessage.getBytes();
			
			byte[] senderBuffer = new byte[1008];
			System.arraycopy(Senderbuffer, 0, senderBuffer, 0, Senderbuffer.length);
			
		 
		//	checksum = q.convertToByteArray(q.checksum(Servermessage));
		//	ack.storeCheckSum(q.checksum(clientmessage));
			
		//	numberOfPac = numberOfPackets; 
			
			
		//	System.arraycopy(checksum, 0, udpHeader, 0, checksum.length);
		//	System.arraycopy(numberOfPac, 0, udpHeader, checksum.length, numberOfPac.length);
			
			
          ByteBuffer headd = ByteBuffer.allocate(16);
			
		headd.putInt(q.ByteToInt(id));
		headd.putInt(q.checksum(senderBuffer));
		headd.putInt(q.ByteToInt(currentPacket));
		headd.putInt(q.ByteToInt(numberOfPackets));
			
			
			
			byte[] newSenderbuffer = new byte[1024];

			System.arraycopy(headd.array(), 0, newSenderbuffer, 0, headd.array().length);
			System.arraycopy(Senderbuffer, 0, newSenderbuffer, headd.array().length, Senderbuffer.length);
			
			
			
			
			
            
			
			
			
			for(Address a: address) {
				DatagramPacket send = new DatagramPacket(newSenderbuffer, newSenderbuffer.length, a.address, a.port);
				ServerDatagramSocket.send(send);
			}
			
			
			
			
			
//			DatagramPacket send = new DatagramPacket(newSenderbuffer, newSenderbuffer.length, Address, port);
//			ServerDatagramSocket.send(send);
			
		//	System.out.println("sent checksum value: " + q.ByteToInt(check));
			
			if(Servermessage.equalsIgnoreCase("End")) {
				System.out.println("Connection closed by server");
			break; 
			}
			
			
			
			
		}
		
		ServerDatagramSocket.close();
		
		//S.close();
	}

}
