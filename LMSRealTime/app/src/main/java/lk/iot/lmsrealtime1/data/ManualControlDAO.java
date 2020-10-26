package lk.iot.lmsrealtime1.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import lk.iot.lmsrealtime1.db.DatabaseHelper;
import lk.iot.lmsrealtime1.model.Category1HomeAppliance;
import lk.iot.lmsrealtime1.model.Category2HomeAppliance;
import lk.iot.lmsrealtime1.model.Category3HomeAppliance;
import lk.iot.lmsrealtime1.model.ManualControl;

public class ManualControlDAO {

    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    Context context;
    FirebaseAuth fAuth;
   // FirebaseFirestore fStore;
    String userID;
    public static final String TAG = "TAG";

    public ManualControlDAO(Context context){
        this.context = context;
        dbHelper = new DatabaseHelper(context);
        fAuth = FirebaseAuth.getInstance();
     //   fStore = FirebaseFirestore.getInstance();
    }

    public void open() throws SQLException{
        dB = dbHelper.getWritableDatabase();
    }



    //##################################### INSERT ###################################
    public int insert(ArrayList<ManualControl> list){
        int count = 0;
            for(ManualControl manualControl : list){
                count+=insert(manualControl);
            }
        return count;
    }

    public int insert(ManualControl manualControl) {
        int count =0;

        System.out.println(manualControl);
        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try {

            String select = "SELECT * FROM " + dbHelper.TABLE_MANUAL_CONTROL + " WHERE " + dbHelper.M_ID + " = '" + manualControl.getM_ID()+"'";

            Log.v("Query", select);

            Cursor cur_s = dB.rawQuery(select, null);

            if (cur_s.getCount() == 0) {
                //insertCategory1
                ContentValues values = new ContentValues();

                values.put(dbHelper.M_LABEL, manualControl.getM_LABEL());
                values.put(dbHelper.M_USER_ID, manualControl.getM_USER_ID());
                values.put(dbHelper.M_CATEGORY_ID, manualControl.getM_CATEGORY_ID());
                values.put(dbHelper.M_STATUS, manualControl.getM_STATUS());


                count = (int) dB.insert(dbHelper.TABLE_MANUAL_CONTROL, null, values);

                if(count>0){
                    System.out.println("* inserted "+count);
                }
            }else{
                //update
                ContentValues values = new ContentValues();

                values.put(dbHelper.M_LABEL, manualControl.getM_LABEL());
                values.put(dbHelper.M_USER_ID, manualControl.getM_USER_ID());
                values.put(dbHelper.M_CATEGORY_ID, manualControl.getM_CATEGORY_ID());
                values.put(dbHelper.M_STATUS, manualControl.getM_STATUS());


                count = dB.update(dbHelper.TABLE_MANUAL_CONTROL, values, dbHelper.M_ID + " =?", new String[]{manualControl.getM_ID() });

                if(count>0){
                    System.out.println("* updated "+count);
                }
            }



        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            dB.close();
        }
        return count;
    }



    // insert new label
    public int insert(String category_id,String userId,String label) {
        int count =0;

       if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try {

            String select = "SELECT * FROM " + dbHelper.TABLE_MANUAL_CONTROL + " WHERE " + dbHelper.M_LABEL + " = '" + label+"'";

            Log.v("Query", select);

            Cursor cur_s = dB.rawQuery(select, null);

            if (cur_s.getCount() == 0) {
                //insert new Category1
                ContentValues values = new ContentValues();

                values.put(dbHelper.M_LABEL, label);
                values.put(dbHelper.M_USER_ID, userId);
                values.put(dbHelper.M_CATEGORY_ID, category_id);
                values.put(dbHelper.M_STATUS,"0");


                count = (int) dB.insert(dbHelper.TABLE_MANUAL_CONTROL, null, values);

                if(count>0){
                    System.out.println("* inserted "+count);
                }
            }else{
                //update
                System.out.println("* manual control already has "+label);
            }



        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            dB.close();
        }
        return count;
    }

    private boolean isNumber(String number){
        try{
            int num  = Integer.parseInt(number);
            return true;
        }catch (Exception e){
            return false;
        }
    }



