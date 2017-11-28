package com.jry.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author wsg
 * @time 2017-11-15
 * @description:ResultSet转换器
 *
 */
public class ResultSetConverter {
	
	/**
	 *@param <T>
	 * @param rs
	 *@description ResultSet转换为List 
	 ***/
	public static List convertList(ResultSet rs) throws SQLException {
		List list = new ArrayList();
		ResultSetMetaData md = rs.getMetaData();
		int columnCount = md.getColumnCount(); // Map rowData;
		while (rs.next()) { // rowData = new HashMap(columnCount);
			Map rowData = new HashMap();
			for (int i = 1; i <= columnCount; i++) {
				rowData.put(md.getColumnName(i), rs.getObject(i));
			}
			list.add(rowData);
		}
		return list;
	}
	
	/**
	 * @param rs
	 * @description:通用取结果方案,返回list
	 * **/
	public static List extractData(ResultSet rs) throws SQLException {
		ResultSetMetaData md = rs.getMetaData();
		int num = md.getColumnCount();
		List listOfRows = new ArrayList();
		while (rs.next()) {
			Map mapOfColValues = new HashMap(num);
			for (int i = 1; i <= num; i++) {
				mapOfColValues.put(md.getColumnName(i), rs.getObject(i));
			}
			listOfRows.add(mapOfColValues);
		}
		return listOfRows;
	}
	
	
	/**
	 * @param rs
	 * @return JSON
	 * @throws SQLException
	 * @description 通用取结果方案，返回JSON格式数据
	 */
	public static JSONArray extractJSONArray(ResultSet rs) throws SQLException {
		ResultSetMetaData md = rs.getMetaData();
		int num = md.getColumnCount();
		JSONArray array = new JSONArray();
		while (rs.next()) {
			JSONObject mapOfColValues = new JSONObject();
			for (int i = 1; i <= num; i++) {
				mapOfColValues.put(md.getColumnName(i), rs.getObject(i));
			}
			array.add(mapOfColValues);
		}
		return array;
	}
	
	
	
	

}
