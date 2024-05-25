import services.ChatGPTAI;
import org.junit.Test;

public class ChatGPTAITest {
    @Test
    public void testGetResponse() {
        ChatGPTAI chatGPTAI = new ChatGPTAI();
        String response = chatGPTAI.getResponse("Hello");
        assert response.equals("Hello, I am ChatGPTAI. I am a chatbot that uses the GPT-3 model to generate responses. I am still learning, so please be patient with me!");
        assert true;
    }
}
