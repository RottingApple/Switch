package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;

public class ProgramData {
	private static ProgramData instance = null;
	private static List<PcapIf> alldevs;
	private static StringBuilder errbuf;
	private int snaplen = 64 * 1024;
	private int flags = Pcap.MODE_PROMISCUOUS;
	private int timeout = 10;
	private ArrayList<StatisticsRecord> statsTable;
	private HashMap<String, SwitchingTableRecord> switchingTable;
	private HashMap<Integer, Byte> senderTable;
	private ArrayList<AclList> aclListmanager;
	public synchronized static ProgramData getinstance() {
		if (instance == null) {
			instance = new ProgramData();
			alldevs = new ArrayList<PcapIf>();
			errbuf = new StringBuilder();
			instance.statsTable = new ArrayList<StatisticsRecord>();
			instance.switchingTable = new HashMap<String, SwitchingTableRecord>();
			instance.senderTable = new HashMap<Integer, Byte>();
			instance.aclListmanager = new ArrayList<AclList>();
			int r = Pcap.findAllDevs(alldevs, errbuf);
			if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
				System.err.printf("Can't read list of devices, error is %s", errbuf.toString());
				System.exit(0);
				;
			}
			System.out.println("Network devices found:");
			int i = 0;
			for (PcapIf device : alldevs) {
				String description = (device.getDescription() != null) ? device.getDescription()
						: "No description available";
				System.out.printf("#%d: %s [%s]\n", i++, device.getName(), description);
			}
		}
		return instance;
	}

	public void setInstance(ProgramData instance) {
		this.instance = instance;
	}

	public List<PcapIf> getAlldevs() {
		return alldevs;
	}

	public void setAlldevs(List<PcapIf> alldevs) {
		this.alldevs = alldevs;
	}

	public StringBuilder getErrbuf() {
		return errbuf;
	}

	public void setErrbuf(StringBuilder errbuf) {
		this.errbuf = errbuf;
	}

	public int getSnaplen() {
		return snaplen;
	}

	public void setSnaplen(int snaplen) {
		this.snaplen = snaplen;
	}

	public int getFlags() {
		return flags;
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public synchronized HashMap<String, SwitchingTableRecord> getSwitchingTable() {
		synchronized (switchingTable) {
			return switchingTable;
		}
	}

	public synchronized void addItem(SwitchingTableRecord item) {
		synchronized (switchingTable) {
			switchingTable.put(item.getSrcMac(), item);
		}
	}

	public synchronized void writeTabletoGui(DefaultTableModel model) {
		synchronized (switchingTable) {
			for (int i = model.getRowCount() - 1; i >= 0; i--) {
				model.removeRow(i);
			}
			ArrayList<String> recordsToDelete = new ArrayList<String>();
			long currentTime = System.currentTimeMillis();
			//System.out.println("Velkost tabulky je " + switchingTable.values().size());
			for (SwitchingTableRecord record : switchingTable.values()) {
				if (currentTime - record.getAlive() < 60000)
					model.addRow(new Object[] { record.getSrcMac(), record.getPort(),
							60000 - (currentTime - record.getAlive()) });
				else
					recordsToDelete.add(record.getSrcMac());
			}
			for (String mac : recordsToDelete) {
				switchingTable.remove(mac);
				System.out.println("Mazem z prepinacej tabulky " + mac);
			}
		}
	}

	// public synchronized void removeOldRecords() {
	// synchronized (switchingTable) {
	// long currentTime = System.currentTimeMillis();
	// for (SwitchingTableRecord record : switchingTable.values()) {
	// if (currentTime - record.getAlive() > 60000) {
	// switchingTable.remove(record.getSrcMac());
	// }
	// System.out.println("SOurce mac: " + record.getSrcMac());
	// }
	// }
	// }

	public void setSwitchingTable(HashMap<String, SwitchingTableRecord> switchingTable) {
		synchronized (switchingTable) {
			this.switchingTable = switchingTable;
		}
	}

	public boolean isInTable(String srcMac) {
		synchronized (switchingTable) {
			if (switchingTable.containsKey(srcMac)) {
				// System.out.println("Nachadza sa v tabulke");
				return true;
			} else
				return false;
		}
	}

	public boolean isInTableAlive(String dstMac) {
		synchronized (switchingTable) {
			if (switchingTable.containsKey(dstMac)) {
				// System.out.println(System.currentTimeMillis() -
				// switchingTable.get(dstMac).getAlive() + " cas ktory
				// kontrolujem");
				if (System.currentTimeMillis() - switchingTable.get(dstMac).getAlive() < 60000)
					return true;
			}
			return false;
		}
	}

	public void isPortActual(Integer port, String srcMac) {
		synchronized (switchingTable) {
			SwitchingTableRecord record = switchingTable.get(srcMac);
			if (record.getPort() != port)
				record.setPort(port);
			record.setAlive(System.currentTimeMillis());
		}
	}

	public void resetTable() {
		synchronized (switchingTable) {
			switchingTable.clear();
		}
	}
	
	public void resetStatTable(){
		int j = statsTable.size();
		statsTable.clear();
		for (int i = 0; i < j; i++) {
			StatisticsRecord record = new StatisticsRecord(alldevs.get(i).getDescription());
			statsTable.add(record);
		}
	}
	public void updateStatTable(DefaultTableModel statModel) {
		// TODO Auto-generated method stub
		for (int i = statModel.getRowCount() - 1; i >= 0; i--) {
			statModel.removeRow(i);
		}
		for (StatisticsRecord record : statsTable) {
			statModel.addRow(new Object[] { record.getDevice(), record.getInARP() + "/" + record.getOutARP(),
					record.getInIP() + "/" + record.getOutIP(), record.getInICMP() + "/" + record.getOutICMP(),
					record.getInTCP() + "/" + record.getOutTCP(), record.getInUDP() + "/" + record.getOutUDP() });
		}
		// model.addRow(new Object[] { record.getSrcMac(), record.getPort(),
		// 60000 - (currentTime - record.getAlive()) });
	}

	public HashMap<Integer, Byte> getSenderTable() {
		return senderTable;
	}

	public void setSenderTable(HashMap<Integer, Byte> senderTable) {
		this.senderTable = senderTable;
	}

	public ArrayList<StatisticsRecord> getStatsTable() {
		return statsTable;
	}

	public void setStatsTable(ArrayList<StatisticsRecord> statsTable) {
		this.statsTable = statsTable;
	}

	public ArrayList<AclList> getAclListmanager() {
		return aclListmanager;
	}

	public void setAclListmanager(ArrayList<AclList> aclListmanager) {
		this.aclListmanager = aclListmanager;
	}

}
