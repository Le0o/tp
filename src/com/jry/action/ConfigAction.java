package com.jry.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.jry.model.TPConfig;

/**
 * @author xipeng
 * @time 2017-11-22
 * @description tpconfigAction
 */

public class ConfigAction {

	/**
	 * @param conn
	 * @param TPConfig
	 * @description insert config 信息
	 * @throws SQLException
	 */
	public int saveConfigAction(Connection conn,TPConfig tpConfig) throws SQLException{
		//表里只保留一条记录，所以每次保存设备配置信息之前都清空表里的其他设备配置信息
		String delSql = "delete from tp_config where 1=1";
		PreparedStatement ps = conn.prepareStatement(delSql);
		ps.executeUpdate();
		
		String insertConfigSql = "insert into tp_config (ip,subnetmask,gateway,equipmentmarking,creater,remarks) "
				+ "values (?,?,?,'','xipeng','')";
		ps = conn.prepareStatement(insertConfigSql);
		ps.setString(1,tpConfig.getIp());
		ps.setString(2,tpConfig.getSubnetmask());
		ps.setString(3,tpConfig.getGateway());
		int rs = ps.executeUpdate();
		/*JSONArray json = ResultSetConverter.extractJSONArray(rs);
		System.out.println("json----------------->"+json+"logname:"+tpUser.getLogName()+"passwoord:"+tpUser.getPassword());*/
		
		if(rs>0){
			System.out.println("保存配置成功！");
		}else {
			System.out.println("保存配置失败！");
		}
		
		return rs;
	}
	
	public TPConfig getConfig(Connection conn) throws SQLException{
		TPConfig config = new TPConfig();
		String Sql = "select * from tp_config";
		PreparedStatement ps = conn.prepareStatement(Sql);
		ResultSet rs = ps.executeQuery();
		if(rs.next()){
			config.setIp(rs.getString("ip"));
			config.setSubnetmask(rs.getString("subnetmask"));
			config.setGateway(rs.getString("gateway"));
			System.out.println(config.toString());
			return config;
		}else
			return null;
		
		
		
	}
}
