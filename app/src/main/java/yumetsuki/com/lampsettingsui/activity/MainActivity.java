package yumetsuki.com.lampsettingsui.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import java.util.ArrayList;
import java.util.List;

import yumetsuki.com.Data.Lamp;
import yumetsuki.com.lampsettingsui.R;
import yumetsuki.com.Data.manager.RemoteLampManager;
import yumetsuki.com.lampsettingsui.fragment.LampSensorUIFragment;
import yumetsuki.com.lampsettingsui.fragment.LampSettingsUIFragment;
import yumetsuki.com.lampsettingsui.fragment.NullLampFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG_LAMP_SETTING = "tag_lamp_setting_fragment";
    private static final String TAG_LAMP_SENSOR = "tag_lamp_sensor_fragment";
    private static final String TAG_NULL_LAMP = "tag_null_lamp";

    private List<Pair<Fragment,Fragment>> fragments = new ArrayList<>();

    private BottomNavigationView mBottom;

    private RemoteLampManager remoteLampManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lamp_menu,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        upDateMenu(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_lamp:
                addLamp();
                break;
            case R.id.swap_lamp:
                break;

            case R.id.remove_lamp:
                removeCurrentLamp();
                break;
        }
        return true;
    }

    /**初始化视图*/
    private void initView(){

        //获取RemoteLampManager单例
        remoteLampManager = RemoteLampManager.getInstance();
        //初始化BottomNavigationView
        mBottom = findViewById(R.id.bottom_view);
        //初始化添加按钮
        mBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.lamp_setting_item:
                        swapSensorToLampView();
                        return true;
                    case R.id.lamp_sensor_item:
                        swapLampToSensorView();
                        return true;
                }
                return false;
            }
        });

        initFragment();

    }

    /**连接新的智能灯实例*/
    private void addLamp(){

        FragmentManager manager = getSupportFragmentManager();

        if (manager.findFragmentByTag(TAG_NULL_LAMP).isVisible()){
            remoteLampManager.linkNewLamp();
            LampSettingsUIFragment newLampSettingFragment = new LampSettingsUIFragment();
            LampSensorUIFragment newLampSensorFragment = new LampSensorUIFragment();
            Fragment nullLampFragment = manager.findFragmentByTag(TAG_NULL_LAMP);

            manager.beginTransaction()
                    .hide(nullLampFragment)
                    .add(R.id.single_fragment_container,newLampSettingFragment)
                    .add(R.id.single_fragment_container,newLampSensorFragment)
                    .hide(newLampSensorFragment)
                    .commit();

            fragments.add(new Pair<Fragment, Fragment>(newLampSettingFragment,newLampSensorFragment));

            invalidateOptionsMenu();

        } else {
            Fragment lampSettingsUIFragment
                    = fragments.get(remoteLampManager.getCurrentLampIndex()).first;

            Fragment lampSensorUIFragment
                    = fragments.get(remoteLampManager.getCurrentLampIndex()).second;

            LampSettingsUIFragment newLampSettingFragment = new LampSettingsUIFragment();
            LampSensorUIFragment newLampSensorFragment = new LampSensorUIFragment();

            //隐藏旧的fragments
            manager.beginTransaction().hide(lampSettingsUIFragment).commit();
            manager.beginTransaction().hide(lampSensorUIFragment).commit();

            remoteLampManager.linkNewLamp();

            manager.beginTransaction()
                    .add(R.id.single_fragment_container,newLampSettingFragment)
                    .add(R.id.single_fragment_container,newLampSensorFragment)
                    .hide(newLampSensorFragment)
                    .commit();

            fragments.add(new Pair<Fragment, Fragment>(newLampSettingFragment,newLampSensorFragment));

            invalidateOptionsMenu();

        }

        mBottom.getMenu().findItem(R.id.lamp_setting_item).setChecked(true);

    }

    /**初始化fragment*/
    private void initFragment(){
        FragmentManager manager=getSupportFragmentManager();
        Fragment fragment=manager
                .findFragmentById(R.id.single_fragment_container);

        if (fragment==null){
            fragment=new NullLampFragment();
            manager
                .beginTransaction()
                .add(R.id.single_fragment_container,fragment,TAG_NULL_LAMP)
                .commit();
        }
    }

    /**切换当前操作的智能灯*/
    private void swapLamp(int index){
        FragmentManager manager = getSupportFragmentManager();

        Fragment lampSettingsUIFragment
                = fragments.get(remoteLampManager.getCurrentLampIndex()).first;

        Fragment lampSensorUIFragment
                = fragments.get(remoteLampManager.getCurrentLampIndex()).second;

        //隐藏旧的fragments
        manager.beginTransaction().hide(lampSettingsUIFragment).commit();
        manager.beginTransaction().hide(lampSensorUIFragment).commit();

        remoteLampManager.setCurrentLamp(index);

        Fragment newLampSettingFragment = fragments.get(index).first;
        Fragment newLampSensorFragment = fragments.get(index).second;

        manager.beginTransaction()
                .show(newLampSettingFragment)
                .hide(newLampSensorFragment)
                .commit();

        mBottom.getMenu().findItem(R.id.lamp_setting_item).setChecked(true);
    }

    /**添加或切换智能灯时更新fragment*/
    private void upDateFragment(int index){
        FragmentManager manager = getSupportFragmentManager();

        Fragment nullLampFragment
                =manager.findFragmentByTag(TAG_NULL_LAMP);

        if (remoteLampManager.getLamps().isEmpty()){
            manager.beginTransaction()
                    .show(nullLampFragment)
                    .commit();

            invalidateOptionsMenu();
            return;
        }

        LampSettingsUIFragment newLampSettingFragment = new LampSettingsUIFragment();
        LampSensorUIFragment newLampSensorFragment = new LampSensorUIFragment();

        //添加新的fragments
        manager.beginTransaction()
                .add(R.id.single_fragment_container,newLampSettingFragment,TAG_LAMP_SETTING + index)
                .add(R.id.single_fragment_container,newLampSensorFragment,TAG_LAMP_SENSOR + index)
                .hide(newLampSensorFragment)
                .commit();

        invalidateOptionsMenu();
    }

    /**底部导航切换到Sensor*/
    private void swapLampToSensorView() {
        FragmentManager manager = getSupportFragmentManager();

        if (!remoteLampManager.getLamps().isEmpty()){
            manager.beginTransaction()
                    .hide(fragments.get(remoteLampManager.getCurrentLampIndex()).first)
                    .show(fragments.get(remoteLampManager.getCurrentLampIndex()).second)
                    .commit();
        }
    }

    /**底部导航切换到Setting*/
    private void swapSensorToLampView(){
        FragmentManager manager = getSupportFragmentManager();

        if (!remoteLampManager.getLamps().isEmpty()){
            manager.beginTransaction()
                    .hide(fragments.get(remoteLampManager.getCurrentLampIndex()).second)
                    .show(fragments.get(remoteLampManager.getCurrentLampIndex()).first)
                    .commit();
            }
    }

    /**更新menu*/
    private void upDateMenu (Menu menu){
        MenuItem swapItem = menu.findItem(R.id.swap_lamp);

        SubMenu subMenu = swapItem.getSubMenu();

        for (final Lamp lamp: remoteLampManager.getLamps()) {
            subMenu.add(lamp.getName()).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int index = remoteLampManager.getLamps().indexOf(lamp);
                    swapLamp(index);
                    return true;
                }
            });
        }

    }

    /**删除一个Lamp*/
    private void removeCurrentLamp(){

        if (remoteLampManager.getCurrentLamp() != null){
            remoteLampManager.removeCurrentLamp();

            FragmentManager manager = getSupportFragmentManager();
            Fragment lampSettingsUIFragment
                    = fragments.get(remoteLampManager.getCurrentLampIndex()).first;

            Fragment lampSensorUIFragment
                    = fragments.get(remoteLampManager.getCurrentLampIndex()).second;


            //移除旧的fragments
            manager.beginTransaction().remove(lampSettingsUIFragment).commit();
            manager.beginTransaction().remove(lampSensorUIFragment).commit();

            fragments.remove(remoteLampManager.getCurrentLampIndex());

            if (!remoteLampManager.getLamps().isEmpty()){
                remoteLampManager.setCurrentLamp(remoteLampManager.getLamps().size()-1);
            } else {
                remoteLampManager.setCurrentLamp(-1);
                Fragment nullLampFragment = manager.findFragmentByTag(TAG_NULL_LAMP);
                manager.beginTransaction().show(nullLampFragment).commit();
                invalidateOptionsMenu();
                return;
            }

            Fragment newLampSettingFragment = fragments.get(remoteLampManager.getCurrentLampIndex()).first;
            Fragment newLampSensorFragment = fragments.get(remoteLampManager.getCurrentLampIndex()).second;

            manager.beginTransaction()
                    .show(newLampSettingFragment)
                    .hide(newLampSensorFragment)
                    .commit();

            invalidateOptionsMenu();

            mBottom.getMenu().findItem(R.id.lamp_setting_item).setChecked(true);
        }

    }

    private void hideCurrentFragment(){
        FragmentManager manager = getSupportFragmentManager();

    }

    private void removeCurrentFragment(){

    }
}
