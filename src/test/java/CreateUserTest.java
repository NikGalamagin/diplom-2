import api.BurgersApi;
import io.qameta.allure.Step;
import io.restassured.internal.common.assertion.Assertion;
import io.restassured.response.Response;
import java.util.Random;
import jsons.createUser.CreateUserModel;
import jsons.login.LoginModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class CreateUserTest {

    private static boolean userCreated = false;
    BurgersApi burgersApi = new BurgersApi();
    CreateUserModel createUserModel;
    LoginModel loginModel;
    Random random = new Random();
    String randomEmail;
    String randomPassword;
    String randomName;

    @BeforeEach
    public void setUp() {
        getRandomCredentials();
    }

    @Test
    @DisplayName("Создание пользователя")
    @Tag("createUser")
    @Step("Создание пользователя")
    public void createUserTest() {
        createUserModel = new CreateUserModel(randomEmail, randomPassword, randomName);

        Response response = burgersApi.createUser(createUserModel);
        response.then().log().all().statusCode(200);
        userCreated = true;
    }

    @Test
    @DisplayName("Создание зарегистрированного пользователя")
    @Tag("createUser")
    @Step("Создание зарегистрированного пользователя")
    public void createRegisteredUserTest() {
        createUserModel = new CreateUserModel(randomEmail, randomPassword, randomName);

        Response response = burgersApi.createUser(createUserModel);
        response.then().log().all().statusCode(200);
        Response cloneResponse = burgersApi.createUser(createUserModel);
        cloneResponse.then().log().all().statusCode(403);
        userCreated = true;
    }

    @Test
    @DisplayName("Создание пользователя без логина")
    @Step("Создание пользователя без логина")
    public void createUserWithoutLoginTest() {
        createUserModel = new CreateUserModel(randomEmail, randomPassword, null);

        Response response = burgersApi.createUser(createUserModel);
        response.then().log().all().statusCode(403);

    }

    public void getRandomCredentials() {
        randomEmail = 10000 + random.nextInt(10000) + "@yandex.ru";
        randomPassword = "password" + 10000 + random.nextInt(10000);
        randomName = "login" + 10000 + random.nextInt(10000);
    }

    public String getRandomEmail() {
        return randomEmail;
    }

    public String getRandomPassword() {
        return randomPassword;
    }

    public String getRandomName() {
        return randomName;
    }

    @AfterEach
    @DisplayName("Удаление юзера")
    @Step("Удаление пользвателя")
    public void deleteUser() {
        if (userCreated) {
            loginModel = new LoginModel(randomEmail, randomPassword);
            String token = burgersApi.login(loginModel).getAccessToken();
            burgersApi.deleteUser(token);
        }
    }
}