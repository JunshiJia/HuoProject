package com.junshijia.HuoV3.dao;


import com.junshijia.HuoV3.domain.EMS_enDataFromDB;

public interface EMS_enDataDao {

    /**
     * 查询最后一条操作
     * @return
     */
    EMS_enDataFromDB findLast();

    //写数据库
    //void updateData(EMS_enDataFromDB data);
}
