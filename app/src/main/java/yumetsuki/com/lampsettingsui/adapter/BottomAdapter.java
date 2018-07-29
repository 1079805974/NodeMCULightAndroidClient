package yumetsuki.com.lampsettingsui.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class BottomAdapter extends FragmentStatePagerAdapter{

    private List<Fragment> fragments = new ArrayList<>();

    private boolean isFirstAdd = true;

    public BottomAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    public void addFragment(Fragment fragment) {
        fragments.add(fragment);
    }

    public void upDateFragment(int index,Fragment...fragment){
        if (isFirstAdd){
            for (int i = 0; i < fragment.length; i++){
                fragments.set(index+i, fragment[i]);
            }
            isFirstAdd = false;
        }

    }
}
