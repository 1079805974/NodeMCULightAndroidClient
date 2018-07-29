package yumetsuki.com.lampSensor;

import java.util.Timer;

public abstract class LampSensor<T>{

    /**
     * 监听间隔。单位为ms*/
    protected int period = 1000;

    /**
     * 监听回调，用于视图更新*/
    protected OnStateChangeListener<T> lampListener;

    /**
     * 设置监听回调*/
    public LampSensor<T> setOnStateChangeListener(OnStateChangeListener<T> lampListener){
        this.lampListener = lampListener;
        return this;

    }

    private boolean isOpen = false;

    /**返回监听器是否打开
     * @return 返回监听器的状态*/
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * 设置监听器的状态*/
    public void setOpen(boolean open) {
        isOpen = open;
    }

    /**
     * 监听器*/
    public interface OnStateChangeListener<T>{
        void onStateChange(T t);
    }

    /**
     * 开启监听*/
    public abstract void startListen();

    /**
     * 停止监听(并不销毁监听器实例)*/
    public abstract void stopListen();

    /**
     * 设置监听间隔
     * @param period 监听间隔，单位为ms
     * @return 返回传感器本身*/
    public LampSensor<T> setPeriod(int period) {
        this.period = period;
        return this;
    }
}
