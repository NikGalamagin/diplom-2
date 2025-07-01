import api.BurgersApi;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import jsons.createUser.CreateUserModel;
import jsons.login.LoginModel;
import jsons.login.LoginResponseModel;
import jsons.userModel.UserModel;
import jsons.userModel.UserModelResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest {

    BurgersApi burgersApi = new BurgersApi();
    CreateUserModel createUserModel;
    LoginModel loginModel;
    CreateUserTest createUserTest = new CreateUserTest();
    UserModel userModel;
    String token;
    private boolean userCreated = true;

    @Test
    @DisplayName("Изменение данных без авторизации")
    @Step("Изменение данных без авторизации")
    public void changeDataWithoutAuthTest() {
        userCreated = false;
        createUserTest.getRandomCredentials();
        String randomEmail = createUserTest.getRandomEmail();
        String randomPassword = createUserTest.getRandomPassword();
        String randomName = createUserTest.getRandomName();

        createUserModel = new CreateUserModel(randomEmail, randomPassword, randomName);
        Response createResponse = burgersApi.createUser(createUserModel);

        createResponse.then().log().all().statusCode(200);

        userModel = new UserModel("salt" + randomEmail, randomName + "salt");
        Response userResponse = burgersApi.user(userModel, " ");
        userResponse.then().log().all().statusCode(401);
        UserModelResponse userModelResponse = burgersApi.user(userModel, " ").as(UserModelResponse.class);

        Assertions.assertFalse(userModelResponse.getSuccess());

    }

    @Test
    @DisplayName("Изменение данных")
    @Step("Изменение данных")
    public void changeDataTest() {

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

        userModel = new UserModel("salt" + randomEmail, randomName + "salt");
        Response userResponse = burgersApi.user(userModel, token);
        userResponse.then().log().all().statusCode(200);

        UserModelResponse userModelResponse = burgersApi.user(userModel, token).as(UserModelResponse.class);
        Assertions.assertTrue(userModelResponse.getSuccess());
    }

    @AfterEach
    @DisplayName("Удаление юзера")
    @Step("Удаление юзера")
    public void deleteUser() {
        if (userCreated) {
            burgersApi.deleteUser(token);
        }
    }
}
