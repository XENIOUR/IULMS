package dataBase;

public class Profile {
    private int id;
    private String name, email, pass;
    private boolean gender, admin;

    public Profile() {
        this.id = 0;
        this.name = "";
        this.email = "";
        this.pass = "";
        this.gender = true;
        this.admin = false;
    }

    public Profile(int id, String name, String email, String pass, boolean gender, boolean admin) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.gender = gender;
        this.admin = admin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
