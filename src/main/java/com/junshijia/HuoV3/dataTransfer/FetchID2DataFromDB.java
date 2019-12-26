package com.junshijia.HuoV3.dataTransfer;




import com.junshijia.HuoV3.dao.ID2DataDao;
import com.junshijia.HuoV3.domain.ID2DataFromDB;
import com.junshijia.HuoV3.util.HuoUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class FetchID2DataFromDB implements Runnable{
    //打开数据库的配置
    private InputStream is;
    private SqlSessionFactory factory;
    private SqlSession session;
    private ID2DataDao dataDao;
    //5条来自数据库的数据
    private List<ID2DataFromDB> dataList;
    private volatile ID2DataFromDB t1Data;
    private volatile ID2DataFromDB t2Data;
    private volatile ID2DataFromDB t3Data;
    private volatile ID2DataFromDB t4Data;
    private volatile ID2DataFromDB t5Data;

    //数据库信息对象的属性array，构造器里获得一次就行了
    private String[] fieldNames;

    //构造器
    public FetchID2DataFromDB() {
        //从class得到属性名字的array
        this.setNames();
        //打开配置文件
        try {
            this.is = Resources.getResourceAsStream("DatabaseConfig.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //打开配置文件中的test1，也就是使用第一个database
        factory = new SqlSessionFactoryBuilder().build(is,"test1");
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
        }
        //打开配置文件中的test1，也就是使用第一个database
        factory = new SqlSessionFactoryBuilder().build(is, "test1");
    }

    //从数据库获取前5行数据，也就是风机1-5的数据，对应id2-6
    private void setDataList(){
        this.dataList = dataDao.findFirstFive();
    }
    //获取每条数据
    private void setTnData(){
        this.t1Data = dataDao.findFirstFive().get(0);
        this.t2Data = dataDao.findFirstFive().get(1);
        this.t3Data = dataDao.findFirstFive().get(2);
        this.t4Data = dataList.get(3);
        this.t5Data = dataList.get(4);
    }

    //得到名字array的方法
    private void setNames(){
        this.fieldNames = HuoUtils.getFiledNameByClz("com.junshijia.HuoV3.domain.ID2DataFromDB");
    }
    //返回名字array
    public String[] getFieldNames() {
        return fieldNames;
    }

    //得到数据的容器
    public List<ID2DataFromDB> getDataList() {
        return dataList;
    }

    public ID2DataFromDB getT1Data() {
        return t1Data;
    }

    public ID2DataFromDB getT2Data() {
        return t2Data;
    }

    public ID2DataFromDB getT3Data() {
        return t3Data;
    }

    public ID2DataFromDB getT4Data() {
        return t4Data;
    }

    public ID2DataFromDB getT5Data() {
        return t5Data;
    }

    //循环从数据库获取，暂定2秒一次可
    @Override
    public void run() {
        while (true) {
            this.fetchInfo();

            //6 休息2秒
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //从数据库获取一次数据
    public void fetchInfo(){
        boolean flag = true;
        while(flag) {
            try {
                //0 得到session
                session = factory.openSession();
                dataDao = session.getMapper(ID2DataDao.class);
                //1 set data
                this.setDataList();
                this.setTnData();
                //2 清除缓存
                //利用update机制可以不重开session
                //enDataDao.updateData(testData);
                session.commit();
                session.clearCache();
                //测试：输出结果
                //System.out.println(data);
                //3 关闭session
                session.close();
                flag = false;
            } catch (Exception e) {
                System.out.println("turbine DB error, wait 5mins and reconnect...");
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
