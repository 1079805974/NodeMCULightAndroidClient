package yumetsuki.com.Data

import yumetsuki.com.lampSensor.InfraredSensor
import yumetsuki.com.lampSensor.LampLightSensor
import java.io.Serializable

class Lamp(id:String): Serializable{

    /**
     * 默认颜色，会在设置时更新灯的颜色*/
    var defaultColor = DefaultColor.LIGHT
        set(value) {
            field = value
            red = value.r
            green = value.g
            blue = value.b
        }

    /**
     * 灯的id*/
    var id = id

    /**
     * 灯名，用于在ui上对它们进行区分*/
    var name: String = ""

    /**
     * 灯的开关状态*/
    var isOpen:Boolean = false //开关

    /**
     * 是否自定义灯的颜色*/
    var isSelfColor = false //自定义颜色

    /**
     * 灯的亮度*/
    var lux:Int = 0

    /**
     * 红色值0-255*/
    var red: Int = 255

    /**
     * 绿色值0-255*/
    var green: Int = 255

    /**
     * 蓝色值0-255*/
    var blue: Int = 255

    /**
     * 时刻表*/
    var schedule: Schedule = Schedule()

    /**
     * 亮度传感器*/
    var lightSensor: LampLightSensor? = null

    /**
     * 红外传感器*/
    var infraredSensor: InfraredSensor? = null

    /**
     * 设置颜色
     * @param red 红色
     * @param green 绿色
     * @param blue 蓝色*/
    fun setColor(red: Int, green: Int, blue: Int) {
        this.red=red
        this.green=green
        this.blue=blue
    }

    /**
     * 枚举类，默认颜色*/
    enum class DefaultColor(val r:Int,val g:Int,val b:Int){
        LIGHT(255,255,255),
        BLUE(3,169,244),
        GREEN(139,195,74)
    }

}

class Schedule : Serializable {

    /**
     * 小时*/
    var hour: Int = 0

    /**
     * */
    var minute: Int = 0

    /**
     * 是否在周一开启*/
    var isOpenMonday: Boolean = false

    /**
     * 是否在周二开启*/
    var isOpenTuesday: Boolean = false

    /**
     * 是否在周三开启*/
    var isOpenWednesday: Boolean = false

    /**
     * 是否在周四开启*/
    var isOpenThursday: Boolean = false

    /**
     * 是否在周五开启*/
    var isOpenFriday: Boolean = false

    /**
     * 是否在周六开启*/
    var isOpenSaturday: Boolean = false

    /**
     * 是否在周日开启*/
    var isOpenSunday: Boolean = false

    /**
     * 格式化字符串，用于描述时刻表信息*/
    override fun toString(): String {

        val flag = (!isOpenMonday
                and !isOpenTuesday
                and !isOpenWednesday
                and !isOpenThursday
                and !isOpenFriday
                and !isOpenSaturday
                and !isOpenSunday)

        if (flag){
            return "选择时间"
        }

        val builder = StringBuilder()
        if (isOpenMonday) {
            builder.append("${MONDAY}、")
        }
        if (isOpenTuesday) {
            builder.append("${TUESDAY}、")
        }
        if (isOpenWednesday) {
            builder.append("${WEDNESDAY}、")

        }
        if (isOpenThursday) {
            builder.append("${THURSDAY}、")
        }
        if (isOpenFriday) {
            builder.append("${FRIDAY}、")
        }
        if (isOpenSaturday) {
            builder.append("${SATURDAY}、")
        }
        if (isOpenSunday) {
            builder.append("$SUNDAY-")
        }
        builder.append(hour.toString() + " : ")
        if (minute.toString().length == 1) {
            builder.append("0$minute")
        } else {
            builder.append(minute)
        }
        return builder.toString()
    }

    companion object {

        private const val MONDAY = "周一"
        private const val TUESDAY = "周二"
        private const val WEDNESDAY = "周三"
        private const val THURSDAY = "周四"
        private const val FRIDAY = "周五"
        private const val SATURDAY = "周六"
        private const val SUNDAY = "周日"
    }

}