    public void insertNewLabel(String category_id,String label,String countORstatus,String userID){

        System.out.println("insert new label");

        try{
            if(isNumber(countORstatus)){
                for(int i=0;i<Integer.parseInt(countORstatus); i++){
                    insert(category_id,userID,label+" "+(i+1));

                }

            }else{
                if(countORstatus.equalsIgnoreCase("yes")){
                    insert(category_id,userID,label);
                }

            }

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    //##################### GET ####################
        public ArrayList<ManualControl> getAll(String userID){

        ArrayList<ManualControl> list = new ArrayList<>();

        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try{

            String select = "SELECT * FROM " + dbHelper.TABLE_MANUAL_CONTROL +" WHERE "+dbHelper.M_USER_ID+" = '"+userID+
                    "' ORDER BY "+dbHelper.M_LABEL+" ASC";

            Log.v("Query",select);

            Cursor cur = dB.rawQuery(select, null);

            while (cur.moveToNext()) {
                ManualControl h = new ManualControl();
                h.setM_ID(cur.getString(cur.getColumnIndex(dbHelper.M_ID)));
                h.setM_LABEL(cur.getString(cur.getColumnIndex(dbHelper.M_LABEL)));
                h.setM_USER_ID(cur.getString(cur.getColumnIndex(dbHelper.M_USER_ID)));
                h.setM_STATUS(cur.getString(cur.getColumnIndex(dbHelper.M_STATUS)));
                h.setM_CATEGORY_ID(cur.getString(cur.getColumnIndex(dbHelper.M_CATEGORY_ID)));
                list.add(h);
            }

        }catch (Exception e) {
           e.printStackTrace();
        }finally {
            dB.close();
        }

        return list;

    }

    public ManualControl get(String h_id, String userID){


        ManualControl h = new ManualControl();

        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try{

            String select = "SELECT * FROM " + dbHelper.TABLE_MANUAL_CONTROL +
                    " WHERE "+dbHelper.M_USER_ID +" = '"+userID+"' AND "+dbHelper.M_ID +" = '"+h_id+"'";

            Log.v("Query",select);

            Cursor cur = dB.rawQuery(select, null);

            while (cur.moveToNext()) {

                h.setM_ID(cur.getString(cur.getColumnIndex(dbHelper.M_ID)));
                h.setM_LABEL(cur.getString(cur.getColumnIndex(dbHelper.M_LABEL)));
                h.setM_USER_ID(cur.getString(cur.getColumnIndex(dbHelper.M_USER_ID)));
                h.setM_CATEGORY_ID(cur.getString(cur.getColumnIndex(dbHelper.M_CATEGORY_ID)));

            }

        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            dB.close();
        }

        return h;

    }
    //################################## DELETE ########################

    public  int deleteAll(String userID){
        int count = 0;

        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }
        Cursor cursor = null;

        try{

            String deleteQuery = "DELETE FROM "+dbHelper.TABLE_MANUAL_CONTROL
                    +" WHERE "+dbHelper.M_USER_ID +" = '"+userID+"'";

            System.out.println("* delete query "+deleteQuery);

            Cursor cur = dB.rawQuery(deleteQuery, null);

            count = cur.getCount();
            if(count>0){
                System.out.println("Deleted ");
            }

        } catch (Exception e) {

            Log.v(" Exception", e.toString());

        } finally {
            if (cursor !=null) {
                cursor.close();
            }
            dB.close();
        }
        return count;

    }



    public int deleteHomeAppliance(String h_id,String userID) {

        int count =0;
        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }
        Cursor cursor = null;

        try{

            String deleteQuery = "DELETE FROM "+dbHelper.TABLE_MANUAL_CONTROL
                    +" WHERE "+dbHelper.M_ID +" = '"+h_id+"' AND "+dbHelper.M_USER_ID +" = '"+userID+"'";

            System.out.println("* delete query "+deleteQuery);

            Cursor cur = dB.rawQuery(deleteQuery, null);

            count = cur.getCount();
            if(count>0){
                System.out.println("* deleted tbl "+count);

            }

        } catch (Exception e) {

            Log.v(" Exception", e.toString());

        } finally {
            if (cursor !=null) {
                cursor.close();
            }
            dB.close();
        }
        return count;
    }

}
