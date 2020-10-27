package lk.iot.lmsrealtime1.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "load_management.db";
    private static final int DATABASE_VERSION = 1;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    //#################### MANUAL CONTROL TABLE ######################
    // TABLE
    public static final String TABLE_MANUAL_CONTROL = "manual_control_tbl";
    // TABLE ATTRIBUTES
    public static final String M_ID = "m_Id";
    public static final String M_USER_ID = "m_user_id";
    public static final String M_LABEL = "m_label_name";
    public static final String M_STATUS = "m_status";
    public static final String M_CATEGORY_ID = "m_category_id";

    //TABLE CREATING STRING
    private static final String CREATE_MANUAL_CONTROL_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_MANUAL_CONTROL + " ("
            + M_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + M_USER_ID + " TEXT, "
            + M_LABEL + " TEXT, "
            + M_CATEGORY_ID + " TEXT, "
            + M_STATUS + " TEXT ); ";



    //#################### CATEGORY1_HOME_APPLIANCE TABLE ######################
        // TABLE
        public static final String TABLE_CATEGORY1_HOME_APPLIANCE = "category1_tbl";
        // TABLE ATTRIBUTES
        public static final String C_ID = "c_Id";
        public static final String C_USER_ID = "c_user_id";
        public static final String C_LABEL = "c_label_name";
        public static final String C_Count  =  "c_count";


        //TABLE CREATING STRING
        private static final String CREATE_CATEGORY1_HOME_APPLIANCE_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_CATEGORY1_HOME_APPLIANCE + " ("
                + C_ID + " TEXT PRIMARY KEY, "
                + C_USER_ID + " TEXT, "
                + C_LABEL + " TEXT, "
                + C_Count + " INTEGER ); ";


 //#################### CATEGORY2_HOME_APPLIANCE TABLE ######################
        // TABLE
        public static final String TABLE_CATEGORY2_HOME_APPLIANCE = "category2_tbl";
        // TABLE ATTRIBUTES
        public static final String C2_ID = "c2_Id";
        public static final String C2_USER_ID = "c2_user_id";
        public static final String C2_LABEL = "c2_label_name";
        public static final String C2_STATUS  =  "c2_status";


        //TABLE CREATING STRING
        private static final String CREATE_CATEGORY2_HOME_APPLIANCE_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_CATEGORY2_HOME_APPLIANCE + " ("
                + C2_ID + " TEXT PRIMARY KEY, "
                + C2_USER_ID + " TEXT, "
                + C2_LABEL + " TEXT, "
                + C2_STATUS + " TEXT ); ";

