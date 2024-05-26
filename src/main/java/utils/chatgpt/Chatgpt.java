package utils.chatgpt;

import com.intellij.openapi.components.Service;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import toolWindow.ComplexityWindowFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@Service(Service.Level.PROJECT)
public class Chatgpt {

    private static final String url =  "https://api.openai.com/v1/chat/completions";
    private static final String apiKey = "sk-proj-PNaJCdlWEdGZjDMdYHAGT3BlbkFJmS3oZtrGmvdQT6YHJiUA";
    private static final String model = "gpt-4o";


    public Chatgpt() {

    }

    /**
     * Returns the prompt in JSON.
     * @param prompt the prompt to send.
     * @return the response in JSON.
     */
    public String getResponse(String systemPrompt, String prompt) {
        ComplexityWindowFactory.ComplexityWindow.displayLoad();
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
            ComplexityWindowFactory.ComplexityWindow.undisplayLoad();
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
}

