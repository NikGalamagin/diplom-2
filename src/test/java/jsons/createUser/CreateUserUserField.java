package jsons.createUser;

public class CreateUserUserField {
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CreateUserUserField(String email, String name) {
        this.email = email;
        this.name = name;
    }
    public CreateUserUserField() {

    }

    String email;
    String name;

}
