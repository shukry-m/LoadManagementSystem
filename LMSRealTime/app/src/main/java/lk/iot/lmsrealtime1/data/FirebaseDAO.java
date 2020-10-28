package lk.iot.lmsrealtime1.data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import lk.iot.lmsrealtime1.model.AllStatus;
import lk.iot.lmsrealtime1.model.AutomaticSchedule;
import lk.iot.lmsrealtime1.model.Category1HomeAppliance;
import lk.iot.lmsrealtime1.model.Category2HomeAppliance;
import lk.iot.lmsrealtime1.model.Category3HomeAppliance;
import lk.iot.lmsrealtime1.model.ManualControl;
import lk.iot.lmsrealtime1.model.ScheduleManual;

public class FirebaseDAO {


    Context context;
    FirebaseAuth fAuth;
    public static final String TAG = "TAG";
    final FirebaseDatabase database;
    private DatabaseReference mDatabase;
    String userId;

    public FirebaseDAO(Context context) {
        this.context = context;
        fAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        userId = (fAuth.getCurrentUser()!= null)? fAuth.getCurrentUser().getUid():"0";
    }

    public void insertCategory1(ArrayList<Category1HomeAppliance> list){

        for(Category1HomeAppliance appliance : list){
            //insertCategory1 to firebase
            mDatabase.child("users").child(userId).child("appliance").child("category1").child(appliance.getC_ID()+"").setValue(appliance).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "onSuccess: appliance is created  "+ userId);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure:  Category1HomeAppliance " + e.toString());
                }
            });
        }

    }
    public void getCategory(final String category){

        String path = "users/" + userId + "/appliance/"+category;
        System.out.println(" ** get path ... "+path);
        final DatabaseReference ref = database.getReference(path);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    switch (category){
                        case "category1":{
                            Category1HomeAppliance homeAppliance = children.getValue(Category1HomeAppliance.class);
                            System.out.println(homeAppliance);
                            new Category1HomeApplianceDAO(context).insert(homeAppliance);


                            break;
                        }
                        case "category2":{
                            Category2HomeAppliance homeAppliance = children.getValue(Category2HomeAppliance.class);
                            System.out.println(homeAppliance);
                            new Category2HomeApplianceDAO(context).insert(homeAppliance);


                            break;
                        }
                        case "category3":{
                            Category3HomeAppliance homeAppliance = children.getValue(Category3HomeAppliance.class);
                            System.out.println(homeAppliance);
                            try {
                                new Category3HomeApplianceDAO(context).insert(homeAppliance);

                            }catch (Exception e){
                                System.out.println("* error in category3 insert to local db "+e.getMessage());
                            }

                            break;
                        }
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

    public void insertCategory2(ArrayList<Category2HomeAppliance> list){

        for(Category2HomeAppliance appliance : list){
            //insertCategory1 to firebase
            mDatabase.child("users").child(userId).child("appliance").child("category2").child(appliance.getC2_ID()+"").setValue(appliance).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "onSuccess: appliance is created  "+ userId);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure:  Category2HomeAppliance " + e.toString());
                }
            });
        }

    }
    public void insertCategory3(ArrayList<Category3HomeAppliance> list){

        System.out.println("* list is "+list);
        for(Category3HomeAppliance appliance : list){
            System.out.println(appliance);
            //insertCategory1 to firebase
            mDatabase.child("users").child(userId).child("appliance").child("category3").child(appliance.getC3_ID()+"").setValue(appliance).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "onSuccess: appliance is created  "+ userId);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure:  Category3HomeAppliance " + e.toString());
                }
            });
        }

    }



    public void insertManualControl(ArrayList<ManualControl> list){

        System.out.println("* list is "+list);
        for(ManualControl element : list){
            System.out.println(element);
            //insertCategory1 to firebase
            mDatabase.child("users").child(userId).child("manualControl").child(element.getM_ID()).setValue(element).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "onSuccess: ManualControl is created  "+ userId);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure:  ManualControl " + e.toString());
                }
            });
        }

    }

    public void getManualControl(){

        String path = "users/" + userId + "/manualControl";
        System.out.println(" ** get path ... "+path);
        final DatabaseReference ref = database.getReference(path);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    ManualControl mcontrol = children.getValue(ManualControl.class);
                    System.out.println(mcontrol);
                    if(mcontrol != null){
                        new ManualControlDAO(context).insert(mcontrol);
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The ManualControl read failed: " + databaseError.getCode());
            }
        });

    }




    public void insertAutomaicSchedule(ArrayList<AutomaticSchedule> list){

        System.out.println("* list is "+list);
        for(AutomaticSchedule element : list){
            System.out.println(element);

            //insertCategory1 to firebase
            mDatabase.child("users").child(userId).child("automaticSchedule").child(element.getA_ID()+"").setValue(element).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "onSuccess: AutomaicSchedule  is created  "+ userId);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure:  insertAutomaicSchedule " + e.toString());
                }
            });
        }

    }

    public void getAutomaicSchedule(){

        String path = "users/" + userId + "/automaticSchedule";
        System.out.println(" ** get path ... "+path);
        final DatabaseReference ref = database.getReference(path);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    AutomaticSchedule automaticSchedule = children.getValue(AutomaticSchedule.class);
                    System.out.println(automaticSchedule);
                    if(automaticSchedule != null) {
                        new AutomaticScheduleDAO(context).insert(automaticSchedule);
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The AutomaicSchedule  read failed: " + databaseError.getCode());
            }
        });

    }



    public void insertScheduleCooking(ArrayList<ScheduleManual> list){

        System.out.println("* list is "+list);
        for(ScheduleManual element : list){
            System.out.println(element);

            //insertCategory1 to firebase
            mDatabase.child("users").child(userId).child("ScheduleCooking").child(element.getS_ID()+"").setValue(element).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "onSuccess: AutomaicSchedule  is created  "+ userId);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure:  insertAutomaicSchedule " + e.toString());
                }
            });
        }

    }

    public void getScheduleCooking(){

        String path = "users/" + userId + "/ScheduleCooking";
        System.out.println(" ** get path ... "+path);
        final DatabaseReference ref = database.getReference(path);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    ScheduleManual scheduleManual = children.getValue(ScheduleManual.class);
                    System.out.println(scheduleManual);
                    if(scheduleManual != null) {
                        new ScheduleManualCookingDAO(context).insert(scheduleManual);
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The AutomaicSchedule  read failed: " + databaseError.getCode());
            }
        });

    }

    public void insertScheduleFlexibleLoads(ArrayList<ScheduleManual> list){

        System.out.println("* list is "+list);
        for(ScheduleManual element : list){
            System.out.println(element);

            //insertCategory1 to firebase
            mDatabase.child("users").child(userId).child("ScheduleFlexibleLoads").child(element.getS_ID()+"").setValue(element).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "onSuccess: AutomaicSchedule  is created  "+ userId);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure:  insertAutomaicSchedule " + e.toString());
                }
            });
        }

    }

    public void getScheduleFlexibleLoads(){

        String path = "users/" + userId + "/ScheduleFlexibleLoads";
        System.out.println(" ** get path ... "+path);
        final DatabaseReference ref = database.getReference(path);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    ScheduleManual scheduleManual = children.getValue(ScheduleManual.class);
                    System.out.println(scheduleManual);
                    if(scheduleManual != null) {
                        new ScheduleManualFlexibleLoadsDAO(context).insert(scheduleManual);
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The AutomaicSchedule  read failed: " + databaseError.getCode());
            }
        });
    }

    //##################### STATUS #################################################333

    public void insertAutomaticScheduleStatus(AllStatus allStatus){

        System.out.println("* list is "+allStatus);
        mDatabase.child("users").child(userId).child("automaticScheduleStatus").setValue(allStatus.getALL_STATUS()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: AutomaicSchedule  is created  "+ userId);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure:  insertAutomaicSchedule " + e.toString());
            }
        });

    }

    public void getAutomaticScheduleStatus(){

        String path = "users/" + userId + "/automaticScheduleStatus";
        System.out.println(" ** get path ... "+path);
        final DatabaseReference ref = database.getReference(path);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    System.out.println(dataSnapshot);
                    String sts = dataSnapshot.getValue().toString();
                    new AllStatusDAO(context).insert(new AllStatus(1,userId, "automaticSchedule", dataSnapshot.getValue().toString()));
                }catch (Exception e){
                    System.out.println("Error in getAutomaticScheduleStatus");
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The AutomaicSchedule  read failed: " + databaseError.getCode());
            }
        });

    }
    public void insertManualControlStatus(AllStatus allStatus){

        System.out.println("* list is "+allStatus);
        mDatabase.child("users").child(userId).child("manualControlStatus").setValue(allStatus.getALL_STATUS()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: insertManualControlStatus  is created  "+ userId);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure:  insertManualControlStatus " + e.toString());
            }
        });

    }
    public void getManualControlStatus(){

        String path = "users/" + userId + "/manualControlStatus";
        System.out.println(" ** get path ... "+path);
        final DatabaseReference ref = database.getReference(path);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    System.out.println(dataSnapshot);
                    String sts = dataSnapshot.getValue().toString();
                    new AllStatusDAO(context).insert(new AllStatus(2,userId, "manualControl", dataSnapshot.getValue().toString()));
                }catch (Exception e){
                    System.out.println("Error in getManualControlStatus");
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The ManualControl status read failed: " + databaseError.getCode());
            }
        });

    }

    //########################################## DELETE #############################33
    public void deleteFromFirebase(final String path, final String id) {

        userId = (fAuth.getCurrentUser() != null) ? fAuth.getCurrentUser().getUid() : "0";


        DatabaseReference ref = database.getReference(path);
        ref.child(id).removeValue().onSuccessTask(new SuccessContinuation<Void, Object>() {
            @NonNull
            @Override
            public Task<Object> then(@Nullable Void aVoid) throws Exception {
                System.out.println("* delete success "+path +" : "+ id);
                return null;
            }
        });

    }

}
