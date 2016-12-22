package Model;

public class AclRule {
	private String ipaddress;
	private String macaddress;
	private String protocol;
	private String akcia;
	public String getIpaddress() {
		return ipaddress;
	}
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}
	public String getMacaddress() {
		return macaddress;
	}
	public void setMacaddress(String macaddress) {
		this.macaddress = macaddress;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getAkcia() {
		return akcia;
	}
	public void setAkcia(String akcia) {
		this.akcia = akcia;
	}
}
