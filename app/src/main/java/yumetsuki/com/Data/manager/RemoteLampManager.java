package yumetsuki.com.Data.manager;

import java.util.ArrayList;

import yumetsuki.com.Data.Lamp;
import yumetsuki.com.Data.Schedule;
import yumetsuki.com.lampSensor.InfraredSensor;
import yumetsuki.com.lampSensor.LampLightSensor;

public class RemoteLampManager {

    public static final int LIGHT_SENSOR = 0;
    public static final int INFRARED_SENSOR = 1;

    private static RemoteLampManager remoteLampManager=new RemoteLampManager();

    private ArrayList<Lamp> lamps;

    private Lamp currentLamp;

    private int currentLampIndex;

    public static RemoteLampManager getInstance(){
        return remoteLampManager;
    }

    public RemoteLampManager(){
        lamps=new ArrayList<>();
    }


    /**
     * 连接一个新的灯*/
    public void linkNewLamp(){
        //请把代码只写在这行注释下

        //请把代码只写在这行注释上

        /*连接完成之后添加加新的灯的实例，并将当前的操作灯转换为新的灯*/
        Lamp lamp = new Lamp("");
        lamps.add(lamp);
        lamp.setName("智能灯");
        setCurrentLamp(lamps.size()-1);
    }

    public void setCurrentLamp(int index){
        if (index < 0){
            currentLamp = null;
            return;
        }
        currentLamp=lamps.get(index);
        currentLampIndex = index;
    }

    public int getCurrentLampIndex() {
        return currentLampIndex;
    }

    /**
     * 设置颜色
     * @param r 红色值
     * @param g 绿色值
     * @param b 蓝色值*/
    public void setColor(int r, int g, int b){

        //请把代码只写在这行注释下

        //请把代码只写在这行注释上
        currentLamp.setColor(r,g,b);
        System.out.print(r + "" + g+ "" +b);
    }

    /**
     * 设置定时
     * @param schedule 定时时刻表*/
    public void setSchedule(Schedule schedule){

        //请把代码只写在这行注释下

        //请把代码只写在这行注释上
        currentLamp.setSchedule(schedule);
    }

    /**
     * 设置亮度
     * @param lux 亮度值
     * */
    public void setLux(int lux){

        //请把代码只写在这行注释下

        //请把代码只写在这行注释上
        currentLamp.setLux(lux);

    }

    public void setOpen(boolean flag){

        //请把代码只写在这行注释下

        //请把代码只写在这行注释上
        currentLamp.setOpen(flag);
    }

    public ArrayList<Lamp> getLamps() {
        return lamps;
    }

    public Lamp getCurrentLamp() {
        return currentLamp;
    }

    public void removeCurrentLamp(){

        //请把代码只写在这行注释下

        //请把代码只写在这行注释上
        lamps.remove(currentLamp);
    }

    public LampLightSensor newLampLightSensor(){
        if (currentLamp.getLightSensor() == null){
            currentLamp.setLightSensor(new LampLightSensor());
        }
        return currentLamp.getLightSensor();
    }

    public InfraredSensor newInfraredSensor(){
        if (currentLamp.getInfraredSensor() == null){
            currentLamp.setInfraredSensor(new InfraredSensor());
        }
        return currentLamp.getInfraredSensor();
    }

}
