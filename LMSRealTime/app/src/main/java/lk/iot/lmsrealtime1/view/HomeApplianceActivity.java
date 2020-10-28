package lk.iot.lmsrealtime1.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import lk.iot.lmsrealtime1.R;
import lk.iot.lmsrealtime1.data.FirebaseDAO;

public class HomeApplianceActivity extends AppCompatActivity {

    Button btncategory1, btncategory2, btncategory3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_appliance);

        btncategory1 = findViewById(R.id.btncategory1);
        btncategory2 = findViewById(R.id.btncategory2);
        btncategory3 = findViewById(R.id.btncategory3);

        downloadFromFirebase();

        //if user click category 1
        btncategory1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navigate to category 1
                startActivity(new Intent(getApplicationContext(),Category1Activity.class));
            }
        });

        //if user click category 2
        btncategory2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navigate to category 2
                startActivity(new Intent(getApplicationContext(),Category2Activity.class));
            }
        });

        //if user click category 3
        btncategory3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navigate to category 3
                startActivity(new Intent(getApplicationContext(),Category3Activity.class));
            }
        });

    }

    //if user pressed back button from phone

    @Override
    public void onBackPressed() {
        //navigate to Home page
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    void downloadFromFirebase(){
        new FirebaseDAO(HomeApplianceActivity.this).getCategory("category1");
        new FirebaseDAO(HomeApplianceActivity.this).getCategory("category2");
        new FirebaseDAO(HomeApplianceActivity.this).getCategory("category3");
        new FirebaseDAO(HomeApplianceActivity.this).getManualControl();


    }
    // enable "downloadFromFirebase" functionality that needs to run while the component is visible and in the foreground
    @Override
    protected void onResume() {
        super.onResume();
        downloadFromFirebase();
    }
}