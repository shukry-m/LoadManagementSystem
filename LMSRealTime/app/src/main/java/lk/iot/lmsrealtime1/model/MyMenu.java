package lk.iot.lmsrealtime1.model;

public class MyMenu {
    private String menuName;
    private int menuImage;

    public MyMenu(String menuName, int menuImage) {
        this.menuName = menuName;
        this.menuImage = menuImage;
    }

    public MyMenu(){}

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getMenuImage() {
        return menuImage;
    }

    public void setMenuImage(int menuImage) {
        this.menuImage = menuImage;
    }

}
