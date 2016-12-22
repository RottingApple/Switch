package Control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapBpfProgram;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Ip4;

import Model.AclList;
import Model.AclRule;
import Model.ProgramData;
import Model.StatisticsRecord;
import Model.SwitchingTableRecord;
import View.View_Switch;

public class Main_Controller {
	ArrayList<Thread> activeSniffers;
	View_Switch viewSwitch;

	public Main_Controller(View_Switch viewSwitch) {
		activeSniffers = new ArrayList<Thread>();
		this.viewSwitch = viewSwitch;
		try {
			this.startingComunication();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int numberofDevices = ProgramData.getinstance().getAlldevs().size();
		viewSwitch.setResetTableListener(new ResetMacTable(ProgramData.getinstance().getSwitchingTable()));
		viewSwitch.setResetStatisticsListener(new ResetStatTable());
		//viewSwitch.setFilterListener(new AddFilter( viewSwitch,ProgramData.getinstance().getAclListmanager()));
	}

	// public void Testing(){
	// // TODO Auto-generated method stub
	// /* List<PcapIf> alldevs = new ArrayList<PcapIf>();
	// StringBuilder errbuf = new StringBuilder();
	// int r = Pcap.findAllDevs(alldevs, errbuf);
	// if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
	// System.err.printf("Can't read list of devices, error is %s",
	// errbuf.toString());
	// return;
	// }
	// System.out.println("Network devices found:");
	// int i = 0;
	// for (PcapIf device : alldevs) {
	// String description = (device.getDescription() != null) ? device
	// .getDescription() : "No description available";
	// System.out.printf("#%d: %s [%s]\n", i++, device.getName(),
	// description);
	// }
	// PcapIf device = alldevs.get(1);
	// System.out.printf("\nChoosing '%s' on your behalf:\n",
	// (device.getDescription() != null) ? device.getDescription()
	// : device.getName());
	// int snaplen = 64 * 1024;
	// int flags = Pcap.MODE_PROMISCUOUS;
	// int timeout = 10 * 1000;*/
	// ProgramData data = ProgramData.getinstance();
	// Pcap pcap;
	// pcap = Pcap.openLive(data.getAlldevs().get(1).getName(),
	// data.getSnaplen(), data.getFlags(), data.getTimeout(), data.getErrbuf());
	// PcapPacketHandler<String> jpacketHandler = new
	// PcapPacketHandler<String>() {
	// public void nextPacket(PcapPacket packet, String user) {
	// byte[] data = packet.getByteArray(0, packet.size());
	// byte[] sIP = new byte[4];
	// byte[] dIP = new byte[4];
	// byte[] smac = new byte[6];
	// Ethernet et = new Ethernet();
	// Ip4 ip = new Ip4();
	// //System.out.println("Nasiel som tolkoto
	// hlaviciek"+packet.getHeaderCount());
	// System.out.println(packet.toHexdump());
	// if (packet.hasHeader(et) == true){
	// System.out.println(et.toHexdump());
	// //System.out.println("Dobre je");
	// smac = et.source();
	// System.out.println("source mac je
	// "+org.jnetpcap.packet.format.FormatUtils.mac(smac));
	// }
	// if (packet.hasHeader(ip) == false) {
	// return;
	// }
	// sIP=ip.source();
	// dIP=ip.destination();
	//
	// String sourceIP = org.jnetpcap.packet.format.FormatUtils.ip(sIP);
	// String destinationIP = org.jnetpcap.packet.format.FormatUtils.ip(dIP);
	//
	// System.out.println("srcIP=" + sourceIP +
	// " dstIP=" + destinationIP +
	// " caplen=" + packet.getCaptureHeader().caplen());
	//
	// }
	// };
	// pcap.loop(5, jpacketHandler, "jNetPcap");
	// /*byte[] a = new byte[14];
	// Arrays.fill(a, (byte) 0xff);
	// ByteBuffer b = ByteBuffer.wrap(a);
	// if (pcap.sendPacket(b) != Pcap.OK) {
	// System.err.println(pcap.getErr());
	// } */
	// pcap.close();
	// }
	public void startingComunication() throws IOException {
		ProgramData pData = ProgramData.getinstance();
		for (int i = 0; i < pData.getAlldevs().size(); i++) {
			StatisticsRecord record = new StatisticsRecord(pData.getAlldevs().get(i).getDescription());
			AclList list1 = new AclList(i);
			AclList list2 = new AclList(i);
			pData.getAclListmanager().add(list1);
			pData.getAclListmanager().add(list2);
			Thread newThread = new Thread(new SwitchPort(i,record,pData.getAlldevs().get(i).getHardwareAddress(),list1));
			pData.getStatsTable().add(record);
			newThread.start();
			activeSniffers.add(newThread);
			
		}
		Thread guiThread = new Thread(new WriteToGui());
		guiThread.start();
	//	Thread nitka = new Thread(new Nitka(Pcap.openLive(pData.getAlldevs().get(2).getName(), pData.getSnaplen(), pData.getFlags(),
	//			pData.getTimeout(), pData.getErrbuf())));
		//nitka.start();
		// for (Thread thread : activeSniffers) {
		// thread.interrupt();
		// }
	}

	public class WriteToGui implements Runnable {
		DefaultTableModel model;

		// public WriteToGui(DefaultTableModel model){
		// this.model = model;
		//
		// }
		@Override
		public void run() {
			// TODO Auto-generated method stub
			ProgramData pData = ProgramData.getinstance();
			while (true) {
				try {
					Thread.sleep(1000);
					pData.writeTabletoGui(viewSwitch.getModel());
					pData.updateStatTable(viewSwitch.getStatModel());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	public class ResetMacTable implements ActionListener{
		private HashMap<String,SwitchingTableRecord>macTable;
		public ResetMacTable(HashMap<String,SwitchingTableRecord>macTable){
			this.macTable = macTable;
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			synchronized(macTable){
				macTable.clear();
			}
		}
		
	}
	
	public class ResetStatTable implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			ProgramData pdata = ProgramData.getinstance();
			pdata.resetStatTable();
		}
		
	}
	
	public class AddFilter implements ActionListener{
		View_Switch view_manager;
		ArrayList<AclList>aclListManager;
		public AddFilter(View_Switch view_manager,ArrayList<AclList>aclListManager){
			this.view_manager = view_manager;
			this.aclListManager = aclListManager;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stud
			String macAddress = null,ipAddress = null,protocol,action;
			int portNumber;
			boolean in,out;
			portNumber = Integer.parseInt(view_manager.getTextfPortNumber().getText());
			if(view_manager.getCheckBAllow().isSelected())
				action = new String("allow");
			else
				action = new String("deny");
			if(view_manager.getCheckbIn().isSelected())
				in = true;
			else
				in = false;
			if(view_manager.getCheckbOut().isSelected())
				out = true;
			else
				out = false;
			if(view_manager.getTxtFIPAddress().getText().length() != 0)
				ipAddress = view_manager.getTxtFIPAddress().getText();
			if(view_manager.getTxtfMacAddress().getText().length() != 0)
				macAddress = view_manager.getTxtfMacAddress().getText();
			protocol = view_manager.getCmbBProtocol().getSelectedItem().toString();
			if(in){
			AclList aclList = aclListManager.get(portNumber*2);
			AclRule aclRule = new AclRule();
			if(ipAddress != null)
				aclRule.setIpaddress(ipAddress);
			if(macAddress != null)
				aclRule.setMacaddress(macAddress);
			aclRule.setProtocol(protocol);
			aclRule.setAkcia(action);
			}
			System.out.println("Udaje ktore vlozil uzivatel su: "+macAddress+ipAddress+protocol+portNumber+action);
			
		}
		
		
	}
}
