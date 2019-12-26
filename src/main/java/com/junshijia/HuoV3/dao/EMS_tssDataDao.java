package com.junshijia.HuoV3.dao;


import com.junshijia.HuoV3.domain.EMS_enDataFromDB;
import com.junshijia.HuoV3.domain.EMS_tssDataFromDB;

public interface EMS_tssDataDao {

    /**
     * update
     *
     * @return
     */
    //写数据库
    void updateData(EMS_tssDataFromDB data);
}
