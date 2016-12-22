import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jnetpcap.ByteBufferHandler;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Ip4;

import Control.Main_Controller;
import View.View_Switch;

public class switchmain{

	public static void main(String[] args) {
		View_Switch view_Switch = new View_Switch();
		byte bajtik = 1;
		bajtik++;
		System.out.println("Hodnata bajtika je "+bajtik);
		Main_Controller main_Con = new Main_Controller(view_Switch);
	}

}

