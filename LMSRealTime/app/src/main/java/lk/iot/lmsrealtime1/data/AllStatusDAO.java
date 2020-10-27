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
import lk.iot.lmsrealtime1.model.AllStatus;

public class AllStatusDAO {

    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    Context context;
    FirebaseAuth fAuth;
    public static final String TAG = "TAG";

    public AllStatusDAO(Context context){
        this.context = context;
        dbHelper = new DatabaseHelper(context);
        fAuth = FirebaseAuth.getInstance();
     //   fStore = FirebaseFirestore.getInstance();
    }

    public void open() throws SQLException{
        dB = dbHelper.getWritableDatabase();
    }



    //##################################### INSERT ###################################

    public int insert(AllStatus allStatus) {
        int count =0;

        System.out.println(allStatus);
        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try {


            String select = "SELECT * FROM " + dbHelper.TABLE_ALL_STATUS + " WHERE " + dbHelper.ALL_ID + " = '" + allStatus.getALL_ID()+"'";

            Log.v("Query", select);

            Cursor cur_s = dB.rawQuery(select, null);

            if (cur_s.getCount() == 0) {
                //insertCategory1
                ContentValues values = new ContentValues();

                values.put(dbHelper.ALL_USER_ID, allStatus.getALL_USER_ID());
                values.put(dbHelper.ALL_LABEL, allStatus.getALL_LABEL());
                values.put(dbHelper.ALL_STATUS, allStatus.getALL_STATUS());


                count = (int) dB.insert(dbHelper.TABLE_ALL_STATUS, null, values);

                if(count>0){
                    System.out.println("* inserted "+count);
                }
            }else{
                //update
                ContentValues values = new ContentValues();

                values.put(dbHelper.ALL_USER_ID, allStatus.getALL_USER_ID());
                values.put(dbHelper.ALL_LABEL, allStatus.getALL_LABEL());
                if(allStatus.getALL_STATUS() != null) {
                    values.put(dbHelper.ALL_STATUS, allStatus.getALL_STATUS());
                }


                count = dB.update(dbHelper.TABLE_ALL_STATUS, values, dbHelper.ALL_ID + " =?", new String[]{allStatus.getALL_ID()+"" });

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


    public AllStatus get(String label, String userID){


        AllStatus status = new AllStatus();

        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }

        try{

            String select = "SELECT * FROM " + dbHelper.TABLE_ALL_STATUS +
                    " WHERE "+dbHelper.ALL_USER_ID +" = '"+userID+"' AND "+dbHelper.ALL_LABEL +" = '"+label+"'";

            Log.v("Query",select);

            Cursor cur = dB.rawQuery(select, null);

            while (cur.moveToNext()) {

                status.setALL_ID(cur.getInt(cur.getColumnIndex(dbHelper.ALL_ID)));
                status.setALL_USER_ID(cur.getString(cur.getColumnIndex(dbHelper.ALL_USER_ID)));
                status.setALL_LABEL(cur.getString(cur.getColumnIndex(dbHelper.ALL_LABEL)));
                status.setALL_STATUS(cur.getString(cur.getColumnIndex(dbHelper.ALL_STATUS)));

            }

        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            dB.close();
        }

        return status;

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

            String deleteQuery = "DELETE FROM "+dbHelper.TABLE_ALL_STATUS
                    +" WHERE "+dbHelper.ALL_USER_ID +" = '"+userID+"'";

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
