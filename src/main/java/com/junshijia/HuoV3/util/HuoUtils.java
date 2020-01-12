package com.junshijia.HuoV3.util;

import com.junshijia.HuoV3.domain.DataFromCSVShort;
import com.junshijia.HuoV3.domain.FeedbackFromMod;
import com.junshijia.HuoV3.domain.ID2DataFromDB;
import com.serotonin.modbus4j.BasicProcessImage;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.code.RegisterRange;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.msg.ReadCoilsRequest;
import com.serotonin.modbus4j.msg.ReadCoilsResponse;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class HuoUtils {
    //判断字符串是否是数字
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    //删除中括号
    public static String deleteChar(String str) {
        String delStr = "";
        char delChar1 = '[', delChar2= ']';
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != delChar1 && str.charAt(i) != delChar2 ) {
                delStr += str.charAt(i);
            }
        }
        return delStr;
    }

    /**
     * 获取属性名数组
     * */
    public static String[] getFiledName(Object o){
        Field[] fields=o.getClass().getDeclaredFields();
        String[] fieldNames=new String[fields.length];
        for(int i=0;i<fields.length;i++){
            //System.out.println(fields[i].getType());
            fieldNames[i]=fields[i].getName();
        }
        return fieldNames;
    }

    //获取类的属性名的字符串
    public static String[] getFiledNameByClz(String path){
        Class clz = null;
        try {
            clz = Class.forName(path);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Field[] fields=clz.getDeclaredFields();
        String[] fieldNames=new String[fields.length];
        for(int i=0;i<fields.length;i++){
            //System.out.println(fields[i].getType());
            fieldNames[i]=fields[i].getName();
        }
        return fieldNames;
    }

    //获取modbus slave中，id = n的processimage
    public static BasicProcessImage getModscanProcessImage(int slaveId) {
        BasicProcessImage processImage = new BasicProcessImage(slaveId);
        processImage.setAllowInvalidAddress(true);
        processImage.setInvalidAddressValue(Short.MIN_VALUE);
        processImage.setExceptionStatus((byte) 151);

        return processImage;
    }

    //获取field
    public static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, null);
            Object value = method.invoke(o, null);
            return value;
        } catch (Exception e) {
            return null;
        }
    }
    //测试，另一种由属性名获得属性值的方法 返回单个object
    public static Object getNumberValueByName2(String fieldName, Object o) {
        Object value = new Object();
        try {
            Field f = o.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            value = f.get(o);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return value;
    }

    //更新id=n的Image中有值的reg
    public static void updateProcessImage4HasData(BasicProcessImage processImage, List<DataFromCSVShort> avaList){
        int regNum = 0;
        for(DataFromCSVShort data : avaList){
            //3开头
            if(data.getSlaveAddress()<40000 && data.getSlaveAddress()>30000){//input reg
                regNum = data.getSlaveAddress()-30001;
                if(data.getValue()!=null) {
                    //小于1000用浮点
                    if (regNum < 1000) {
                        processImage.setNumeric(RegisterRange.INPUT_REGISTER, regNum,
                                DataType.FOUR_BYTE_FLOAT_SWAPPED, data.getValue().floatValue());
                    } else {//大于1000用long
                        processImage.setNumeric(RegisterRange.INPUT_REGISTER, regNum,
                                DataType.EIGHT_BYTE_INT_UNSIGNED, data.getValue().intValue());
                    }
                }
            }
            //1开头
            else if(data.getSlaveAddress()>10000 && data.getSlaveAddress()<20000){   //input status
                regNum = data.getSlaveAddress()-10000;
                if(data.getValue()!=null && data.getValue().equals(1))
                    processImage.setInput(regNum,true);
                else
                    processImage.setInput(regNum,false);
            }
            //4开头
            else if(data.getSlaveAddress()>40000 && data.getSlaveAddress()<50000){//HOLDING_REGISTER
                regNum = data.getSlaveAddress()-40001;
                if(data.getValue()!=null)
                    processImage.setNumeric(RegisterRange.HOLDING_REGISTER,regNum,
                            DataType.FOUR_BYTE_FLOAT_SWAPPED,data.getValue().floatValue());
            }
            //0开头
            else if(data.getSlaveAddress()<10000){   //coil
                regNum = data.getSlaveAddress() - 1;
                if(data.getValue()!=null && data.getValue().equals(1))
                    processImage.setCoil(regNum,true);
                else
                    processImage.setCoil(regNum,false);
            }else{
                System.out.println("slave address read error...");
            }
        }
    }

    //初始化300个点
    public static void initID2to6(BasicProcessImage processImage){
        for(int i = 0; i < 300; i++){
            processImage.setInput(i,false);
            processImage.setNumeric(RegisterRange.INPUT_REGISTER,i, DataType.TWO_BYTE_INT_UNSIGNED,0);
            processImage.setNumeric(RegisterRange.HOLDING_REGISTER,i, DataType.TWO_BYTE_INT_UNSIGNED,0);
        }
    }

    //更新id=n的Image中无值的reg，只运行一次
    public static void updateProcessImage4NoData(BasicProcessImage processImage, List<DataFromCSVShort> nonAvaList){
        int regNum = 0;
        //1,2开头的不管，3，4开头的设为-99.99
        for(DataFromCSVShort data : nonAvaList){
            if(data.getSlaveAddress()<40000 && data.getSlaveAddress()>30000){
                regNum = data.getSlaveAddress()-30000;
                processImage.setNumeric(RegisterRange.INPUT_REGISTER,regNum, DataType.FOUR_BYTE_FLOAT,0);
            }else if(data.getSlaveAddress()>40000 && data.getSlaveAddress()<50000){
                regNum = data.getSlaveAddress()-40000;
                processImage.setNumeric(RegisterRange.HOLDING_REGISTER,regNum, DataType.FOUR_BYTE_FLOAT,0);
            }
        }
    }

    //从内存中已经获取到的数据库信息，得到数据，更新到list中：update info into dataList_hasData
    public static void updateDataList_hasData(Object data, List<DataFromCSVShort> hasDataList, String[] fieldNames){
        //容器*2
        String name;
        Number value;
        //对csv中每一条进行循环
        for(DataFromCSVShort hasData : hasDataList){
            for(int i=0 ; i<fieldNames.length; i++){     //遍历所有属性名
                name = fieldNames[i]; //获取属性的名字
                if(name.equals(hasData.getDatabaseColumn())) {
                    //System.out.println("same!!!!!!!!!!!!!!");
                    value = (Number) HuoUtils.getFieldValueByName(name, data);
                    //System.out.println(value.toString());
                    hasData.setValue(value);
                }
            }
            //检查用
            //if(hasData.getValue() != null)
            //    System.out.println(hasData.toString());
        }
    }

    //从ems得到的feedbackdata，更新id=7的Image中的一些值
    public static void updateProcessImageFromFeedbackData(BasicProcessImage processImage, FeedbackFromMod feedbackData){
        //写来自ems modbus的值
        if(feedbackData!=null) {
            processImage.setNumeric(RegisterRange.INPUT_REGISTER, 19, DataType.FOUR_BYTE_FLOAT, (float) feedbackData.getPowerUpperBound());
            processImage.setNumeric(RegisterRange.INPUT_REGISTER, 21, DataType.FOUR_BYTE_FLOAT, (float) feedbackData.getPowerLowerBound());
            processImage.setNumeric(RegisterRange.INPUT_REGISTER, 49, DataType.FOUR_BYTE_FLOAT, (float) feedbackData.getLimitPowerTurbineNumber());
            processImage.setNumeric(RegisterRange.INPUT_REGISTER, 51, DataType.FOUR_BYTE_FLOAT, (float) feedbackData.getLimitPowerCap());
            //processImage.setNumeric(RegisterRange.INPUT_REGISTER,21, DataType.FOUR_BYTE_FLOAT,(float)feedbackData.getAdjustableCap());
        }
    }

    public static void writeEMSConstants(BasicProcessImage processImage){
        processImage.setNumeric(RegisterRange.INPUT_REGISTER,9, DataType.FOUR_BYTE_FLOAT, 20200);
        processImage.setNumeric(RegisterRange.INPUT_REGISTER,25, DataType.FOUR_BYTE_FLOAT,9000);
        //agc avc
        processImage.setInput(2,true);
        processImage.setInput(3,true);
        processImage.setHoldingRegister(0,(short)20000);
    }
}
