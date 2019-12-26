package com.junshijia.HuoV3.dao;

import com.junshijia.HuoV3.domain.ID2DataFromDB;

import java.util.List;

public interface ID2DataDao {
    ID2DataFromDB findLast();
    List<ID2DataFromDB> findFirstFive();
}
