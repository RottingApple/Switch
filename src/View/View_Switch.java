package View;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

public class View_Switch {
	private JFrame switch_Frame;
	private String[] columnNames = {"MAC Address","Port","Timer"};
	private String[] columnNames2 = {"Device","ARP","IP","ICMP","TCP","UDP"};
	private DefaultTableModel model;
	private DefaultTableModel statModel;
	private JTable table;
	private JTable table2;
	private JButton btnResetTable;
	private JTextField txtFIPAddress;
	private JCheckBox checkBAllow;
	private JCheckBox checkBForbid;
	private JComboBox<String> cmbBProtocol;
	private JCheckBox checkbIn;
	private JCheckBox checkbOut;
	private JButton btnFilter;
	private JTextField txtfMacAddress;
	public JTextField getTxtfMacAddress() {
		return txtfMacAddress;
	}
	public void setTxtfMacAddress(JTextField txtfMacAddress) {
		this.txtfMacAddress = txtfMacAddress;
	}
	private JTextField textfPortNumber;
	private JButton btnResetStats;
	public View_Switch(){
		super();
		switch_Frame = new JFrame("Switch Table");
		switch_Frame.setSize(1000,700);
		switch_Frame.getContentPane().setLayout(null);
		switch_Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		switch_Frame.setLocation(dim.width/2-switch_Frame.getSize().width/2, dim.height/2-switch_Frame.getSize().height/2);
		JScrollPane scrp_table = new JScrollPane();
		scrp_table.setSize(330,250);
		scrp_table.setLocation(650,40);
		switch_Frame.getContentPane().add(scrp_table);
		model = new DefaultTableModel();
		for (int i = 0; i < columnNames.length; i++) {
			model.addColumn(columnNames[i]);
		}
		table = new JTable(model);
		scrp_table.setViewportView(table);
		table.setPreferredScrollableViewportSize(new Dimension(	250,100));
		table.setFillsViewportHeight(true);
		
		JScrollPane scrp_table2 = new JScrollPane();
		scrp_table2.setSize(500,250);
		scrp_table2.setLocation(480,350);
		switch_Frame.getContentPane().add(scrp_table2);
		statModel = new DefaultTableModel();
		for (int i = 0; i < columnNames2.length; i++) {
			statModel.addColumn(columnNames2[i]);
		}
		table2 = new JTable(statModel);
		scrp_table2.setViewportView(table2);
		table2.setPreferredScrollableViewportSize(new Dimension(250,100));
		table2.setFillsViewportHeight(true);
		
		JLabel lblSwitchMacTable = new JLabel("Switch Mac Table");
		lblSwitchMacTable.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblSwitchMacTable.setBounds(836, 11, 144, 18);
		switch_Frame.getContentPane().add(lblSwitchMacTable);
		
		btnResetTable = new JButton("Reset Table");
		btnResetTable.setBounds(650, 11, 110, 23);
		switch_Frame.getContentPane().add(btnResetTable);
		
		txtFIPAddress = new JTextField();
		txtFIPAddress.setBounds(10, 44, 153, 31);
		switch_Frame.getContentPane().add(txtFIPAddress);
		txtFIPAddress.setColumns(10);
		
		checkBAllow = new JCheckBox("Allow");
		checkBAllow.setBounds(169, 37, 61, 23);
		switch_Frame.getContentPane().add(checkBAllow);
		
		 checkBForbid = new JCheckBox("Forbid");
		checkBForbid.setBounds(169, 64, 61, 23);
		switch_Frame.getContentPane().add(checkBForbid);
		
		JLabel lblAddress = new JLabel("Enter IP Address");
		lblAddress.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblAddress.setBounds(10, 20, 127, 14);
		switch_Frame.getContentPane().add(lblAddress);
		
		 cmbBProtocol = new JComboBox();
		cmbBProtocol.setBounds(10, 193, 153, 29);
		switch_Frame.getContentPane().add(cmbBProtocol);
		cmbBProtocol.addItem("ARP");
		cmbBProtocol.addItem("HTTP");
		cmbBProtocol.addItem("ICMP");
		cmbBProtocol.addItem("FTP");
		JLabel lblProtocol = new JLabel("Select Protocol");
		lblProtocol.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblProtocol.setBounds(10, 160, 127, 22);
		switch_Frame.getContentPane().add(lblProtocol);
		
		 checkbIn = new JCheckBox("In");
		checkbIn.setBounds(169, 106, 45, 23);
		switch_Frame.getContentPane().add(checkbIn);
		
		 checkbOut = new JCheckBox("Out");
		checkbOut.setBounds(169, 132, 45, 23);
		switch_Frame.getContentPane().add(checkbOut);
		
		 btnFilter = new JButton("ADD FILTER");
		 btnFilter.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnFilter.setBounds(247, 125, 153, 37);
		switch_Frame.getContentPane().add(btnFilter);
		
		JLabel statisticslabel = new JLabel("Statistics");
		statisticslabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		statisticslabel.setBounds(887, 316, 93, 23);
		switch_Frame.getContentPane().add(statisticslabel);
		
		btnResetStats = new JButton("Reset Statistics");
		btnResetStats.setBounds(750, 316, 107, 23);
		switch_Frame.getContentPane().add(btnResetStats);
		
		JLabel lblEnterMacAddress = new JLabel("Enter Mac Address");
		lblEnterMacAddress.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblEnterMacAddress.setBounds(10, 86, 143, 14);
		switch_Frame.getContentPane().add(lblEnterMacAddress);
		
		txtfMacAddress = new JTextField();
		txtfMacAddress.setColumns(10);
		txtfMacAddress.setBounds(10, 107, 153, 31);
		switch_Frame.getContentPane().add(txtfMacAddress);
		
		textfPortNumber = new JTextField();
		textfPortNumber.setBounds(278, 49, 74, 20);
		switch_Frame.getContentPane().add(textfPortNumber);
		textfPortNumber.setColumns(10);
		
		JLabel lblEnterPortNumber = new JLabel("Enter Port Number");
		lblEnterPortNumber.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblEnterPortNumber.setBounds(247, 20, 153, 14);
		switch_Frame.getContentPane().add(lblEnterPortNumber);
		
		
		switch_Frame.setVisible(true);
	}
	public JFrame getSwitch_Frame() {
		return switch_Frame;
	}
	public void setSwitch_Frame(JFrame switch_Frame) {
		this.switch_Frame = switch_Frame;
	}
	public DefaultTableModel getModel() {
		return model;
	}
	public void setModel(DefaultTableModel model) {
		this.model = model;
	}
	public JTable getTable() {
		return table;
	}
	public void setTable(JTable table) {
		this.table = table;
	}
	public JButton getBtnResetTable() {
		return btnResetTable;
	}
	public void setBtnResetTable(JButton btnResetTable) {
		this.btnResetTable = btnResetTable;
	}
	public void setResetTableListener(ActionListener listener){
		btnResetTable.addActionListener(listener);
	}
	public void setFilterListener(ActionListener listener){
		btnFilter.addActionListener(listener);
	}
	public void setResetStatisticsListener(ActionListener listener){
		btnResetStats.addActionListener(listener);
	}
	public DefaultTableModel getStatModel() {
		return statModel;
	}
	public void setStatModel(DefaultTableModel statModel) {
		this.statModel = statModel;
	}
	public JTextField getTxtFIPAddress() {
		return txtFIPAddress;
	}
	public void setTxtFIPAddress(JTextField txtFIPAddress) {
		this.txtFIPAddress = txtFIPAddress;
	}
	public JCheckBox getCheckBAllow() {
		return checkBAllow;
	}
	public void setCheckBAllow(JCheckBox checkBAllow) {
		this.checkBAllow = checkBAllow;
	}
	public JCheckBox getCheckBForbid() {
		return checkBForbid;
	}
	public void setCheckBForbid(JCheckBox checkBForbid) {
		this.checkBForbid = checkBForbid;
	}
	public JComboBox getCmbBProtocol() {
		return cmbBProtocol;
	}
	public void setCmbBProtocol(JComboBox cmbBProtocol) {
		this.cmbBProtocol = cmbBProtocol;
	}
	public JCheckBox getCheckbIn() {
		return checkbIn;
	}
	public void setCheckbIn(JCheckBox checkbIn) {
		this.checkbIn = checkbIn;
	}
	public JCheckBox getCheckbOut() {
		return checkbOut;
	}
	public void setCheckbOut(JCheckBox checkbOut) {
		this.checkbOut = checkbOut;
	}
	public JTextField getTextfPortNumber() {
		return textfPortNumber;
	}
	public void setTextfPortNumber(JTextField textfPortNumber) {
		this.textfPortNumber = textfPortNumber;
	}
}
