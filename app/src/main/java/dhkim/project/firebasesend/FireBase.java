package dhkim.project.firebasesend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class FireBase {
    private String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
    private String SERVER_KEY = "YOUR_FCM_SERVER_KEY";
    private JSONObject root;

    public FireBase(String title, String message) throws JSONException {
        root = new JSONObject();
        JSONObject data = new JSONObject();
        data.put("title", title);
        data.put("message", message);
        root.put("data", data);
    }


    // Send to Topic
    public String sendToTopicMessage(String topic) throws Exception {
        System.out.println("Send to Topic");
        root.put("condition", "'"+topic+"' in topics");
        return sendPushMessage(true);
    }

    // SendToGroup
    public String sendToGroupMessage(JSONArray mobileTokens) throws Exception { // SEND TO GROUP OF PHONES - ARRAY OF TOKENS
        root.put("registration_ids", mobileTokens);
        return sendPushMessage(false);
    }

    // SendToToken
    public String sendToTokenMessage(String token) throws Exception {//SEND MESSAGE TO SINGLE MOBILE - TO TOKEN
        root.put("to", token);
        return sendPushMessage(false);
    }


    // Send Push Message
    private String sendPushMessage(boolean toTopic)  throws Exception {
        URL url = new URL(API_URL_FCM);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");

        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", "key=" + SERVER_KEY);

        System.out.println(root.toString());

        try {
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(root.toString());
            wr.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader( (conn.getInputStream())));

            String output;
            StringBuilder builder = new StringBuilder();
            while ((output = br.readLine()) != null) {
                builder.append(output);
            }
            System.out.println(builder);
            String result = builder.toString();

            JSONObject obj = new JSONObject(result);

            if(toTopic){
                if(obj.has("message_id")){
                    return  "SUCCESS";
                }
            } else {
                int success = Integer.parseInt(obj.getString("success"));
                if (success > 0) {
                    return "SUCCESS";
                }
            }

            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }
}
