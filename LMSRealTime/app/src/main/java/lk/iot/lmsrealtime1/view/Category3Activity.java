package lk.iot.lmsrealtime1.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.travijuu.numberpicker.library.NumberPicker;

import java.util.ArrayList;

import lk.iot.lmsrealtime1.R;
import lk.iot.lmsrealtime1.data.Category3HomeApplianceDAO;
import lk.iot.lmsrealtime1.data.FirebaseDAO;
import lk.iot.lmsrealtime1.model.Category3HomeAppliance;

public class Category3Activity extends AppCompatActivity {

     ToggleButton WaterPump_Switch, WashingMachine_Switch,Poolpump_Switch,Dishwasher_switch,
            VacuumCleaner_switch,Iron_switch;
     Button buttonSubmit;
     NumberPicker PowerBank;
    String userId = "0";
    FirebaseAuth fAuth;

    ArrayList<Category3HomeAppliance> list = new ArrayList<>();
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category3);

        fAuth = FirebaseAuth.getInstance();
        userId = (fAuth.getCurrentUser()!= null)? fAuth.getCurrentUser().getUid():"0";

        //top_bar =  findViewById(R.id.tb_category3);
        //Getting the ToggleButton and Button instance from the layout xml file
        WaterPump_Switch= findViewById(R.id.WaterPump_Switch);
        WashingMachine_Switch= findViewById(R.id.WashingMachine_Switch);
        Poolpump_Switch= findViewById(R.id.Poolpump_Switch);
        Dishwasher_switch= findViewById(R.id.Dishwasher_switch);
        VacuumCleaner_switch= findViewById(R.id.VacuumCleaner_switch);
        Iron_switch= findViewById(R.id.Iron_switch);
        PowerBank= findViewById(R.id.PowerBank);

        buttonSubmit= findViewById(R.id.btn_save);
        progressBar = findViewById(R.id.progressBar);

        setData();

        addListenerOnButtonClick();

    }

    private void addListenerOnButtonClick() {

        buttonSubmit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);

                ArrayList<Category3HomeAppliance> appliancelist= new ArrayList<>();
                appliancelist.add(new Category3HomeAppliance("C3_1","Water Pump", WaterPump_Switch.getText().toString(),userId));
                appliancelist.add(new Category3HomeAppliance("C3_2","Washing Machine", WashingMachine_Switch.getText().toString(),userId));
                appliancelist.add(new Category3HomeAppliance("C3_3","Pool pump", Poolpump_Switch.getText().toString(),userId));
                appliancelist.add(new Category3HomeAppliance("C3_4","Dish washer", Dishwasher_switch.getText().toString(),userId));
                appliancelist.add(new Category3HomeAppliance("C3_5","Vacuum Cleaner", VacuumCleaner_switch.getText().toString(),userId));
                appliancelist.add(new Category3HomeAppliance("C3_6","Iron", Iron_switch.getText().toString(),userId));
                appliancelist.add(new Category3HomeAppliance("C3_7","Power Bank", PowerBank.getValue()+"",userId));

                final int count = new Category3HomeApplianceDAO(Category3Activity.this).insert(appliancelist);

                new FirebaseDAO(Category3Activity.this).insertCategory3(appliancelist);

                new FirebaseDAO(Category3Activity.this).getCategory("category3");
                startActivity(new Intent(getApplicationContext(),HomeApplianceActivity.class));

                //timout 100
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Category3Activity.this,count+ " Records Inserted ",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),HomeApplianceActivity.class));
                    }
                }, 5600);
            }

        });
    }

    private void setData() {
        userId = (fAuth.getCurrentUser()!= null)? fAuth.getCurrentUser().getUid():"0";
        list = new Category3HomeApplianceDAO(Category3Activity.this).getAll(userId);
        System.out.println(list);
        if(list.size() >0){
            for(Category3HomeAppliance homeAppliance : list){
                switch(homeAppliance.getC3_LABEL()){
                    case "Water Pump":{
                        WaterPump_Switch.setText(homeAppliance.getC3_STATUS_OR_COUNT());
                        break;
                    }
                    case "Washing Machine":{
                        WashingMachine_Switch.setText(homeAppliance.getC3_STATUS_OR_COUNT());
                        break;
                    }
                    case "Pool pump":{
                        Poolpump_Switch.setText(homeAppliance.getC3_STATUS_OR_COUNT());
                        break;
                    }
                    case "Dish washer":{
                        Dishwasher_switch.setText(homeAppliance.getC3_STATUS_OR_COUNT());
                        break;
                    }
                    case "Vacuum Cleaner":{
                        VacuumCleaner_switch.setText(homeAppliance.getC3_STATUS_OR_COUNT());
                        break;
                    }
                    case "Iron":{
                        Iron_switch.setText(homeAppliance.getC3_STATUS_OR_COUNT());
                        break;
                    }
                    case "Power Bank":{
                        PowerBank.setValue(Integer.parseInt(homeAppliance.getC3_STATUS_OR_COUNT()));
                        break;
                    }
                }

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), HomeApplianceActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
