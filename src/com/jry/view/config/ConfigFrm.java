package com.jry.view.config;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.jry.action.ConfigAction;
import com.jry.model.TPConfig;
import com.jry.util.StringUtil;
import com.jry.util.sqlitepool.ConnectionFactory;


public class ConfigFrm extends JFrame{
	
//	IP��ַ
	private static final String REGEX_IP = "^([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]"
			+ "\\d|25[0-5])){3}$";
	
	private static final String REGEX_SUBNET_MASK = "^(255\\.0\\.0)|(255\\.255\\.0)|(255\\.255\\.255)\\.0$";
	
	private static final String REGEX_GATEWAY = "^192\\.168(\\.(\\d|([1-9]\\d)|(1\\d{2})|(2[0-4]\\d)|(25[0-5]))){2}$";
	private ConfigAction configAction = new ConfigAction();
	
	//ip
	private JLabel jLabel_ip;
	private JTextField ipTxt;
	
	//子网掩码
	private JLabel jLabel_subnet_mask;
	private JTextField subnetMaskTxt;
	
	//网关
	private JLabel jLabel_gateway;
	private JTextField gatewayTxt;
	
	private JButton jB_save;

	/** Creates new ConfigFrm */
	public ConfigFrm() {
		setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH); //mainframe最大化
		initComponents();
	}
	
	private void initComponents() {

		jLabel_ip = new JLabel();
		ipTxt = new JTextField();
		
		jLabel_subnet_mask = new JLabel();
		subnetMaskTxt = new JTextField();
		
		jLabel_gateway = new JLabel();
		gatewayTxt = new JTextField();
		
		jB_save = new JButton();

		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

		jLabel_ip.setText("IP：");

		jLabel_subnet_mask.setText("子网掩码：");

		jLabel_gateway.setText("网关：");

		jB_save.setText("保存");
		jB_save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					saveActionPerformed(evt);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		GroupLayout layout = new GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		
		SequentialGroup hGroup = layout.createSequentialGroup()
				.addGap(500)
				;  
        hGroup.addGap(50);//添加间隔   
        hGroup.addGroup(layout.createParallelGroup().addComponent(jLabel_ip)  
                .addComponent(jLabel_subnet_mask).addComponent(jLabel_gateway));  
        hGroup.addGap(50);  
        hGroup.addGroup(layout.createParallelGroup().addComponent(ipTxt,
		        		300,
						300,
						300)  
                .addComponent(subnetMaskTxt,
                		300,
        				300,
        				300)
                .addComponent(gatewayTxt,
                		300,
        				300,
        				300)
                .addComponent(jB_save,100,100,100));  
        hGroup.addGap(50);  
        layout.setHorizontalGroup(hGroup); 
		
        
        SequentialGroup vGroup = layout.createSequentialGroup()
        		.addGap(300)
        		;  
        vGroup.addGap(50);  
        vGroup.addGroup(layout.createParallelGroup().addComponent(jLabel_ip).addComponent(ipTxt,
        		GroupLayout.PREFERRED_SIZE,
				GroupLayout.DEFAULT_SIZE,
				GroupLayout.PREFERRED_SIZE));  
        vGroup.addGap(50);  
        vGroup.addGroup(layout.createParallelGroup().addComponent(jLabel_subnet_mask).addComponent(subnetMaskTxt,
        		GroupLayout.PREFERRED_SIZE,
				GroupLayout.DEFAULT_SIZE,
				GroupLayout.PREFERRED_SIZE));  
        vGroup.addGap(50);  
        vGroup.addGroup(layout.createParallelGroup().addComponent(jLabel_gateway).addComponent(gatewayTxt,
        		GroupLayout.PREFERRED_SIZE,
				GroupLayout.DEFAULT_SIZE,
				GroupLayout.PREFERRED_SIZE));  
        vGroup.addGap(50);
        vGroup.addGroup(layout.createParallelGroup().addComponent(jB_save));
  
        vGroup.addGap(80);  
      //设置垂直组  
        layout.setVerticalGroup(vGroup); 
        
		pack();
	}
	
	

	private void saveActionPerformed(ActionEvent evt) throws Exception{

		String ip = this.ipTxt.getText();
		String subnetMask = this.subnetMaskTxt.getText();
		String gateway = this.gatewayTxt.getText();
		Connection conn = ConnectionFactory.gerSqliteFactory().getCon();
		if (StringUtil.isEmpty(ip)) {
			JOptionPane.showMessageDialog(null, "IP不能为空！");
			return;
		}else if(!ip.matches(REGEX_IP)){
			JOptionPane.showMessageDialog(null,  "IP不合法！");
			return;
		}
		if (StringUtil.isEmpty(subnetMask)) {
			JOptionPane.showMessageDialog(null, "子网掩码不能为空！");
			return;
		}
//		else if(!ip.matches(REGEX_SUBNET_MASK)){
//			JOptionPane.showMessageDialog(null,  "子网掩码不合法！");
//			return;
//		}
		if (StringUtil.isEmpty(gateway)) {
			JOptionPane.showMessageDialog(null, "网关不能为空！");
			return;
		}
//		else if(!ip.matches(REGEX_GATEWAY)){
//			JOptionPane.showMessageDialog(null,  "网关不合法！");
//			return;
//		}

		TPConfig tpConfig = new TPConfig();
		tpConfig.setIp(ip);
		tpConfig.setSubnetmask(subnetMask);
		tpConfig.setGateway(gateway);
		int rs = configAction.saveConfigAction(conn,tpConfig);
		conn.close();
		if(rs>0){
			JOptionPane.showMessageDialog(null, "保存成功！");
			this.setVisible(false);
		}else{
			JOptionPane.showMessageDialog(null, "保存失败！");
		}
		
	}
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new ConfigFrm().setVisible(true);
			}
		});
	}
	
	
}
