package com.jry.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jry.model.TPInstruction;

public class InstructionAction {

	/**
	 * @param conn
	 * @param TPConfig
	 * @description 
	 * @throws SQLException
	 */
	public List<TPInstruction> getInstructionsResultList(Connection conn,String guid) throws SQLException{
		String Sql = "select * from tp_instructions b inner Join tp_result a on a.instructionid = b.id "
				+ "where guid = ? order by b.testno ";
		PreparedStatement ps = conn.prepareStatement(Sql);
		ps.setString(1,guid);
		ResultSet rs = ps.executeQuery();
		
		List<TPInstruction> resultList = new ArrayList<TPInstruction>();
		while(rs.next()){
			TPInstruction ins = new TPInstruction();
			
			ins.setId(rs.getInt("id"));
			ins.setTestno(rs.getInt("testno"));
			ins.setInstructionid(rs.getInt("instructionid"));
			ins.setInstructionname(rs.getString("instructionname"));
			ins.setInstruction(rs.getString("instruction"));
			ins.setCreater(rs.getString("creater"));
			ins.setCreattime(null);
			ins.setRemarks(rs.getString("remarks"));
			ins.setTestresult(rs.getString("testresult"));
			ins.setVerify(rs.getString("verify"));
			resultList.add(ins);
		}
		
		return resultList;
	}
	public List<TPInstruction> getInstructionsList(Connection conn) throws SQLException{
		String Sql = "select * from tp_instructions order by testno";
		PreparedStatement ps = conn.prepareStatement(Sql);
		ResultSet rs = ps.executeQuery();
		
		List<TPInstruction> resultList = new ArrayList<TPInstruction>();
		while(rs.next()){
			TPInstruction ins = new TPInstruction();
			
			ins.setId(rs.getInt("id"));
			ins.setTestno(rs.getInt("testno"));
			ins.setInstructionid(rs.getInt("instructionid"));
			ins.setInstructionname(rs.getString("instructionname"));
			ins.setInstruction(rs.getString("instruction"));
			ins.setCreater(rs.getString("creater"));
			ins.setCreattime(rs.getString("creattime"));
			ins.setRemarks(rs.getString("remarks"));
			resultList.add(ins);
		}
		
		return resultList;
	}
	
}
