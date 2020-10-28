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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import lk.iot.lmsrealtime1.R;
import lk.iot.lmsrealtime1.data.Category2HomeApplianceDAO;
import lk.iot.lmsrealtime1.data.FirebaseDAO;
import lk.iot.lmsrealtime1.data.ManualControlDAO;
import lk.iot.lmsrealtime1.data.ScheduleManualCookingDAO;
import lk.iot.lmsrealtime1.model.Category2HomeAppliance;
import lk.iot.lmsrealtime1.model.Category3HomeAppliance;
import lk.iot.lmsrealtime1.model.ManualControl;
import lk.iot.lmsrealtime1.model.ScheduleManual;

public class Category2Activity extends AppCompatActivity {

    private ToggleButton Blender_Switch, RiceCooker_Switch,HotPlate_Switch,Oven_switch,
    AirFryers_switch,MicroWaveOven_switch,ElectricKettle_switch,HandMixture_switch,SandwichToaster_switch;
    private Button buttonSubmit;
    String userId = "0";
    FirebaseAuth fAuth;
    private DatabaseReference mDatabase;
    //Toolbar top_bar;
    ArrayList<Category2HomeAppliance> list = new ArrayList<>();
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ///set ui
        setContentView(R.layout.activity_category2);



        fAuth = FirebaseAuth.getInstance();
        userId = (fAuth.getCurrentUser()!= null)? fAuth.getCurrentUser().getUid():"0";
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //map ui elements to these variables
        // so we can access the data which user has inserted
        Blender_Switch= findViewById(R.id.Blender_Switch);
        RiceCooker_Switch= findViewById(R.id.RiceCooker_Switch);
        HotPlate_Switch= findViewById(R.id.HotPlate_Switch);
        Oven_switch= findViewById(R.id.Oven_switch);
        AirFryers_switch= findViewById(R.id.AirFryers_switch);
        MicroWaveOven_switch= findViewById(R.id.MicroWaveOven_switch);
        ElectricKettle_switch= findViewById(R.id.ElectricKettle_switch);
        HandMixture_switch= findViewById(R.id.HandMixture_switch);
        SandwichToaster_switch= findViewById(R.id.SandwichToaster_switch);
        buttonSubmit= findViewById(R.id.btn_save);
        progressBar = findViewById(R.id.progressBar);

        //get All data from this app's (local) database and Set those values to relevent ToggleButtons
        setData();

