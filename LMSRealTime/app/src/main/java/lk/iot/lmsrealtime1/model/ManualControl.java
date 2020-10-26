package lk.iot.lmsrealtime1.model;

public class ManualControl {

    private String M_ID;
    private String M_USER_ID;
    private String M_LABEL;
    private String M_STATUS;
    private String M_CATEGORY_ID;

    public ManualControl() {
    }


    public ManualControl(String m_USER_ID, String m_LABEL, String m_CATEGORY_ID) {
        M_USER_ID = m_USER_ID;
        M_LABEL = m_LABEL;
        M_CATEGORY_ID = m_CATEGORY_ID;
    }

    public ManualControl(String m_USER_ID, String m_LABEL, String m_STATUS, String m_CATEGORY_ID) {

        M_STATUS = m_STATUS;
        M_USER_ID = m_USER_ID;
        M_LABEL = m_LABEL;
        M_CATEGORY_ID = m_CATEGORY_ID;
    }

    public String getM_CATEGORY_ID() {
        return M_CATEGORY_ID;
    }

    public void setM_CATEGORY_ID(String m_CATEGORY_ID) {
        M_CATEGORY_ID = m_CATEGORY_ID;
    }

    public String getM_ID() {
        return M_ID;
    }

    public void setM_ID(String m_ID) {
        M_ID = m_ID;
    }

    public String getM_USER_ID() {
        return M_USER_ID;
    }

    public void setM_USER_ID(String m_USER_ID) {
        this.M_USER_ID = m_USER_ID;
    }

    public String getM_LABEL() {
        return M_LABEL;
    }

    public void setM_LABEL(String m_LABEL) {
        M_LABEL = m_LABEL;
    }

    public String getM_STATUS() {
        return M_STATUS;
    }

    public void setM_STATUS(String m_STATUS) {
        M_STATUS = m_STATUS;
    }

    @Override
    public String toString() {
        return "ManualControl{" +
                "M_ID='" + M_ID + '\'' +
                ", M_USER_ID='" + M_USER_ID + '\'' +
                ", M_LABEL='" + M_LABEL + '\'' +
                ", M_STATUS='" + M_STATUS + '\'' +
                '}';
    }
}
