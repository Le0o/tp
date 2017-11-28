package com.jry.view.testing;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.Calendar;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import com.jry.action.ConfigAction;
import com.jry.action.InstructionAction;
import com.jry.model.TPConfig;
import com.jry.model.TPInstruction;
import com.jry.util.sqlitepool.ConnectionFactory;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;



public class ExportFrm extends JFrame{

	private JButton jB_export;
	private JLabel jl_title;
	
	private GroupLayout layout = new GroupLayout(getContentPane());
	private JScrollPane scrollPane = new JScrollPane();
	private JTable table;
	private String head[]=null;
	private Object [][]data=null;
	private InstructionAction instructionAction = new InstructionAction();
	private ConfigAction configAction = new ConfigAction();
	private TPConfig config;
	
	
	Document document = new Document();// 建立一个Document对象     
    
    private static Font headfont ;// 设置字体大小 
    private static Font keyfont;// 设置字体大小 
    private static Font textfont;// 设置字体大小
    
    int maxWidth = 520;
    static{ 
        BaseFont bfChinese; 
        try { 
            bfChinese = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED); 
            headfont = new Font(bfChinese, 12, Font.BOLD);// 设置字体大小 
            keyfont = new Font(bfChinese, 10, Font.BOLD);// 设置字体大小 
            textfont = new Font(bfChinese, 8, Font.NORMAL);// 设置字体大小 
        } catch (Exception e) {          
            e.printStackTrace(); 
        }  
    }
    
    public void PDFReport(File file) {         
        document.setPageSize(PageSize.A4);// 设置页面大小 
        try { 
           PdfWriter.getInstance(document,new FileOutputStream(file)); 
           document.open();  
       } catch (Exception e) { 
           e.printStackTrace(); 
       }  
   }
    
    public PdfPCell createCell(String value,Font font,int align){ 
        PdfPCell cell = new PdfPCell(); 
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);         
        cell.setHorizontalAlignment(align);     
        cell.setPhrase(new Phrase(value,font)); 
       return cell; 
   } 
      
    public PdfPCell createCell(String value,Font font){ 
        PdfPCell cell = new PdfPCell(); 
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); 
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);  
        cell.setPhrase(new Phrase(value,font)); 
       return cell; 
   } 
  
    public PdfPTable createTable(int colNumber){ 
       PdfPTable table = new PdfPTable(colNumber); 
       try{ 
           table.setTotalWidth(maxWidth); 
           table.setLockedWidth(true); 
           table.setHorizontalAlignment(Element.ALIGN_CENTER);      
           table.getDefaultCell().setBorder(1); 
       }catch(Exception e){ 
           e.printStackTrace(); 
       } 
       return table; 
   } 
      
	
	private void jB_ExportActionPerformed() throws Exception{
		
		Connection conn = ConnectionFactory.gerSqliteFactory().getCon();
    	List<TPInstruction> resultList = instructionAction.getInstructionsResultList(conn,config.getIp());
    	System.out.println("list size:"+resultList.size());
    	
    	Calendar cal = Calendar.getInstance();
    	String nowStr = cal.get(Calendar.YEAR)+"年"+String.valueOf(cal.get(Calendar.MONTH)+1)+"月";
    	
    	Paragraph title = new Paragraph("混合存储分发测试报告", headfont);
        title.setAlignment(Element.ALIGN_CENTER);
        
        Paragraph content = new Paragraph("苏州金瑞阳信息科技有限责任公司", keyfont);
        content.setAlignment(Element.ALIGN_CENTER);
        content.setSpacingBefore(350f);
        Paragraph time = new Paragraph(nowStr, keyfont);
        time.setAlignment(Element.ALIGN_CENTER);
    	
        PdfPTable table = createTable(4); 
        
        table.addCell(createCell("序号", keyfont, Element.ALIGN_CENTER)); 
        table.addCell(createCell("测试项", keyfont, Element.ALIGN_CENTER)); 
        table.addCell(createCell("测试状态", keyfont, Element.ALIGN_CENTER)); 
        table.addCell(createCell("审核", keyfont, Element.ALIGN_CENTER)); 
           
        for(int i=0;i<resultList.size();i++){ 
            table.addCell(createCell(String.valueOf(resultList.get(i).getTestno()), textfont)); 
            table.addCell(createCell(resultList.get(i).getInstructionname(), textfont)); 
            table.addCell(createCell(("Y".equals(resultList.get(i).getTestresult()))?"√":"×", textfont)); 
            table.addCell(createCell(resultList.get(i).getVerify(), textfont)); 
        } 
        
        document.setPageSize(PageSize.A4);// 设置页面大小 
        File file = new File("E:\\TestResult.pdf"); 
        file.createNewFile(); 
        try { 
           PdfWriter.getInstance(document,new FileOutputStream(file)); 
           document.open();  
	    } catch (Exception e) { 
	      e.printStackTrace(); 
	    }   
        
        
        document.add(title);
        document.add(content);
        document.add(time);
        document.newPage();
        
        document.add(table); 
        document.close();
        
        System.out.println("报告已生成，路径:E:\\TestResult.pdf");
        JOptionPane.showMessageDialog(null, "导出成功！"); 
        this.setVisible(false);//关闭当前子窗口
    	
	}
	
	
	private void initComponents() throws Exception{
		jB_export = new JButton();
		jB_export.setText("导出报告");
		jB_export.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					jB_ExportActionPerformed();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		jl_title = new JLabel();
		jl_title.setText("测试结果");
		
		
		table = new JTable();
        table.setBorder(new LineBorder(new Color(0, 0, 0)));
        head=new String[] {"序号", "测试项", "测试状态̬", "审核"};
        DefaultTableModel tableModel = new DefaultTableModel(queryData(),head){
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        table.setModel(tableModel);
        scrollPane.setViewportView(table);
        
//		页面布局
        
		getContentPane().setLayout(layout);
		SequentialGroup hGroup = layout.createSequentialGroup().addGap(300);  
       
        hGroup.addGroup(layout.createParallelGroup().addComponent(jB_export)); 
        
        hGroup.addGroup(layout.createParallelGroup().addComponent(scrollPane, 500, 500, 500));  
        layout.setHorizontalGroup(hGroup);
        
		SequentialGroup vGroup = layout.createSequentialGroup().addGap(100);
        vGroup.addGroup(layout.createParallelGroup().addComponent(jB_export));
        vGroup.addGap(50);
        
        vGroup.addGroup(layout.createParallelGroup().addComponent(scrollPane, 100, 300, 500));
        layout.setVerticalGroup(vGroup);
	}
	
	public Object[][] queryData() throws Exception{
    	Connection conn = ConnectionFactory.gerSqliteFactory().getCon();
    	List<TPInstruction> resultList = instructionAction.getInstructionsResultList(conn,config.getIp());
    	int size = resultList.size();
        data=new Object[size][4];
        for(int i=0;i<size;i++){
        	data[i][0] = resultList.get(i).getTestno();
        	data[i][1] = resultList.get(i).getInstructionname();
        	data[i][2] = "Y".equals(resultList.get(i).getTestresult())? "√":"×";
        	data[i][3] = resultList.get(i).getVerify();
        }

        conn.close();
        return data;
    }
	
	public ExportFrm() throws Exception {
		setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH); //mainframe最大化
		config = queryConfig();
		initComponents();
			
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
