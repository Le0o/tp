/*
 * MainFrm.java
 *
 * Created on __DATE__, __TIME__
 */

package com.jry.view;

import java.sql.Connection;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.jry.action.ConfigAction;
import com.jry.model.TPConfig;
import com.jry.util.sqlitepool.ConnectionFactory;
import com.jry.view.config.ConfigFrm;
import com.jry.view.testing.ExportFrm;
import com.jry.view.testing.InstructionFrm;

/**
 *
 * @author  __USER__
 */
public class MainFrm extends JFrame {

	/** Creates new form MainFrm */
	public MainFrm() {
		setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH); //mainframe���
		initComponents();
	}

	//GEN-BEGIN:initComponents
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		jMenuBar1 = new javax.swing.JMenuBar();
		jMenu2 = new javax.swing.JMenu();
		jMenu3 = new javax.swing.JMenu();
		jMenu1 = new javax.swing.JMenu();
		jMenu4 = new javax.swing.JMenu();
		jMenuItem1 = new javax.swing.JMenuItem();
		jMenuItem2 = new javax.swing.JMenuItem();
		jMenuItem3 = new javax.swing.JMenuItem();
		jMenuItem4 = new javax.swing.JMenuItem();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		jMenu2.setText("配置");
		jMenuItem2.setText("配置设备");
		jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItem2ActionPerformed(evt);
			}
		});
		jMenu2.add(jMenuItem2);
		jMenuBar1.add(jMenu2);

		jMenu3.setText("指令测试");
		jMenuItem3.setText("检测");
		jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					jMenuItem3ActionPerformed(evt);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		jMenu3.add(jMenuItem3);
		
		
		jMenuBar1.add(jMenu3);

		jMenu1.setText("生成报告");
		jMenuItem4.setText("查看报告");
		jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					jMenuItem4ActionPerformed(evt);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		jMenu1.add(jMenuItem4);
		jMenuBar1.add(jMenu1);

		jMenu4.setText("系统");

		jMenuItem1.setText("退出系统");
		jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItem1ActionPerformed(evt);
			}
		});
		jMenu4.add(jMenuItem1);

		jMenuBar1.add(jMenu4);

		setJMenuBar(jMenuBar1);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 803,
				Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 619,
				Short.MAX_VALUE));

		pack();
	}// </editor-fold>
	//GEN-END:initComponents

	private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {
		int back = JOptionPane.showConfirmDialog(null, "确定退出吗?", "确认对话框",
				JOptionPane.YES_NO_OPTION);
		//int back = JOptionPane.showConfirmDialog(null, "确定退出系统");//含有取消对话框
		if (back == JOptionPane.OK_OPTION) {
			System.exit(0);
		}
		System.out.println(back);
	}
	
	/**
	 * 打开导出页面
	 */
	private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) throws Exception {
		if(queryConfig()==null){
			JOptionPane.showMessageDialog(null, "请先配置设备信息");
		}else{
			ExportFrm exFrm = new ExportFrm();
			exFrm.setVisible(true);
		}
	}
	
	/**
	 * 打开检测页面
	 */
	private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) throws Exception {
		if(queryConfig()==null){
			JOptionPane.showMessageDialog(null, "请先配置设备信息");
		}else{
			InstructionFrm insFrm = new InstructionFrm();
			insFrm.setVisible(true);
		}
	}
	
	/**
	 * 获取要检测的设备GIT测试提交
	 */
	public TPConfig queryConfig() throws Exception{
		System.out.println("获取要检测的设备*******");
		Connection conn = ConnectionFactory.gerSqliteFactory().getCon();
		TPConfig config = configAction.getConfig(conn);
		conn.close();
		return config;
	}
	
	/**
	 * 打开配置页面
	 */
	private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {
		new ConfigFrm().setVisible(true);
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new MainFrm().setVisible(true);
			}
		});
	}

	//GEN-BEGIN:variables
	// Variables declaration - do not modify
	private javax.swing.JMenu jMenu1;
	private javax.swing.JMenu jMenu2;
	private javax.swing.JMenu jMenu3;
	private javax.swing.JMenu jMenu4;
	private javax.swing.JMenuBar jMenuBar1;
	private javax.swing.JMenuItem jMenuItem1;
	private javax.swing.JMenuItem jMenuItem2;
	private javax.swing.JMenuItem jMenuItem3;
	private javax.swing.JMenuItem jMenuItem4;
	private ConfigAction configAction = new ConfigAction();
	// End of variables declaration//GEN-END:variables

}