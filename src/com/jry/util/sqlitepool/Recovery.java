package com.jry.util.sqlitepool;

import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

/**
 * @author wsg
 * @time:2017-11-15
 * @description���̻߳���
 *
 */
final class Recovery extends SqliteFactory implements Runnable {
    public Recovery() {
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                print("INFO:[����������:", sList.size(), "]", "[����������:", sRunList.size(), "]");
                Vector<SqlitePojo> list = sRunList;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getCon().isClosed() || new Date().getTime() - list.get(i).getCreateTime() > timeout) {
                        changeRunList(list, 1, i);
                        if (list.size() >= i)
                            --i;
                    }
                }
            } catch (InterruptedException e) {
                sList.clear();
                sRunList.clear();
                print("ERROR:[�ػ����߳�����]");
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ExecuteTypeIsException e) {
                e.printStackTrace();
            }
        }
    }
}