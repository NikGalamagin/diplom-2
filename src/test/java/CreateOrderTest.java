import api.BurgersApi;
import api.Helper;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jsons.createUser.CreateUserModel;
import jsons.ingredients.Ingredients;
import jsons.login.LoginModel;
import jsons.login.LoginResponseModel;
import jsons.orders.CreateOrderResponseModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateOrderTest {

    BurgersApi burgersApi = new BurgersApi();
    Helper helper = new Helper();
    String ingredientIdOne;
    String ingredientIdTwo;
    CreateUserModel createUserModel;
    LoginModel loginModel;
    CreateUserTest createUserTest = new CreateUserTest();
    String token;
    private boolean userCreated = true;


    @Test
    @DisplayName("Проверка создания заказа с авторизацией")
    @Step("Проверка создания заказа с авторизацией")
    public void createOrderWithAuthTest() {

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

        ingredientIdOne = helper.getSomeIngredient(0);
        ingredientIdTwo = helper.getSomeIngredient(1);

        //создание заказа с полученным токеном
        List<String> ingredientsIds = Arrays.asList(ingredientIdOne, ingredientIdTwo);
        Ingredients ingredients = new Ingredients(ingredientsIds);
        Response response = burgersApi.createOrder(ingredients, token);
        response.then().log().all().statusCode(200);
        CreateOrderResponseModel createOrderResponseModel = burgersApi.createOrder(ingredients, token).as(CreateOrderResponseModel.class);

        assertTrue(createOrderResponseModel.isSuccess());
    }

    @Test
    @DisplayName("Проверка создания заказа без авторизации")
    @Step("Проверка создания заказа без авторизации")
    public void createOrderWithoutAuthTest() {

        userCreated = false;

        ingredientIdOne = helper.getSomeIngredient(0);
        ingredientIdTwo = helper.getSomeIngredient(1);

        List<String> ingredientsIds = Arrays.asList(ingredientIdOne, ingredientIdTwo);
        Ingredients ingredients = new Ingredients(ingredientsIds);
        CreateOrderResponseModel createOrderResponseModel = burgersApi.createOrder(ingredients, "failToken").as(CreateOrderResponseModel.class);
        System.out.println(createOrderResponseModel);

        assertFalse(createOrderResponseModel.isSuccess());
    }

    @Test
    @DisplayName("Проверка создания заказа с авторизацией без ингридиентов")
    @Step("Проверка создания заказа с авторизацией без ингридиентов")
    public void createOrderWithoutIngredientsTest() {

        //создание и авторизация для получения токена
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

        List<String> emptyList = new ArrayList<>();

        //создание заказа с полученным токеном

        Ingredients ingredients = new Ingredients(emptyList);
        Response response = burgersApi.createOrder(ingredients, token);
        response.then().log().all().statusCode(200);
        CreateOrderResponseModel createOrderResponseModel = burgersApi.createOrder(ingredients, token).as(CreateOrderResponseModel.class);

        assertTrue(createOrderResponseModel.isSuccess());
    }

    @Test
    @DisplayName("Проверка создания заказа с некорректным хешом ингридиента")
    @Step("Проверка создания заказа с некорректным хешом ингридиента")
    public void createOrderWithFalseIngredientsTest() {

        //создание и авторизация для получения токена
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

        List<String> falseIngredients = Arrays.asList("koko", "pipi", "zuzu");

        //создание заказа с полученным токеном

        Ingredients ingredients = new Ingredients(falseIngredients);
        Response response = burgersApi.createOrder(ingredients, token);
        response.then().log().all().statusCode(200);
        CreateOrderResponseModel createOrderResponseModel = burgersApi.createOrder(ingredients, token).as(CreateOrderResponseModel.class);

        assertTrue(createOrderResponseModel.isSuccess());
    }

    @AfterEach
    @Step("Удаление пользвателя")
    public void deleteUser() {
        if (userCreated) {
            burgersApi.deleteUser(token);
        }
    }
}
