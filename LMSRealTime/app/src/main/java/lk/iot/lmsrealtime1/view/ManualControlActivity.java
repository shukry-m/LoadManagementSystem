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
import lk.iot.lmsrealtime1.adapter.ManualControlAdapter;
import lk.iot.lmsrealtime1.data.AllStatusDAO;
import lk.iot.lmsrealtime1.data.FirebaseDAO;
import lk.iot.lmsrealtime1.data.ManualControlDAO;
import lk.iot.lmsrealtime1.helper.ClickListener;
import lk.iot.lmsrealtime1.model.AllStatus;
import lk.iot.lmsrealtime1.model.ManualControl;


public class ManualControlActivity extends AppCompatActivity {

    RecyclerView rvMCItems;
    ManualControlAdapter adapter;
    ArrayList<ManualControl> list;
    LinearLayout NoData;
    //Toolbar MCToolBar;
    FirebaseAuth fAuth;
    Button btn_save;
    String userID;
    ProgressBar progressBar;
    ToggleButton Manual_Switch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_control);
       // MCToolBar = findViewById(R.id.tb_manual_control);
        rvMCItems = findViewById(R.id.rvMCItems);
        fAuth = FirebaseAuth.getInstance();
        userID = (fAuth.getCurrentUser()!= null)? fAuth.getCurrentUser().getUid():"0";
        btn_save = findViewById(R.id.btn_save);
        //MCToolBar.setTitle("Manual Control");
        progressBar = findViewById(R.id.progressBar);
        NoData = findViewById(R.id.NoData);
        Manual_Switch = findViewById(R.id.Manual_Switch);
        downloadData();

        final HashMap<String, ManualControl> dbList = new HashMap<>();

        if(list.size() == 0){
            NoData.setVisibility(View.VISIBLE);
        }else{
            NoData.setVisibility(View.GONE);
        }


        adapter = new ManualControlAdapter(ManualControlActivity.this,list,new ClickListener(){

            @Override
            public void onCheckedChanged(int position, CompoundButton cb, boolean on) {
                ManualControl hm = list.get(position);
                //Toast.makeText(ManualControlActivity.this,hm.getM_ID()+" : "+on,Toast.LENGTH_LONG).show();


                if(on){

                    ManualControl m = new ManualControl();
                    m.setM_ID(hm.getM_ID());
                    m.setM_USER_ID(userID);
                    m.setM_LABEL(hm.getM_LABEL());
                    m.setM_STATUS("1");
                    m.setM_CATEGORY_ID(hm.getM_CATEGORY_ID());
                    dbList.put(hm.getM_ID(),m);

                } else{


                    ManualControl m = new ManualControl();
                    m.setM_ID(hm.getM_ID());
                    m.setM_USER_ID(userID);
                    m.setM_LABEL(hm.getM_LABEL());
                    m.setM_STATUS("0");
                    m.setM_CATEGORY_ID(hm.getM_CATEGORY_ID());
                    dbList.put(hm.getM_ID(),m);
                }


            }

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {}
            @Override
            public void onPositionClicked(int position, View view) {}


        });

        LinearLayoutManager layoutManager = new LinearLayoutManager( ManualControlActivity.this,  RecyclerView.VERTICAL, false );
        rvMCItems.setLayoutManager( layoutManager );
        rvMCItems.setHasFixedSize( true );
        rvMCItems.getRecycledViewPool().setMaxRecycledViews(0, 0);
        rvMCItems.setAdapter(adapter);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int count = 0;
                progressBar.setVisibility(View.VISIBLE);
                for (Map.Entry<String, ManualControl> entry : dbList.entrySet()) {

                     System.out.println("*** "+entry.getKey() + " = " + entry.getValue());
                     count += new ManualControlDAO(ManualControlActivity.this).insert(entry.getValue());

                }
                final int sum = count;
                AllStatus allStatus = new AllStatus(userID,"manualControl");
                    if(Manual_Switch.getText().toString().equalsIgnoreCase("on")){
                        allStatus.setALL_STATUS("1");
                    }else{
                        allStatus.setALL_STATUS("0");
                    }
                    allStatus.setALL_ID(2);


                new AllStatusDAO(ManualControlActivity.this).insert(allStatus);
                ArrayList<ManualControl> allData = new ManualControlDAO(ManualControlActivity.this).getAll(userID);
                AllStatus allSt = new AllStatusDAO(ManualControlActivity.this).get("manualControl",userID);

                new FirebaseDAO(ManualControlActivity.this).insertManualControlStatus(allSt);

                new FirebaseDAO(ManualControlActivity.this).insertManualControl(allData);


                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ManualControlActivity.this,sum+" Records Inserted ",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }
                }, 4600);
            }
        });

    }

    void downloadData(){
        new FirebaseDAO(ManualControlActivity.this).getManualControl();
        new FirebaseDAO(ManualControlActivity.this).getManualControlStatus();
        try {
            list = new ManualControlDAO(ManualControlActivity.this).getAll(userID);
            AllStatus allStatus = new AllStatusDAO(ManualControlActivity.this).get("manualControl", userID);
            System.out.println(allStatus);
            if (allStatus.getALL_STATUS().equals("1")) {
                Manual_Switch.setText("ON");
            } else {
                Manual_Switch.setText("OFF");
            }
        }catch (Exception e){
            Manual_Switch.setText("OFF");
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
