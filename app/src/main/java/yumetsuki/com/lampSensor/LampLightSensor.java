package yumetsuki.com.lampSensor;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import yumetsuki.com.Data.LightSensorData;

public class LampLightSensor extends LampSensor<LightSensorData>{

    private Timer timer;

    /**
     * 开始监听，使用timer开启一个任务*/
    @Override
    public void startListen() {

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {

                    //请把代码只写在这行注释下

                    //请把代码只写在这行注释上

                    // 请在获取到亮度值后调用此方法(注意修改)
                    lampListener.onStateChange(new LightSensorData(0));
                }catch (Exception e){
                    Log.i("233","no View");
                }
            }
        },0,period);

    }

    /**停止监听，即取消timer的任务*/
    @Override
    public void stopListen() {
        timer.cancel();
    }
}
