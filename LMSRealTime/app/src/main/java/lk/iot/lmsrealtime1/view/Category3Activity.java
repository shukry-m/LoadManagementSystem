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
import lk.iot.lmsrealtime1.data.AutomaticScheduleDAO;
import lk.iot.lmsrealtime1.data.Category3HomeApplianceDAO;
import lk.iot.lmsrealtime1.data.FirebaseDAO;
import lk.iot.lmsrealtime1.data.ManualControlDAO;
import lk.iot.lmsrealtime1.data.ScheduleManualCookingDAO;
import lk.iot.lmsrealtime1.data.ScheduleManualFlexibleLoadsDAO;
import lk.iot.lmsrealtime1.model.AutomaticSchedule;
import lk.iot.lmsrealtime1.model.Category2HomeAppliance;
import lk.iot.lmsrealtime1.model.Category3HomeAppliance;
import lk.iot.lmsrealtime1.model.ManualControl;
import lk.iot.lmsrealtime1.model.ScheduleManual;

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

                for(Category3HomeAppliance category3 : appliancelist){
                    ArrayList<ManualControl> AllCategoryList = new ManualControlDAO(Category3Activity.this).getAllCategory(category3.getC3_ID(), userId);
                    ArrayList<AutomaticSchedule> AllAutomaticScheduleList = new AutomaticScheduleDAO(Category3Activity.this).getAllCategory(userId,category3.getC3_ID() );
                    ArrayList<ScheduleManual> AllScheduleManualFlexibleList = new ScheduleManualFlexibleLoadsDAO(Category3Activity.this).getAllCategory(userId,category3.getC3_ID());
                    if(!category3.getC3_LABEL().equals("Power Bank")) {

                        if (category3.getC3_STATUS_OR_COUNT().equalsIgnoreCase("yes")) {

                            new ManualControlDAO(Category3Activity.this).insert(category3.getC3_ID(), userId, category3.getC3_LABEL());
                            new ScheduleManualFlexibleLoadsDAO(Category3Activity.this).insert(category3.getC3_ID(),userId, category3.getC3_LABEL());
                           if(!category3.getC3_LABEL().equals("Iron")) {
                               new AutomaticScheduleDAO(Category3Activity.this).insert(userId, category3.getC3_LABEL(), category3.getC3_ID());
                           }

                        } else {
                            //remove all categories
                            if (AllCategoryList.size() > 0) {
                                new ManualControlDAO(Category3Activity.this).deleteAllCategory(category3.getC3_ID(), userId);
                                for (ManualControl m : AllCategoryList) {
                                    String path = "users/" + userId + "/manualControl";
                                    new FirebaseDAO(Category3Activity.this).deleteFromFirebase(path, m.getM_ID());
                                }
                            }

                            ScheduleManual scheduleManual = new ScheduleManualFlexibleLoadsDAO(Category3Activity.this).getFromLabel(userId, category3.getC3_LABEL());
                            if (scheduleManual != null) {
                                //delete
                                new ScheduleManualFlexibleLoadsDAO(Category3Activity.this).delete(userId,category3.getC3_LABEL());
                                String path = "users/" + userId + "/ScheduleFlexibleLoads";
                                new FirebaseDAO(Category3Activity.this).deleteFromFirebase(path, scheduleManual.getS_ID() + "");
                            }
                            AutomaticSchedule automaticSchedule = new AutomaticScheduleDAO(Category3Activity.this).getFromLabel(userId, category3.getC3_LABEL());
                            if (automaticSchedule != null) {
                                //delete
                                new AutomaticScheduleDAO(Category3Activity.this).delete(userId,category3.getC3_LABEL());
                                String path = "users/" + userId + "/automaticSchedule";
                                new FirebaseDAO(Category3Activity.this).deleteFromFirebase(path, automaticSchedule.getA_ID()+"");
                            }


                        }
                    }else{
                        int Category_count = Integer.parseInt(category3.getC3_STATUS_OR_COUNT());
                        if(Category_count > 0){

                            //if count is different


                            if(AllCategoryList.size() > Category_count){
                                //remove last
                                int diff = AllCategoryList.size() - Category_count; //3-1 = 2
                                for(int i=0;i<diff;i++){
                                    String path = "users/" + userId + "/manualControl";

                                    new ManualControlDAO(Category3Activity.this).deletelabel(category3.getC3_LABEL()+" "+(AllCategoryList.size()-i),userId);
                                    new FirebaseDAO(Category3Activity.this).deleteFromFirebase(path,AllCategoryList.get(AllCategoryList.size()-i-1).getM_ID());

                                    String path_auto = "users/" + userId + "/automaticSchedule";

                                    new AutomaticScheduleDAO(Category3Activity.this).delete(userId,category3.getC3_LABEL()+" "+(AllAutomaticScheduleList.size()-i));
                                    new FirebaseDAO(Category3Activity.this).deleteFromFirebase(path_auto,AllAutomaticScheduleList.get(AllAutomaticScheduleList.size()-i-1).getA_ID()+"");

                                    String path_schedule = "users/" + userId + "/ScheduleFlexibleLoads";

                                    new ScheduleManualFlexibleLoadsDAO(Category3Activity.this).delete(userId,category3.getC3_LABEL()+" "+(AllScheduleManualFlexibleList.size()-i));
                                    new FirebaseDAO(Category3Activity.this).deleteFromFirebase(path_schedule,AllScheduleManualFlexibleList.get(AllScheduleManualFlexibleList.size()-i-1).getS_ID()+"");

                                }
                            }else{
                                //insert new
                                for(int i=0;i<Category_count ; i++){
                                    new ManualControlDAO(Category3Activity.this).insert(category3.getC3_ID(),userId,category3.getC3_LABEL()+" "+(i+1));
                                    new AutomaticScheduleDAO(Category3Activity.this).insert(userId,category3.getC3_LABEL()+" "+(i+1),category3.getC3_ID());
                                    new ScheduleManualFlexibleLoadsDAO(Category3Activity.this).insert(category3.getC3_ID(),userId,category3.getC3_LABEL()+" "+(i+1));
                                }
                            }

                        }else{
                            //remove all categories

                            if(AllCategoryList.size()>0) {
                                new ManualControlDAO(Category3Activity.this).deleteAllCategory(category3.getC3_ID(), userId);
                                for(ManualControl m : AllCategoryList){
                                    String path = "users/" + userId + "/manualControl";
                                    new FirebaseDAO(Category3Activity.this).deleteFromFirebase(path,m.getM_ID());
                                }

                            }
                            if(AllAutomaticScheduleList.size()>0) {
                                new AutomaticScheduleDAO(Category3Activity.this).deleteAllCategory(userId,category3.getC3_ID());
                                for(AutomaticSchedule as : AllAutomaticScheduleList){
                                    String path = "users/" + userId + "/automaticSchedule";
                                    new FirebaseDAO(Category3Activity.this).deleteFromFirebase(path,as.getA_ID()+"");
                                }

                            }
                            if(AllScheduleManualFlexibleList.size()>0) {
                                new ScheduleManualFlexibleLoadsDAO(Category3Activity.this).deleteAllCategory(userId,category3.getC3_ID());
                                for(ScheduleManual sm : AllScheduleManualFlexibleList){
                                    String path = "users/" + userId + "/ScheduleFlexibleLoads";
                                    new FirebaseDAO(Category3Activity.this).deleteFromFirebase(path,sm.getS_ID()+"");
                                }

                            }
                        }

                    }
                }

                //timout
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Category3Activity.this,count+ " Records Inserted ",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),HomeApplianceActivity.class));
                    }
                }, 10600);
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
