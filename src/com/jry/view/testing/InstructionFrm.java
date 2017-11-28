package com.jry.view.testing;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.jry.action.ConfigAction;
import com.jry.action.InstructionAction;
import com.jry.action.ResultAction;
import com.jry.model.TPConfig;
import com.jry.model.TPInstruction;
import com.jry.model.TPResult;
import com.jry.util.TcpUtil;
import com.jry.util.sqlitepool.ConnectionFactory;


public class InstructionFrm extends JFrame{
	
	private InstructionAction instructionAction = new InstructionAction();
	private ResultAction resultAction = new ResultAction();
	private ConfigAction configAction = new ConfigAction();
	private JButton jB_start;
	private JButton jB_pause;
	private JButton jB_end;

	private GroupLayout layout = new GroupLayout(getContentPane());
	private JScrollPane scrollPane = new JScrollPane();
	private JTable table;
	private String head[]=null;
	private Object [][]body=null;
	
	static Thread testThread;
	private List<TPInstruction> list;
	private TPConfig config;
	
//	private String ip = "192.168.100.97";//要测试的设备IP
//	private int port = 55555;//要测试的设备端口
	
	public InstructionFrm() throws Exception {
		setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH); 
		config = queryConfig();
		initComponents();
	}
	
	private void initComponents() throws Exception {

		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		jB_start = new JButton();
		jB_pause = new JButton();
		jB_end = new JButton();
		
		jB_start.setText("开始");
		jB_start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					startActionPerformed(evt);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		jB_pause.setText("暂停");
		jB_pause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					pauseActionPerformed(evt);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		jB_end.setText("结束");
		jB_end.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					endActionPerformed(evt);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

//		加载表格数据	
        table = new JTable();
        table.setBorder(new LineBorder(new Color(0, 0, 0)));
        head=new String[] {"序号", "测试项", "测试状态̬", "审核"};
        body = queryData();
        
//        DefaultTableModel tableModel = new DefaultTableModel(body,head){
//            public boolean isCellEditable(int row, int column)
//            {
//                return false;
//            }
//        };
//      tableModel.setDataVector(body, head);
//      table.setModel(tableModel);
//        scrollPane.setViewportView(table);
        
        DefaultTableModel dm = new DefaultTableModel();
        dm.setDataVector(body, head);
        
        JTable table = new JTable(dm) {
            public void tableChanged(TableModelEvent e) {
                super.tableChanged(e);
                repaint();
            }
        };
        
        table.getColumn("审核").setCellRenderer(new RadioButtonRenderer());
        table.getColumn("审核").setCellEditor(new RadioButtonEditor(new JCheckBox()));
        scrollPane.setViewportView(table);
       
//		页面布局
        
		getContentPane().setLayout(layout);
		SequentialGroup hGroup = layout.createSequentialGroup().addGap(300);  
       
        hGroup.addGroup(layout.createParallelGroup().addComponent(jB_start));  
        hGroup.addGap(50);
        hGroup.addGroup(layout.createParallelGroup().addComponent(jB_pause));  
        hGroup.addGap(50);
        hGroup.addGroup(layout.createParallelGroup().addComponent(jB_end)); 
        
        hGroup.addGroup(layout.createParallelGroup().addComponent(scrollPane, 500, 500, 500));  
        layout.setHorizontalGroup(hGroup);
        
		SequentialGroup vGroup = layout.createSequentialGroup().addGap(100);
        vGroup.addGroup(layout.createParallelGroup().addComponent(jB_start).addComponent(jB_pause).addComponent(jB_end));
        vGroup.addGap(50);
        
        vGroup.addGroup(layout.createParallelGroup().addComponent(scrollPane, 100, 300, 500));
        layout.setVerticalGroup(vGroup);
        
	}
	
	
	
	/**
	 * 获取要发送的指令项集合
	 */
	public List<TPInstruction> queryList() throws Exception{
		System.out.println("获取要发送的指令项集合*******");
		Connection conn = ConnectionFactory.gerSqliteFactory().getCon();
		List<TPInstruction> list = instructionAction.getInstructionsList(conn);
		conn.close();
		return list;
		
	}
    /**
     * 刷新table数据
     */
    public Object[][] queryData() throws Exception{
    	System.out.println("刷新table数据******");
    	Connection conn = ConnectionFactory.gerSqliteFactory().getCon();
    	List<TPInstruction> resultList = instructionAction.getInstructionsResultList(conn,config.getIp());
    	int size = resultList.size();
        body=new Object[size][4];
        for(int i=0;i<size;i++){
        	body[i][0] = resultList.get(i).getTestno();
        	body[i][1] = resultList.get(i).getInstructionname();
        	body[i][2] = "Y".equals(resultList.get(i).getTestresult())? "√":"×";
        	
        	JRadioButton jrb = new JRadioButton("Y");
        	jrb.addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent evt) {
        			jRadioButtonActionPerformed(evt);
			}
		});
        	body[i][3] = jrb ;
        }

        conn.close();
        return body;
    }
    
    public void startActionPerformed(ActionEvent evt) throws Exception, IOException {
		if(testThread!=null){
			System.out.println("暂停后重新开始继续测试*******");
			testThread.resume();
			return;
		}
		Connection conn = ConnectionFactory.gerSqliteFactory().getCon();
		if(config==null)
			return;
		list = queryList();
		testThread = new Thread(new Runnable() {
			public void run() {
				try {
					if(list!=null && list.size()>0){
						String backMessage;
						Connection conn = ConnectionFactory.gerSqliteFactory().getCon();
						for(TPInstruction  tpIns: list){
							
							TPResult result = new TPResult();
							backMessage = TcpUtil.sendMessage(config.getIp(), Integer.valueOf(config.getGateway()).intValue(), tpIns.getInstruction());
							
							result.setGuid(config.getIp());
							result.setTestresult("Y");
							result.setInstructionid(tpIns.getId());
							
							System.out.println("backMessage"+backMessage);
							if(backMessage==null || "".equals(backMessage) || backMessage.contains("ERR"))
								result.setTestresult("N");
							else if(backMessage.contains("OK") || (backMessage.contains("(") && backMessage.contains(")")))
								result.setTestresult("Y");
							 
							resultAction.saveResult(conn, result);
							initComponents();
						}
						JOptionPane.showMessageDialog(null, "测试完成！");
						conn.close();
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		testThread.start();
	}
    public void jRadioButtonActionPerformed(ActionEvent evt){
    	System.out.println("evt"+evt);
    }

    
    private void pauseActionPerformed(ActionEvent evt) {
    	System.out.println("pause......");
    	testThread.suspend();//暂停测试线程
    }
    
    private void endActionPerformed(ActionEvent evt) {
    	System.out.println("end.......");
    	testThread.stop();//结束测试线程ֹ
    }
    
    /**
	 * 获取要检测的设备
	 */
	public TPConfig queryConfig() throws Exception{
		System.out.println("获取要检测的设备*******");
		Connection conn = ConnectionFactory.gerSqliteFactory().getCon();
		TPConfig config = configAction.getConfig(conn);
		conn.close();
		return config;
	}
}

class RadioButtonRenderer implements TableCellRenderer {
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        if (value == null)
            return null;
//        System.out.println("row:"+row);
        return (Component) value;
    }
}

class RadioButtonEditor extends DefaultCellEditor implements ItemListener {
    /**
     */
    private static final long serialVersionUID = 1L;
    private JRadioButton button;

    public RadioButtonEditor(JCheckBox checkBox) {
        super(checkBox);
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        if (value == null)
            return null;
        button = (JRadioButton) value;
        button.addItemListener(this);
        return (Component) value;
    }

    public Object getCellEditorValue() {
        button.removeItemListener(this);
        return button;
    }

    public void itemStateChanged(ItemEvent e) {
        super.fireEditingStopped();
//        System.out.println("e"+e);
    }
    
}
