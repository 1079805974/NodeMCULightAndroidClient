package yumetsuki.com.lampsettingsui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import yumetsuki.com.Data.InfraredSensorData;
import yumetsuki.com.Data.Lamp;
import yumetsuki.com.Data.LightSensorData;
import yumetsuki.com.Data.manager.RemoteLampManager;
import yumetsuki.com.lampSensor.InfraredSensor;
import yumetsuki.com.lampSensor.LampLightSensor;
import yumetsuki.com.lampSensor.LampSensor;
import yumetsuki.com.lampsettingsui.R;

public class LampSensorUIFragment extends Fragment {

    private LineChart mSensorChart;

    private List<Entry> entries=new ArrayList<>();

    private SwitchCompat mSensorSwitch;

    private AppCompatButton mSensorSwapBtn;

    private TextView mSensorTitle;

    private TextView mInfraredSensorTextView;

    private PopupMenu mPopupMenu;

    private RemoteLampManager remoteLampManager = RemoteLampManager.getInstance();

    private LampLightSensor lampLightSensor;

    private InfraredSensor infraredSensor;

    private int currentSensorType = RemoteLampManager.LIGHT_SENSOR;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lamp_sensor_fragment,container,false);
        initView(view);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroySensor();
    }

    private void initChart(){
        setX();
        setY();

        mSensorChart.setDrawGridBackground(true);
        mSensorChart.setGridBackgroundColor(Color.rgb(255,255,255));
        mSensorChart.setNoDataTextDescription("无数据");  //设置无数据时显示的描述信息

        mSensorChart.setDrawGridBackground(false); //设置是否绘制网格背景

        //设置图表是否缩放
        mSensorChart.setScaleEnabled(true);

        //设置图表的描述信息
        mSensorChart.setDescription("折线图");

        //设置描述信息的字体颜色
        mSensorChart.setDescriptionColor(
                getResources().getColor(R.color.newPrimary, getResources().newTheme())
        );
        //mSensorChart.setDescriptionPosition(550,330);     //设置描述信息的显示位置
        mSensorChart.setDescriptionTextSize(12);          //设置描述信息的字体大小

    }

    private void upDateChart(int lux){
        if (entries.size() < 4){
            entries.add(new Entry(entries.size(),lux));
        } else {
            entries.remove(0);
            for (Entry entry:entries){
                entry.setX(entry.getX()-1);
            }
            entries.add(new Entry(entries.size(),lux));
        }

        //将数据源装进折线对象LineDataSet ，并设置折线对象的相关属性：
        LineDataSet set = new LineDataSet(entries, "BarDataSet");

        //设置线条颜色
        set.setColor(
                getResources().getColor(R.color.newPink, getResources().newTheme())
        );

        //设置显示数据点值
        set.setDrawValues(true);


        //设置显示值的字体颜色
        set.setValueTextColor(
                getResources()
                        .getColor(R.color.light_green, getResources().newTheme())
        );

        set.setValueTextSize(18);

        //设置折线图数据并绘制：
        LineData lineData = new LineData(set);
        mSensorChart.setData(lineData);
        mSensorChart.invalidate();
    }

    private void testChart(){
        setAbout();
        setY();
        setX();
        ////初始化一条折线的数据源 一个数据点对应一个Entry对象 Entry对象包含x值和y值
        Random random=new Random();
        if (entries.size() < 4){
            entries.add(new Entry(entries.size(),random.nextInt(240) + 30));
        } else {
            entries.remove(0);
            for (Entry entry:entries){
                entry.setX(entry.getX()-1);
            }
            entries.add(new Entry(entries.size(),random.nextInt(240) + 30));
        }


        //将数据源装进折线对象LineDataSet ，并设置折线对象的相关属性：
        LineDataSet set = new LineDataSet(entries, "BarDataSet");

        //设置线条颜色
        set.setColor(
                getResources().getColor(R.color.newPink, getResources().newTheme())
        );

        //设置显示数据点值
        set.setDrawValues(true);


        //设置显示值的字体颜色
        set.setValueTextColor(
                getResources()
                        .getColor(R.color.light_green, getResources().newTheme())
        );

        set.setValueTextSize(18);

        //设置折线图数据并绘制：
        LineData lineData = new LineData(set);
        mSensorChart.setData(lineData);
        mSensorChart.invalidate();                                                //刷新显示绘图
        //mSensorChart.setBackgroundColor(Color.rgb(255,255,255));  //设置LineChart的背景颜色
        //设置折线图中每个数据点的选中监听
        mSensorChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    private void setAbout(){

        mSensorChart.setDrawGridBackground(true);
        mSensorChart.setGridBackgroundColor(Color.rgb(255,255,255));
        mSensorChart.setNoDataTextDescription("无数据");  //设置无数据时显示的描述信息

        mSensorChart.setDrawGridBackground(false); //设置是否绘制网格背景

        //设置图表是否缩放
        mSensorChart.setScaleEnabled(true);

        //设置图表的描述信息
        mSensorChart.setDescription("折线图");

        //设置描述信息的字体颜色
        mSensorChart.setDescriptionColor(
                getResources().getColor(R.color.newPrimary, getResources().newTheme())
        );
        //mSensorChart.setDescriptionPosition(550,330);     //设置描述信息的显示位置
        mSensorChart.setDescriptionTextSize(12);          //设置描述信息的字体大小
    }

    private void setY(){
        YAxis leftAxis = mSensorChart.getAxisLeft();

        leftAxis.setTextColor( getResources()
                .getColor(R.color.light_green, getResources().newTheme())
        );

        leftAxis.setDrawAxisLine(true);
        leftAxis.setDrawGridLines(true);
        leftAxis.setTextSize(18f);
        leftAxis.setAxisLineWidth(2);
        leftAxis.setAxisMinValue(0);
        leftAxis.setLabelCount(5);
        leftAxis.setAxisMaxValue(1000);
        leftAxis.setAxisLineColor(getResources().getColor(R.color.light_blue,getResources().newTheme()));
        mSensorChart.getAxisRight().setEnabled(false); // 设置不显示右y轴  默认会显示右y轴
    }

    private void setX(){
        XAxis xAxis = mSensorChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(15f);
        xAxis.setAxisLineWidth(2);
        xAxis.setAxisMinValue(0);
        xAxis.setLabelCount(3);
        xAxis.setAxisMaxValue(3);
        xAxis.setAxisLineColor(getResources().getColor(R.color.light_blue,getResources().newTheme()));

        xAxis.setTextColor(getResources().getColor(R.color.light_green, getResources().newTheme()));

        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return getFormattedTime(value);
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

    }

    private String getFormattedTime(float value){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = (int) (calendar.get(Calendar.SECOND) + value);
        String strHour;
        String strMinute;
        String strSecond;
        if (second > 60){
            minute++;
            second = 0;
        }
        if (minute > 60){
            hour++;
            minute = 0;
        }
        if (hour > 24){
            hour = 0;
        }
        strHour = String.valueOf(hour);
        strMinute = String.valueOf(minute);
        strSecond = String.valueOf(second);
        if (hour < 10){
            strHour = "0"+hour;
        }
        if (minute < 10){
            strMinute = "0"+minute;
        }
        if (second < 10){
            strSecond = "0"+second;
        }

        return strHour+":"+strMinute+":"+strSecond;
    }

    private void swapToLightSensor(){
        mSensorTitle.setText("环境亮度监测");
        mSensorSwapBtn.setText("亮度传感器");
        currentSensorType = RemoteLampManager.LIGHT_SENSOR;
        mSensorChart.setVisibility(View.VISIBLE);
        mInfraredSensorTextView.setVisibility(View.GONE);
        if (lampLightSensor == null) {
            mSensorSwitch.setChecked(false);
            return;
        }
        mSensorSwitch.setChecked(lampLightSensor.isOpen());
    }

    private void swapToInfraredSensor(){
        mSensorTitle.setText("红外状态监测");
        mSensorSwapBtn.setText("红外传感器");
        currentSensorType = RemoteLampManager.INFRARED_SENSOR;
        mSensorChart.setVisibility(View.GONE);
        mInfraredSensorTextView.setVisibility(View.VISIBLE);
        if (infraredSensor == null) {
            mSensorSwitch.setChecked(false);
            return;
        }
        mSensorSwitch.setChecked(infraredSensor.isOpen());
    }

    private void initView(View view){
        mSensorChart  = view.findViewById(R.id.lamp_sensor_chart);

        mSensorSwitch = view.findViewById(R.id.sensor_switch);

        mSensorSwapBtn = view.findViewById(R.id.sensor_swap_btn);

        mPopupMenu = new PopupMenu(getActivity(),mSensorSwapBtn);

        mSensorTitle = view.findViewById(R.id.sensor_title);

        mInfraredSensorTextView = view.findViewById(R.id.infrared_sensor_text);

        mPopupMenu.inflate(R.menu.sensor_menu);

        //initChart();

        //传感器开关监听
        mSensorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    openSensor();
                } else {
                    closeSensor();
                }
            }
        });

        //弹出切换传感器菜单
        mSensorSwapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupMenu.show();
            }
        });

        //监听菜单选项
        mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.light_sensor_item:
                        swapToLightSensor();
                        break;
                    case R.id.infrared_sensor_item:
                        swapToInfraredSensor();
                        break;
                }
                return true;
            }
        });

    }

    /**打开当前操作传感器*/
    private void openSensor(){
        if (currentSensorType == RemoteLampManager.LIGHT_SENSOR){
            if (lampLightSensor == null){
                lampLightSensor = remoteLampManager.newLampLightSensor();
            }
            lampLightSensor.setOnStateChangeListener(new LampSensor.OnStateChangeListener<LightSensorData>() {
                @Override
                public void onStateChange(LightSensorData lightSensorData) {
                    //upDateChart(lightSensorData.getLux());
                    testChart();
                    Log.i("233","233");
                }
            });
            if (!lampLightSensor.isOpen()){
                lampLightSensor.startListen();
                lampLightSensor.setOpen(true);
            }
        } else if (currentSensorType == RemoteLampManager.INFRARED_SENSOR){
            if (infraredSensor == null){
                infraredSensor = remoteLampManager.newInfraredSensor();
            }
            infraredSensor.setOnStateChangeListener(new LampSensor.OnStateChangeListener<InfraredSensorData>() {
                @Override
                public void onStateChange(InfraredSensorData infraredSensorData) {
                    upDateStatus(infraredSensorData.isHasPerson());
                }
            });
            if (!infraredSensor.isOpen()){
                infraredSensor.startListen();
                infraredSensor.setOpen(true);
            }
        }
    }

    /**关闭当前操作传感器*/
    private void closeSensor(){
        if (currentSensorType == RemoteLampManager.LIGHT_SENSOR && lampLightSensor != null){
            if (lampLightSensor.isOpen()){
                lampLightSensor.stopListen();
                lampLightSensor.setOpen(false);
            }
        } else if (currentSensorType == RemoteLampManager.INFRARED_SENSOR && infraredSensor != null){
            if (infraredSensor.isOpen()){
                infraredSensor.stopListen();
                infraredSensor.setOpen(false);
            }
        }
    }

    private void upDateStatus(boolean isHasPerson){
        if (isHasPerson){
            mInfraredSensorTextView.setText("有人");
            mInfraredSensorTextView.setTextColor(getResources().getColor(R.color.newPrimary,getResources().newTheme()));
        } else {
            mInfraredSensorTextView.setText("无人");
            mInfraredSensorTextView.setTextColor(getResources().getColor(R.color.info_warn,getResources().newTheme()));
        }
    }

    private void destroySensor(){
        if (lampLightSensor != null){
            if (lampLightSensor.isOpen()){
                lampLightSensor.stopListen();
            }
        }
        if (infraredSensor != null){
            if (infraredSensor.isOpen()){
                infraredSensor.stopListen();
            }
        }
    }
}
