package com.junshijia.HuoV3.dataTransfer;

import com.junshijia.HuoV3.domain.*;
import com.junshijia.HuoV3.process.CSVDataListsArray;
import com.junshijia.HuoV3.util.HuoUtils;
import sun.nio.cs.SingleByte;

import java.util.List;

public class UpdateCSVDataLists {
    //1级结构
    //来自csv的7个对象，2-6个放进一个list方便循环，对象固定一条，不会变，只是不停更新；
    private CSVDataListsArray allDataLists;
    //来自DB的7个对象，2-5也在一个list中，对象不固定，不停从db获取新对象，需要循环；
    private FetchEMSDataFromDB fetchEMS;
    private FetchID2DataFromDB fetchTurbine;

    //2级别结构-1，来自csv，需要不断被更新，但是是同一个对象

    private volatile List<CSV2DataLists> id1to7Lists;
    private volatile CSV2DataLists id1List;
    private volatile CSV2DataLists id2List;
    private volatile CSV2DataLists id3List;
    private volatile CSV2DataLists id4List;
    private volatile CSV2DataLists id5List;
    private volatile CSV2DataLists id6List;
    private volatile boolean[] status;

    //得到id = 7 的csv对应的object
    private CSV2DataLists id7List;

    //3级别结构
    //2-6
    private String[] id2to6Fieldname;
    //1和7共3个名字
    private String[] enFieldname;
    private String[] tshowFieldname;
    private String[] fshowFieldname;

    public UpdateCSVDataLists() {
        //初始化1级结构
        this.allDataLists = new CSVDataListsArray();
        this.fetchEMS = new FetchEMSDataFromDB();
        this.fetchTurbine = new FetchID2DataFromDB();

        //初始化2级结构-1
        this.id1List = this.allDataLists.getList1();
        this.id1to7Lists = this.allDataLists.getListArray1to7();
        this.id2List = this.allDataLists.getList2();
        this.id3List = this.allDataLists.getList3();
        this.id4List = this.allDataLists.getList4();
        this.id5List = this.allDataLists.getList5();
        this.id6List = this.allDataLists.getList6();
        this.id7List = this.allDataLists.getList7();

        //初始化3级结构
        this.id2to6Fieldname = this.fetchTurbine.getFieldNames();
        this.enFieldname = this.fetchEMS.getEnFieldNames();
        this.tshowFieldname = this.fetchEMS.getTshowFieldNames();
        this.fshowFieldname = this.fetchEMS.getFshowFieldNames();
        this.status = new boolean[5];
    }

    //更新一次list1
    private void updateList1(){
        HuoUtils.updateDataList_hasData(fetchEMS.getEnData(),id1List.getDataList_hasData(),enFieldname);
        HuoUtils.updateDataList_hasData(fetchEMS.getTshowData(),id1List.getDataList_hasData(),tshowFieldname);
    }

    //更新一次list-2
    private void updateList2(){
        HuoUtils.updateDataList_hasData(fetchTurbine.getT1Data(),id2List.getDataList_hasData(),id2to6Fieldname);
        HuoUtils.updateDataList_hasData(fetchTurbine.getT2Data(),id3List.getDataList_hasData(),id2to6Fieldname);
    }
    //更新一次list-3
    private void updateList3(){
        HuoUtils.updateDataList_hasData(fetchTurbine.getT2Data(),id3List.getDataList_hasData(),id2to6Fieldname);
    }

    //更新一次list-4
    private void updateList4(){
        HuoUtils.updateDataList_hasData(fetchTurbine.getT3Data(),id4List.getDataList_hasData(),id2to6Fieldname);
    }

    //更新一次list-5
    private void updateList5(){
        HuoUtils.updateDataList_hasData(fetchTurbine.getT4Data(),id5List.getDataList_hasData(),id2to6Fieldname);
    }

    //更新一次list-6
    private void updateList6(){
        HuoUtils.updateDataList_hasData(fetchTurbine.getT5Data(),id6List.getDataList_hasData(),id2to6Fieldname);
    }


    //更新一次list7
    private void updateList7(){
        HuoUtils.updateDataList_hasData(fetchEMS.getEnData(),id7List.getDataList_hasData(),enFieldname);
        HuoUtils.updateDataList_hasData(fetchEMS.getFshowData(),id7List.getDataList_hasData(),fshowFieldname);
    }

    private void updateList1and7(){
        while(true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //list1已经被废弃掉了 19-11-4
            //updateList1();
            updateList7();
        }
    }

    public boolean[] getStatus() {
        return status;
    }

    private void setStatus(){
        int[] count = new int[5];
        char[] before = new char[5];
        char[] after = new char[5];
        for(int i = 0; i < before.length; i++){
            before[i] = 0;
            after[i] = 0;
            count[i] = 0;
        }

        while(true){
            try {
                after[0] = this.getFetchTurbine().getT1Data().getTime().charAt(22);
                after[1] = this.getFetchTurbine().getT2Data().getTime().charAt(22);
                after[2] = this.getFetchTurbine().getT3Data().getTime().charAt(22);
                after[3] = this.getFetchTurbine().getT4Data().getTime().charAt(22);
                after[4] = this.getFetchTurbine().getT5Data().getTime().charAt(22);
                for(int i = 0; i < after.length;i++){
                    if(after[i] == before[i]){
                        count[i]++;
                    }else{
                        count[i] = 0;
                    }
                    before[i] = after[i];
                    if(count[i] > 4){
                        this.status[i] = false;
                        count[i] = 6;
                    }else{
                        this.status[i] = true;
                    }
                }
                Thread.sleep(300);
            } catch (Exception e) {
                System.out.println("w8 2 seconds, set status error...");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void combine(){
        Thread t1 = new Thread(this.fetchTurbine);
        t1.start();
        Thread t2 = new Thread(this.fetchEMS);
        t2.start();
        Thread t3 = new Thread(this::updateList1and7);
        t3.start();
        Thread t7 = new Thread(this::setStatus);
        t7.start();

        Thread t4 = new Thread(()->{
            while(true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                updateList2();
                updateList3();
            }
        });
        t4.start();

        Thread t5 = new Thread(()->{
            while(true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                updateList4();
                updateList5();
            }
        });
        t5.start();

        Thread t6 = new Thread(()->{
            while(true) {
                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                updateList6();
            }
        });
        t6.start();

        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();
            t6.join();
            t7.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void combine2(){
        new Thread(this.fetchTurbine).start();
        new Thread(this.fetchEMS).start();
        while(true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //updateList1();
            updateList2();
            updateList3();
            updateList4();
            updateList5();
            updateList6();
            updateList7();
        }
    }

    public CSV2DataLists getId1List() {
        return id1List;
    }

    public CSV2DataLists getId2List() {
        return id2List;
    }

    public CSV2DataLists getId3List() {
        return id3List;
    }

    public CSV2DataLists getId4List() {
        return id4List;
    }

    public CSV2DataLists getId5List() {
        return id5List;
    }

    public CSV2DataLists getId6List() {
        return id6List;
    }

    public CSV2DataLists getId7List() {
        return id7List;
    }

    public List<CSV2DataLists> getId1to7Lists() {
        return id1to7Lists;
    }

    public FetchEMSDataFromDB getFetchEMS() {
        return fetchEMS;
    }

    public FetchID2DataFromDB getFetchTurbine() {
        return fetchTurbine;
    }
}
