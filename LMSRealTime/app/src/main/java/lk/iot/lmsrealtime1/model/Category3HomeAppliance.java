package lk.iot.lmsrealtime1.model;

public class Category3HomeAppliance {

    private String C3_ID ;
    private String C3_USER_ID;
    private String C3_LABEL;
    private String C3_STATUS_OR_COUNT;


    public Category3HomeAppliance() {
    }

    public Category3HomeAppliance(String c3_ID,String c3_LABEL, String c3_STATUS_OR_COUNT,String c3_USER_ID) {
        C3_ID = c3_ID;
        C3_USER_ID = c3_USER_ID;
        C3_LABEL = c3_LABEL;
        C3_STATUS_OR_COUNT = c3_STATUS_OR_COUNT;
    }

    public String getC3_ID() {
        return C3_ID;
    }

    public void setC3_ID(String c3_ID) {
        C3_ID = c3_ID;
    }

    public String getC3_USER_ID() {
        return C3_USER_ID;
    }

    public void setC3_USER_ID(String c3_USER_ID) {
        C3_USER_ID = c3_USER_ID;
    }

    public String getC3_LABEL() {
        return C3_LABEL;
    }

    public void setC3_LABEL(String c3_LABEL) {
        C3_LABEL = c3_LABEL;
    }

    public String getC3_STATUS_OR_COUNT() {
        return C3_STATUS_OR_COUNT;
    }

    public void setC3_STATUS_OR_COUNT(String c3_STATUS_OR_COUNT) {
        C3_STATUS_OR_COUNT = c3_STATUS_OR_COUNT;
    }

    @Override
    public String toString() {
        return "Category3HomeAppliance{" +
                "C3_ID='" + C3_ID + '\'' +
                ", C3_USER_ID='" + C3_USER_ID + '\'' +
                ", C3_LABEL='" + C3_LABEL + '\'' +
                ", C3_STATUS_OR_COUNT='" + C3_STATUS_OR_COUNT + '\'' +
                '}';
    }
}
