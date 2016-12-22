package Control;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.jnetpcap.Pcap;

public class Nitka implements Runnable{
	Pcap pcap;
	public Nitka(Pcap pcap){
		this.pcap = pcap;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		byte[] a = new byte[14];
		System.out.println("Zapinam nitku");
		while(true){
			Arrays.fill(a, (byte) 0xff);
			ByteBuffer b = ByteBuffer.wrap(a);
			if (pcap.sendPacket(b) != Pcap.OK) {
				System.err.println(pcap.getErr());
				break;
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}