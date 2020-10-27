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
import lk.iot.lmsrealtime1.model.ScheduleManual;

public class ScheduleManualCookingDAO {

    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    Context context;
    FirebaseAuth fAuth;
    String userID;
    public static final String TAG = "TAG";

    public ScheduleManualCookingDAO(Context context){
        this.context = context;
        dbHelper = new DatabaseHelper(context);
        fAuth = FirebaseAuth.getInstance();
    }

    public void open() throws SQLException{
        dB = dbHelper.getWritableDatabase();
    }



    //##################################### INSERT ###################################
    public int insert(ArrayList<ScheduleManual> list){
        int count = 0;
            for(ScheduleManual scheduleManual : list){
                count+=insert(scheduleManual);
            }
        return count;
    }

    public int insert(ScheduleManual scheduleManual) {
        int count =0;

        System.out.println(scheduleManual);
        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try {

            String select = "SELECT * FROM " + dbHelper.TABLE_SCHEDULE_MANUAL_COOKING + " WHERE " + dbHelper.S_ID + " = '" + scheduleManual.getS_ID()+"'";

            Log.v("Query", select);

            Cursor cur_s = dB.rawQuery(select, null);

            if (cur_s.getCount() == 0) {
                //insertCategory1
                ContentValues values = new ContentValues();

                values.put(dbHelper.S_LABEL, scheduleManual.getS_LABEL());
                values.put(dbHelper.S_USER_ID, scheduleManual.getS_USER_ID());
                values.put(dbHelper.S_CATEGORY_ID, scheduleManual.getS_Category_Id());
                values.put(dbHelper.S_Start_Time_Hour, scheduleManual.getS_Start_Time_Hour());
                values.put(dbHelper.S_Start_Time_Minute, scheduleManual.getS_Start_Time_Minute());
                values.put(dbHelper.S_End_Time_Hour, scheduleManual.getS_End_Time_Hour());
                values.put(dbHelper.S_End_Time_Minute, scheduleManual.getS_End_Time_Minute());


                count = (int) dB.insert(dbHelper.TABLE_SCHEDULE_MANUAL_COOKING, null, values);

                if(count>0){
                    System.out.println("* inserted "+count);
                }
            }else{
                //update
                ContentValues values = new ContentValues();

                values.put(dbHelper.S_LABEL, scheduleManual.getS_LABEL());
                values.put(dbHelper.S_USER_ID, scheduleManual.getS_USER_ID());
                values.put(dbHelper.S_CATEGORY_ID, scheduleManual.getS_Category_Id());
                values.put(dbHelper.S_Start_Time_Hour, scheduleManual.getS_Start_Time_Hour());
                values.put(dbHelper.S_Start_Time_Minute, scheduleManual.getS_Start_Time_Minute());
                values.put(dbHelper.S_End_Time_Hour, scheduleManual.getS_End_Time_Hour());
                values.put(dbHelper.S_End_Time_Minute, scheduleManual.getS_End_Time_Minute());


                count = dB.update(dbHelper.TABLE_SCHEDULE_MANUAL_COOKING, values, dbHelper.S_ID + " =?", new String[]{scheduleManual.getS_ID()+"" });

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

        System.out.println(userId +": "+ label);

       if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try {

            String select = "SELECT * FROM " + dbHelper.TABLE_SCHEDULE_MANUAL_COOKING + " WHERE " + dbHelper.S_LABEL + " = '" + label+"'";

            Log.v("Query", select);

            Cursor cur_s = dB.rawQuery(select, null);

            if (cur_s.getCount() == 0) {
                //insert new Category1
                ContentValues values = new ContentValues();

                values.put(dbHelper.S_LABEL, label);
                values.put(dbHelper.S_USER_ID, userId);
                values.put(dbHelper.S_CATEGORY_ID, category_id);
                values.put(dbHelper.S_Start_Time_Hour,"00");
                values.put(dbHelper.S_Start_Time_Minute,"00");
                values.put(dbHelper.S_End_Time_Hour,"00");
                values.put(dbHelper.S_End_Time_Minute,"00");

                count = (int) dB.insert(dbHelper.TABLE_SCHEDULE_MANUAL_COOKING, null, values);

                if(count>0){
                    System.out.println("* inserted "+count);
                }
            }else{
                //update
                System.out.println("* automatic control already has "+label);
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







    //##################### GET ####################
        public ArrayList<ScheduleManual> getAll(String userID){

        ArrayList<ScheduleManual> list = new ArrayList<>();

        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try{

            String select = "SELECT * FROM " + dbHelper.TABLE_SCHEDULE_MANUAL_COOKING +" WHERE "+dbHelper.S_USER_ID+" = '"+userID+
                    "' ORDER BY "+dbHelper.S_LABEL+" ASC";

            Log.v("Query",select);

            Cursor cur = dB.rawQuery(select, null);

            while (cur.moveToNext()) {
                ScheduleManual h = new ScheduleManual();
                h.setS_ID(cur.getInt(cur.getColumnIndex(dbHelper.S_ID)));
                h.setS_LABEL(cur.getString(cur.getColumnIndex(dbHelper.S_LABEL)));
                h.setS_USER_ID(cur.getString(cur.getColumnIndex(dbHelper.S_USER_ID)));
                h.setS_Category_Id(cur.getString(cur.getColumnIndex(dbHelper.S_CATEGORY_ID)));
                h.setS_Start_Time_Hour(cur.getString(cur.getColumnIndex(dbHelper.S_Start_Time_Hour)));
                h.setS_Start_Time_Minute(cur.getString(cur.getColumnIndex(dbHelper.S_Start_Time_Minute)));
                h.setS_End_Time_Hour(cur.getString(cur.getColumnIndex(dbHelper.S_End_Time_Hour)));
                h.setS_End_Time_Minute(cur.getString(cur.getColumnIndex(dbHelper.S_End_Time_Minute)));
                list.add(h);
            }

        }catch (Exception e) {
           e.printStackTrace();
        }finally {
            dB.close();
        }

        return list;
    }

    public ScheduleManual getFromLabel(String userID,String label){

        ScheduleManual h = new ScheduleManual();

        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try{

            String select = "SELECT * FROM " + dbHelper.TABLE_SCHEDULE_MANUAL_COOKING +" WHERE "+dbHelper.S_USER_ID+" = '"+userID+
                    "' AND "+dbHelper.S_LABEL+" = '"+label+"'";

            Log.v("Query",select);

            Cursor cur = dB.rawQuery(select, null);

            while (cur.moveToNext()) {

                h.setS_ID(cur.getInt(cur.getColumnIndex(dbHelper.S_ID)));
                h.setS_LABEL(cur.getString(cur.getColumnIndex(dbHelper.S_LABEL)));
                h.setS_USER_ID(cur.getString(cur.getColumnIndex(dbHelper.S_USER_ID)));
                h.setS_Category_Id(cur.getString(cur.getColumnIndex(dbHelper.S_CATEGORY_ID)));
                h.setS_Start_Time_Hour(cur.getString(cur.getColumnIndex(dbHelper.S_Start_Time_Hour)));
                h.setS_Start_Time_Minute(cur.getString(cur.getColumnIndex(dbHelper.S_Start_Time_Minute)));
                h.setS_End_Time_Hour(cur.getString(cur.getColumnIndex(dbHelper.S_End_Time_Hour)));
                h.setS_End_Time_Minute(cur.getString(cur.getColumnIndex(dbHelper.S_End_Time_Minute)));

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

            String deleteQuery = "DELETE FROM "+dbHelper.TABLE_SCHEDULE_MANUAL_COOKING
                    +" WHERE "+dbHelper.S_USER_ID +" = '"+userID+"'";

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
    public  int delete(String userID,String label){
        int count = 0;

        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }
        Cursor cursor = null;

        try{

            String deleteQuery = "DELETE FROM "+dbHelper.TABLE_SCHEDULE_MANUAL_COOKING
                    +" WHERE "+dbHelper.S_USER_ID +" = '"+userID+"' AND "+dbHelper.S_LABEL+" = '"+label+"'";

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

}
