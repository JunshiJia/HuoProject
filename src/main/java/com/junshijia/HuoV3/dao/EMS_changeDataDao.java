package com.junshijia.HuoV3.dao;


import com.junshijia.HuoV3.domain.EMS_changeDataFromDB;

public interface EMS_changeDataDao {

    /**
     * update
     *
     * @return
     */
    //写数据库
    void updateData(EMS_changeDataFromDB data);
}
