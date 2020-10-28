package lk.iot.lmsrealtime1.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import lk.iot.lmsrealtime1.R;
import lk.iot.lmsrealtime1.helper.Network;
import lk.iot.lmsrealtime1.model.Profile;

public class RegisterActivity extends AppCompatActivity {
    EditText mFullName,mEmail,mPassword,mPhone;
    Button mRegisterBtn;
    TextView mLoginBtn,textView2;
    ProgressBar progressBar;
    public static final String TAG = "TAG";
    FirebaseAuth fAuth;
    private DatabaseReference mDatabase;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set ui
        setContentView(R.layout.activity_register);
        //map ui elements to these variables
        // so we can access the data which user has inserted
        mFullName   = findViewById(R.id.fullName);
        mEmail      = findViewById(R.id.Email);
        mPassword   = findViewById(R.id.password);
        mPhone      = findViewById(R.id.phone);
        mRegisterBtn= findViewById(R.id.registerBtn);
        mLoginBtn   = findViewById(R.id.createText);
        textView2   = findViewById(R.id.textView2);
        progressBar = findViewById(R.id.progressBar);

        //initialize Firebase Authorization  to get userId
        fAuth = FirebaseAuth.getInstance();
        //initialize Firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //get userId from Firebase Authorization
        userID =  (fAuth.getCurrentUser()!= null)? fAuth.getCurrentUser().getUid():"0";

        //if userId not null
        if(fAuth.getCurrentUser() != null  ){
            //if userId not zero
            if(!userID.equals("0")){
                //navigate to Home screen
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                //close this activity
                finish();
            }

        }


        //if user clicks Register button
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //get the email from Interface
                final String email = mEmail.getText().toString().trim();
                //get the password from Interface
                String password = mPassword.getText().toString().trim();
                //get the fullname from Interface
                final String fullName = mFullName.getText().toString();
                //get the phone from Interface
                final String phone    = mPhone.getText().toString();

                //if email is empty
                if(TextUtils.isEmpty(email)){
                    //set Email error message
                    mEmail.setError("Email is Required.");
                    return;
                }

                //if password is empty
                if(TextUtils.isEmpty(password)){
                    //set Password error message
                    mPassword.setError("Password is Required.");
                    return;
                }

                //if password is less than 6 characters
                if(password.length() < 6){
                    //set Password error message
                    mPassword.setError("Password Must be >= 6 Characters");
                    return;
                }

                //show progress bar
                progressBar.setVisibility(View.VISIBLE);

                // if network is available
                if(Network.isNetworkAvailable(RegisterActivity.this)) {
                    //create user from firebase with email and password
                    fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                //firebase created an user

                                //get the user from firebase
                                FirebaseUser fuser = fAuth.getCurrentUser();

                                //then  send verification link to that email
                                fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //verification email has been sent successfully
                                        //then show success message
                                        Toast.makeText(RegisterActivity.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        //verification email has been Failed
                                        // just print error message in the app
                                        Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                                    }
                                });

                                //show success message user has created
                                Toast.makeText(RegisterActivity.this, "User Created.", Toast.LENGTH_SHORT).show();

                                //get the user Id From firebase
                                userID = fAuth.getCurrentUser().getUid();

                                //create a profile model and set Fullname,Email and PhoneNumber
                                Profile profile = new Profile();
                                profile.setfName(fullName);
                                profile.setEmail(email);
                                profile.setPhone(phone);
                                profile.setActive("true");

                                //create " /user/userId/profile " path in firebase and add profile details
                                mDatabase.child("users").child(userID).child("profile").setValue(profile).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                 // firebase successfully inserted data
                                        Log.d(TAG, "onSuccess: user Profile is created for " + userID);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // firebase is unable to insert data
                                        Log.d(TAG, "onFailure: " + e.toString());
                                    }
                                });

                                //firebase created a user so then user can login
                                // so navigate to Login page
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                            } else {
                                //firebase failed to create user so display error message
                                Toast.makeText(RegisterActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                //hide progressbar
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }else{
                    //show Internet connection error message
                    Toast.makeText(RegisterActivity.this, "Please Check your Internet connection", Toast.LENGTH_SHORT).show();

                }
            }
        });



        //when user clicks Login Here text
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navigate to login activity
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });

    }



    //when user press  back
    @Override
    public void onBackPressed() {

        //close this app
        System.exit(0);
        finish();

    }
}
