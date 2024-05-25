package utils.chatgpt;

import com.google.gson.Gson;
import utils.Complexity;
import utils.GroupInfo;
import utils.MethodInfo;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TimeComplexityUpdater {
    private static final String systemPrompt = readFile();
    private static final Chatgpt chatgpt  = new Chatgpt();;

    private static String readFile() {
        try {
            Scanner sc = new Scanner(new FileReader("./systemPrompt.txt"));
            String fileContents = "";
            while (sc.hasNextLine()) {
                fileContents += sc.nextLine();
            }
            return fileContents;
        }
        catch(FileNotFoundException e){
            return "L bozo didn't find anything";
        }
    }

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
