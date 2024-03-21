package org.nomadenjoyers.minestack.openstack;

import com.google.gson.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;

public class Nova {

    public static String BASE_URL = "http://localhost:8774/v2.1";
    public static String IMAGE_ID = "66d85733-1234-43fd-afef-c7cbfef1699f";
    public static String FLAVOR_REF = "1";
    public static String NETWORK_ID = "18edcf42-e172-4d32-a5d1-c6231376f59d";

    public static String createVm(String keystoneToken, String vmName) {
        String url = BASE_URL + "/servers";
        String payload = createVmPayload(vmName);
        HttpResponse response = sendPostRequest(url, keystoneToken, payload);

        if (response == null) {
            return "";
        }


        return extractValueFromResponse(response);
    }

    public static Integer destroyVM(String keystoneToken, String vmID) {
        String url = BASE_URL + "/servers/" + vmID;
        HttpResponse response = sendDeleteRequest(url, keystoneToken);

        if (response.getStatusLine().getStatusCode() == 204) {
            return 1;
        } else {
            return -1;
        }
    }

    private static HttpResponse sendPostRequest(String url, String token, String jsonData) {
        HttpClient client = HttpClientBuilder.create().build();
        try {
            HttpPost req = new HttpPost(url);
            StringEntity params = new StringEntity(jsonData);
            req.addHeader("content-type", "application/json");
            req.addHeader("X-Auth-Token", token);
            req.addHeader("X-Subject-Token", token);
            req.setEntity(params);
            HttpResponse response = client.execute(req);
            return response;
        } catch (Exception e) {
            return null;
        }
    }

    private static HttpResponse sendDeleteRequest(String url, String token) {
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpDelete req = new HttpDelete(url);
            req.addHeader("X-Auth-Token", token);
            req.addHeader("X-Subject-Token", token);
            HttpResponse response = client.execute(req);
            return response;
        } catch (Exception e) {
            return null;
        }
    }

    public static String extractValueFromResponse(HttpResponse response) {
        try {
            String jsonString = EntityUtils.toString(response.getEntity());
            JsonElement jelement = new JsonParser().parse(jsonString);
            JsonObject  jobject = jelement.getAsJsonObject();
            JsonObject serverObject = jobject.getAsJsonObject("server");
            String result = serverObject.get("id").getAsString();
            return result;
        } catch (Exception e) {
            return "";
        }
    }

    private static String createVmPayload(String vmName) {
        JsonObject vmData = new JsonObject();

        JsonArray networks = new JsonArray();

        JsonObject network = new JsonObject();
        network.addProperty("uuid", NETWORK_ID);
        networks.add(network);
        vmData.add("networks", networks);

        vmData.addProperty("flavorRef", FLAVOR_REF);
        vmData.addProperty("imageRef", IMAGE_ID);
        vmData.addProperty("name", vmName);

        JsonObject payload = new JsonObject();
        payload.add("server", vmData);

        System.out.println(new Gson().toJson(payload));
        return new Gson().toJson(payload);
    }
}
