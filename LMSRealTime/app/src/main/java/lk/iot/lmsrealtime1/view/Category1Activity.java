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
        setContentView(R.layout.activity_category1);
        fAuth = FirebaseAuth.getInstance();
        userId = (fAuth.getCurrentUser()!= null)? fAuth.getCurrentUser().getUid():"0";
        database = FirebaseDatabase.getInstance();

        Fan_number_picker =findViewById(R.id.Fan_number_picker);

        CFL_number_picker = findViewById(R.id.CFL_number_picker);

        LED_number_picker = findViewById(R.id.LED_number_picker);

        Incandescent_number_picker =findViewById(R.id.Incandescent_number_picker);

        Halogen_number_picker = findViewById(R.id.Halogen_number_picker);

        progressBar = findViewById(R.id.progressBar);

        setData();



    }

    public void Save(View view) {
        progressBar.setVisibility(View.VISIBLE);
        ArrayList<Category1HomeAppliance> list= new ArrayList<>();
        list.add(new Category1HomeAppliance("C1_1",userId,"Fan", Fan_number_picker.getValue()));
        list.add(new Category1HomeAppliance("C1_2",userId,"CFL", CFL_number_picker.getValue()));
        list.add(new Category1HomeAppliance("C1_3",userId,"LED", LED_number_picker.getValue()));
        list.add(new Category1HomeAppliance("C1_4",userId,"Incandescent", Incandescent_number_picker.getValue()));
        list.add(new Category1HomeAppliance("C1_5",userId,"Halogen", Halogen_number_picker.getValue()));

        for(Category1HomeAppliance c : list){
            System.out.println(" **** "+c);
        }
        final int count = new Category1HomeApplianceDAO(Category1Activity.this).insert(list);


        new FirebaseDAO(Category1Activity.this).insertCategory1(list);

        new FirebaseDAO(Category1Activity.this).getCategory("category1");

        for(Category1HomeAppliance  category1 : list){
            ArrayList<ManualControl> AllCategoryList = new ManualControlDAO(Category1Activity.this).getAllCategory(category1.getC_ID(),userId);
            if(category1.getC_Count() > 0){

                //if count is different

                if(AllCategoryList.size() > category1.getC_Count()){
                    //remove last
                    int diff = AllCategoryList.size() - category1.getC_Count(); //3-1 = 2
                    for(int i=0;i<diff;i++){
                        String path = "users/" + userId + "/manualControl";

                        new ManualControlDAO(Category1Activity.this).deletelabel(category1.getC_LABEL()+" "+(AllCategoryList.size()-i),userId);

                        new FirebaseDAO(Category1Activity.this).deleteFromFirebase(path,AllCategoryList.get(AllCategoryList.size()-i-1).getM_ID());
                    }
                }else{
                    //insert new
                    for(int i=0;i<category1.getC_Count() ; i++){
                        new ManualControlDAO(Category1Activity.this).insert(category1.getC_ID(),userId,category1.getC_LABEL()+" "+(i+1));
                    }
                }
            }else{
                //remove all categories
                if(AllCategoryList.size()>0) {
                    new ManualControlDAO(Category1Activity.this).deleteAllCategory(category1.getC_ID(), userId);
                    for(ManualControl m : AllCategoryList){
                        String path = "users/" + userId + "/manualControl";
                        new FirebaseDAO(Category1Activity.this).deleteFromFirebase(path,m.getM_ID());
                    }

                }
            }
        }


        new Handler().postDelayed(new Runnable() {
            public void run() {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Category1Activity.this,count+" Records Inserted ",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),HomeApplianceActivity.class));
            }
        }, 4600);




    }




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
