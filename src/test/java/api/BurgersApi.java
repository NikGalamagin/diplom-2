package api;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.List;
import jsons.ingredients.Ingredients;
import jsons.login.LoginResponseModel;
import jsons.orders.OrdersModel;
import jsons.userModel.UserModel;
import jsons.userModel.UserModelResponse;
import jsons.createUser.CreateUserModel;
import jsons.login.LoginModel;

public class BurgersApi {

    private Gson gson = new Gson();

    public static String BASE_URL = "https://stellarburgers.nomoreparties.site/api/";

    public static String CREATE_USER_ENDPOINT = BASE_URL + "auth/register";
    public static String LOGIN_ENDPOINT = BASE_URL + "auth/login";
    public static String USER_ENDPOINT = BASE_URL + "auth/user";
    public static String ORDERS_ENDPOINT = BASE_URL + "orders";
    public static String INGREDIENTS_ENDPOINT = BASE_URL + "ingredients";

    public Response createUser(CreateUserModel createUserModel) {
        return RestAssured
                .given()
                .header("Content-type", "application/json")
                .and()
                .log()
                .all()
                .body(createUserModel)
                .when()
                .post(CREATE_USER_ENDPOINT);
    }

    public LoginResponseModel login(LoginModel loginModel) {
        Response response = RestAssured
                .given()
                .header("Content-type", "application/json")
                .and()
                .log()
                .all()
                .body(loginModel)
                .when()
                .post(LOGIN_ENDPOINT);

        return gson.fromJson(response.asString(), LoginResponseModel.class);
    }

    public Response user(UserModel userModel, String token) {
        return RestAssured
                .given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .and()
                .log()
                .all()
                .body(userModel)
                .when()
                .patch(USER_ENDPOINT);
    }

    public Response deleteUser(String token) {
        return RestAssured
                .given()
                .header("Authorization", token)
                .and()
                .log()
                .all()
                .when()
                .delete(USER_ENDPOINT);
    }

    public Response createOrder(Ingredients ingredients, String token) {
        return RestAssured
                .given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .and()
                .log()
                .all()
                .body(ingredients)
                .when()
                .get(ORDERS_ENDPOINT);
    }

    public Response orders(String token) {
        return RestAssured
                .given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .and()
                .log()
                .all()
                .when()
                .get(ORDERS_ENDPOINT );
    }

    public Response ingridientsInfo() {
        return RestAssured
                .given()
                .and()
                .log()
                .all()
                .when()
                .get(INGREDIENTS_ENDPOINT);

    }

}
