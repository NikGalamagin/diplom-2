import api.BurgersApi;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import jsons.createUser.CreateUserModel;
import jsons.ingredients.Ingredients;
import jsons.login.LoginModel;
import jsons.login.LoginResponseModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrdersTest {

    BurgersApi burgersApi = new BurgersApi();
    CreateUserModel createUserModel;
    LoginModel loginModel;
    CreateUserTest createUserTest = new CreateUserTest();
    String token;
    Ingredients ingridsList;

    @Test
    @DisplayName("Получение заказов пользователя")
    @Step("Получение заказов пользователя\"")
    public void ordersTest() {
        createUserTest.getRandomCredentials();
        String randomEmail = createUserTest.getRandomEmail();
        String randomPassword = createUserTest.getRandomPassword();
        String randomName = createUserTest.getRandomName();

        createUserModel = new CreateUserModel(randomEmail, randomPassword, randomName);
        Response createResponse = burgersApi.createUser(createUserModel);
        createResponse.then().log().all().statusCode(200);

        loginModel = new LoginModel(randomEmail, randomPassword);
        LoginResponseModel loginResponseModel = burgersApi.login(loginModel);
        token = loginResponseModel.getAccessToken();

        Response response = burgersApi.orders(token);
        response.then().log().all().statusCode(200);
    }

    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    @Step("Получение заказов неавторизованного пользователя")
    public void ordersWithoutAuthTest() {
        createUserTest.getRandomCredentials();
        String randomEmail = createUserTest.getRandomEmail();
        String randomPassword = createUserTest.getRandomPassword();
        String randomName = createUserTest.getRandomName();

        createUserModel = new CreateUserModel(randomEmail, randomPassword, randomName);
        Response createResponse = burgersApi.createUser(createUserModel);
        createResponse.then().log().all().statusCode(200);

        loginModel = new LoginModel(randomEmail, randomPassword);
        LoginResponseModel loginResponseModel = burgersApi.login(loginModel);
        token = loginResponseModel.getAccessToken();

        Response response = burgersApi.orders(" ");
        response.then().log().all().statusCode(401);
    }


    @AfterEach
    @Step("Удаление пользователя")
    public void deleteUser() {
        burgersApi.deleteUser(token);
    }
}

