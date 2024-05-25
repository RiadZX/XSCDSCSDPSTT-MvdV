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
            "You are a time complexity analysis tool. " +
                    "You are asked to analyze the time complexity of the provided code. " +
                    "Respond with the signature of the method with its time complexity. Do not give any extra content." +
                    "Example:\\nMethodSignature(Args[] a): O(n^2)\\nMethodSignature(Args[] a): O(n log n)\\nMethodSignature(Args[] a): O(1)\\n, etc. Nothing else.";
    private static final Chatgpt chatgpt  = new Chatgpt();;



    public static void updateTimeComplexities(GroupInfo groupInfo) {
        String prompt = makePrompt(groupInfo);
        String response = chatgpt.getResponse(systemPrompt, prompt);
        parseResponse(groupInfo, response);
    }


    private static String makePrompt(GroupInfo group) {
        String prompt = "Give the time complexity(ies) of the following method(s):\n";
        int counter = 0;
        for(MethodInfo method : group.getMethods()){
            prompt += "Method " + counter++ + ": \n";
            prompt += method.getPsiElement().getText() + "\n";
        }

        Map<MethodInfo, Complexity> knownComplexities = associateComplexityToMethods(group);

        if(!knownComplexities.keySet().isEmpty()) {
            prompt += "The time complexities of the following methods are already known. Please use them: \n";
            for (MethodInfo meth : knownComplexities.keySet()) {
                prompt += "Method: " + meth.getMethod().getText().substring(0,meth.getMethod().getText().indexOf(')')+1) + " Complexity: " + knownComplexities.get(meth) + "\n";
            }
        }

        return prompt;
    }

    private static Map<MethodInfo, Complexity> associateComplexityToMethods(GroupInfo group){
        Map<MethodInfo, Complexity> res = new HashMap<>();
        List<GroupInfo> children = group.getChildren();
        for(GroupInfo child : children){
            List<MethodInfo> methodInfo = child.getMethods();
            for(MethodInfo method : methodInfo){
                MethodInfo name = method;
                Complexity compl = Complexity.fromString(method.getTimeComplexity().getValue());
                res.put(name, compl);
            }
        }
        return res;
    }

    private static void parseResponse(GroupInfo groupInfo, String response) {
        ChatgptResponse chatgptResponse = new Gson().fromJson(response, ChatgptResponse.class);
        String content = chatgptResponse.choices.get(0).message.content;
        String[] items = content.split("\\n");
        for (String item : items) {
            String[] parts = content.split(":");
            String methodSignature = parts[0];
            String timeComplexity = parts[1];

            Complexity complexity = Complexity.fromString(timeComplexity);
            MethodInfo methodInfo = groupInfo.getMethodBySignature(methodSignature);
            methodInfo.setTimeComplexity(complexity);
        }
    }
}
