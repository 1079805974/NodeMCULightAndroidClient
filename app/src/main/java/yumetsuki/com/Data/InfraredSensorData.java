package yumetsuki.com.Data;

public class InfraredSensorData {

    public InfraredSensorData(boolean isHasPerson) {
        this.isHasPerson = isHasPerson;
    }

    //有人和无人状态
    private boolean isHasPerson = false;

    public boolean isHasPerson() {
        return isHasPerson;
    }


}
