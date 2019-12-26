import com.junshijia.HuoV3.dataTransfer.CSV2DataLists;
import com.junshijia.HuoV3.dataTransfer.FetchEMSDataFromDB;
import com.junshijia.HuoV3.dataTransfer.FetchID2DataFromDB;
import com.junshijia.HuoV3.dataTransfer.UpdateCSVDataLists;
import com.junshijia.HuoV3.domain.DataFromCSVShort;
import com.junshijia.HuoV3.extraEMS.RWMySlave;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import oracle.jrockit.jfr.jdkevents.ThrowableTracer;
import org.junit.Test;

import java.sql.SQLOutput;
import java.util.List;

public class test {
    //测试从3个csv表到3个csv对应的DataFromCSVShort对象
    @Test public void CSV2Objtest(){
        UpdateCSVDataLists process = new UpdateCSVDataLists();
        Thread t1 = new Thread(process::combine);
        t1.start();

        while(true){
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(process.getId1List().getDataList_hasData().toString());
            System.out.println(process.getId7List().getDataList_hasData().toString());
            System.out.println(process.getId2List().getDataList_hasData().toString());
            System.out.println(process.getId3List().getDataList_hasData().toString());
            System.out.println(process.getId4List().getDataList_hasData().toString());
            System.out.println(process.getId5List().getDataList_hasData().toString());
            System.out.println(process.getId6List().getDataList_hasData().toString());
        }
    }

    @Test
    public void db() {
        UpdateCSVDataLists update = new UpdateCSVDataLists();
        new Thread(update::combine2).start();

        while(true){
            System.out.println(update.getId3List().getDataList_hasData().toString());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    @Test
    public void turbineState() {



    }
}
