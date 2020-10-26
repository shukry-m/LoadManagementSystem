package lk.iot.lmsrealtime1.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import lk.iot.lmsrealtime1.R;
import lk.iot.lmsrealtime1.helper.CustomMessage;
import lk.iot.lmsrealtime1.model.Profile;

public class CustomerInfo extends AppCompatActivity {

    private static final int GALLERY_INTENT_CODE = 1023 ;
    EditText fullName,email,phone,street,city,province, zip;
    TextView verifyMsg;
    FirebaseAuth fAuth;
    //FirebaseFirestore fStore;
    String userId;
    Button resendCode;
    Button resetPassLocal,changeProfileImage;
    FirebaseUser user;
    ImageView profileImage;
    FirebaseDatabase database;
    //StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_info);

        phone = findViewById(R.id.profilePhone);
        fullName = findViewById(R.id.profileName);
        email    = findViewById(R.id.profileEmail);
        street    = findViewById(R.id.profileStreet);
        province    = findViewById(R.id.profileProvince);
        zip = findViewById(R.id.profileZip);
        city = findViewById(R.id.profileCity);

        fAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        // fStore = FirebaseFirestore.getInstance();
        //storageReference = FirebaseStorage.getInstance().getReference();
        resendCode = findViewById(R.id.resendCode);
        verifyMsg = findViewById(R.id.verifyMsg);

        userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        user = fAuth.getCurrentUser();

        if(!user.isEmailVerified()){
            verifyMsg.setVisibility(View.VISIBLE);
            resendCode.setVisibility(View.VISIBLE);

            resendCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(v.getContext(), "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("tag", "onFailure: Email not sent " + e.getMessage());
                        }
                    });
                }
            });
        }

        DatabaseReference ref = database.getReference("users/"+userId +"/profile");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Post post = dataSnapshot.getValue(Post.class);
                if(dataSnapshot.exists()){
                    Profile prof = dataSnapshot.getValue(Profile.class);
                    phone.setText(prof.getPhone());
                    fullName.setText(prof.getfName());
                    email.setText(prof.getEmail());

                    if(prof.getStreet()!=null)
                        street.setText(prof.getStreet());

                    if(prof.getProvince()!=null)
                        province.setText(prof.getProvince());

                    if(prof.getZip()!=null)
                        zip.setText(prof.getZip());

                    if(prof.getCity()!=null)
                        city.setText(prof.getCity());
                }else{
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


    }

    public void Save(View view) {

        if(fullName.getText().toString().isEmpty() || email.getText().toString().isEmpty() || phone.getText().toString().isEmpty()){
            CustomMessage.displayStatusMessage("FullName, Email , Phone Number Fields are required",3,3,CustomerInfo.this);
            return;
        }

        final String email_ = email.getText().toString();
        user.updateEmail(email_).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                DatabaseReference ref = database.getReference("users/"+user.getUid());
                //DocumentReference docRef = fStore.collection("users").document(user.getUid());


                Profile profile = new Profile(fullName.getText().toString(),
                        phone.getText().toString(),
                        city.getText().toString(),
                        street.getText().toString(),
                        province.getText().toString(),
                        zip.getText().toString(),
                        email_,"true");
                ref.child("profile").setValue(profile).onSuccessTask(new SuccessContinuation<Void, Object>() {
                    @NonNull
                    @Override
                    public Task<Object> then(@Nullable Void aVoid) throws Exception {
                        Toast.makeText(CustomerInfo.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                        return  null;
                    }
                });


                Toast.makeText(CustomerInfo.this, "Email is changed.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CustomerInfo.this,   e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void Cancel(View view) {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
}
