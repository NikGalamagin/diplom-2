import api.BurgersApi;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import jsons.createUser.CreateUserModel;
import jsons.login.LoginModel;
import jsons.login.LoginResponseModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest {

    BurgersApi burgersApi = new BurgersApi();
    CreateUserModel createUserModel;
    LoginModel loginModel;
    CreateUserTest createUserTest = new CreateUserTest();
    LoginResponseModel loginResponseModel;
    String randomEmail;
    String randomPassword;


    @Test
    @DisplayName("Авторизация пользователя")
    @Step("Авторизация пользователя")
    public void loginTest() {

        createUserTest.getRandomCredentials();
        randomEmail = createUserTest.getRandomEmail();
        randomPassword = createUserTest.getRandomPassword();
        String randomName = createUserTest.getRandomName();

        createUserModel = new CreateUserModel(randomEmail, randomPassword, randomName);
        Response createResponse = burgersApi.createUser(createUserModel);
        createResponse.then().log().all().statusCode(200);

        loginModel = new LoginModel(randomEmail, randomPassword);
        loginResponseModel = burgersApi.login(loginModel);
        assertTrue(loginResponseModel.isSuccess(), "Не удалось авторизоваться");
    }

    @Test
    @DisplayName("Авторизация пользователя с некорректным паролем")
    @Step("Авторизация пользователя с некорректным паролем")
    public void loginWrongCredentialsTest() {
        String wrongPassword = "wrongPassword123";

        createUserTest.getRandomCredentials();
        randomEmail = createUserTest.getRandomEmail();
        randomPassword = createUserTest.getRandomPassword();
        String randomName = createUserTest.getRandomName();

        createUserModel = new CreateUserModel(randomEmail, randomPassword, randomName);
        Response createResponse = burgersApi.createUser(createUserModel);
        createResponse.then().log().all().statusCode(200);

        loginModel = new LoginModel(randomEmail, wrongPassword);
        loginResponseModel = burgersApi.login(loginModel);
        assertFalse(loginResponseModel.isSuccess(), "Удалось авторизоваться");
    }

    @AfterEach
    @DisplayName("Удаление пользователя")
    @Step("Удаление пользователя")
    public void deleteUser() {
        loginModel = new LoginModel(randomEmail, randomPassword);
        loginResponseModel = burgersApi.login(loginModel);
        String token = loginResponseModel.getAccessToken();
        burgersApi.deleteUser(token);

    }

}
