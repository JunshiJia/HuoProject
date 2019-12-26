package com.junshijia.HuoV3.dao;

import com.junshijia.HuoV3.domain.EMS_fshowDataFromDB;

public interface EMS_fshowDataDao {
    /**
     * 查询最后一条操作
     * @return
     */
    EMS_fshowDataFromDB findLast();

    //写数据库
    //void updateData(EMS_fshowDataFromDB data);
}
