package lk.iot.lmsrealtime1.model;

public class AutomaticSchedule {

    private int A_ID;
    private String A_USER_ID;
    private String A_LABEL;
    private String A_STATUS;
    private String A_CATEGORY_ID;
    private String A_START_TIME_HOUR;
    private String A_START_TIME_MINUTE;
    private String A_END_TIME_HOUR;
    private String A_END_TIME_MINUTE;

    public AutomaticSchedule() {
    }

    public AutomaticSchedule(String a_USER_ID, String a_LABEL, String a_STATUS) {
        A_USER_ID = a_USER_ID;
        A_LABEL = a_LABEL;
        A_STATUS = a_STATUS;
    }

    public int getA_ID() {
        return A_ID;
    }

    public void setA_ID(int a_ID) {
        A_ID = a_ID;
    }

    public String getA_USER_ID() {
        return A_USER_ID;
    }

    public void setA_USER_ID(String a_USER_ID) {
        A_USER_ID = a_USER_ID;
    }

    public String getA_LABEL() {
        return A_LABEL;
    }

    public void setA_LABEL(String a_LABEL) {
        A_LABEL = a_LABEL;
    }

    public String getA_STATUS() {
        return A_STATUS;
    }

    public void setA_STATUS(String a_STATUS) {
        A_STATUS = a_STATUS;
    }

    public String getA_START_TIME_HOUR() {
        return A_START_TIME_HOUR;
    }

    public void setA_START_TIME_HOUR(String a_START_TIME_HOUR) {
        A_START_TIME_HOUR = a_START_TIME_HOUR;
    }

    public String getA_START_TIME_MINUTE() {
        return A_START_TIME_MINUTE;
    }

    public void setA_START_TIME_MINUTE(String a_START_TIME_MINUTE) {
        A_START_TIME_MINUTE = a_START_TIME_MINUTE;
    }

    public String getA_END_TIME_HOUR() {
        return A_END_TIME_HOUR;
    }

    public void setA_END_TIME_HOUR(String a_END_TIME_HOUR) {
        A_END_TIME_HOUR = a_END_TIME_HOUR;
    }

    public String getA_END_TIME_MINUTE() {
        return A_END_TIME_MINUTE;
    }

    public void setA_END_TIME_MINUTE(String a_END_TIME_MINUTE) {
        A_END_TIME_MINUTE = a_END_TIME_MINUTE;
    }

    public String getA_CATEGORY_ID() {
        return A_CATEGORY_ID;
    }

    public void setA_CATEGORY_ID(String a_CATEGORY_ID) {
        A_CATEGORY_ID = a_CATEGORY_ID;
    }

    @Override
    public String toString() {
        return "AutomaticSchedule{" +
                "A_ID=" + A_ID +
                ", A_USER_ID='" + A_USER_ID + '\'' +
                ", A_LABEL='" + A_LABEL + '\'' +
                ", A_STATUS='" + A_STATUS + '\'' +
                ", A_CATEGORY_ID='" + A_CATEGORY_ID + '\'' +
                ", A_START_TIME_HOUR='" + A_START_TIME_HOUR + '\'' +
                ", A_START_TIME_MINUTE='" + A_START_TIME_MINUTE + '\'' +
                ", A_END_TIME_HOUR='" + A_END_TIME_HOUR + '\'' +
                ", A_END_TIME_MINUTE='" + A_END_TIME_MINUTE + '\'' +
                '}';
    }
}
