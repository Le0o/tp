package com.jry.util.sqlitepool;

/**
 * @author wsg
 * @time 2017-11-15
 * @description:����Ψһ��SqliteFactroyʵ��
 *
 */
public class ConnectionFactory {
    public static SqliteFactory sf = new SqliteFactory();
    private ConnectionFactory(){}
    public static SqliteFactory gerSqliteFactory(){
    	return sf;
    }
}