        //click submit button
        addListenerOnButtonClick();

    }

    private void addListenerOnButtonClick() {

        //click submit button
        buttonSubmit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                //show progress bar
                progressBar.setVisibility(View.VISIBLE);

                //insert All ui elements to the list
                ArrayList<Category2HomeAppliance> list= new ArrayList<>();
                list.add(new Category2HomeAppliance("C2_1",userId,"Blender", Blender_Switch.getText().toString()));
                list.add(new Category2HomeAppliance("C2_2",userId,"Rice Cooker", RiceCooker_Switch.getText().toString()));
                list.add(new Category2HomeAppliance("C2_3",userId,"Hot Plate", HotPlate_Switch.getText().toString()));
                list.add(new Category2HomeAppliance("C2_4",userId,"Oven", Oven_switch.getText().toString()));
                list.add(new Category2HomeAppliance("C2_5",userId,"Air Fryers", AirFryers_switch.getText().toString()));
                list.add(new Category2HomeAppliance("C2_6",userId,"Micro Wave Oven", MicroWaveOven_switch.getText().toString()));
                list.add(new Category2HomeAppliance("C2_7",userId,"Electric Kettle", ElectricKettle_switch.getText().toString()));
                list.add(new Category2HomeAppliance("C2_8",userId,"Hand Mixture", HandMixture_switch.getText().toString()));
                list.add(new Category2HomeAppliance("C2_9",userId,"Sandwich Toaster", SandwichToaster_switch.getText().toString()));

                //insert all datas to app's local database
                final int count = new Category2HomeApplianceDAO(Category2Activity.this).insert(list);


                //insert all categories to firebase database
                new FirebaseDAO(Category2Activity.this).insertCategory2(list);

                //iterate the list
                for(Category2HomeAppliance  category2 : list){

                    //get All categories from manual control database
                    ArrayList<ManualControl> AllManualCategoryList = new ManualControlDAO(Category2Activity.this).getAllCategory(category2.getC2_ID(),userId);

                    //if category status is yes
                    if(category2.getC2_STATUS().equalsIgnoreCase("yes")){

                        //insert manualcontrol
                        new ManualControlDAO(Category2Activity.this).insert(category2.getC2_ID(),userId,category2.getC2_LABEL());
                        //insert Schedule cooking
                        new ScheduleManualCookingDAO(Category2Activity.this).insert(category2.getC2_ID(),userId,category2.getC2_LABEL());
                    }else{
                        //if category status is no; delete all manualcontrol and schedule manual data

                        //delete all data from manual control
                        if(AllManualCategoryList.size()>0) {
                            //delete from app's local database
                            new ManualControlDAO(Category2Activity.this).deleteAllCategory(category2.getC2_ID(), userId);

                            //delete manaual control from firebase
                            for(ManualControl m : AllManualCategoryList){
                                String path = "users/" + userId + "/manualControl";
                                new FirebaseDAO(Category2Activity.this).deleteFromFirebase(path,m.getM_ID());
                            }
                        }
                        //get Manual Schedule Cooking with that label name
                        ScheduleManual scheduleManual = new ScheduleManualCookingDAO(Category2Activity.this).getFromLabel(userId,category2.getC2_LABEL());
                        if(scheduleManual != null){
                            //delete from app's local db
                            new ScheduleManualCookingDAO(Category2Activity.this).delete(userId,category2.getC2_LABEL());

                            //delete from firebase
                            String path = "users/" + userId + "/ScheduleCooking";
                            new FirebaseDAO(Category2Activity.this).deleteFromFirebase(path,scheduleManual.getS_ID()+"");
                        }

                    }
                }

                //wait for 6 seconds
                new Handler().postDelayed(new Runnable() {
                    public void run() {

                        //after 6 seconds
                        //hide progressbar
                        progressBar.setVisibility(View.GONE);

                        //display success message
                        Toast.makeText(Category2Activity.this,count+ " Records Inserted ",Toast.LENGTH_LONG).show();

                        //naviate to HomeAppliace Activity
                        startActivity(new Intent(getApplicationContext(),HomeApplianceActivity.class));
                    }
                }, 6600);



            }

        });
    }


    //get All data from this app's (local) database and Set those values to relevent ToggleButtons
    private void setData() {
        userId = (fAuth.getCurrentUser()!= null)? fAuth.getCurrentUser().getUid():"0";
        list = new Category2HomeApplianceDAO(Category2Activity.this).getAll(userId);
        System.out.println(list);
        if(list.size() >0){
            for(Category2HomeAppliance homeAppliance : list){
                switch(homeAppliance.getC2_LABEL()){
                    case "Blender":{
                        Blender_Switch.setText(homeAppliance.getC2_STATUS());
                        break;
                    }
                    case "Rice Cooker":{
                        RiceCooker_Switch.setText(homeAppliance.getC2_STATUS());
                        break;
                    }
                    case "Hot Plate":{
                        HotPlate_Switch.setText(homeAppliance.getC2_STATUS());
                        break;
                    }
                    case "Oven":{
                        Oven_switch.setText(homeAppliance.getC2_STATUS());
                        break;
                    }
                    case "Air Fryers":{
                        AirFryers_switch.setText(homeAppliance.getC2_STATUS());
                        break;
                    }
                    case "Micro Wave Oven":{
                        MicroWaveOven_switch.setText(homeAppliance.getC2_STATUS());
                        break;
                    }
                    case "Electric Kettle":{
                        ElectricKettle_switch.setText(homeAppliance.getC2_STATUS());
                        break;
                    }
                    case "Hand Mixture":{
                        HandMixture_switch.setText(homeAppliance.getC2_STATUS());
                        break;
                    }
                    case "Sandwich Toaster":{
                        SandwichToaster_switch.setText(homeAppliance.getC2_STATUS());
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
