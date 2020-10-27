package lk.iot.lmsrealtime1.model;

public class AllStatus {

    private int ALL_ID;
    private String ALL_USER_ID;
    private String ALL_LABEL;
    private String ALL_STATUS;

    public AllStatus() {
    }

    public AllStatus( String ALL_USER_ID, String ALL_LABEL) {
        this.ALL_USER_ID = ALL_USER_ID;
        this.ALL_LABEL = ALL_LABEL;
    }

    public AllStatus(String ALL_USER_ID, String ALL_LABEL, String ALL_STATUS) {
        this.ALL_USER_ID = ALL_USER_ID;
        this.ALL_LABEL = ALL_LABEL;
        this.ALL_STATUS = ALL_STATUS;
    }

    public AllStatus(int ALL_ID, String ALL_USER_ID, String ALL_LABEL, String ALL_STATUS) {
        this.ALL_ID = ALL_ID;
        this.ALL_USER_ID = ALL_USER_ID;
        this.ALL_LABEL = ALL_LABEL;
        this.ALL_STATUS = ALL_STATUS;
    }

    public int getALL_ID() {
        return ALL_ID;
    }

    public void setALL_ID(int ALL_ID) {
        this.ALL_ID = ALL_ID;
    }

    public String getALL_USER_ID() {
        return ALL_USER_ID;
    }

    public void setALL_USER_ID(String ALL_USER_ID) {
        this.ALL_USER_ID = ALL_USER_ID;
    }

    public String getALL_LABEL() {
        return ALL_LABEL;
    }

    public void setALL_LABEL(String ALL_LABEL) {
        this.ALL_LABEL = ALL_LABEL;
    }

    public String getALL_STATUS() {
        return ALL_STATUS;
    }

    public void setALL_STATUS(String ALL_STATUS) {
        this.ALL_STATUS = ALL_STATUS;
    }

    @Override
    public String toString() {
        return "AllStatus{" +
                "ALL_ID='" + ALL_ID + '\'' +
                ", ALL_USER_ID='" + ALL_USER_ID + '\'' +
                ", ALL_LABEL='" + ALL_LABEL + '\'' +
                ", ALL_STATUS='" + ALL_STATUS + '\'' +
                '}';
    }
}
