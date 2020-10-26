package lk.iot.lmsrealtime1.view;

import android.app.FragmentManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import lk.iot.lmsrealtime1.R;

public class CostAndPower extends AppCompatActivity {

    FirebaseAuth fAuth;
   // FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost_and_power);

        fAuth = FirebaseAuth.getInstance();
      //  fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();
        FragmentManager fragmentManager = getFragmentManager();
/// db.collection("cities").document("DC").delete();

        // db.collection("cities").document("DC").delete();
       // fStore.collection("users").document(userID).collection("Appliances").document(6+"").delete();
        //System.out.println("* delete");
    }
}
