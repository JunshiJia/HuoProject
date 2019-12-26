package com.junshijia.HuoV3.domain;

public class DataFromCSVShort {

    //modbus slave address eg:10004
    private int slaveAddress;

    //eg:RP_Control_EN(东方电表里的第12列)
    private String databaseColumn;

    //value from database(float 和int两种)
    private volatile Number value;

    @Override
    public String toString() {
        return "DataFromCSVShort{" +
                "slaveAddress=" + slaveAddress +
                ", databaseColumn='" + databaseColumn + '\'' +
                ", value=" + value +
                '}';
    }

    public DataFromCSVShort() {
    }

    public int getSlaveAddress() {
        return slaveAddress;
    }

    public void setSlaveAddress(int slaveAddress) {
        this.slaveAddress = slaveAddress;
    }

    public String getDatabaseColumn() {
        return databaseColumn;
    }

    public void setDatabaseColumn(String databaseColumn) {
        this.databaseColumn = databaseColumn;
    }

    public Number getValue() {
        return value;
    }

    public void setValue(Number value) {
        this.value = value;
    }


}
