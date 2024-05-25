package utils;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class ChatGpt {

    private static final String url =  "https://api.openai.com/v1/chat/completions";
    private static final String apiKey = "sk-proj-PNaJCdlWEdGZjDMdYHAGT3BlbkFJmS3oZtrGmvdQT6YHJiUA";
    private static final String model = "gpt-4o";

    private static final String systemPrompt =
            "You are a time complexity analysis tool. You are asked to analyze the time complexity of the following code. Respond with a time complexity, no extra content, only the time complexity." +
            "Example Answer: O(n^2), O(n log n), O(1), etc. Nothing else.";

    public ChatGpt() {

    }

    public void askPrompt(String prompt) {
        String response = getResponse(prompt);
        System.out.println(extractMessageFromJSONResponse(response));
    }

    /**
     * Returns the prompt in JSON.
     * @param prompt the prompt to send.
     * @return the response in JSON.
     */
    private String getResponse(String prompt) {
        try {
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setRequestProperty("Content-Type", "application/json");

            // The request body
            String body = generateBody(systemPrompt, prompt, model);

            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            // Response from ChatGPT
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            StringBuilder response = new StringBuilder();

            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            // calls the method to extract the message.
            return response.toString();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateBody(String systemPrompt, String prompt, String model) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(new JSONObject(new HashMap<String, String>() {{
            put("role", "system");
            put("content", systemPrompt);
        }}));
        jsonArray.add(new JSONObject(new HashMap<String, String>() {{
            put("role", "user");
            put("content", prompt);
        }}));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("model", model);
        jsonObject.put("messages", jsonArray);

        return jsonObject.toJSONString();
    }

    private String extractMessageFromJSONResponse(String response) {
        int start = response.indexOf("content")+ 11;

        int end = response.indexOf("\"", start);

        return response.substring(start, end);

    }

}

