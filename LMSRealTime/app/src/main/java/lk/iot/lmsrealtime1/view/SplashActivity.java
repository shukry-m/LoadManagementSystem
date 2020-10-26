package lk.iot.lmsrealtime1.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import lk.iot.lmsrealtime1.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }
}
