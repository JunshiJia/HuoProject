package com.junshijia.HuoV3.switchTurbine;

import com.junshijia.HuoV3.dao.EMS_changeDataDao;
import com.junshijia.HuoV3.dao.EMS_tssDataDao;
import com.junshijia.HuoV3.domain.EMS_changeDataFromDB;
import com.junshijia.HuoV3.domain.EMS_tssDataFromDB;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

public class EMSWritesMainControl {

    public static void updateDB(int i,int ssFlag){
        boolean flag = true;
        while(flag) {
            try {
                InputStream is = Resources.getResourceAsStream("DatabaseConfig.xml");
                SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
                SqlSessionFactory factory = builder.build(is, "test3");
                SqlSession session = factory.openSession();
                EMS_tssDataDao tssDao = session.getMapper(EMS_tssDataDao.class);
                EMS_changeDataDao changeDao = session.getMapper(EMS_changeDataDao.class);
                EMS_tssDataFromDB tssData = new EMS_tssDataFromDB();
                EMS_changeDataFromDB changeData = new EMS_changeDataFromDB();
                changeData.setTSS_Change(1);
                if(i == 0 && ssFlag == 1){
                    tssData.setWT5(3);
                }else if(i == 0 && ssFlag == 2){
                    tssData.setWT5(2);
                }else if(i == 1 && ssFlag == 1){
                    tssData.setWT4(3);
                }else if(i == 1 && ssFlag == 2){
                    tssData.setWT4(2);
                }else if(i == 2 && ssFlag == 1){
                    tssData.setWT1(3);
                }else if(i == 2 && ssFlag == 2){
                    tssData.setWT1(2);
                }else if(i == 3 && ssFlag == 1){
                    tssData.setWT2(3);
                }else if(i == 3 && ssFlag == 2){
                    tssData.setWT2(2);
                }else if(i == 4 && ssFlag == 1){
                    tssData.setWT3(3);
                }else if(i == 4 && ssFlag == 2){
                    tssData.setWT3(2);
                }
                tssDao.updateData(tssData);
                Thread.sleep(1000);
                Thread.sleep(1000);
                changeDao.updateData(changeData);
                session.commit();
                session.close();
                is.close();
                flag = false;
            } catch (Exception e) {
                System.out.println("pms DB switch mode error, block 1min and restart...");
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
