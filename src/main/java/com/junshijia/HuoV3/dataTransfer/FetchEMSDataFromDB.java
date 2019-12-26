package com.junshijia.HuoV3.dataTransfer;


import com.junshijia.HuoV3.dao.EMS_enDataDao;
import com.junshijia.HuoV3.dao.EMS_fshowDataDao;
import com.junshijia.HuoV3.dao.EMS_tshowDataDao;
import com.junshijia.HuoV3.domain.EMS_enDataFromDB;
import com.junshijia.HuoV3.domain.EMS_fshowDataFromDB;
import com.junshijia.HuoV3.domain.EMS_tshowDataFromDB;
import com.junshijia.HuoV3.util.HuoUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class FetchEMSDataFromDB implements Runnable{
    //数据库所需配置
    private InputStream is;
    private SqlSessionFactory factory;
    private SqlSession session;
    //3张ems的表
    private EMS_enDataDao enDataDao;
    private volatile EMS_enDataFromDB enData;
    private EMS_fshowDataDao fshowDataDao;
    private volatile EMS_fshowDataFromDB fshowData;
    private EMS_tshowDataDao tshowDataDao;
    private volatile EMS_tshowDataFromDB tshowData;
    //3个对应属性的名字
    private String[] enFieldNames;
    private String[] fshowFieldNames;
    private String[] tshowFieldNames;
    //构造器
    public FetchEMSDataFromDB() {
        //1.获取名字arrays
        this.setEnNames();
        this.setFshowNames();
        this.setTshowNames();
        //2.读取配置表
        try {
            is = Resources.getResourceAsStream("DatabaseConfig.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //使用第二个database
        this.factory = new SqlSessionFactoryBuilder().build(is,"test2");
    }

    private void reconnectDB(){
        //关闭文件输入流
        if(is!=null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            this.is = Resources.getResourceAsStream("DatabaseConfig.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }//打开配置文件中的test1，也就是使用第一个database
        factory = new SqlSessionFactoryBuilder().build(is, "test2");
    }

    //3个数据库信息对应对象的getter
    public EMS_enDataFromDB getEnData() {
        return enData;
    }
    public EMS_fshowDataFromDB getFshowData() {
        return fshowData;
    }
    public EMS_tshowDataFromDB getTshowData() {
        return tshowData;
    }

    //从数据库获取3个表的信息对象
    private void setEnData() {
        this.enData = enDataDao.findLast();
    }
    private void setFshowData() {
        this.fshowData = fshowDataDao.findLast();
    }
    private void setTshowData() {
        this.tshowData = tshowDataDao.findLast();
    }

    //得到3个对象的名字array
    private void setEnNames(){
        this.enFieldNames = HuoUtils.getFiledNameByClz("com.junshijia.HuoV3.domain.EMS_enDataFromDB");
    }
    private void setFshowNames(){
        this.fshowFieldNames = HuoUtils.getFiledNameByClz("com.junshijia.HuoV3.domain.EMS_fshowDataFromDB");
    }
    private void setTshowNames(){
        this.tshowFieldNames = HuoUtils.getFiledNameByClz("com.junshijia.HuoV3.domain.EMS_tshowDataFromDB");
    }
    //3个名字对象的getter
    public String[] getEnFieldNames() {
        return enFieldNames;
    }
    public String[] getFshowFieldNames() {
        return fshowFieldNames;
    }
    public String[] getTshowFieldNames() {
        return tshowFieldNames;
    }

    //测试用
    @Override
    public void run() {
        while (true) {
            this.fetchInfo();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void fetchInfo(){
        boolean flag = true;
        while(flag) {
            try {
                //0 得到session
                session = factory.openSession();
                enDataDao = session.getMapper(EMS_enDataDao.class);
                fshowDataDao = session.getMapper(EMS_fshowDataDao.class);
                tshowDataDao = session.getMapper(EMS_tshowDataDao.class);
                //1 得到3条data的对象
                this.setEnData();
                this.setFshowData();
                this.setTshowData();
                //2 清除缓存
                session.commit();
                session.clearCache();
                //3 关闭session
                session.close();
                flag = false;
            } catch (Exception e) {
                System.out.println("EMS DB error, trying to reconnect 5min later...");
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                this.reconnectDB();
            }
        }
    }
}
