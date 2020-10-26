package lk.iot.lmsrealtime1.model;


public class Profile {

    private String fName;
    private String phone;
    private String city;
    private String street;
    private String province;
    private String zip;
    private String email;
    private String isActive;


    public Profile() {
    }

    public Profile(String fName, String phone, String city, String street, String province, String zip, String email,String isActive) {
        this.fName = fName;
        this.phone = phone;
        this.city = city;
        this.street = street;
        this.province = province;
        this.zip = zip;
        this.email = email;
        this.isActive = isActive;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setActive(String active) {
        isActive = active;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "fName='" + fName + '\'' +
                ", phone='" + phone + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", province='" + province + '\'' +
                ", zip='" + zip + '\'' +
                ", email='" + email + '\'' +
                ", isActive='" + isActive + '\'' +
                '}';
    }
}
