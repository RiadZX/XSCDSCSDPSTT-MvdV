import utils.ChatGpt;
import org.junit.Test;

public class ChatGptTest {
    @Test
    public void testGetResponse() {
        ChatGpt chatGpt = new ChatGpt();
        String response = chatGpt.getResponse("""
                def quicksort(array, low=0, high=None):
                    if high is None:
                        high = len(array) - 1
                         
                    if low < high:
                        pivot_index = partition(array, low, high)
                        quicksort(array, low, pivot_index-1)
                        quicksort(array, pivot_index+1, high)
                """);
        System.out.println(response);
         System.out.println(ChatGpt.extractMessageFromJSONResponse(response));
        assert true;
    }
}
