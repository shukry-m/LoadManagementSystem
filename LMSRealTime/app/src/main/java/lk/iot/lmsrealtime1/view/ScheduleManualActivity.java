package lk.iot.lmsrealtime1.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import lk.iot.lmsrealtime1.R;

public class ScheduleManualActivity extends AppCompatActivity {

    Button btnCooking, btnFlexible;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_manual);
        btnCooking = findViewById(R.id.btnCooking);
        btnFlexible = findViewById(R.id.btnFlexible);

        btnCooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScheduleManualActivity.this, ScheduleManualCookingActivity.class));
            }
        });

        btnFlexible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScheduleManualActivity.this, ScheduleManualFlexibleLoadsActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), ScheduleActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
