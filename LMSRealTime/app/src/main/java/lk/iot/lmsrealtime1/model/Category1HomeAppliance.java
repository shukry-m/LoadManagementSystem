package lk.iot.lmsrealtime1.model;

public class Category1HomeAppliance {

    private String C_ID ;
    private String C_USER_ID;
    private String C_LABEL;
    private int C_Count;


    public Category1HomeAppliance() {
    }

    public Category1HomeAppliance(String c_ID,String c_USER_ID, String c_LABEL, int c_Count) {
        C_ID = c_ID;
        C_USER_ID = c_USER_ID;
        C_LABEL = c_LABEL;
        C_Count = c_Count;
    }

    public String getC_USER_ID() {
        return C_USER_ID;
    }

    public void setC_USER_ID(String c_USER_ID) {
        C_USER_ID = c_USER_ID;
    }

    public String getC_LABEL() {
        return C_LABEL;
    }

    public void setC_LABEL(String c_LABEL) {
        C_LABEL = c_LABEL;
    }

    public String getC_ID() {
        return C_ID;
    }

    public void setC_ID(String c_ID) {
        C_ID = c_ID;
    }

    public int getC_Count() {
        return C_Count;
    }

    public void setC_Count(int c_Count) {
        C_Count = c_Count;
    }

    @Override
    public String toString() {
        return "Category1HomeAppliance{" +
                "C_ID='" + C_ID + '\'' +
                ", C_USER_ID='" + C_USER_ID + '\'' +
                ", C_LABEL='" + C_LABEL + '\'' +
                ", C_Count='" + C_Count + '\'' +
                '}';
    }
}
