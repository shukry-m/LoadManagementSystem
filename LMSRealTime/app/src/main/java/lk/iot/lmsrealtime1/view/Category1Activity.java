package lk.iot.lmsrealtime1.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.travijuu.numberpicker.library.NumberPicker;

import java.util.ArrayList;

import lk.iot.lmsrealtime1.R;
import lk.iot.lmsrealtime1.data.Category1HomeApplianceDAO;
import lk.iot.lmsrealtime1.data.Category3HomeApplianceDAO;
import lk.iot.lmsrealtime1.data.Firebase1DAO;
import lk.iot.lmsrealtime1.model.Category1HomeAppliance;

public class Category1Activity extends AppCompatActivity {

   // Toolbar top_bar;
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

       // top_bar =  findViewById(R.id.tb_category1);
        //top_bar.setTitle("Category 1");

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


        new Firebase1DAO(Category1Activity.this).insertCategory1(list);

        new Firebase1DAO(Category1Activity.this).getCategory("category1");


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
