package com.junshijia.HuoV3.process;

import com.junshijia.HuoV3.dataTransfer.CSV2DataLists;
import com.junshijia.HuoV3.domain.DataFromCSVShort;

import java.util.ArrayList;
import java.util.List;

public class CSVDataListsArray {
    private volatile CSV2DataLists list1;
    private volatile CSV2DataLists list2;
    private volatile CSV2DataLists list3;
    private volatile CSV2DataLists list4;
    private volatile CSV2DataLists list5;
    private volatile CSV2DataLists list6;
    private volatile List<CSV2DataLists> listArray2to6;
    private volatile List<CSV2DataLists> listArray1to7;
    private volatile CSV2DataLists list7;

    public CSVDataListsArray() {
        this.listArray2to6 = new ArrayList<>();
        this.listArray1to7= new ArrayList<>();
        this.list1 = new CSV2DataLists("id1.csv");
        this.list2 = new CSV2DataLists("id2to6.csv");
        this.list3 = new CSV2DataLists("id2to6.csv");
        this.list4 = new CSV2DataLists("id2to6.csv");
        this.list5 = new CSV2DataLists("id2to6.csv");
        this.list6 = new CSV2DataLists("id2to6.csv");
        this.list7 = new CSV2DataLists("id7.csv");

        this.listArray1to7.add(list1);
        this.listArray1to7.add(list2);
        this.listArray1to7.add(list3);
        this.listArray1to7.add(list4);
        this.listArray1to7.add(list5);
        this.listArray1to7.add(list6);
        this.listArray1to7.add(list7);

        this.listArray2to6.add(list2);
        this.listArray2to6.add(list3);
        this.listArray2to6.add(list4);
        this.listArray2to6.add(list5);
        this.listArray2to6.add(list6);
    }

    public CSV2DataLists getList1() {
        return list1;
    }

    public CSV2DataLists getList2() {
        return list2;
    }

    public CSV2DataLists getList3() {
        return list3;
    }

    public CSV2DataLists getList4() {
        return list4;
    }

    public CSV2DataLists getList5() {
        return list5;
    }

    public CSV2DataLists getList6() {
        return list6;
    }

    public List<CSV2DataLists> getListArray2to6() {
        return listArray2to6;
    }

    public List<CSV2DataLists> getListArray1to7() {
        return listArray1to7;
    }

    public CSV2DataLists getList7() {
        return list7;
    }

    @Override
    public String toString() {
        return "CSVDataListsArray{" +
                "list1=" + list1 +
                ", listArray2to6=" + listArray2to6 +
                ", list7=" + list7 +
                '}';
    }
}