//#################### CATEGORY3_HOME_APPLIANCE TABLE ######################
        // TABLE
        public static final String TABLE_CATEGORY3_HOME_APPLIANCE = "category3_tbl";
        // TABLE ATTRIBUTES
        public static final String C3_ID = "c3_Id";
        public static final String C3_USER_ID = "c3_user_id";
        public static final String C3_LABEL = "c3_label_name";
        public static final String C3_STATUS_OR_COUNT  =  "c3_status_OR_count";


        //TABLE CREATING STRING
        private static final String CREATE_TABLE_CATEGORY3_HOME_APPLIANCE = "CREATE  TABLE IF NOT EXISTS " + TABLE_CATEGORY3_HOME_APPLIANCE + " ("
                + C3_ID + " TEXT PRIMARY KEY, "
                + C3_USER_ID + " TEXT, "
                + C3_LABEL + " TEXT, "
                + C3_STATUS_OR_COUNT + " TEXT ); ";


    //####################SCHEDULE AUTOMATIC TABLE ######################
    // TABLE
    public static final String TABLE_SCHEDULE_AUTOMATIC = "automatic_schedule_tbl";
    // TABLE ATTRIBUTES
    public static final String A_ID = "a_Id";
    public static final String A_USER_ID = "a_user_id";
    public static final String A_LABEL = "a_label_name";
    public static final String A_CATEGORY_ID = "a_category_id";
    public static final String A_STATUS  =  "a_status";
    public static final String A_START_TIME_HOUR  =  "a_start_time_hour";
    public static final String A_START_TIME_MINUTE  =  "a_start_time_minute";
    public static final String A_END_TIME_HOUR  =  "a_end_time_hour";
    public static final String A_END_TIME_MINUTE  =  "a_end_time_minute";


    //TABLE CREATING STRING
    private static final String CREATE_TABLE_SCHEDULE_AUTOMATIC = "CREATE  TABLE IF NOT EXISTS " + TABLE_SCHEDULE_AUTOMATIC + " ("
            + A_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + A_USER_ID + " TEXT, "
            + A_LABEL + " TEXT, "
            + A_CATEGORY_ID + " TEXT, "
            + A_START_TIME_HOUR + " TEXT, "
            + A_START_TIME_MINUTE + " TEXT, "
            + A_END_TIME_HOUR + " TEXT, "
            + A_END_TIME_MINUTE + " TEXT, "
            + A_STATUS + " TEXT ); ";

 //#################### SCHEDULE MANUAL COOKING TABLE ######################

    // TABLE
    public static final String TABLE_SCHEDULE_MANUAL_COOKING = "table_schedule_manual_cooking";
    // TABLE ATTRIBUTES
    public static final String S_ID = "s_Id";
    public static final String S_USER_ID = "s_user_id";
    public static final String S_LABEL = "s_label_name";
    public static final String S_Start_Time_Hour  =  "s_start_time_hour";
    public static final String S_Start_Time_Minute  =  "s_start_time_minute";
    public static final String S_End_Time_Hour  =  "s_end_time_hour";
    public static final String S_End_Time_Minute  =  "s_end_time_minute";


    //TABLE CREATING STRING
    private static final String CREATE_TABLE_SCHEDULE_MANUAL_COOKING = "CREATE  TABLE IF NOT EXISTS " + TABLE_SCHEDULE_MANUAL_COOKING + " ("
            + S_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + S_USER_ID + " TEXT, "
            + S_LABEL + " TEXT, "
            + S_Start_Time_Hour + " TEXT, "
            + S_Start_Time_Minute + " TEXT, "
            + S_End_Time_Hour + " TEXT, "
            + S_End_Time_Minute + " TEXT ); ";

    //#################### SCHEDULE MANUAL FLEXIBLE LOADS TABLE ######################

    // TABLE
    public static final String TABLE_SCHEDULE_MANUAL_FLEXIBLE_LOADS = "table_schedule_manual_flexible_loads";
    // TABLE ATTRIBUTES
    public static final String F_ID = "f_Id";
    public static final String F_USER_ID = "f_user_id";
    public static final String F_LABEL = "f_label_name";
    public static final String F_Start_Time_Hour  =  "f_start_time_hour";
    public static final String F_Start_Time_Minute  =  "f_start_time_minute";
    public static final String F_End_Time_Hour  =  "f_end_time_hour";
    public static final String F_End_Time_Minute  =  "f_end_time_minute";


    //TABLE CREATING STRING
    private static final String CREATE_TABLE_SCHEDULE_MANUAL_FLEXIBLE_LOADS = "CREATE  TABLE IF NOT EXISTS " + TABLE_SCHEDULE_MANUAL_FLEXIBLE_LOADS + " ("
            + F_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + F_USER_ID + " TEXT, "
            + F_LABEL + " TEXT, "
            + F_Start_Time_Hour + " TEXT, "
            + F_Start_Time_Minute + " TEXT, "
            + F_End_Time_Hour + " TEXT, "
            + F_End_Time_Minute + " TEXT ); ";

    //#################### ALL STATUS TABLE ######################

    // TABLE
    public static final String TABLE_ALL_STATUS = "table_all_status";
    // TABLE ATTRIBUTES
    public static final String ALL_ID = "all_id";
    public static final String ALL_USER_ID = "all_user_id";
    public static final String ALL_LABEL = "all_label";
    public static final String ALL_STATUS  =  "all_status";


    //TABLE CREATING STRING
    private static final String CREATE_TABLE_ALL_STATUS = "CREATE  TABLE IF NOT EXISTS " + TABLE_ALL_STATUS + " ("
            + ALL_ID + " INTEGER PRIMARY KEY,"
            + ALL_USER_ID + " TEXT, "
            + ALL_LABEL + " TEXT, "
            + ALL_STATUS + " TEXT ); ";


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_MANUAL_CONTROL_TABLE);
        db.execSQL(CREATE_CATEGORY1_HOME_APPLIANCE_TABLE);
        db.execSQL(CREATE_CATEGORY2_HOME_APPLIANCE_TABLE);
        db.execSQL(CREATE_TABLE_CATEGORY3_HOME_APPLIANCE);
        db.execSQL(CREATE_TABLE_SCHEDULE_AUTOMATIC);
        db.execSQL(CREATE_TABLE_SCHEDULE_MANUAL_COOKING);
        db.execSQL(CREATE_TABLE_SCHEDULE_MANUAL_FLEXIBLE_LOADS);
        db.execSQL(CREATE_TABLE_ALL_STATUS);




    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
