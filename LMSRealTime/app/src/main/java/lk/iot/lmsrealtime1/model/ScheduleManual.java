package lk.iot.lmsrealtime1.model;

public class ScheduleManual {

    private int S_ID;
    private String S_USER_ID;
    private String S_LABEL;
    private String S_Start_Time;
    private String S_End_Time;

    public ScheduleManual() {
    }

    public ScheduleManual(String s_USER_ID, String s_LABEL, String s_STATUS, String s_Start_Time, String s_End_Time) {
        S_USER_ID = s_USER_ID;
        S_LABEL = s_LABEL;
        S_Start_Time = s_Start_Time;
        S_End_Time = s_End_Time;
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



    public String getS_Start_Time() {
        return S_Start_Time;
    }

    public void setS_Start_Time(String s_Start_Time) {
        S_Start_Time = s_Start_Time;
    }

    public String getS_End_Time() {
        return S_End_Time;
    }

    public void setS_End_Time(String s_End_Time) {
        S_End_Time = s_End_Time;
    }

    @Override
    public String toString() {
        return "ScheduleManual{" +
                "S_ID=" + S_ID +
                ", S_USER_ID='" + S_USER_ID + '\'' +
                ", S_LABEL='" + S_LABEL + '\'' +
                ", S_Start_Time='" + S_Start_Time + '\'' +
                ", S_End_Time='" + S_End_Time + '\'' +
                '}';
    }
}

