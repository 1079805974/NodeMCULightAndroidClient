package yumetsuki.com.lampsettingsui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import yumetsuki.com.Data.Schedule;
import yumetsuki.com.lampsettingsui.R;


public class TimePickerFragment extends DialogFragment {

    public static final String ARG_DATE="weekDay";
    public static final String EXTRA_DATE="time_picker_date";

    private Schedule schedule =null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        if (bundle!=null){
            schedule =(Schedule) bundle.getSerializable(ARG_DATE);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.time_picker_dialog,null);

        final AppCompatCheckBox mondayBox=view.findViewById(R.id.monday_box);
        final AppCompatCheckBox tuesdayBox=view.findViewById(R.id.tuesday_box);
        final AppCompatCheckBox wednesdayBox=view.findViewById(R.id.wednesday_box);
        final AppCompatCheckBox thursdayBox=view.findViewById(R.id.thursday_box);
        final AppCompatCheckBox fridayBox=view.findViewById(R.id.friday_box);
        final AppCompatCheckBox saturdayBox=view.findViewById(R.id.saturday_box);
        final AppCompatCheckBox sundayBox=view.findViewById(R.id.sunday_box);
        final TimePicker timePicker=view.findViewById(R.id.time_picker);

        if(schedule!=null){
            mondayBox.setChecked(schedule.isOpenMonday());
            tuesdayBox.setChecked(schedule.isOpenTuesday());
            wednesdayBox.setChecked(schedule.isOpenWednesday());
            thursdayBox.setChecked(schedule.isOpenThursday());
            fridayBox.setChecked(schedule.isOpenFriday());
            saturdayBox.setChecked(schedule.isOpenFriday());
            sundayBox.setChecked(schedule.isOpenSunday());
            timePicker.setHour(schedule.getHour());
            timePicker.setMinute(schedule.getMinute());
        }

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setIcon(R.drawable.ic_alarm_24dp)
                .setTitle("定时")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (schedule==null){
                            schedule=new Schedule();
                        }
                        //更新date
                        schedule.setOpenMonday(mondayBox.isChecked());
                        schedule.setOpenTuesday(tuesdayBox.isChecked());
                        schedule.setOpenWednesday(wednesdayBox.isChecked());
                        schedule.setOpenThursday(thursdayBox.isChecked());
                        schedule.setOpenFriday(fridayBox.isChecked());
                        schedule.setOpenSaturday(saturdayBox.isChecked());
                        schedule.setOpenSunday(sundayBox.isChecked());
                        schedule.setHour(timePicker.getHour());
                        schedule.setMinute(timePicker.getMinute());

                        //返回更新的date
                        if (getTargetFragment()!=null){
                            Intent intent=new Intent();
                            intent.putExtra(EXTRA_DATE, schedule);
                            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,intent);
                        }

                    }
                }).create();
    }

    public static TimePickerFragment newInstance(Schedule schedule){
        Bundle bundle=new Bundle();
        bundle.putSerializable(ARG_DATE, schedule);
        TimePickerFragment fragment=new TimePickerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

}
