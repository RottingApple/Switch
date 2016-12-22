package Model;

public class StatisticsRecord {
	private String device;
	private int inTCP;
	private int inUDP;
	private int inICMP;
	private int inHTTP;
	private int inARP;
	private int outTCP;
	private int outUDP;
	private int outICMP;
	private int outHTTP;
	private int inIP;
	private int outIP;
	private int outARP;

	public StatisticsRecord(String device) {
		this.device = device;
		this.inTCP = 0;
		this.inUDP = 0;
		this.inICMP = 0;
		this.inHTTP = 0;
		this.inARP = 0;
		this.outTCP = 0;
		this.outUDP = 0;
		this.outICMP = 0;
		this.outHTTP = 0;
		this.outARP = 0;
		this.inIP = 0;
		this.outIP = 0;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public int getInTCP() {
		return inTCP;
	}

	public void setInTCP(int inTCP) {
		this.inTCP = inTCP;
	}

	public int getInUDP() {
		return inUDP;
	}

	public void setInUDP(int inUDP) {
		this.inUDP = inUDP;
	}

	public int getInICMP() {
		return inICMP;
	}

	public void setInICMP(int inICMP) {
		this.inICMP = inICMP;
	}

	public int getInHTTP() {
		return inHTTP;
	}

	public void setInHTTP(int inHTTP) {
		this.inHTTP = inHTTP;
	}

	public int getInARP() {
		return inARP;
	}

	public void setInARP(int inARP) {
		this.inARP = inARP;
	}

	public int getOutTCP() {
		return outTCP;
	}

	public void setOutTCP(int outTCP) {
		this.outTCP = outTCP;
	}

	public int getOutUDP() {
		return outUDP;
	}

	public void setOutUDP(int outUDP) {
		this.outUDP = outUDP;
	}

	public int getOutICMP() {
		return outICMP;
	}

	public void setOutICMP(int outICMP) {
		this.outICMP = outICMP;
	}

	public int getOutHTTP() {
		return outHTTP;
	}

	public void setOutHTTP(int outHTTP) {
		this.outHTTP = outHTTP;
	}

	public int getOutARP() {
		return outARP;
	}

	public void setOutARP(int outARP) {
		this.outARP = outARP;
	}

	public int getInIP() {
		return inIP;
	}

	public void setInIP(int inIP) {
		this.inIP = inIP;
	}

	public int getOutIP() {
		return outIP;
	}

	public void setOutIP(int outIP) {
		this.outIP = outIP;
	}

}
