package com.jry.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jry.model.TPInstruction;
import com.jry.model.TPResult;

public class ResultAction {

	/**
	 * @param conn
	 * @param TPConfig
	 * @description insert config 信息
	 * @throws SQLException
	 */

	public int saveResult(Connection conn,TPResult tpResult) throws SQLException{
		String insertResultSql = "insert into tp_result "
				+ "(guid,instructionid,testresult,verify,creater,iscreated,remarks) "
				+ "values (?,?,?,?,?,?,?)";
		PreparedStatement ps = conn.prepareStatement(insertResultSql);
		ps.setString(1,tpResult.getGuid());
		ps.setInt(2,tpResult.getInstructionid());
		ps.setString(3,tpResult.getTestresult());
		ps.setString(4,tpResult.getVerify());
		ps.setString(5,tpResult.getCreater());
		ps.setString(6,tpResult.getIscreated());
		ps.setString(7,tpResult.getRemarks());
		int rs = ps.executeUpdate();
		
		if(rs>0){
			System.out.println("插入成功！");
		}else {
			System.out.println("插入失败！");
		}
		
		return rs;
	}
	
}
