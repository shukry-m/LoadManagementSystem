package lk.iot.lmsrealtime1.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
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
import com.google.firebase.database.DatabaseReference;

import lk.iot.lmsrealtime1.R;
import lk.iot.lmsrealtime1.data.FirebaseDAO;
import lk.iot.lmsrealtime1.helper.Network;

public class LoginActivity extends AppCompatActivity {
    DatabaseReference ref;
    EditText mEmail,mPassword;
    Button mLoginBtn;
    TextView mCreateBtn,forgotTextLink;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //map ui elements to these variables
        // so we can access the data which user has inserted
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();
        mLoginBtn = findViewById(R.id.loginBtn);
        mCreateBtn = findViewById(R.id.createText);
        forgotTextLink = findViewById(R.id.forgotPassword);


        //when click login button
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get email and password values
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                //validate email
                // If Email is empty set Error message
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }

                //validate Password
                // If Password is empty set Error message
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    return;
                }

                //validate Password
                // If Password is less than 6 characters set Error message
                if(password.length() < 6){
                    mPassword.setError("Password Must be >= 6 Characters");
                    return;
                }

                // set progressbar visible to indicate data is loading
                //then user will wait
                progressBar.setVisibility(View.VISIBLE);

                //if Internet connection is available
                if(Network.isNetworkAvailable(LoginActivity.this)) {
                    // authenticate the user from firebase
                    fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // user is successfully logged on
                                //so download data from firebase
                                downloadFromFirebase();
                            } else {
                                //Login is failed
                                //so display a Error message
                                Toast.makeText(LoginActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                //hide Progress bar
                                //then user can understand loading was completed
                                progressBar.setVisibility(View.GONE);
                            }

                        }
                    });
                }else{
                    //show internet connection error message
                    Toast.makeText(LoginActivity.this, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //when user click  -> Create Account Text
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //page redirect to Register Activity
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });

        //when user click -> Forgot Password Text
        forgotTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //generate an alert box
                //it has reset mail box. then user can enter new email address
                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());

                //set alert box title to Reset Password
                passwordResetDialog.setTitle("Reset Password ?");
                //set alert box message to Reset Password
                passwordResetDialog.setMessage("Enter Your Email To Received Reset Link.");
                //set alert box
                passwordResetDialog.setView(resetMail);

                //alert box has yes and no buttons

                //if user click yes button
                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        // call firebase to  Reset Email
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //reset Email was successfull. so, display success message
                                Toast.makeText(LoginActivity.this, "Reset Link Sent To Your Email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //reset Email was failed. so, display error message
                                Toast.makeText(LoginActivity.this, "Error ! Reset Link is Not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
                //if user click no button
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //automatically close the dialog
                    }
                });

                //create this Reset dialog box
                passwordResetDialog.create().show();

            }
        });

    }

    //download Data from firebase
    void downloadFromFirebase(){

        //show the progress bar
        //then user will wait for the response from firebase
        progressBar.setVisibility(View.VISIBLE);

        //get category1 details
        new FirebaseDAO(LoginActivity.this).getCategory("category1");
        //get category2 details
        new FirebaseDAO(LoginActivity.this).getCategory("category2");
        //get category3 details
        new FirebaseDAO(LoginActivity.this).getCategory("category3");
        //get manual control details
        new FirebaseDAO(LoginActivity.this).getManualControl();
        //get automatic schedule details
        new FirebaseDAO(LoginActivity.this).getAutomaicSchedule();
        //get schedule cooking details
        new FirebaseDAO(LoginActivity.this).getScheduleCooking();
        //get schedule flexible loads
        new FirebaseDAO(LoginActivity.this).getScheduleFlexibleLoads();
        //get manual control status
        new FirebaseDAO(LoginActivity.this).getManualControlStatus();
        //get automatic schedule status
        new FirebaseDAO(LoginActivity.this).getAutomaticScheduleStatus();

        //delay this code for 7 seconds
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //after 7 seconds  hide the progress bar
                progressBar.setVisibility(View.GONE);
                //show success message
                Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                //navigate  to Home screen
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }, 7600);


    }

    //when user click back button
    @Override
    public void onBackPressed() {

        //close the app
         System.exit(0);
         finish();

    }

}
