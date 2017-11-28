package com.jry.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.json.JSONArray;

import com.jry.model.TpUser;
import com.jry.util.ResultSetConverter;

/**
 * @author wsg
 * @time 2017-11-17
 * @description 登录验证
 */
public class LoginAction {
	
	/**
	 * @param conn
	 * @param tpUser
	 * @return tpUser
	 * @description 登录方法
	 * @throws SQLException
	 */
	public boolean loginAction(Connection conn,TpUser tpUser) throws SQLException{
		TpUser tu = new TpUser();
		String loginSql = "select * from tp_user where logname = ? and password = ?";
		PreparedStatement ps = conn.prepareStatement(loginSql);
		ps.setString(1,tpUser.getLogName());
		ps.setString(2,tpUser.getPassword());
		ResultSet rs = ps.executeQuery();
		/*JSONArray json = ResultSetConverter.extractJSONArray(rs);
		System.out.println("json----------------->"+json+"logname:"+tpUser.getLogName()+"passwoord:"+tpUser.getPassword());*/
		return rs.next();
	}
}
