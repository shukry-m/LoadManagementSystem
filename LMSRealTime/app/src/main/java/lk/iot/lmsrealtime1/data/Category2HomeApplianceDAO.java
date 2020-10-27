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
import lk.iot.lmsrealtime1.model.ManualControl;

public class Category2HomeApplianceDAO {

    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    Context context;
    FirebaseAuth fAuth;
   // FirebaseFirestore fStore;
    String userID;
    public static final String TAG = "TAG";

    public Category2HomeApplianceDAO(Context context){
        this.context = context;
        dbHelper = new DatabaseHelper(context);
        fAuth = FirebaseAuth.getInstance();
     //   fStore = FirebaseFirestore.getInstance();
    }

    public void open() throws SQLException{
        dB = dbHelper.getWritableDatabase();
    }

    //##################################### INSERT ###################################


    public int insert(ArrayList<Category2HomeAppliance> list) {
        int count =0;
        if(list.size() >0){
            for(Category2HomeAppliance homeAppliance : list){
                count = count+ insert(homeAppliance);
            }
        }
        return count;
    }


    public int insert(Category2HomeAppliance homeAppliance) {
        int count =0;

        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try {

            String select = "SELECT * FROM " + dbHelper.TABLE_CATEGORY2_HOME_APPLIANCE + " WHERE " + dbHelper.C2_ID + " = '" + homeAppliance.getC2_ID()+"'";

            Log.v("Query", select);

            Cursor cur_s = dB.rawQuery(select, null);

            if (cur_s.getCount() == 0) {
                //insertCategory2
                ContentValues values = new ContentValues();
                values.put(dbHelper.C2_ID, homeAppliance.getC2_ID());
                values.put(dbHelper.C2_LABEL, homeAppliance.getC2_LABEL());
                values.put(dbHelper.C2_USER_ID, homeAppliance.getC2_USER_ID());
                values.put(dbHelper.C2_STATUS,homeAppliance.getC2_STATUS());

                count = (int) dB.insert(dbHelper.TABLE_CATEGORY2_HOME_APPLIANCE, null, values);

                if(count>0){
                    System.out.println("* inserted "+count);
                }

            }else{
                //update
                ContentValues values = new ContentValues();

                values.put(dbHelper.C2_LABEL, homeAppliance.getC2_LABEL());
                values.put(dbHelper.C2_USER_ID, homeAppliance.getC2_USER_ID());
                values.put(dbHelper.C2_STATUS,homeAppliance.getC2_STATUS());

                count = (int) dB.update(dbHelper.TABLE_CATEGORY2_HOME_APPLIANCE, values, dbHelper.C2_ID + " =?", new String[]{homeAppliance.getC2_ID()+""});


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

    private void checkOtherTables(String c2_id,String userId) {
        //status is no so, remove from local dbs and firebase db
        //check manualcontrol db
        ManualControl m = new ManualControlDAO(context).get(c2_id,userId);
        if(m != null){

            //delete from firebase
            String path = "users/" + userId + "/manualControl";
            new FirebaseDAO(context).deleteFromFirebase(path,c2_id);
            //delete from local
            new ManualControlDAO(context).delete(c2_id,userId);
        }
    }

    //##################### GET ####################

    public ArrayList<Category2HomeAppliance> getAll(String userID){

        ArrayList<Category2HomeAppliance> list = new ArrayList<>();


        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try{

            String select = "SELECT * FROM " + dbHelper.TABLE_CATEGORY2_HOME_APPLIANCE +" WHERE "+dbHelper.C2_USER_ID+" = '"+userID+"'";

            Log.v("Query",select);

            Cursor cur = dB.rawQuery(select, null);

            while (cur.moveToNext()) {
                Category2HomeAppliance c = new Category2HomeAppliance();
                c.setC2_ID(cur.getString(cur.getColumnIndex(dbHelper.C2_ID)));
                c.setC2_LABEL(cur.getString(cur.getColumnIndex(dbHelper.C2_LABEL)));
                c.setC2_USER_ID(cur.getString(cur.getColumnIndex(dbHelper.C2_USER_ID)));
                c.setC2_STATUS(cur.getString(cur.getColumnIndex(dbHelper.C2_STATUS)));

                list.add(c);
            }

        }catch (Exception e) {
           e.printStackTrace();
        }finally {
            dB.close();
        }

        return list;

    }

    public Category2HomeAppliance get(String c_id,String userID){


        Category2HomeAppliance c = new Category2HomeAppliance();

        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try{

            String select = "SELECT * FROM " + dbHelper.TABLE_CATEGORY2_HOME_APPLIANCE+
                    " WHERE "+dbHelper.C2_USER_ID+" = '"+userID+"' AND "+dbHelper.C2_ID+" = '"+c_id+"'";

            Log.v("Query",select);

            Cursor cur = dB.rawQuery(select, null);

            while (cur.moveToNext()) {

                c.setC2_ID(cur.getString(cur.getColumnIndex(dbHelper.C2_ID)));
                c.setC2_LABEL(cur.getString(cur.getColumnIndex(dbHelper.C2_LABEL)));
                c.setC2_USER_ID(cur.getString(cur.getColumnIndex(dbHelper.C2_USER_ID)));
                c.setC2_STATUS(cur.getString(cur.getColumnIndex(dbHelper.C2_STATUS)));

            }

        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            dB.close();
        }

        return c;

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

            String deleteQuery = "DELETE FROM "+dbHelper.TABLE_CATEGORY2_HOME_APPLIANCE
                    +" WHERE "+dbHelper.C_USER_ID+" = '"+userID+"'";

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



    public int delete(String c_id,String userID) {

        int count =0;
        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }
        Cursor cursor = null;

        try{

            String deleteQuery = "DELETE FROM "+dbHelper.TABLE_CATEGORY2_HOME_APPLIANCE
                    +" WHERE "+dbHelper.C2_ID+" = '"+c_id+"' AND "+dbHelper.C2_USER_ID+" = '"+userID+"'";

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
