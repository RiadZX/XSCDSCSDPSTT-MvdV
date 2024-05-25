package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChatGPTAI {

    /**
     * Returns the prompt in JSON.
     * @param prompt the prompt to send.
     * @return the response in JSON.
     */
    public String getResponse(String prompt) {
        String url = "https://api.openai.com/v1/chat/completions";
        String apiKey = "sk-proj-PNaJCdlWEdGZjDMdYHAGT3BlbkFJmS3oZtrGmvdQT6YHJiUA";
        String model = "gpt-4o";

        try {
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setRequestProperty("Content-Type", "application/json");
            String systemPrompt = "You are a time complexity analysis tool. You are asked to analyze the time complexity of the following code. Respond with a time complexity, no extra content, only the time complexity." +
                    "Example Answer: O(n^2)";
            // The request body
            String body = generatePayload(systemPrompt, prompt, model);

            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            // Response from ChatGPT
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            StringBuffer response = new StringBuffer();

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

    public String generatePayload(String systemPrompt, String prompt, String model) {
        //online the prompt
        prompt = prompt.replace("\n", "\\n");
        return "{\n" +
                "  \"model\": \"" + model + "\",\n" +
                "  \"messages\": [\n" +
                "    {\n" +
                "      \"role\": \"system\",\n" +
                "      \"content\": \"" + systemPrompt + "\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"role\": \"user\",\n" +
                String.format("      \"content\": \"%s\"\n}]}", prompt);
    }

    public static String extractMessageFromJSONResponse(String response) {
        int start = response.indexOf("content")+ 11;

        int end = response.indexOf("\"", start);

        return response.substring(start, end);

    }

}

