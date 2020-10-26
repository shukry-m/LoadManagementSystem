package lk.iot.lmsrealtime1.model;

public class Category2HomeAppliance {

    private String C2_ID ;
    private String C2_USER_ID;
    private String C2_LABEL;
    private String C2_STATUS;


    public Category2HomeAppliance() {
    }

    public Category2HomeAppliance(String c2_ID, String c2_USER_ID, String c2_LABEL, String c2_STATUS) {
        C2_ID = c2_ID;
        C2_USER_ID = c2_USER_ID;
        C2_LABEL = c2_LABEL;
        C2_STATUS = c2_STATUS;
    }

    public String getC2_ID() {
        return C2_ID;
    }

    public void setC2_ID(String c2_ID) {
        C2_ID = c2_ID;
    }

    public String getC2_USER_ID() {
        return C2_USER_ID;
    }

    public void setC2_USER_ID(String c2_USER_ID) {
        C2_USER_ID = c2_USER_ID;
    }

    public String getC2_LABEL() {
        return C2_LABEL;
    }

    public void setC2_LABEL(String c2_LABEL) {
        C2_LABEL = c2_LABEL;
    }

    public String getC2_STATUS() {
        return C2_STATUS;
    }

    public void setC2_STATUS(String c2_STATUS) {
        C2_STATUS = c2_STATUS;
    }

    @Override
    public String toString() {
        return "Category2HomeAppliance{" +
                "C2_ID=" + C2_ID +
                ", C2_USER_ID='" + C2_USER_ID + '\'' +
                ", C2_LABEL='" + C2_LABEL + '\'' +
                ", C2_STATUS='" + C2_STATUS + '\'' +
                '}';
    }
}
