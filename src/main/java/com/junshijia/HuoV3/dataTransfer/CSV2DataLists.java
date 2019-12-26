package com.junshijia.HuoV3.dataTransfer;

import com.junshijia.HuoV3.domain.DataFromCSVShort;
import com.junshijia.HuoV3.util.HuoUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSV2DataLists {
    private String path;
    //list of data that unavailable
    private List<DataFromCSVShort> dataList_noData;
    //list of data that available
    private volatile List<DataFromCSVShort> dataList_hasData;

    public List<DataFromCSVShort> getDataList_noData() {
        return dataList_noData;
    }

    public List<DataFromCSVShort> getDataList_hasData() {
        return dataList_hasData;
    }



    public CSV2DataLists() {
        this.path = "id7.csv";
        dataList_noData = new ArrayList<>();
        dataList_hasData = new ArrayList<>();
        this.getData();
    }

    public CSV2DataLists(String path) {
        this.path = path;
        dataList_noData = new ArrayList<>();
        dataList_hasData = new ArrayList<>();
        this.getData();
    }

    private void getData(){
        String[] values = null;
        DataFromCSVShort farmData = null;
        try (
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(path), "UTF-8"))
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                values = line.split(",");
                if(HuoUtils.isInteger(values[0])){
                    farmData = new DataFromCSVShort();
                    farmData.setDatabaseColumn(HuoUtils.deleteChar(values[12]));
                    farmData.setSlaveAddress(Integer.parseInt(values[7]));
                    //turbineData.setDatabaseColumn(null);
                    //add to big list
                    if(values[11].equals("无")) {
                        //add to data unavailable list
                        this.getDataList_noData().add(farmData);
                    }else{
                        //add to available list
                        this.getDataList_hasData().add(farmData);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
