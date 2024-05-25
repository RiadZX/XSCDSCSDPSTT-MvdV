import services.ChatGPTAI;
import org.junit.Test;

public class ChatGPTAITest {
    @Test
    public void testGetResponse() {
        ChatGPTAI chatGPTAI = new ChatGPTAI();
        String response = chatGPTAI.getResponse("Hello");
        System.out.println(ChatGPTAI.extractMessageFromJSONResponse(response));
        assert true;
    }
}
