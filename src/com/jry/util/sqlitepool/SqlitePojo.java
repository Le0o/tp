package com.jry.util.sqlitepool;

import java.sql.Connection;

/**
 * @author wsg
 * @time 2017-11-15
 * @description : sqlite pojo类
 *
 */
public class SqlitePojo {
	private long createTime;// 时间戳
    private Connection con;// 链接对象

    public SqlitePojo() {
        // TODO Auto-generated constructor stub
    }

    public SqlitePojo(long createTime, Connection con) {
        this.createTime = createTime;
        this.con = con;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public Connection getCon() {
        return con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }
}
