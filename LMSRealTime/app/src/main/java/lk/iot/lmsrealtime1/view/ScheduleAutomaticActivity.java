package lk.iot.lmsrealtime1.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lk.iot.lmsrealtime1.R;
import lk.iot.lmsrealtime1.adapter.ScheduleAutomaticAdapter;
import lk.iot.lmsrealtime1.data.AllStatusDAO;
import lk.iot.lmsrealtime1.data.AutomaticScheduleDAO;
import lk.iot.lmsrealtime1.data.FirebaseDAO;
import lk.iot.lmsrealtime1.helper.ClickListener;
import lk.iot.lmsrealtime1.model.AllStatus;
import lk.iot.lmsrealtime1.model.AutomaticSchedule;

public class ScheduleAutomaticActivity extends AppCompatActivity {

    RecyclerView rvSHItems;
    ScheduleAutomaticAdapter adapter;
    ArrayList<AutomaticSchedule> list;
    LinearLayout NoData;
  //  Toolbar MCToolBar;
    FirebaseAuth fAuth;
    Button btn_save;
    String userID;
    ProgressBar progressBar;
    ToggleButton Automatic_Switch;
    boolean clickedToggle = false;
    final HashMap<Integer, AutomaticSchedule> dbList = new HashMap<>();
    int sum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_automatic);
       // MCToolBar = findViewById(R.id.tb_automatic);
        rvSHItems = findViewById(R.id.rvSHItems);
        fAuth = FirebaseAuth.getInstance();
        userID = (fAuth.getCurrentUser()!= null)? fAuth.getCurrentUser().getUid():"0";
        btn_save = findViewById(R.id.btn_save);
      //  MCToolBar.setTitle("Automatic Schedule");
        progressBar = findViewById(R.id.progressBar);
        NoData = findViewById(R.id.NoData);
        Automatic_Switch = findViewById(R.id.Automatic_Switch);

        downloadData();



        //if list has no items display noData messge
        if(list.size() == 0){
            NoData.setVisibility(View.VISIBLE);
        }else{
            //if list has  items hide noData messge
            NoData.setVisibility(View.GONE);
        }

        ChangeAdapter(dbList);

        LinearLayoutManager layoutManager = new LinearLayoutManager( ScheduleAutomaticActivity.this,  RecyclerView.VERTICAL, false );
        rvSHItems.setLayoutManager( layoutManager );
        rvSHItems.setHasFixedSize( true );
        rvSHItems.getRecycledViewPool().setMaxRecycledViews(0, 0);
        rvSHItems.setAdapter(adapter);

        System.out.println(Automatic_Switch.getText().toString());


        //user click Set automatic schedule button
        Automatic_Switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dbList.size() >0) {
                    clickedToggle = true;
                   // sum = displayStatusMessage("Do you want To set Automatic On or Off", ScheduleAutomaticActivity.this);
                }else{
                    Toast.makeText(ScheduleAutomaticActivity.this, "Please check items ", Toast.LENGTH_LONG).show();
                }
            }

        });


        //user click save button
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show progress bar
                progressBar.setVisibility(View.VISIBLE);

                AllStatus allStatus = new AllStatus(userID,"automaticSchedule");

                //if Automatic schedule button is on set status to 1
                if(Automatic_Switch.getText().toString().equalsIgnoreCase("on")){
                   allStatus.setALL_STATUS("1");
                }else{
                    allStatus.setALL_STATUS("0");
                }

                allStatus.setALL_ID(1);
                sum = SaveData("1" , dbList);

                //insert status
                new AllStatusDAO(ScheduleAutomaticActivity.this).insert(allStatus);

                //get all Automatic schedule
                ArrayList<AutomaticSchedule> allData = new AutomaticScheduleDAO(ScheduleAutomaticActivity.this).getAll(userID);
                AllStatus allSt = new AllStatusDAO(ScheduleAutomaticActivity.this).get("automaticSchedule",userID);

                //insert into firebase
                new FirebaseDAO(ScheduleAutomaticActivity.this).insertAutomaticScheduleStatus(allSt);
                new FirebaseDAO(ScheduleAutomaticActivity.this).insertAutomaicSchedule(allData);


                //wait for 4 seconds
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        //hide progress bar
                        progressBar.setVisibility(View.GONE);
                        //show success messgae
                        Toast.makeText(ScheduleAutomaticActivity.this, sum + " Records Inserted ", Toast.LENGTH_LONG).show();
                        //navigate to schedule
                        startActivity(new Intent(getApplicationContext(), ScheduleActivity.class));
                    }
                }, 4600);



            }
        });

    }

    //insert Data into app's local database
    public int SaveData(final String status , HashMap<Integer, AutomaticSchedule> dbList){
        int count = 0;

       // new AutomaticScheduleDAO(ScheduleAutomaticActivity.this).deleteAll(userID);
        for (Map.Entry<Integer, AutomaticSchedule> entry : dbList.entrySet()) {

            AutomaticSchedule  automaticSchedule = entry.getValue();
            if(!status.equals("1")){
                automaticSchedule.setA_STATUS(status);
            }
            automaticSchedule.setA_USER_ID(userID);
            count += new AutomaticScheduleDAO(ScheduleAutomaticActivity.this).insert(automaticSchedule);

        }

        return  count;
    }

    //initialize adpater for recycleview
    private void ChangeAdapter( final HashMap<Integer, AutomaticSchedule> dbList) {
        adapter =  new ScheduleAutomaticAdapter(ScheduleAutomaticActivity.this,userID,list,new ClickListener(){



            @Override
            public void onCheckedChanged(int position, CompoundButton cb, boolean on) {

                AutomaticSchedule as = list.get(position);
                System.out.println("* before "+as);

                String res = on ? "1" : "0";
                as.setA_STATUS(res);
                as.setA_USER_ID(userID);
                System.out.println("* AutomaticSchedule "+as+" res "+res);

                dbList.put(as.getA_ID(),as);

            }

            @Override
            public void onPositionClicked(int position, View view) {

            }

            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on) {

            }
        });
        rvSHItems.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //download all data from firebase and insert into list
    void downloadData(){
        new FirebaseDAO(ScheduleAutomaticActivity.this).getAutomaicSchedule();
        new FirebaseDAO(ScheduleAutomaticActivity.this).getAutomaticScheduleStatus();
        list = new AutomaticScheduleDAO(ScheduleAutomaticActivity.this).getAll(userID);
        try {
            AllStatus allStatus = new AllStatusDAO(ScheduleAutomaticActivity.this).get("automaticSchedule", userID);
            if (allStatus.getALL_STATUS().equals("1")) {
                Automatic_Switch.setText("ON");
            } else {
                Automatic_Switch.setText("OFF");
            }
        }catch (Exception e){
            Automatic_Switch.setText("OFF");
            e.printStackTrace();
        }
    }

    //if user press back
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), ScheduleActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
