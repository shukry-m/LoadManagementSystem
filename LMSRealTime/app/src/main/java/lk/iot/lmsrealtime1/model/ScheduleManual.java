package lk.iot.lmsrealtime1.model;

public class ScheduleManual {

    private int S_ID;
    private String S_USER_ID;
    private String S_LABEL;
    private String S_Start_Time_Hour;
    private String S_Start_Time_Minute;
    private String S_End_Time_Hour;
    private String S_End_Time_Minute;
    private String S_Category_Id;

    public ScheduleManual() {
    }


    public ScheduleManual(String s_USER_ID, String s_LABEL, String s_Start_Time_Hour, String s_Start_Time_Minute, String s_End_Time_Hour, String s_End_Time_Minute) {
        S_USER_ID = s_USER_ID;
        S_LABEL = s_LABEL;
        S_Start_Time_Hour = s_Start_Time_Hour;
        S_Start_Time_Minute = s_Start_Time_Minute;
        S_End_Time_Hour = s_End_Time_Hour;
        S_End_Time_Minute = s_End_Time_Minute;
    }

    public int getS_ID() {
        return S_ID;
    }

    public void setS_ID(int s_ID) {
        S_ID = s_ID;
    }

    public String getS_USER_ID() {
        return S_USER_ID;
    }

    public void setS_USER_ID(String s_USER_ID) {
        S_USER_ID = s_USER_ID;
    }

    public String getS_LABEL() {
        return S_LABEL;
    }

    public void setS_LABEL(String s_LABEL) {
        S_LABEL = s_LABEL;
    }

    public String getS_Start_Time_Hour() {
        return S_Start_Time_Hour;
    }

    public void setS_Start_Time_Hour(String s_Start_Time_Hour) {
        S_Start_Time_Hour = s_Start_Time_Hour;
    }

    public String getS_Start_Time_Minute() {
        return S_Start_Time_Minute;
    }

    public void setS_Start_Time_Minute(String s_Start_Time_Minute) {
        S_Start_Time_Minute = s_Start_Time_Minute;
    }

    public String getS_End_Time_Hour() {
        return S_End_Time_Hour;
    }

    public void setS_End_Time_Hour(String s_End_Time_Hour) {
        S_End_Time_Hour = s_End_Time_Hour;
    }

    public String getS_End_Time_Minute() {
        return S_End_Time_Minute;
    }

    public void setS_End_Time_Minute(String s_End_Time_Minute) {
        S_End_Time_Minute = s_End_Time_Minute;
    }

    public String getS_Category_Id() {
        return S_Category_Id;
    }

    public void setS_Category_Id(String s_Category_Id) {
        S_Category_Id = s_Category_Id;
    }

    @Override
    public String toString() {
        return "ScheduleManual{" +
                "S_ID=" + S_ID +
                ", S_USER_ID='" + S_USER_ID + '\'' +
                ", S_LABEL='" + S_LABEL + '\'' +
                ", S_Start_Time_Hour='" + S_Start_Time_Hour + '\'' +
                ", S_Start_Time_Minute='" + S_Start_Time_Minute + '\'' +
                ", S_End_Time_Hour='" + S_End_Time_Hour + '\'' +
                ", S_End_Time_Minute='" + S_End_Time_Minute + '\'' +
                ", S_Category_Id='" + S_Category_Id + '\'' +
                '}';
    }
}

