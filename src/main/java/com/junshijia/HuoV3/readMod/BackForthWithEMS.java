package com.junshijia.HuoV3.readMod;


import com.junshijia.HuoV3.domain.FeedbackFromMod;
import com.junshijia.HuoV3.util.HuoUtils;

public class BackForthWithEMS implements Runnable{
    private ReadSlave r;
    private SendData2EMSMod s;
    private FeedbackFromMod feedbackData;
    private String[] feedbackDataFieldNames;

    public BackForthWithEMS() {
        this.r = new ReadSlave();
        this.s = new SendData2EMSMod();
        this.feedbackDataFieldNames = HuoUtils.getFiledNameByClz("com.junshijia.HuoV3.domain.FeedbackFromMod");
    }

    public String[] getFeedbackDataFieldNames() {
        return feedbackDataFieldNames;
    }

    public FeedbackFromMod getFeedbackData() {
        return feedbackData;
    }

    public void fetchData(){
        //读已经有的modbus
        this.r.readFromSlave2();
        //得到读取的有功无功
        this.s.setRealPower(this.r.getResult1());
        this.s.setReactivePower(this.r.getResult2());
        //发送有功无功到ecs，然后从ecs读反馈值
        this.s.send2ModAndReadFeedback();
        //System.out.println(s.getFeedbackData().toString());
        //得到反馈值对象
        this.feedbackData = this.s.getFeedbackData();
    }

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.fetchData();
        }
    }
}
