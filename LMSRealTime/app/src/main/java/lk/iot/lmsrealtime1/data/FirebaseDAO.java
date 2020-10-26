package lk.iot.lmsrealtime1.data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lk.iot.lmsrealtime1.model.ManualControl;

public class FirebaseDAO {

    Context context;
    FirebaseAuth fAuth;
    String userID;
    public static final String TAG = "TAG";
    final FirebaseDatabase database;

    public FirebaseDAO(Context context) {
        this.context = context;
        fAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
    }
    //############################### DOWNLOAD ###########################

    public void downloadAllFromFireBase() {

        userID = (fAuth.getCurrentUser() != null) ? fAuth.getCurrentUser().getUid() : "0";

        new ManualControlDAO(context).deleteAll(userID);

        DatabaseReference ref = database.getReference("users/" + userID + "/Appliances");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot children : dataSnapshot.getChildren()) {

                    ManualControl hm = children.getValue(ManualControl.class);
                    System.out.println("***********");
                    System.out.println(hm.getM_ID());
                    InsertToLocalDb(hm.getM_ID());
                    System.out.println("***********");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });



          //new ManualControlDAO(context).getAll(userID);
    }

    private void InsertToLocalDb(final String id) {

        DatabaseReference ref = database.getReference("users/" + userID + "/Appliances/" + id);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                System.out.println(dataSnapshot.getValue(ManualControl.class));
                ManualControl hm = dataSnapshot.getValue(ManualControl.class);
                if (Objects.requireNonNull(dataSnapshot).exists()) {
//
//                    new ManualControlDAO(context).insert(id, userID,
//                            hm.getM_LABEL(),
//                            hm.getM__STATUS()
//                    );

                } else {
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }


    //############################### CHECK Active ##############################

    public String checkActive() {
       /* userID = (fAuth.getCurrentUser()!= null)? fAuth.getCurrentUser().getUid():"0";
        final String[] isActive = new String[1];
        DatabaseReference ref = database.getReference("users/"+userID +"/profile");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Post post = dataSnapshot.getValue(Post.class);
                if(dataSnapshot.exists()){

                    Profile prof = dataSnapshot.getValue(Profile.class);
                    System.out.println(prof);
                    isActive[0] ="true";//prof.getIsActive();//dataSnapshot.getValue().toString();

                }else{
                    isActive[0] = "false";
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        System.out.println(isActive[0]);
        if(isActive[0]==null){
            isActive[0] = "false";
        }
        return isActive[0];*/
        return "true";
    }
    //############################### INSERT ##############################
//
//    public int insertToFirebase(final String label_name) {
//
//        userID = (fAuth.getCurrentUser() != null) ? fAuth.getCurrentUser().getUid() : "0";
//        new ManualControlDAO(context).insert(label_name, userID);
//        final int[] h_id = {0};
//        final int[] id = {2};
//        //insertToLocalDb
//
//        DatabaseReference ref = database.getReference("users/" + userID + "/Appliances");
//
//        ref.addValueEventListener(new ValueEventListener() {
//
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                List<Integer> list = new ArrayList<>();
//                int max = 0;
//                boolean isDuplicate = false;
//                if (Objects.requireNonNull(dataSnapshot).exists()) {
//                    for (DataSnapshot children : dataSnapshot.getChildren()) {
//
//
//                        ManualControl hm = children.getValue(ManualControl.class);
//                        System.out.println(label_name);
//                        System.out.println(hm);
//                        int id = Integer.parseInt(hm.getM_ID());
//                        if (max < id) {
//                            max = id;
//                            list.clear();
//                            list.add(max + 1);
//                        }
//                        if (hm.getM_LABEL().equals(label_name)) {
//                            isDuplicate = true;
//                            break;
//                        }
//                        Log.d(TAG, "max " + hm.getM_ID());
//                    }
//                    System.out.println("Before insertCategory1 Data " + isDuplicate);
//                    if (!isDuplicate) {
//                        id[0] = insertData(list.get(0), label_name);
//                        Log.d(TAG, list.toString());
//                    }
//
//                } else {
//                    Log.d("tag", "onEvent: Document do not exists insertToFirebase");
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                System.out.println("The insertToFirebase read failed: " + databaseError.getCode());
//            }
//        });
//        return id[0];
//
//    }

    private int insertData(final int newID, final String label_name) {

        System.out.println("insertData");
        DatabaseReference ref = database.getReference();


        Map<String, Object> appliance1 = new HashMap<>();
        appliance1.put("M_ID", newID + "");
        appliance1.put("M_LABEL", label_name);
        appliance1.put("T_5_TO_8", "0");
        appliance1.put("T_8_TO_17", "0");
        appliance1.put("T_17_TO_22", "0");
        appliance1.put("T_22_TO_5", "0");
        appliance1.put("M_STATUS", "0");

        ref.child("users").child(userID).child("Appliances").child(newID + "").setValue(appliance1);
        //new ManualControlDAO(context).insert(label_name, newID + "", userID);

        return newID;
    }


    //############################### UPDATE ################
    public void UpdateManualControlToFirebase(final String h_id, final String m__status) {
        userID = (fAuth.getCurrentUser() != null) ? fAuth.getCurrentUser().getUid() : "0";
        // DocumentReference docRef = fStore.collection("users").document(userID).collection("Appliances").document(h_id);
        DatabaseReference ref = database.getReference("users/" + userID + "/Appliances/" + h_id + "/M_STATUS");

        Map<String, Object> edited = new HashMap<>();

        edited.put("M_STATUS", m__status);
        ref.setValue(m__status).onSuccessTask(new SuccessContinuation<Void, Object>() {
            @NonNull
            @Override
            public Task<Object> then(@Nullable Void aVoid) throws Exception {
               // new ManualControlDAO(context).updateManualControl(h_id, m__status, userID);
                return null;
            }
        });

    }

    public void UpdateTimeToFirebase(final int place_id, final String h_id, final String status) {

        userID = (fAuth.getCurrentUser() != null) ? fAuth.getCurrentUser().getUid() : "0";
        DatabaseReference ref = database.getReference();


        Map<String, Object> edited = new HashMap<>();


        switch (place_id) {
            case 1: {
                edited.put("T_5_TO_8", status);
                ref = database.getReference("users/" + userID + "/Appliances/" + h_id + "/T_5_TO_8");
            }
            break;
            case 2: {
                edited.put("T_8_TO_17", status);
                ref = database.getReference("users/" + userID + "/Appliances/" + h_id + "/T_8_TO_17");
            }
            break;
            case 3: {
                edited.put("T_17_TO_22", status);
                ref = database.getReference("users/" + userID + "/Appliances/" + h_id + "/T_17_TO_22");
            }
            break;
            case 4: {
                edited.put("T_22_TO_5", status);
                ref = database.getReference("users/" + userID + "/Appliances/" + h_id + "/T_22_TO_5");
            }
            break;
        }

        ref.setValue(status).onSuccessTask(new SuccessContinuation<Void, Object>() {
            @NonNull
            @Override
            public Task<Object> then(@Nullable Void aVoid) throws Exception {
              //  new ManualControlDAO(context).updateTime(place_id, h_id, status, userID);
                return null;
            }
        });


    }

    ///############################ DELETE ##################

    public void deleteFirebaseHomeAppliance(final String h_id) {

        userID = (fAuth.getCurrentUser() != null) ? fAuth.getCurrentUser().getUid() : "0";
        new ManualControlDAO(context).deleteHomeAppliance(h_id + "", userID);
        DatabaseReference ref = database.getReference("users/" + userID + "/Appliances");
        ref.child(h_id).removeValue().onSuccessTask(new SuccessContinuation<Void, Object>() {
            @NonNull
            @Override
            public Task<Object> then(@Nullable Void aVoid) throws Exception {
                new ManualControlDAO(context).deleteHomeAppliance(h_id + "", userID);
                return null;
            }
        });

    }
}