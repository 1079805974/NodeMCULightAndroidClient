package yumetsuki.com.lampsettingsui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import android.widget.SeekBar;

import com.larswerkman.holocolorpicker.ColorPicker;


import yumetsuki.com.Data.Lamp;
import yumetsuki.com.Data.Schedule;
import yumetsuki.com.lampsettingsui.R;
import yumetsuki.com.Data.manager.RemoteLampManager;

import static yumetsuki.com.lampsettingsui.fragment.TimePickerFragment.EXTRA_DATE;

public class LampSettingsUIFragment extends Fragment {

    public static final int REQUEST_DATE=0;
    public static final String TAG_TIME="time_picker_tag";

    private RemoteLampManager remoteLampManager;

    //设置相关控件
    private TextInputEditText mLampNameEdit;
    private SwitchCompat mLampSwitcher;
    private AppCompatButton mTimeSettingBtn;
    private ColorPicker mColorPicker;
    private AppCompatTextView mLightProgressText;
    private AppCompatSeekBar mLightSeekBar;
    private AppCompatImageView mColorImageView;
    private AppCompatCheckBox mSelfColorCheckBox;
    private AppCompatButton mDefaultColorBtn;
    private PopupMenu mDefaultColorMenu;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        remoteLampManager=RemoteLampManager.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lamp_setting_fragment,container,false);
        initView(view);
        return view;
    }

    private void showTimePicker(){
        TimePickerFragment fragment=TimePickerFragment.newInstance(remoteLampManager.getCurrentLamp().getSchedule());
        fragment.setTargetFragment(LampSettingsUIFragment.this,REQUEST_DATE);
        fragment.show(getActivity().getSupportFragmentManager(),TAG_TIME);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DATE&&resultCode == Activity.RESULT_OK){
            remoteLampManager.setSchedule((Schedule) data.getSerializableExtra(EXTRA_DATE));
            upDataUI();
        }
    }

    private void upDataUI(){
        mTimeSettingBtn.setText(remoteLampManager.getCurrentLamp().getSchedule().toString());
    }

    private void initView(View view){
        //初始化控件
        mLampNameEdit=view.findViewById(R.id.lamp_name_edt);
        mLampSwitcher=view.findViewById(R.id.lamp_switch);
        mTimeSettingBtn=view.findViewById(R.id.time_setting_btn);
        mColorPicker=view.findViewById(R.id.color_picker);
        mLightProgressText=view.findViewById(R.id.light_progress_text);
        mLightSeekBar=view.findViewById(R.id.light_seek_bar);
        mColorImageView =view.findViewById(R.id.color_toggle_btn);
        mDefaultColorBtn=view.findViewById(R.id.default_color_btn);
        mSelfColorCheckBox=view.findViewById(R.id.self_setting_color_checkbox);
        mDefaultColorMenu = new PopupMenu(getActivity(),mDefaultColorBtn);
        mDefaultColorMenu.inflate(R.menu.default_color_menu);
        mDefaultColorMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (remoteLampManager.getCurrentLamp().isSelfColor()){
                    return true;
                }
                switch (item.getItemId()){
                    case R.id.light:
                        Lamp.DefaultColor defaultColor1 = Lamp.DefaultColor.LIGHT;
                        remoteLampManager.getCurrentLamp().setDefaultColor(defaultColor1);
                        remoteLampManager.setColor(defaultColor1.getR(),defaultColor1.getG(),defaultColor1.getB());
                        mDefaultColorBtn.setText(item.getTitle());
                        break;
                    case R.id.blue:
                        Lamp.DefaultColor defaultColor2 = Lamp.DefaultColor.BLUE;
                        remoteLampManager.getCurrentLamp().setDefaultColor(defaultColor2);
                        remoteLampManager.setColor(defaultColor2.getR(),defaultColor2.getG(),defaultColor2.getB());
                        mDefaultColorBtn.setText(item.getTitle());
                        break;
                    case R.id.green:
                        Lamp.DefaultColor defaultColor3 = Lamp.DefaultColor.GREEN;
                        remoteLampManager.getCurrentLamp().setDefaultColor(defaultColor3);
                        remoteLampManager.setColor(defaultColor3.getR(),defaultColor3.getG(),defaultColor3.getB());
                        mDefaultColorBtn.setText(item.getTitle());
                        break;
                }

                upDateColorImageView();

                return true;
            }
        });

        //初始化各控件
        initWeight();

        mDefaultColorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDefaultColorMenu.show();
            }
        });

        //颜色变化监听
        mColorPicker.setOnColorSelectedListener(new ColorPicker.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                if (remoteLampManager.getCurrentLamp().isSelfColor()){
                    String hexColor=Integer.toHexString(color);
                    int red=Integer.valueOf(hexColor.substring(2,4),16);
                    int green=Integer.valueOf(hexColor.substring(4,6),16);
                    int blue=Integer.valueOf(hexColor.substring(6,8),16);
                    remoteLampManager.setColor(red,green,blue);
                }
            }
        });

        mColorPicker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                if (mSelfColorCheckBox.isChecked()){
                    mColorImageView.setImageTintList(ColorStateList.valueOf(color));
                }
            }
        });


        mColorPicker.setShowOldCenterColor(false);

        //监听文本变化，设置灯名
        mLampNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //文本变化前
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                remoteLampManager.getCurrentLamp().setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //文本变化后
                getActivity().invalidateOptionsMenu();
            }
        });

        //监听开关变化，设置灯的开关
        mLampSwitcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //当选择变化时
                remoteLampManager.setOpen(isChecked);

            }
        });

        //设置时间的按钮
        mTimeSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });


        //调整亮度的seekBar
        mLightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mLightProgressText.setText(String.valueOf(progress)); //同步更新数据
                remoteLampManager.setLux(progress);
                //监听progress变化 write your code at here
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //设置是否自定义Color
        mSelfColorCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                remoteLampManager.getCurrentLamp().setSelfColor(isChecked);
                if (isChecked) {
                    String hexColor=Integer.toHexString(mColorPicker.getColor());
                    int red=Integer.valueOf(hexColor.substring(2,4),16);
                    int green=Integer.valueOf(hexColor.substring(4,6),16);
                    int blue=Integer.valueOf(hexColor.substring(6,8),16);
                    remoteLampManager.setColor(red,green,blue);
                } else {
                    Lamp.DefaultColor defaultColor = remoteLampManager.getCurrentLamp().getDefaultColor();
                    remoteLampManager.setColor(defaultColor.getR(), defaultColor.getG(), defaultColor.getB());
                }

                upDateColorImageView();
            }
        });

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden){
            initWeight();
        }
        super.onHiddenChanged(hidden);
    }

    private void initWeight(){

        Lamp currentLamp = remoteLampManager.getCurrentLamp();

        int color = Color.rgb(currentLamp.getRed(),currentLamp.getGreen(),currentLamp.getBlue());

        boolean isDefaultColor =
                color == Color.rgb(255,255,255);

        mColorPicker.setColor(color);

        if (isDefaultColor){
            mColorImageView.setImageTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
        } else {
            mColorImageView.setImageTintList(ColorStateList.valueOf(color));

        }

        mLampNameEdit.setText(currentLamp.getName());
        mLampSwitcher.setChecked(currentLamp.isOpen());
        mLightSeekBar.setProgress(currentLamp.getLux());
        mLightProgressText.setText(String.valueOf(remoteLampManager.getCurrentLamp().getLux()));
        mTimeSettingBtn.setText(currentLamp.getSchedule().toString());
        mSelfColorCheckBox.setChecked(currentLamp.isSelfColor());

    }

    private void upDateColorImageView(){
        Lamp lamp = remoteLampManager.getCurrentLamp();

        int color = Color.rgb(lamp.getRed(),lamp.getGreen(),lamp.getBlue());

        boolean isDefaultColor =
                color == Color.rgb(255,255,255);

        if (isDefaultColor){
            mColorImageView.setImageTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
        } else {
            mColorImageView.setImageTintList(ColorStateList.valueOf(color));
        }
    }
}

