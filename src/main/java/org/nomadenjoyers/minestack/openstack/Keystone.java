package org.nomadenjoyers.minestack.openstack;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class Keystone {

    public static String BASE_URL = "http://...:5000/v3";

    public static String getToken(String username, String password)  {
        String url = BASE_URL + "/auth/tokens";
        String payload = createAuthPayload(username, password);
        HttpResponse response = sendPostRequest(url, payload);

        if (response == null) {
            return "";
        }

        if (response.getStatusLine().getStatusCode() != 200) {
            return "";
        }
        return response.getFirstHeader("X-Subject-Token").getValue();
    }

    private static HttpResponse sendPostRequest(String url, String jsonData) {
        HttpClient client = HttpClientBuilder.create().build();
        try {
            HttpPost req = new HttpPost(url);
            StringEntity params = new StringEntity(jsonData);
            req.addHeader("content-type", "application/json");
            req.setEntity(params);
            HttpResponse response = client.execute(req);
            return response;
        } catch (Exception e) {
            return null;
        }
    }

    private static String createAuthPayload(String u, String p) {
        JsonObject authData = new JsonObject();

        JsonObject identity = new JsonObject();

        JsonArray methods = new JsonArray();
        methods.add("password");
        identity.add("methods", methods);

        JsonObject password = new JsonObject();

        JsonObject user = new JsonObject();
        user.addProperty("name", u);

        JsonObject domain = new JsonObject();
        domain.addProperty("id", "default");

        user.add("domain", domain);
        user.addProperty("password", p);

        password.add("user", user);

        identity.add("password", password);

        authData.add("identity", identity);

        JsonObject payload = new JsonObject();
        payload.add("auth", authData);

        System.out.println(new Gson().toJson(payload));
        return new Gson().toJson(payload);
    }
}
