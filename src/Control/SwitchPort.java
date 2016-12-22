package Control;

import java.awt.SecondaryLoop;
import java.util.Arrays;

import org.jnetpcap.Pcap;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Arp;
import org.jnetpcap.protocol.network.Icmp;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;

import Model.AclList;
import Model.ProgramData;
import Model.StatisticsRecord;
import Model.SwitchingTableRecord;
import View.View_Switch;

public class SwitchPort implements Runnable {
	private int deviceNumber;
	private ProgramData pData;
	private StatisticsRecord statRecord;
	private byte[] deviceMacAddress;
	private AclList acllist;
	boolean running;

	public SwitchPort(int deviceNumber, StatisticsRecord statRecord, byte[] deviceMacAddress,AclList acllist) {
		running = true;
		this.pData = ProgramData.getinstance();
		this.deviceNumber = deviceNumber;
		this.statRecord = statRecord;
		this.deviceMacAddress = deviceMacAddress;
		this.acllist = acllist;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Pcap pcap = Pcap.openLive(pData.getAlldevs().get(deviceNumber).getName(), pData.getSnaplen(), pData.getFlags(),
				pData.getTimeout(), pData.getErrbuf());
		pcap.setDirection(1);
		//System.out.println("Zapinam sniffer na: " + pData.getAlldevs().get(deviceNumber).getName());
		while (running) {
			PcapPacketHandler<String> jPacketHandler = new PcapPacketHandler<String>() {
				public void nextPacket(PcapPacket packet, String user) {
					int numofTransportPorts = 0;
					int hash = packet.toHexdump().hashCode();
					//System.out.println("Dostal som paket na porte "+deviceNumber);
					//System.out.println(packet.toHexdump());
					//System.out.println("Hash ktory som dostal je "+hash);
					 //System.out.println("Tabulka odoslanych paketov : "+pData.getSenderTable().keySet());
					Ethernet et = new Ethernet();
					Arp arp = new Arp();
					if (!pData.getSenderTable().containsKey(hash)) {
						// System.out.println("Hash paketu je : " + hash);
						if (packet.hasHeader(et) ) {
							if (!(Arrays.equals(deviceMacAddress, et.source()) || Arrays.equals(deviceMacAddress, et.destination()))) {
								String srcMac = FormatUtils.mac(et.source());
								String dstMac = FormatUtils.mac(et.destination());
								if (pData.isInTable(srcMac)) {
									pData.isPortActual(deviceNumber, srcMac);
								} else {
									SwitchingTableRecord newRecord = new SwitchingTableRecord(deviceNumber, srcMac);
									// System.out.println("Zapisujem do
									// tabulky");
									pData.addItem(newRecord);
								}
								updateStat(deviceNumber, true, packet);
							//	if(dstMac!="ff.ff.ff.ff.ff.ff")
							//		System.out.println("Dostal som broadcastovy ramec");
								if (pData.isInTableAlive(dstMac) && dstMac!="ff.ff.ff.ff.ff.ff" ) {
									if(pData.getSwitchingTable().get(dstMac).getPort() != deviceNumber){
										numofTransportPorts = 1;
								//		System.out.println("Posielam na port "+pData.getSwitchingTable().get(dstMac).getPort()+" dst mac je "+dstMac);
										pData.getSenderTable().put(hash, (byte) numofTransportPorts);
										int sendingPort = pData.getSwitchingTable().get(dstMac).getPort();
										sendPackettoPort(packet, pData, sendingPort, hash);
										updateStat(sendingPort, false, packet);
									}
								} else {
									numofTransportPorts = pData.getAlldevs().size() - 1;
								//	System.out.println("Posielam na ostatne porty, dst mac je "+dstMac);
									pData.getSenderTable().put(hash, (byte) numofTransportPorts);
									for (int i = 0; i < pData.getAlldevs().size(); i++) {
										if(i != deviceNumber){
											sendPackettoPort(packet, pData, i, hash);
											updateStat(i, false, packet);
										}
									}
								}

							}else
								System.out.println("Dostal som paket ktory sa mna netyka");//tento bracket sa maze
						}
					} else {
						if (pData.getSenderTable().get(hash) != 1)
							pData.getSenderTable().put(hash, (byte) (pData.getSenderTable().get(hash) - 1));
						else {
						//	System.out.println("Tabulka: " + pData.getSenderTable().keySet()+" hash ktory mazem" +hash);
							pData.getSenderTable().remove(hash);
						//	System.out.println("Prijal som paket ktory som odoslal a mazem ho a nepreposlem ho dalej");
						}				
					}

				}
			};
			 //System.out.println("Idem pocuvat");
			pcap.loop(1, jPacketHandler, "jNetCap");
			if (Thread.currentThread().isInterrupted()) {
				System.out.println("Bola som interuptnuta");
				pcap.close();
				running = false;
			}
		}
	}

	public void sendPackettoPort(PcapPacket packet, ProgramData pData, int portNumber,int hash) {
		Pcap pcap = Pcap.openLive(pData.getAlldevs().get(portNumber).getName(), pData.getSnaplen(), pData.getFlags(),pData.getTimeout(), pData.getErrbuf());
		try{
		if (pcap.sendPacket(packet) != Pcap.OK) {
			System.out.println("Poslanie paketu zlyhalo");
			System.err.println(pcap.getErr());
			//if (pData.getSenderTable().get(hash) != 1)
			//	pData.getSenderTable().put(hash, (byte) (pData.getSenderTable().get(hash) - 1));
			//else 
				pData.getSenderTable().remove(hash);
		}
		}catch(Exception e){
			System.out.println("Skoro si vytiahol kablik ");
		}
		pcap.close();
	}

	public void updateStat(int deviceNumber, boolean direction, PcapPacket packet) {
		Ip4 ip = new Ip4();
		Icmp icmp = new Icmp();
		Tcp tcp = new Tcp();
		Udp udp = new Udp();
		Arp arp = new Arp();
		while(pData.getStatsTable().size() < deviceNumber){
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		StatisticsRecord record = pData.getStatsTable().get(deviceNumber);
		if (direction) {
			if (packet.hasHeader(ip))
				record.setInIP(record.getInIP() + 1);
			if (packet.hasHeader(icmp))
				record.setInICMP(record.getInICMP() + 1);
			if (packet.hasHeader(udp))
				record.setInUDP(record.getInUDP() + 1);
			if (packet.hasHeader(tcp))
				record.setInTCP(record.getInTCP() + 1);
			if (packet.hasHeader(arp))
				record.setInARP(record.getInARP() + 1);
		} else {
			if (packet.hasHeader(ip))
				record.setOutIP(record.getOutIP() + 1);
			if (packet.hasHeader(icmp))
				record.setOutICMP(record.getOutICMP() + 1);
			if (packet.hasHeader(udp))
				record.setOutUDP(record.getOutUDP() + 1);
			if (packet.hasHeader(tcp))
				record.setOutTCP(record.getOutTCP() + 1);
			if (packet.hasHeader(arp))
				record.setOutARP(record.getOutARP() + 1);
		}
	}
}
