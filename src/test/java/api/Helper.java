package api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.restassured.response.Response;

public class Helper {

    public String getSomeIngredient(int numberOfIngredient) {
        BurgersApi burgersApi = new BurgersApi();
        Response response = burgersApi.ingridientsInfo();
        String jsonString = response.asString();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        JsonArray dataArray = jsonObject.getAsJsonArray("data");
        JsonObject firstElement = dataArray.get(numberOfIngredient).getAsJsonObject();
        return firstElement.get("_id").getAsString();
    }

}
