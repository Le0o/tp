package com.jry.util.sqlitepool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jry.model.TpUser;
import com.jry.util.ResultSetConverter;
import com.jry.util.sqlitepool.SqliteFactory.ExecuteTypeIsException;
import com.jry.util.sqlitepool.SqliteFactory.SqliteMaxException;
import com.jry.util.sqlitepool.SqliteFactory.SqlitePojoException;

public class Test {
	public static void main(String[] args) throws SQLException, SqlitePojoException, SqliteMaxException, ExecuteTypeIsException {
		SqliteFactory sf =  ConnectionFactory.gerSqliteFactory();//new SqliteFactory();
		Connection co = sf.getCon();
		//co.setAutoCommit(true);
		if(null!=co){
			System.out.println("co-----------:"+co);
		}
		 PreparedStatement ps = co.prepareStatement("select * from tp_user");
		 ResultSet rs = ps.executeQuery();
		/* JSONArray jas = ResultSetConverter.extractJSONArray(rs);
		for(Object obj:jas){
			System.out.println(obj.toString());
		}*/
		 
		/* rs.last();
		 System.out.println(rs.getRow());*/
		// TpUser[] tu =new TpUser[rs.getRow()];
		
	/*	for(int i=0;i<tu.length;i++){
			while(rs.next()){
				 tu[i].setCreater(rs.getString("creater"));
				 tu[i].setLogName(rs.getString("logname"));
			 }
			 
		 }
		 System.out.println(tu[0].getLogName());
		 System.out.println(tu[1].getLogName());
		 System.out.println(tu[2].getLogName());*/
		 
		 while(rs.next()){
			 System.out.println(rs.getString("logname")+"----------"+rs.getString("password"));
		 }
		/* PreparedStatement ps = co.prepareStatement("update sqlite_sequence set seq = 9");
		 int rs = ps.executeUpdate();
		 System.out.println(rs);*/
		 
		 rs.close();
		 ps.close();
		 co.close();
	}
}
