package utils.chatgpt;

import com.google.gson.Gson;
import utils.Complexity;
import utils.GroupInfo;
import utils.MethodInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeComplexityUpdater {
    private static final String systemPrompt =
            "You are a time complexity analysis tool. You are asked to analyze the time complexity of the following code. Respond with a time complexity, together with the signature of the method. No extra content, only the time complexity." +
                    "Example Answer: MethodSignature(Args[] a): O(n^2), MethodSignature(Args[] a): O(n log n), MethodSignature(Args[] a): O(1), etc. Nothing else.";
    private Chatgpt chatgpt;

    public TimeComplexityUpdater() {
        this.chatgpt = new Chatgpt();
    }


    public void updateTimeComplexities(GroupInfo groupInfo) {
        String prompt = makePrompt(groupInfo);
        String response = chatgpt.getResponse(systemPrompt, prompt);
        parseReponse(response);
    }


    private static String makePrompt(GroupInfo group) {
        String prompt = "Give the time complexity(ies) of the following method(s):\n";
        int counter = 0;
        for(MethodInfo method : group.getMethods()){
            prompt += "Method " + counter++ + ": \n";
            prompt += method.getPsiElement().getText() + "\n";
        }

        Map<String, String> knownComplexities = associateComplexityToMethods(group);

        if(!knownComplexities.keySet().isEmpty()) {
            prompt += "The time complexities of the following methods are already known. Please use them: \n";
            for (String key : knownComplexities.keySet()) {
                prompt += "Method: " + key + " Complexity: " + knownComplexities.get(key) + "\n";
            }
        }

        return prompt;
    }

    private ChatgptResponse parseReponse(String response) {
        ChatgptResponse chatgptResponse = new Gson().fromJson(response, ChatgptResponse.class);

    }

    private static Map<String, Complexity> associateComplexityToMethods(GroupInfo group){
        Map<String, String> res = new HashMap<>();
        List<GroupInfo> children = group.getChildren();
        for(GroupInfo child : children){
            List<MethodInfo> methodInfo = child.getMethods();
            for(MethodInfo method : methodInfo){
                String name = method.getMethod().getText().substring(0, method.getMethod().getText().indexOf(')')+1);
                String compl = method.getTimeComplexity().getValue();
                res.put(name, compl);
            }
        }
        return res;
    }

    private static MethodInfo findMethodInfoAssociatedToMethodSignature(GroupInfo group){
        //TODO;
        return null;
    }
}
