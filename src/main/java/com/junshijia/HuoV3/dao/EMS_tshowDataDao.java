package com.junshijia.HuoV3.dao;

import com.junshijia.HuoV3.domain.EMS_tshowDataFromDB;

public interface EMS_tshowDataDao {
    /**
     * 查询最后一条操作
     * @return
     */
    EMS_tshowDataFromDB findLast();

    //写数据库
    //void updateData(EMS_tshowDataFromDB data);
}
