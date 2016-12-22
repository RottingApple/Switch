package Model;

public class SwitchingTableRecord {
	private int port;
	private String srcMac;
	private long alive;

	public SwitchingTableRecord(int port, String srcMac) {
		this.port = port;
		this.srcMac = srcMac;
		this.alive = System.currentTimeMillis();
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getSrcMac() {
		return srcMac;
	}

	public void setSrcMac(String srcMac) {
		this.srcMac = srcMac;
	}

	public long getAlive() {
		return alive;
	}

	public void setAlive(long alive) {
		this.alive = alive;
	}

}
