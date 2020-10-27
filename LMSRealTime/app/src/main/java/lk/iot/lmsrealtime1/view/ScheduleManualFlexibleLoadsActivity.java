package lk.iot.lmsrealtime1.view;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lk.iot.lmsrealtime1.R;
import lk.iot.lmsrealtime1.adapter.ScheduleManualAdapter;
import lk.iot.lmsrealtime1.data.FirebaseDAO;
import lk.iot.lmsrealtime1.data.ScheduleManualFlexibleLoadsDAO;
import lk.iot.lmsrealtime1.helper.ClickListener;
import lk.iot.lmsrealtime1.helper.TimePickerFragment;
import lk.iot.lmsrealtime1.model.ScheduleManual;

public class ScheduleManualFlexibleLoadsActivity extends AppCompatActivity {

    RecyclerView rvSHItems;
    ScheduleManualAdapter adapter;
    ArrayList<ScheduleManual> list;
    LinearLayout NoData,header;
   // Toolbar MCToolBar;
    FirebaseAuth fAuth;
    Button btn_save;
    String userID;
    ProgressBar progressBar;
    final HashMap<Integer, ScheduleManual> dbList = new HashMap<>();
    HashMap<String, Double> StartTimeList = new HashMap<>();
    int sum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_manual_flexible_loads);
        rvSHItems = findViewById(R.id.rvMCItems);
        fAuth = FirebaseAuth.getInstance();
        userID = (fAuth.getCurrentUser() != null) ? fAuth.getCurrentUser().getUid() : "0";
        btn_save = findViewById(R.id.btn_save);
        progressBar = findViewById(R.id.progressBar);
        NoData = findViewById(R.id.NoData);
        header = findViewById(R.id.header);

        downloadData();


        if (list.size() == 0) {
            NoData.setVisibility(View.VISIBLE);
            header.setVisibility(View.GONE);
        } else {
            NoData.setVisibility(View.GONE);
            header.setVisibility(View.VISIBLE);
        }

        adapter = new ScheduleManualAdapter(ScheduleManualFlexibleLoadsActivity.this, list,new ClickListener() {

            @Override
            public void onPositionClicked(int position, View view) {

                final ScheduleManual mn = list.get(position);

                if(view.getId() == R.id.endTime) {
                    final TextView tex = view.findViewById(R.id.endTime);
                    TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            mn.setS_End_Time_Hour(getTwoValue(hourOfDay));
                            mn.setS_End_Time_Minute(getTwoValue(minute));

                            tex.setText(getTwoValue(hourOfDay) + ":" + getTwoValue(minute));
                        }
                    };
                    DialogFragment newFragment = new TimePickerFragment(listener);
                    newFragment.show(getSupportFragmentManager(), "timePicker");
                    dbList.put(mn.getS_ID(),mn);
                }
                if(view.getId() == R.id.starttime) {
                    final TextView tex = view.findViewById(R.id.starttime);
                    TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            mn.setS_Start_Time_Hour(getTwoValue(hourOfDay));
                            mn.setS_Start_Time_Minute(getTwoValue(minute));

                            tex.setText(getTwoValue(hourOfDay) + ":" + getTwoValue(minute));
                        }

                    };
                    DialogFragment newFragment = new TimePickerFragment(listener);
                    newFragment.show(getSupportFragmentManager(), "timePicker");
                    dbList.put(mn.getS_ID(),mn);
                }
            }

            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on) {

            }

            @Override
            public void onCheckedChanged(int position, CompoundButton cb, boolean on) {

            }
        });
        rvSHItems.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ScheduleManualFlexibleLoadsActivity.this, RecyclerView.VERTICAL, false);
        rvSHItems.setLayoutManager(layoutManager);
        rvSHItems.setHasFixedSize(true);
        rvSHItems.getRecycledViewPool().setMaxRecycledViews(0, 0);
        rvSHItems.setAdapter(adapter);


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                int count = 0;

                for (Map.Entry<Integer, ScheduleManual> entry : dbList.entrySet()) {

                    ScheduleManual scheduleManual = entry.getValue();

                    scheduleManual.setS_USER_ID(userID);
                    count += new ScheduleManualFlexibleLoadsDAO(ScheduleManualFlexibleLoadsActivity.this).insert(scheduleManual);

                }
                ArrayList<ScheduleManual> allData = new ScheduleManualFlexibleLoadsDAO(ScheduleManualFlexibleLoadsActivity.this).getAll(userID);

                new FirebaseDAO(ScheduleManualFlexibleLoadsActivity.this).insertScheduleFlexibleLoads(allData);

                for(ScheduleManual scheduleManual:allData){
                    if(scheduleManual.getS_LABEL().equalsIgnoreCase("washing machine") || scheduleManual.getS_LABEL().equalsIgnoreCase("water pump")) {

                        double val = Calculation(scheduleManual.getS_LABEL(),scheduleManual.getS_Start_Time_Hour()+":"+scheduleManual.getS_Start_Time_Minute(),scheduleManual.getS_End_Time_Hour()+":"+scheduleManual.getS_End_Time_Minute());
                        System.out.println(val/60.0);

                        BigDecimal b = new BigDecimal(StartTimeList.get(scheduleManual.getS_LABEL()));

                        b= b.setScale(2, BigDecimal.ROUND_HALF_EVEN);

                        System.out.println(StartTimeList.put(scheduleManual.getS_LABEL(),b.doubleValue()));
                    }
                }
                int num = 0;
                String message = " Power usage status for ";
                if(StartTimeList.get("Water Pump") != null){
                    num++;
                    message += " \nWater Pump : Rs."+StartTimeList.get("Water Pump");
                }
                if(StartTimeList.get("Washing Machine") != null){
                    num++;
                    message += "\nWashing Machine : Rs."+StartTimeList.get("Washing Machine");
                }

                final int finalCount = count;
                final String finalMessage = message;
                final int finalNum = num;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        if(finalNum >0) {
                            displayStatusMessage(finalMessage, ScheduleManualFlexibleLoadsActivity.this);
                        }else{
                            Toast.makeText(ScheduleManualFlexibleLoadsActivity.this, finalCount + " Records Inserted ", Toast.LENGTH_LONG).show();

                            startActivity(new Intent(getApplicationContext(), ScheduleManualActivity.class));
                        }

                    }
                }, 4600);

            }
        });

    }


    private String getTwoValue(int hourOfDay) {
        if((hourOfDay+"").length()==1){
            return "0"+hourOfDay;
        }else{
            return hourOfDay+"";
        }
    }

    void downloadData() {
        new FirebaseDAO(ScheduleManualFlexibleLoadsActivity.this).getScheduleFlexibleLoads();
        list = new ScheduleManualFlexibleLoadsDAO(ScheduleManualFlexibleLoadsActivity.this).getAll(userID);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), ScheduleManualActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public static void displayStatusMessage(String s, final Context context) {

        AlertDialog.Builder builder = null;
        View view = null;
        TextView tvOk, tvMessage;
        ImageView imageView;
        int warningColor = R.color.black; // 3

        int warning_image = R.drawable.ic_warning;
        //1,2,3

        int color = warningColor;
        int img = warning_image;

        builder = new AlertDialog.Builder(context);
        LayoutInflater mInflater = LayoutInflater.from(context);
        view =  mInflater.inflate(R.layout.layout_for_custom_message, null);

        tvOk = (TextView) view.findViewById(R.id.tvOk);
        tvMessage = (TextView) view.findViewById(R.id.tvMessage);
        imageView = (ImageView)view.findViewById(R.id.iv_status);

        tvMessage.setTextColor(context.getResources().getColor(color));
        tvMessage.setText(s);
        imageView.setImageResource(img);
        imageView.setVisibility(View.GONE);

        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        tvOk.setOnClickListener(    new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( context, ScheduleManualActivity.class );
                intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                context.startActivity( intent );
                //finish();
                alertDialog.dismiss();

            }
        });

    }
    ///////////////########################## SET COST #################################3333

    private double getAllUsedHoursCost(int wat,String start_time,String end_time,String label){
        double CostCount1 = (getUsedhour("05:30","18:30",start_time,end_time,label));// * 23)/1000;
        double CostCount2 = (getUsedhour("18:30","22:30",start_time,end_time,label));// * 54)/1000;
        double CostCount3 = (getUsedhour("22:30","23:59",start_time,end_time,label));// * 13)/1000;
        double CostCount4 = (getUsedhour("00:00","05:30",start_time,end_time,label));// * 13)/1000;
        return (CostCount1+CostCount2+CostCount3+CostCount4);//wat * (CostCount1+CostCount2+CostCount3+CostCount4);
    }

    private double getUsedhour(String t1,String t2,String start,String end,String label){
        double used_hour = 0;
        try {

            Date time1 = new SimpleDateFormat("HH:mm").parse(t1);
            Calendar time1_calendar = Calendar.getInstance();
            time1_calendar.setTime(time1);
            time1_calendar.add(Calendar.DATE, 1);


            Date time2 = new SimpleDateFormat("HH:mm").parse(t2);
            Calendar time2_calendar = Calendar.getInstance();
            time2_calendar.setTime(time2);
            time2_calendar.add(Calendar.DATE, 1);


            Date start_d = new SimpleDateFormat("HH:mm").parse(start);
            Calendar start_calendar = Calendar.getInstance();
            start_calendar.setTime(start_d);
            start_calendar.add(Calendar.DATE, 1);


            Date end_d = new SimpleDateFormat("HH:mm").parse(end);
            Calendar end_calendar = Calendar.getInstance();
            end_calendar.setTime(end_d);
            end_calendar.add(Calendar.DATE, 1);

            Date start_ = start_calendar.getTime();
            Date end_ = end_calendar.getTime();
            Date time2_ = time2_calendar.getTime();
            Date time1_ = time1_calendar.getTime();
            if (start_.after(time1_calendar.getTime()) && start_.before(time2_calendar.getTime()) || start_.equals(time1_calendar.getTime())) {
                //checkes whether the current time is between string1 and string2.
                System.out.println(true);

                if(end_.before(time2_calendar.getTime()) || end_.equals(time2_calendar.getTime())){

                    double cal_hour = (end_.getTime() - start_.getTime()) / (60000);
                    used_hour += (end_.getTime() - start_.getTime()) / (60000); //* 23
                    System.out.println(used_hour);
                    switch (t1) {
                        case "05:30":{
                            double lastprice = StartTimeList.get(label);
                            StartTimeList.put(label,lastprice+((cal_hour*23.0)/60.0));
                        }
                        break;
                        case "18:30":{
                            double lastprice = StartTimeList.get(label);
                            StartTimeList.put(label,lastprice+((cal_hour*54.0)/60.0));
                        }
                        break;
                        case "00:00":{
                            double lastprice = StartTimeList.get(label);
                            StartTimeList.put(label,lastprice+((cal_hour*13.0)/60.0));
                        }
                        break;
                        case "22:30":{
                            double lastprice = StartTimeList.get(label);
                            StartTimeList.put(label,lastprice+((cal_hour*13.0)/60.0));
                        }
                        break;
                    }

                }else{
                    //move to other calculation
                    //time2 > end time
                    double cal_hour = (end_.getTime() - start_.getTime()) / (60000);
                    used_hour += (time2_.getTime() - start_.getTime()) / (60000);
                    System.out.println(used_hour);

                    switch (t1) {
                        case "05:30": {
                            double lastprice = StartTimeList.get(label);
                            StartTimeList.put(label,lastprice+((cal_hour*23.0)/60.0));
                            used_hour += getUsedhourWithTerm("18:30", "22:30", "18:31", end, 2, label);
                        }
                        break;
                        case "18:30": {
                            double lastprice = StartTimeList.get(label);
                            StartTimeList.put(label,lastprice+((cal_hour*54.0)/60.0));
                            used_hour += getUsedhourWithTerm("22:30", "23:59", "22:31", end, 1, label);
                        }
                        break;
                        case "00:00": {
                            double lastprice = StartTimeList.get(label);
                            StartTimeList.put(label,lastprice+((cal_hour*13.0)/60.0));
                            used_hour += getUsedhourWithTerm("05:30", "18:30", "05:31", end, 3, label);
                        }
                        break;
                        case "22:30":{
                            double lastprice = StartTimeList.get(label);
                            StartTimeList.put(label,lastprice+((cal_hour*13.0)/60.0));
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return used_hour;
    }


    private double getUsedhourWithTerm(String t1, String t2, String start, String end, int term,String label){
        double used_hour = 0;
        try {

            Date time1 = new SimpleDateFormat("HH:mm").parse(t1);
            Calendar time1_calendar = Calendar.getInstance();
            time1_calendar.setTime(time1);
            time1_calendar.add(Calendar.DATE, 1);


            Date time2 = new SimpleDateFormat("HH:mm").parse(t2);
            Calendar time2_calendar = Calendar.getInstance();
            time2_calendar.setTime(time2);
            time2_calendar.add(Calendar.DATE, 1);


            Date start_d = new SimpleDateFormat("HH:mm").parse(start);
            Calendar start_calendar = Calendar.getInstance();
            start_calendar.setTime(start_d);
            start_calendar.add(Calendar.DATE, 1);


            Date end_d = new SimpleDateFormat("HH:mm").parse(end);
            Calendar end_calendar = Calendar.getInstance();
            end_calendar.setTime(end_d);
            end_calendar.add(Calendar.DATE, 1);

            Date start_ = start_calendar.getTime();
            Date end_ = end_calendar.getTime();
            Date time2_ = time2_calendar.getTime();
            Date time1_ = time1_calendar.getTime();
            if (start_.after(time1_calendar.getTime()) && start_.before(time2_calendar.getTime()) || start_.equals(time2_calendar.getTime())) {
                //checkes whether the current time is between string1 and string2.
                System.out.println(true);
                if(end_.before(time2_calendar.getTime())|| end_.equals(time2_calendar.getTime())){
                    double cal_hour =  (end_.getTime() - start_.getTime()) / (60000);
                    used_hour += (end_.getTime() - start_.getTime()) / (60000);
                    System.out.println(used_hour);

                    switch (t1) {
                        case "05:30":{
                            double lastprice = StartTimeList.get(label);
                            StartTimeList.put(label,lastprice+((cal_hour*23.0)/60.0));
                        }
                        break;
                        case "18:30":{
                            double lastprice = StartTimeList.get(label);
                            StartTimeList.put(label,lastprice+((cal_hour*54.0)/60.0));
                        }
                        break;
                        case "00:00":{
                            double lastprice = StartTimeList.get(label);
                            StartTimeList.put(label,lastprice+((cal_hour*13.0)/60.0));
                        }
                        break;
                        case "22:30":{
                            double lastprice = StartTimeList.get(label);
                            StartTimeList.put(label,lastprice+((cal_hour*13.0)/60.0));
                        }
                        break;
                    }

                }else{
                    //move to other calculation
                    //time2 > end time
                    double cal_hour =  (end_.getTime() - start_.getTime()) / (60000);
                    used_hour += (time2_.getTime() - start_.getTime()) / (60000);
                    System.out.println(used_hour);


                    switch (t1) {
                        case "05:30":{
                            double lastprice = StartTimeList.get(label);
                            StartTimeList.put(label,lastprice+((cal_hour*23.0)/60.0));
                        }
                        break;
                        case "18:30":{
                            double lastprice = StartTimeList.get(label);
                            StartTimeList.put(label,lastprice+((cal_hour*54.0)/60.0));
                        }
                        break;
                        case "00:00":{
                            double lastprice = StartTimeList.get(label);
                            StartTimeList.put(label,lastprice+((cal_hour*13.0)/60.0));
                        }
                        break;
                        case "22:30":{
                            double lastprice = StartTimeList.get(label);
                            StartTimeList.put(label,lastprice+((cal_hour*13.0)/60.0));
                        }
                        break;
                    }

                    if(term == 2){
                        used_hour += getUsedhour("22:30","23:59","22:31",end,label);
                    }else if(term == 3){
                        used_hour += getUsedhourWithTerm("18:30","22:30","18:31",end,2,label);
                    }


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return used_hour;
    }

    private double Calculation(String label,String startTime,String endTime){

        double cost = 0;
        switch (label.toLowerCase()){

            case "oven":{
                StartTimeList.put(label,0.0);
                cost = getAllUsedHoursCost(2500,startTime,endTime,label)*2.5;
            }
            break;
            case "washing machine":{
                StartTimeList.put(label,0.0);
                cost = getAllUsedHoursCost(250,startTime,endTime,label)*0.25;
            }
            break;
            case "water pump":{
                StartTimeList.put(label,0.0);
                cost = getAllUsedHoursCost(800,startTime,endTime,label)*0.8;
            }break;

        }
        return cost;
    }

}