package com.jry.util.sqlitepool;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jry.util.PropertiesUtils;


public class SqliteFactory {
	private static final Logger logger = LoggerFactory.getLogger(SqliteFactory.class);
	private static PropertiesUtils properties = new PropertiesUtils(); 
    private static int max = Integer.parseInt(properties.getValue("sqlite.max"));// ��������
    private static int min = Integer.parseInt(properties.getValue("sqlite.min"));// ��ʼ������
    private static int step = Integer.parseInt(properties.getValue("sqlite.step"));// ÿ����󲹳��߳�����
    private static String dbPath;// ���ݿ�����·��
    private static String timeFlag;// ����ʱ���
    protected static long timeout = Integer.parseInt(properties.getValue("sqlite.timeout"));// ��ʱ�̻߳���
    protected static Vector<SqlitePojo> sList = new Vector<SqlitePojo>();// ��������
    protected static Vector<SqlitePojo> sRunList = new Vector<SqlitePojo>();// �ѷ������������
    
    static {
    	logger.info("sqlite.max" +properties.getValue("sqlite.max"));
        ini();
        new Thread(new Recovery()).start();
    }
    /**
     * sqlite�����ȡ�쳣
     *
     */
    final class SqlitePojoException extends Exception {
        private static final long serialVersionUID = 1L;

        @Override
        public void printStackTrace() {
            print("ERROR:[�ض��󴴽��쳣]");
            super.printStackTrace();
        }
    }

    /**
     * �����
     * 
     *
     */
    final class SqliteMaxException extends Exception {
        private static final long serialVersionUID = -3970424418466308414L;

        @Override
        public void printStackTrace() {
            print("ERROR:[���ӳ���������������� max:", max);
            super.printStackTrace();
        }
    }

    /**
     * ���ѷ��������Ĳ������Ͳ�����
     *
     */
    final class ExecuteTypeIsException extends Exception {
        private static final long serialVersionUID = -3970424418466308414L;

        @Override
        public void printStackTrace() {
            print("ERROR:[���ѷ��������Ĳ������Ͳ�����]");
            super.printStackTrace();
        }
    }

    /**
     * ��ʼ�����ӳ�
     * 
     */
    private static void ini() {
        Connection con = null;
        Statement stmt = null;
        try {
            setClass();
            if (timeFlag == null) {
                // ������Ϊʱ�������һ����ǰ����
                timeFlag = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String path = new StringBuffer(properties.getValue("sqlite.dbpath")).append(properties.getValue("sqlite.dbfile").replace("{?}", timeFlag)).toString();
                dbPath = path;
                createDb(con, stmt);
            }
            addD(min);
        } catch (Exception e) {
            sList.clear();
            print("ERROR:[�س�ʼ��ʧ��][�����������]");
            e.printStackTrace();
        } finally {
            try {
                if (con != null)
                    con.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                print("ERROR:[�س�ʼ��ʧ��][�����������]");
                sList.clear();
                e.printStackTrace();
            }
        }

    }

    /**
     * ��������
     */
    private static void setClass() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
    }

    /**
     * ����һ���µ�db�ļ���Ϊ�䴴�����ݿ�
     * 
     */
    private static void createDb(Connection con, Statement stmt) throws SQLException {
        File f = new File(dbPath);
        // ����ļ��������򴴽�
        // �����µ�db�ļ����½���
        if (!f.isFile()) {
            con = DriverManager.getConnection(new StringBuffer("jdbc:sqlite:").append(dbPath).toString());
            stmt = con.createStatement();
            String sql = "CREATE TABLE log ("+
                         "id INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL,"+
                         "action_userId   INT           NOT NULL,"+
                         "action_loginId  VARCHAR (100) NOT NULL,"+
                         "action_userName VARCHAR (20)  NOT NULL,"+
                         "action_name     VARCHAR (30)  NOT NULL,"+
                         "action_param    TEXT          NOT NULL,"+
                         "action_result   VARCHAR (20)  NOT NULL,"+
                         "action_time     TIME (25)     NOT NULL DEFAULT (datetime('now', 'localtime') ),"+
                         "action_ip       VARCHAR (20),"+
                         "action_role     INTEGER (2) "+
                         ");";
            stmt.executeUpdate(sql);
        }
    }

    /**
     * ��ȡ����
     * 
     */
    public Connection getCon() throws SQLException, SqlitePojoException, SqliteMaxException, ExecuteTypeIsException {
        /**
         * �Ƚ��ȳ�ԭ��
         */
        SqlitePojo sPojo = null;
        checkLogDay();
        synchronized (sList) {
            checkList();
            sPojo = sList.get(0);
            sList.remove(0);
            changeRunList(sRunList, 0, sPojo);
        }
        if (sPojo == null || sPojo.getCon().isClosed())
            throw new SqlitePojoException();
        return sPojo.getCon();
    }

    /**
     * ͬ�����ѷ����������в���
     * 
     */
    protected synchronized void changeRunList(Vector<SqlitePojo> list, int type, Object... ob) throws ExecuteTypeIsException {
        switch (type) {
        case 0:
            // add
            list.add((SqlitePojo) ob[0]);
            break;
        case 1:
            // del
            list.remove(Integer.parseInt(ob[0].toString()));
            break;
        default:
            throw new ExecuteTypeIsException();
        }
    }

    /**
     * ��������Ƿ�����ʹ������
     * 
     */
    private void checkList() throws SqliteMaxException, SQLException {
        if (sList.size() == 0) {
            // ��������߳���
            int num = 0;
            if ((num = max - sRunList.size() - sList.size()) <= 0)
                throw new SqliteMaxException();
            // ʣ��������߳���
            num = num > step ? step : num;
            addD(num);
        }
    }

    /**
     * �����־����
     * 
     */
    private synchronized static void checkLogDay() {
        String tempTime = null;
        Connection con = null;
        Statement stmt = null;
        try {
            if (!timeFlag.equals((tempTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date())))) {
                // ʱ����ѹ���
                timeFlag = tempTime;
                dbPath = new StringBuffer(properties.getValue("sqlite.dbpath")).append(properties.getValue("sqlite.dbfile").replace("{?}", timeFlag)).toString();
                createDb(con, stmt);
                for (int i = 0; i < sList.size(); i++) {
                    // ������ʷʱ���list
                    sList.get(i).setCon(DriverManager.getConnection(new StringBuffer("jdbc:sqlite:").append(dbPath).toString()));
                }
            }
        } catch (Exception e) {
            print("ERROR:[�����ڼ�����ʧ��][�����������]");
            e.printStackTrace();
        } finally {
            try {
                if (con != null)
                    con.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * ����µ����ӵ�����
     * 
     */
    private static void addD(int num) throws SQLException {
        for (int i = 0; i < num; i++) {
            checkLogDay();
            sList.add(new SqlitePojo(new Date().getTime(), DriverManager.getConnection(new StringBuffer("jdbc:sqlite:").append(dbPath).toString())));
        }
    }

    /**
     * �ַ�����װ
     * 
     */
    protected static void print(Object... ob) {
        StringBuffer sbf = new StringBuffer();
        for (Object temp : ob)
            sbf.append(temp);
        logger.info(sbf.toString());
    }

/*    public static void main(String[] args) {
        try {
            while (true) {
                Scanner sc = new Scanner(System.in);
                if (sc.next() != null) {
                    long s = System.currentTimeMillis();
                    new SqliteFactory().getCon();
                    System.out.println(System.currentTimeMillis() - s + "ms");

                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }*/
}
