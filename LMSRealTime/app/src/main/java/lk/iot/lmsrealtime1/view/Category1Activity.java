package lk.iot.lmsrealtime1.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.travijuu.numberpicker.library.NumberPicker;

import java.util.ArrayList;

import lk.iot.lmsrealtime1.R;
import lk.iot.lmsrealtime1.data.Category1HomeApplianceDAO;
import lk.iot.lmsrealtime1.data.FirebaseDAO;
import lk.iot.lmsrealtime1.data.ManualControlDAO;
import lk.iot.lmsrealtime1.model.Category1HomeAppliance;
import lk.iot.lmsrealtime1.model.ManualControl;

public class Category1Activity extends AppCompatActivity {

    NumberPicker Fan_number_picker,CFL_number_picker,LED_number_picker,
            Incandescent_number_picker,Halogen_number_picker;
    String userId = "0";
    FirebaseAuth fAuth;
     FirebaseDatabase database;
    ArrayList<Category1HomeAppliance> list = new ArrayList<>();
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ///set ui
        setContentView(R.layout.activity_category1);
        //get Firebase Authentication reference
        fAuth = FirebaseAuth.getInstance();
        //get firebase userId
        userId = (fAuth.getCurrentUser()!= null)? fAuth.getCurrentUser().getUid():"0";
        //get Firebase database reference
        database = FirebaseDatabase.getInstance();

        //map ui elements to these variables
        // so we can access the data which user has inserted
        Fan_number_picker =findViewById(R.id.Fan_number_picker);

        CFL_number_picker = findViewById(R.id.CFL_number_picker);

        LED_number_picker = findViewById(R.id.LED_number_picker);

        Incandescent_number_picker =findViewById(R.id.Incandescent_number_picker);

        Halogen_number_picker = findViewById(R.id.Halogen_number_picker);

        progressBar = findViewById(R.id.progressBar);

        //get All data from this app's (local) database and Set those values to relevent number pickers
        setData();



    }

    //when user click save button
    public void Save(View view) {
        //display progress bar
        progressBar.setVisibility(View.VISIBLE);

        //get all values and insert into the arraylist
        ArrayList<Category1HomeAppliance> list= new ArrayList<>();
        list.add(new Category1HomeAppliance("C1_1",userId,"Fan", Fan_number_picker.getValue()));
        list.add(new Category1HomeAppliance("C1_2",userId,"CFL", CFL_number_picker.getValue()));
        list.add(new Category1HomeAppliance("C1_3",userId,"LED", LED_number_picker.getValue()));
        list.add(new Category1HomeAppliance("C1_4",userId,"Incandescent", Incandescent_number_picker.getValue()));
        list.add(new Category1HomeAppliance("C1_5",userId,"Halogen", Halogen_number_picker.getValue()));

        //insert all data to app database
        final int count = new Category1HomeApplianceDAO(Category1Activity.this).insert(list);

        //insert these datas to firebase
        new FirebaseDAO(Category1Activity.this).insertCategory1(list);



        //iterate the list
        for(Category1HomeAppliance  category1 : list){
            //get All categories from manual control database
            ArrayList<ManualControl> AllCategoryList = new ManualControlDAO(Category1Activity.this).getAllCategory(category1.getC_ID(),userId);

            //if the category1's count number is not zero
            if(category1.getC_Count() > 0){

                //check with manual control data with this count

                //if the manual contol datas are higher than category's count
                if(AllCategoryList.size() > category1.getC_Count()){
                    //check the difference
                    int diff = AllCategoryList.size() - category1.getC_Count(); // Eg : 3-1 = 2

                    //delete those extra items from both firebase and app's local database

                    for(int i=0;i<diff;i++){
                        String path = "users/" + userId + "/manualControl";
                        //delete  from app's local database
                        new ManualControlDAO(Category1Activity.this).deletelabel(category1.getC_LABEL()+" "+(AllCategoryList.size()-i),userId);
                        //delete  from firebase
                        new FirebaseDAO(Category1Activity.this).deleteFromFirebase(path,AllCategoryList.get(AllCategoryList.size()-i-1).getM_ID());
                    }
                }else{
                    // manual contol datas are less than category's count
                    //insert new manual control with new label name
                    for(int i=0;i<category1.getC_Count() ; i++){
                        new ManualControlDAO(Category1Activity.this).insert(category1.getC_ID(),userId,category1.getC_LABEL()+" "+(i+1));
                    }
                }
            }else{
                //category1's count number is  zero
                //remove all categories
                if(AllCategoryList.size()>0) {
                    //delete all from app's local database
                    new ManualControlDAO(Category1Activity.this).deleteAllCategory(category1.getC_ID(), userId);
                    //delete all from firebase
                    for(ManualControl m : AllCategoryList){
                        String path = "users/" + userId + "/manualControl";
                        new FirebaseDAO(Category1Activity.this).deleteFromFirebase(path,m.getM_ID());
                    }

                }
            }
        }

        //wait for 4 seconds
        new Handler().postDelayed(new Runnable() {
            public void run() {

                //hide progressbar
                progressBar.setVisibility(View.GONE);

                //display success message
                Toast.makeText(Category1Activity.this,count+" Records Inserted ",Toast.LENGTH_LONG).show();

                //redirect to HomeApplicance
                startActivity(new Intent(getApplicationContext(),HomeApplianceActivity.class));
            }
        }, 4600);




    }



    //get All data from this app's (local) database and Set those values to relevent number pickers
    private void setData() {
        userId = (fAuth.getCurrentUser()!= null)? fAuth.getCurrentUser().getUid():"0";
        list = new Category1HomeApplianceDAO(Category1Activity.this).getAll(userId);
        System.out.println(list);
       if(list.size() >0){
           for(Category1HomeAppliance homeAppliance : list){
               switch(homeAppliance.getC_LABEL()){
                   case "Fan":{
                       Fan_number_picker.setValue(homeAppliance.getC_Count());
                       break;
                   }
                   case "CFL":{
                       CFL_number_picker.setValue(homeAppliance.getC_Count());
                       break;
                   }
                   case "LED":{
                       LED_number_picker.setValue(homeAppliance.getC_Count());
                       break;
                   }
                   case "Incandescent":{
                       Incandescent_number_picker.setValue(homeAppliance.getC_Count());
                       break;
                   }
                   case "Halogen":{
                       Halogen_number_picker.setValue(homeAppliance.getC_Count());
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
        //get All data from this app's (local) database and Set those values to relevent number pickers
        setData();

    }

    //if use press back
    @Override
    public void onBackPressed() {
        //navigate to HomeAppliance
        Intent intent = new Intent(getApplicationContext(), HomeApplianceActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
