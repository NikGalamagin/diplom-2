package jsons.orders;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jsons.login.User;
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateOrderResponseModel {
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CreateOrderResponseModel(boolean success, User user) {
        this.success = success;
        this.user = user;
    }
    public CreateOrderResponseModel() {

    }

    boolean success;
    User user;
    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
