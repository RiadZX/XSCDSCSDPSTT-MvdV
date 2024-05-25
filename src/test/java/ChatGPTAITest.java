import services.ChatGPTAI;
import org.junit.Test;

public class ChatGPTAITest {
    @Test
    public void testGetResponse() {
        ChatGPTAI chatGPTAI = new ChatGPTAI();
        String response = chatGPTAI.getResponse("""
                def quicksort(array, low=0, high=None):
                    if high is None:
                        high = len(array) - 1
                         
                    if low < high:
                        pivot_index = partition(array, low, high)
                        quicksort(array, low, pivot_index-1)
                        quicksort(array, pivot_index+1, high)
                """);
        System.out.println(response);
         System.out.println(ChatGPTAI.extractMessageFromJSONResponse(response));
        assert true;
    }
}
