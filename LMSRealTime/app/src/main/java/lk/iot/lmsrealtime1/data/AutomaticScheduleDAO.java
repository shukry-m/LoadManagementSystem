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
import lk.iot.lmsrealtime1.model.AutomaticSchedule;

public class AutomaticScheduleDAO {

    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    Context context;
    FirebaseAuth fAuth;
    String userID;
    public static final String TAG = "TAG";

    public AutomaticScheduleDAO(Context context){
        this.context = context;
        dbHelper = new DatabaseHelper(context);
        fAuth = FirebaseAuth.getInstance();
    }

    public void open() throws SQLException{
        dB = dbHelper.getWritableDatabase();
    }



    //##################################### INSERT ###################################
    public int insert(ArrayList<AutomaticSchedule> list){
        int count = 0;
            for(AutomaticSchedule automaticSchedule : list){
                count+=insert(automaticSchedule);
            }
        return count;
    }

    public int insert(AutomaticSchedule automaticSchedule) {
        int count =0;

        System.out.println(automaticSchedule);
        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try {

            String select = "SELECT * FROM " + dbHelper.TABLE_SCHEDULE_AUTOMATIC + " WHERE " + dbHelper.A_ID + " = '" + automaticSchedule.getA_ID()+"'";

            Log.v("Query", select);

            Cursor cur_s = dB.rawQuery(select, null);

            if (cur_s.getCount() == 0) {
                //insertCategory1
                ContentValues values = new ContentValues();

                values.put(dbHelper.A_LABEL, automaticSchedule.getA_LABEL());
                values.put(dbHelper.A_STATUS, automaticSchedule.getA_STATUS());
                values.put(dbHelper.A_USER_ID, automaticSchedule.getA_USER_ID());
                values.put(dbHelper.A_CATEGORY_ID, automaticSchedule.getA_CATEGORY_ID());
                values.put(dbHelper.A_START_TIME,"06:00");
                values.put(dbHelper.A_END_TIME,"22:00");


                count = (int) dB.insert(dbHelper.TABLE_SCHEDULE_AUTOMATIC, null, values);

                if(count>0){
                    System.out.println("* inserted "+count);
                }
            }else{
                //update
                ContentValues values = new ContentValues();

                values.put(dbHelper.A_LABEL, automaticSchedule.getA_LABEL());
                values.put(dbHelper.A_STATUS, automaticSchedule.getA_STATUS());
                values.put(dbHelper.A_USER_ID, automaticSchedule.getA_USER_ID());
                values.put(dbHelper.A_CATEGORY_ID, automaticSchedule.getA_CATEGORY_ID());
                values.put(dbHelper.A_START_TIME,"06:00");
                values.put(dbHelper.A_END_TIME,"22:00");


                count = dB.update(dbHelper.TABLE_SCHEDULE_AUTOMATIC, values, dbHelper.A_ID + " =?", new String[]{automaticSchedule.getA_ID()+"" });

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

    public int insert(String userId,String label,String statusOrcount,String category_id) {
        int count =0;

        System.out.println(userId +": "+ label);

       if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try {

            String select = "SELECT * FROM " + dbHelper.TABLE_SCHEDULE_AUTOMATIC + " WHERE " + dbHelper.A_LABEL + " = '" + label+"'";

            Log.v("Query", select);

            Cursor cur_s = dB.rawQuery(select, null);

            if (cur_s.getCount() == 0) {
                //insert new Category1

                ContentValues values = new ContentValues();

                values.put(dbHelper.A_LABEL, label);
                values.put(dbHelper.A_USER_ID, userId);
                values.put(dbHelper.A_CATEGORY_ID, category_id);
                values.put(dbHelper.A_STATUS,"0");
                values.put(dbHelper.A_START_TIME,"06:00");
                values.put(dbHelper.A_END_TIME,"22:00");


                count = (int) dB.insert(dbHelper.TABLE_SCHEDULE_AUTOMATIC, null, values);

                if(count>0){
                    System.out.println("* inserted "+count);
                }
            }else{
                //update

                //check is powerbank 2 3 case
                System.out.println("* automatic control already has "+label+" statusOrcount "+statusOrcount);
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



    void insertNewLabel(String label, String countORstatus, String userID,String category_id){

        System.out.println(" *** insert new label Automatic"+label+" : "+countORstatus +" : "+category_id);
        System.out.println(" *** userID "+userID);

        try{
            if(isNumber(countORstatus)){
                for(int i=0;i<Integer.parseInt(countORstatus); i++){
                    insert(userID,label+" "+(i+1),countORstatus,category_id);

                }

            }else{
                if(countORstatus.equalsIgnoreCase("yes") && !label.equalsIgnoreCase("Iron")){
                    insert(userID,label,countORstatus,category_id);
                }

            }

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    //##################### GET ####################
        public ArrayList<AutomaticSchedule> getAll(String userID){

        ArrayList<AutomaticSchedule> list = new ArrayList<>();

        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try{

            String select = "SELECT * FROM " + dbHelper.TABLE_SCHEDULE_AUTOMATIC +" WHERE "+dbHelper.A_USER_ID+" = '"+userID+
                    "' ORDER BY "+dbHelper.A_LABEL+" ASC";

            /*String select = "SELECT * FROM " + dbHelper.TABLE_SCHEDULE_AUTOMATIC +" WHERE "+dbHelper.A_USER_ID+" = '"+userID+"' AND "+dbHelper.A_CATEGORY_ID +
                    " IN (SELECT "+dbHelper.C3_ID+" FROM "+dbHelper.TABLE_CATEGORY3_HOME_APPLIANCE+" WHERE "+dbHelper.C3_STATUS_OR_COUNT+" = 'Yes' )"+
                    " ORDER BY "+dbHelper.A_LABEL+" ASC";*/


            Log.v("Query",select);

            Cursor cur = dB.rawQuery(select, null);

            while (cur.moveToNext()) {
                AutomaticSchedule h = new AutomaticSchedule();
                h.setA_ID(cur.getInt(cur.getColumnIndex(dbHelper.A_ID)));
                h.setA_LABEL(cur.getString(cur.getColumnIndex(dbHelper.A_LABEL)));
                h.setA_STATUS(cur.getString(cur.getColumnIndex(dbHelper.A_STATUS)));
                h.setA_USER_ID(cur.getString(cur.getColumnIndex(dbHelper.A_USER_ID)));
                h.setA_CATEGORY_ID(cur.getString(cur.getColumnIndex(dbHelper.A_CATEGORY_ID)));
                h.setA_START_TIME(cur.getString(cur.getColumnIndex(dbHelper.A_START_TIME)));
                h.setA_END_TIME(cur.getString(cur.getColumnIndex(dbHelper.A_END_TIME)));
                list.add(h);
            }

        }catch (Exception e) {
           e.printStackTrace();
        }finally {
            dB.close();
        }

        return list;
    }
    public ArrayList<AutomaticSchedule> getAllStatusIsON(String userID){

        ArrayList<AutomaticSchedule> list = new ArrayList<>();

        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try{

            String select = "SELECT * FROM " + dbHelper.TABLE_SCHEDULE_AUTOMATIC +" WHERE "+dbHelper.A_USER_ID+" = '"+userID +
                    "' AND "+dbHelper.A_STATUS +"= '1'";

            Log.v("Query",select);

            Cursor cur = dB.rawQuery(select, null);

            while (cur.moveToNext()) {
                AutomaticSchedule h = new AutomaticSchedule();
                h.setA_ID(cur.getInt(cur.getColumnIndex(dbHelper.A_ID)));
                h.setA_LABEL(cur.getString(cur.getColumnIndex(dbHelper.A_LABEL)));
                h.setA_STATUS(cur.getString(cur.getColumnIndex(dbHelper.A_STATUS)));
                h.setA_USER_ID(cur.getString(cur.getColumnIndex(dbHelper.A_USER_ID)));
                h.setA_CATEGORY_ID(cur.getString(cur.getColumnIndex(dbHelper.A_CATEGORY_ID)));
                h.setA_START_TIME(cur.getString(cur.getColumnIndex(dbHelper.A_START_TIME)));
                h.setA_END_TIME(cur.getString(cur.getColumnIndex(dbHelper.A_END_TIME)));
                list.add(h);
            }

        }catch (Exception e) {
           e.printStackTrace();
        }finally {
            dB.close();
        }

        return list;
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

            String deleteQuery = "DELETE FROM "+dbHelper.TABLE_SCHEDULE_AUTOMATIC
                    +" WHERE "+dbHelper.A_USER_ID +" = '"+userID+"'";

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

            String deleteQuery = "DELETE FROM "+dbHelper.TABLE_SCHEDULE_AUTOMATIC
                    +" WHERE "+dbHelper.A_USER_ID +" = '"+userID+"' AND "+dbHelper.A_LABEL+" = '"+label+"'";

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
