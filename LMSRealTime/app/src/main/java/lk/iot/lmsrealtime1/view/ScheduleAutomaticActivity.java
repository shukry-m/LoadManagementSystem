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
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lk.iot.lmsrealtime1.R;
import lk.iot.lmsrealtime1.adapter.ScheduleAutomaticAdapter;
import lk.iot.lmsrealtime1.data.AutomaticScheduleDAO;
import lk.iot.lmsrealtime1.data.Firebase1DAO;
import lk.iot.lmsrealtime1.helper.ClickListener;
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



        if(list.size() == 0){
            NoData.setVisibility(View.VISIBLE);
        }else{
            NoData.setVisibility(View.GONE);
        }

        ChangeAdapter(dbList);

        LinearLayoutManager layoutManager = new LinearLayoutManager( ScheduleAutomaticActivity.this,  RecyclerView.VERTICAL, false );
        rvSHItems.setLayoutManager( layoutManager );
        rvSHItems.setHasFixedSize( true );
        rvSHItems.getRecycledViewPool().setMaxRecycledViews(0, 0);
        rvSHItems.setAdapter(adapter);

        System.out.println(Automatic_Switch.getText().toString());


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


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if(Automatic_Switch.getText().toString().equalsIgnoreCase("on")){
                    sum = SaveData("1" , dbList);
                }else{
                    sum = SaveData("0" , dbList);
                }


                ArrayList<AutomaticSchedule> allData = new AutomaticScheduleDAO(ScheduleAutomaticActivity.this).getAll(userID);

                new Firebase1DAO(ScheduleAutomaticActivity.this).insertAutomaicSchedule(allData);

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ScheduleAutomaticActivity.this, sum + " Records Inserted ", Toast.LENGTH_LONG).show();
                        //displayStatusMessage("Records Successfully inserted",getApplicationContext());
                        startActivity(new Intent(getApplicationContext(), ScheduleActivity.class));
                    }
                }, 4600);



            }
        });

    }

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

    void downloadData(){
        new Firebase1DAO(ScheduleAutomaticActivity.this).getAutomaicSchedule();
        list = new AutomaticScheduleDAO(ScheduleAutomaticActivity.this).getAll(userID);
        ArrayList<AutomaticSchedule> getONStatusList = new AutomaticScheduleDAO(ScheduleAutomaticActivity.this).getAllStatusIsON(userID);
        if(getONStatusList.size() > 0){
            Automatic_Switch.setText("ON");
        }else{
            Automatic_Switch.setText("OFF");
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), ScheduleActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
