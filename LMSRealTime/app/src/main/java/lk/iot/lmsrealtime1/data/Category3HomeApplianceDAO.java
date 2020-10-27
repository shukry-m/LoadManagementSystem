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
import lk.iot.lmsrealtime1.model.Category3HomeAppliance;

public class Category3HomeApplianceDAO {

    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    Context context;
    FirebaseAuth fAuth;
   // FirebaseFirestore fStore;
    String userID;
    public static final String TAG = "TAG";

    public Category3HomeApplianceDAO(Context context){
        this.context = context;
        dbHelper = new DatabaseHelper(context);
        fAuth = FirebaseAuth.getInstance();
     //   fStore = FirebaseFirestore.getInstance();
    }

    public void open() throws SQLException{
        dB = dbHelper.getWritableDatabase();
    }

    //##################################### INSERT ###################################


    public int insert(ArrayList<Category3HomeAppliance> list) {
        int count =0;
        if(list.size() >0){
            for(Category3HomeAppliance c : list){
                count = count+ insert(c);
            }
        }
        return count;
    }


    public int insert(Category3HomeAppliance category) {
        int count =0;

        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try {
            System.out.println(category);

            String select = "SELECT * FROM " + dbHelper.TABLE_CATEGORY3_HOME_APPLIANCE + " WHERE " + dbHelper.C3_ID + " = '" + category.getC3_ID()+"'";

            Log.v("Query", select);

            Cursor cur_s = dB.rawQuery(select, null);

            if (cur_s.getCount() == 0) {
                //insertCategory3
                ContentValues values = new ContentValues();
                values.put(dbHelper.C3_ID, category.getC3_ID());
                values.put(dbHelper.C3_LABEL, category.getC3_LABEL());
                values.put(dbHelper.C3_USER_ID, category.getC3_USER_ID());
                values.put(dbHelper.C3_STATUS_OR_COUNT,category.getC3_STATUS_OR_COUNT());

                count = (int) dB.insert(dbHelper.TABLE_CATEGORY3_HOME_APPLIANCE, null, values);

                if(count>0){
                    System.out.println("* inserted "+count);
                }

            }else{
                //update
                ContentValues values = new ContentValues();

                values.put(dbHelper.C3_LABEL, category.getC3_LABEL());
                values.put(dbHelper.C3_USER_ID, category.getC3_USER_ID());
                values.put(dbHelper.C3_STATUS_OR_COUNT,category.getC3_STATUS_OR_COUNT());

                count = (int) dB.update(dbHelper.TABLE_CATEGORY3_HOME_APPLIANCE, values, dbHelper.C3_ID + " =?", new String[]{category.getC3_ID()+""});

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

    public ArrayList<Category3HomeAppliance> getAll(String userID){

        ArrayList<Category3HomeAppliance> list = new ArrayList<>();


        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try{

            String select = "SELECT * FROM " + dbHelper.TABLE_CATEGORY3_HOME_APPLIANCE +" WHERE "+dbHelper.C3_USER_ID+" = '"+userID+"'";

            Log.v("Query",select);

            Cursor cur = dB.rawQuery(select, null);

            while (cur.moveToNext()) {
                Category3HomeAppliance c = new Category3HomeAppliance();
                c.setC3_ID(cur.getString(cur.getColumnIndex(dbHelper.C3_ID)));
                c.setC3_LABEL(cur.getString(cur.getColumnIndex(dbHelper.C3_LABEL)));
                c.setC3_USER_ID(cur.getString(cur.getColumnIndex(dbHelper.C3_USER_ID)));
                c.setC3_STATUS_OR_COUNT(cur.getString(cur.getColumnIndex(dbHelper.C3_STATUS_OR_COUNT)));

                list.add(c);
            }

        }catch (Exception e) {
           e.printStackTrace();
        }finally {
            dB.close();
        }

        return list;

    }


    public Category3HomeAppliance get(String c_id,String userID){


        Category3HomeAppliance c = new Category3HomeAppliance();

        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try{

            String select = "SELECT * FROM " + dbHelper.TABLE_CATEGORY3_HOME_APPLIANCE+
                    " WHERE "+dbHelper.C3_USER_ID+" = '"+userID+"' AND "+dbHelper.C3_ID+" = '"+c_id+"'";

            Log.v("Query",select);

            Cursor cur = dB.rawQuery(select, null);

            while (cur.moveToNext()) {

                c.setC3_ID(cur.getString(cur.getColumnIndex(dbHelper.C3_ID)));
                c.setC3_LABEL(cur.getString(cur.getColumnIndex(dbHelper.C3_LABEL)));
                c.setC3_USER_ID(cur.getString(cur.getColumnIndex(dbHelper.C3_USER_ID)));
                c.setC3_STATUS_OR_COUNT(cur.getString(cur.getColumnIndex(dbHelper.C3_STATUS_OR_COUNT)));

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

            String deleteQuery = "DELETE FROM "+dbHelper.TABLE_CATEGORY3_HOME_APPLIANCE
                    +" WHERE "+dbHelper.C3_USER_ID+" = '"+userID+"'";

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

            String deleteQuery = "DELETE FROM "+dbHelper.TABLE_CATEGORY3_HOME_APPLIANCE
                    +" WHERE "+dbHelper.C3_ID+" = '"+c_id+"' AND "+dbHelper.C3_USER_ID+" = '"+userID+"'";

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
