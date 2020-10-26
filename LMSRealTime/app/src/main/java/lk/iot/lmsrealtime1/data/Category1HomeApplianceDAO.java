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

public class Category1HomeApplianceDAO {

    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    Context context;
    FirebaseAuth fAuth;
   // FirebaseFirestore fStore;
    String userID;
    public static final String TAG = "TAG";

    public Category1HomeApplianceDAO(Context context){
        this.context = context;
        dbHelper = new DatabaseHelper(context);
        fAuth = FirebaseAuth.getInstance();
     //   fStore = FirebaseFirestore.getInstance();
    }

    public void open() throws SQLException{
        dB = dbHelper.getWritableDatabase();
    }

    //##################################### INSERT ###################################


    public int insert(ArrayList<Category1HomeAppliance> list) {
        int count =0;
        if(list.size() >0){
            for(Category1HomeAppliance category : list){
                count = count+ insert(category);
            }
        }
        return count;
    }


    public int insert(Category1HomeAppliance homeAppliance) {
        int count =0;

        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        System.out.println(homeAppliance);
        try {

            String select = "SELECT * FROM " + dbHelper.TABLE_CATEGORY1_HOME_APPLIANCE + " WHERE " + dbHelper.C_ID + " = '" + homeAppliance.getC_ID() +"'";

            Log.v("Query", select);

            Cursor cur_s = dB.rawQuery(select, null);

            if (cur_s.getCount() == 0) {
                //insertCategory1
                ContentValues values = new ContentValues();
                values.put(dbHelper.C_ID, homeAppliance.getC_ID());
                values.put(dbHelper.C_LABEL, homeAppliance.getC_LABEL());
                values.put(dbHelper.C_USER_ID, homeAppliance.getC_USER_ID());
                values.put(dbHelper.C_Count, homeAppliance.getC_Count());

                count = (int) dB.insert(dbHelper.TABLE_CATEGORY1_HOME_APPLIANCE, null, values);

                if(count>0){
                    System.out.println("* inserted "+count);
                }

            }else{
                //update
                ContentValues values = new ContentValues();

                values.put(dbHelper.C_LABEL, homeAppliance.getC_LABEL());
                values.put(dbHelper.C_USER_ID, homeAppliance.getC_USER_ID());
                values.put(dbHelper.C_Count, homeAppliance.getC_Count());

                count = (int) dB.update(dbHelper.TABLE_CATEGORY1_HOME_APPLIANCE, values, dbHelper.C_ID + " =?", new String[]{homeAppliance.getC_ID()+""});

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

    //##################### GET ####################

    public ArrayList<Category1HomeAppliance> getAll(String userID){

        ArrayList<Category1HomeAppliance> list = new ArrayList<>();


        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try{

            String select = "SELECT * FROM " + dbHelper.TABLE_CATEGORY1_HOME_APPLIANCE +" WHERE "+dbHelper.C_USER_ID+" = '"+userID+"'";

            Log.v("Query",select);

            Cursor cur = dB.rawQuery(select, null);

            while (cur.moveToNext()) {
                Category1HomeAppliance c = new Category1HomeAppliance();
                c.setC_ID(cur.getString(cur.getColumnIndex(dbHelper.C_ID)));
                c.setC_LABEL(cur.getString(cur.getColumnIndex(dbHelper.C_LABEL)));
                c.setC_USER_ID(cur.getString(cur.getColumnIndex(dbHelper.C_USER_ID)));
                c.setC_Count(cur.getInt(cur.getColumnIndex(dbHelper.C_Count)));

                list.add(c);
            }

        }catch (Exception e) {
           e.printStackTrace();
        }finally {
            dB.close();
        }

        return list;

    }

    public Category1HomeAppliance get(String c_id,String userID){


        Category1HomeAppliance c = new Category1HomeAppliance();

        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try{

            String select = "SELECT * FROM " + dbHelper.TABLE_CATEGORY1_HOME_APPLIANCE+
                    " WHERE "+dbHelper.C_USER_ID+" = '"+userID+"' AND "+dbHelper.C_ID+" = '"+c_id+"'";

            Log.v("Query",select);

            Cursor cur = dB.rawQuery(select, null);

            while (cur.moveToNext()) {

                c.setC_ID(cur.getString(cur.getColumnIndex(dbHelper.C_ID)));
                c.setC_LABEL(cur.getString(cur.getColumnIndex(dbHelper.C_LABEL)));
                c.setC_USER_ID(cur.getString(cur.getColumnIndex(dbHelper.C_USER_ID)));
                c.setC_Count(cur.getInt(cur.getColumnIndex(dbHelper.C_Count)));

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

            String deleteQuery = "DELETE FROM "+dbHelper.TABLE_CATEGORY1_HOME_APPLIANCE
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

            String deleteQuery = "DELETE FROM "+dbHelper.TABLE_CATEGORY1_HOME_APPLIANCE
                    +" WHERE "+dbHelper.C_ID+" = '"+c_id+"' AND "+dbHelper.C_USER_ID+" = '"+userID+"'";

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
