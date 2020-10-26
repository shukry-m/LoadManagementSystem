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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lk.iot.lmsrealtime1.R;
import lk.iot.lmsrealtime1.adapter.ManualControlAdapter;
import lk.iot.lmsrealtime1.data.Firebase1DAO;
import lk.iot.lmsrealtime1.data.ManualControlDAO;
import lk.iot.lmsrealtime1.helper.ClickListener;
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
                ArrayList<ManualControl> allData = new ManualControlDAO(ManualControlActivity.this).getAll(userID);

                new Firebase1DAO(ManualControlActivity.this).insertManualControl(allData);


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
        new Firebase1DAO(ManualControlActivity.this).getManualControl();
        list = new ManualControlDAO(ManualControlActivity.this).getAll(userID);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
