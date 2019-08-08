package apps.lonewolf.howl;

public class UserObject {
    private String phone,name;

    public UserObject(String phone,String name){
        this.name = name;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }
}
