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

        //get All data from this app's (local) database and Set those values to relevent ToggleButtons and powerbank
        setData();

        //click submit button
        addListenerOnButtonClick();

    }

    private void addListenerOnButtonClick() {

        buttonSubmit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                //show progress bar
                progressBar.setVisibility(View.VISIBLE);

                //get all data from interface and insert into the arraylist
                ArrayList<Category3HomeAppliance> appliancelist= new ArrayList<>();
                appliancelist.add(new Category3HomeAppliance("C3_1","Water Pump", WaterPump_Switch.getText().toString(),userId));
                appliancelist.add(new Category3HomeAppliance("C3_2","Washing Machine", WashingMachine_Switch.getText().toString(),userId));
                appliancelist.add(new Category3HomeAppliance("C3_3","Pool pump", Poolpump_Switch.getText().toString(),userId));
                appliancelist.add(new Category3HomeAppliance("C3_4","Dish washer", Dishwasher_switch.getText().toString(),userId));
                appliancelist.add(new Category3HomeAppliance("C3_5","Vacuum Cleaner", VacuumCleaner_switch.getText().toString(),userId));
                appliancelist.add(new Category3HomeAppliance("C3_6","Iron", Iron_switch.getText().toString(),userId));
                appliancelist.add(new Category3HomeAppliance("C3_7","Power Bank", PowerBank.getValue()+"",userId));

                //insert data to app's local database
                final int count = new Category3HomeApplianceDAO(Category3Activity.this).insert(appliancelist);

                //insert data to firebase database
                new FirebaseDAO(Category3Activity.this).insertCategory3(appliancelist);

                //iterate all object data
                for(Category3HomeAppliance category3 : appliancelist){

                    //get all manual controls releted to this category
                    ArrayList<ManualControl> AllManualCategoryList = new ManualControlDAO(Category3Activity.this).getAllCategory(category3.getC3_ID(), userId);

                    //get all automatic schedule releted to this category
                    ArrayList<AutomaticSchedule> AllAutomaticScheduleList = new AutomaticScheduleDAO(Category3Activity.this).getAllCategory(userId,category3.getC3_ID() );

                    //get all manual schedule flexible releted to this category
                    ArrayList<ScheduleManual> AllScheduleManualFlexibleList = new ScheduleManualFlexibleLoadsDAO(Category3Activity.this).getAllCategory(userId,category3.getC3_ID());

                    //power bank has number value
                    //if category is not powerbank
                    if(!category3.getC3_LABEL().equals("Power Bank")) {

                        //if category status is yes
                        if (category3.getC3_STATUS_OR_COUNT().equalsIgnoreCase("yes")) {

                            //insert manualcontrol
                            new ManualControlDAO(Category3Activity.this).insert(category3.getC3_ID(), userId, category3.getC3_LABEL());

                            //insert Schedule Manual FlexibleLoads
                            new ScheduleManualFlexibleLoadsDAO(Category3Activity.this).insert(category3.getC3_ID(),userId, category3.getC3_LABEL());

                            //insert Automatic Schedule
                           if(!category3.getC3_LABEL().equals("Iron")) {
                               new AutomaticScheduleDAO(Category3Activity.this).insert(userId, category3.getC3_LABEL(), category3.getC3_ID());
                           }

                        } else {
                            //if category status is no;
                            // delete relevant manualcontrol
                            if (AllManualCategoryList.size() > 0) {
                                new ManualControlDAO(Category3Activity.this).deleteAllCategory(category3.getC3_ID(), userId);
                                for (ManualControl m : AllManualCategoryList) {
                                    String path = "users/" + userId + "/manualControl";
                                    new FirebaseDAO(Category3Activity.this).deleteFromFirebase(path, m.getM_ID());
                                }
                            }
                            //delete relevant schedule manual flexible loads
                            ScheduleManual scheduleManual = new ScheduleManualFlexibleLoadsDAO(Category3Activity.this).getFromLabel(userId, category3.getC3_LABEL());
                            if (scheduleManual != null) {
                                //delete
                                new ScheduleManualFlexibleLoadsDAO(Category3Activity.this).delete(userId,category3.getC3_LABEL());
                                String path = "users/" + userId + "/ScheduleFlexibleLoads";
                                new FirebaseDAO(Category3Activity.this).deleteFromFirebase(path, scheduleManual.getS_ID() + "");
                            }
                            //delete relevant automatic schedule
                            AutomaticSchedule automaticSchedule = new AutomaticScheduleDAO(Category3Activity.this).getFromLabel(userId, category3.getC3_LABEL());
                            if (automaticSchedule != null) {
                                //delete
                                new AutomaticScheduleDAO(Category3Activity.this).delete(userId,category3.getC3_LABEL());
                                String path = "users/" + userId + "/automaticSchedule";
                                new FirebaseDAO(Category3Activity.this).deleteFromFirebase(path, automaticSchedule.getA_ID()+"");
                            }


                        }

                    }else{
                        //if category is  powerbank
                        //get the number count
                        int Category_count = Integer.parseInt(category3.getC3_STATUS_OR_COUNT());
                        if(Category_count > 0){
                            //check with manual control data with this count

                            //if the manual contol datas are higher than category's count
                            if(AllManualCategoryList.size() > Category_count){

                                //check the difference
                                int diff = AllManualCategoryList.size() - Category_count; // Eg .3-1 = 2

                                //delete those extra items from both firebase and app's local database
                                for(int i=0;i<diff;i++){
                                    String path = "users/" + userId + "/manualControl";

                                    //delete  from app's local database
                                    new ManualControlDAO(Category3Activity.this).deletelabel(category3.getC3_LABEL()+" "+(AllManualCategoryList.size()-i),userId);
                                    //delete  from firebase database
                                    new FirebaseDAO(Category3Activity.this).deleteFromFirebase(path,AllManualCategoryList.get(AllManualCategoryList.size()-i-1).getM_ID());

                                    String path_auto = "users/" + userId + "/automaticSchedule";
                                    //delete  from app's local database
                                    new AutomaticScheduleDAO(Category3Activity.this).delete(userId,category3.getC3_LABEL()+" "+(AllAutomaticScheduleList.size()-i));
                                    //delete  from firebase database
                                    new FirebaseDAO(Category3Activity.this).deleteFromFirebase(path_auto,AllAutomaticScheduleList.get(AllAutomaticScheduleList.size()-i-1).getA_ID()+"");

                                    String path_schedule = "users/" + userId + "/ScheduleFlexibleLoads";

                                    //delete  from app's local database
                                    new ScheduleManualFlexibleLoadsDAO(Category3Activity.this).delete(userId,category3.getC3_LABEL()+" "+(AllScheduleManualFlexibleList.size()-i));
                                    //delete  from firebase database
                                    new FirebaseDAO(Category3Activity.this).deleteFromFirebase(path_schedule,AllScheduleManualFlexibleList.get(AllScheduleManualFlexibleList.size()-i-1).getS_ID()+"");

                                }
                            }else{
                                // manual control data are less than category's count
                                //insert new manual control ,new AutomaticSchedule,ScheduleFlexible with new label name
                                for(int i=0;i<Category_count ; i++){
                                    new ManualControlDAO(Category3Activity.this).insert(category3.getC3_ID(),userId,category3.getC3_LABEL()+" "+(i+1));
                                    new AutomaticScheduleDAO(Category3Activity.this).insert(userId,category3.getC3_LABEL()+" "+(i+1),category3.getC3_ID());
                                    new ScheduleManualFlexibleLoadsDAO(Category3Activity.this).insert(category3.getC3_ID(),userId,category3.getC3_LABEL()+" "+(i+1));
                                }
                            }

                        }else{
                            //category1's count number is  zero
                            //remove all categories
                            if(AllManualCategoryList.size()>0) {
                                //delete all from app's local database
                                new ManualControlDAO(Category3Activity.this).deleteAllCategory(category3.getC3_ID(), userId);

                                //delete all from firebase
                                for(ManualControl m : AllManualCategoryList){
                                    String path = "users/" + userId + "/manualControl";
                                    new FirebaseDAO(Category3Activity.this).deleteFromFirebase(path,m.getM_ID());
                                }

                            }
                            if(AllAutomaticScheduleList.size()>0) {
                                //delete all from app's local database
                                new AutomaticScheduleDAO(Category3Activity.this).deleteAllCategory(userId,category3.getC3_ID());
                                //delete all from firebase
                                for(AutomaticSchedule as : AllAutomaticScheduleList){
                                    String path = "users/" + userId + "/automaticSchedule";
                                    new FirebaseDAO(Category3Activity.this).deleteFromFirebase(path,as.getA_ID()+"");
                                }

                            }
                            if(AllScheduleManualFlexibleList.size()>0) {
                                //delete all from app's local database
                                new ScheduleManualFlexibleLoadsDAO(Category3Activity.this).deleteAllCategory(userId,category3.getC3_ID());
                                //delete all from firebase
                                for(ScheduleManual sm : AllScheduleManualFlexibleList){
                                    String path = "users/" + userId + "/ScheduleFlexibleLoads";
                                    new FirebaseDAO(Category3Activity.this).deleteFromFirebase(path,sm.getS_ID()+"");
                                }

                            }
                        }

                    }
                }

                //wait for 10 seconds
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        //hide progressbar
                        progressBar.setVisibility(View.GONE);

                        //display success message
                        Toast.makeText(Category3Activity.this,count+ " Records Inserted ",Toast.LENGTH_LONG).show();

                        //redirect to HomeApplicance
                        startActivity(new Intent(getApplicationContext(),HomeApplianceActivity.class));
                    }
                }, 10600);
            }

        });
    }

    //get All data from this app's (local) database and Set those values to relevent ToggleButtons and number pickers
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

    // enable "set Data" functionality that needs to run while the component is visible and in the foreground
    @Override
    protected void onResume() {
        super.onResume();
        setData();

    }

    //if use press back
    @Override
    public void onBackPressed() {
        //navigate to HomeAppliance Activity
        Intent intent = new Intent(getApplicationContext(), HomeApplianceActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
