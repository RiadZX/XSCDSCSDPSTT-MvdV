package utils.chatgpt;

import com.google.gson.Gson;
import utils.Complexity;
import utils.GroupInfo;
import utils.MethodInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeComplexityUpdater {
    private static final String systemPrompt = """
            You are a time complexity analysis tool.
            You must analyze the time complexity of the provided methods.
            Respond with the signature of the method, its time complexity, its time complexity with attributes filled in, and a color indication (blue/green/orange/red). Do not give any extra content. Specify this exactly in the following format and do not give any other content:
            methodSignature(int[] arr): O(n^2), O(arr^2), orange
            
            Example input:
            public static void selectionSort(int[] arr) {
                int n = getArrLength(arr);
                for (int i = 0; i < n - 1; i++) {
                    int minIndex = i;
                    for (int j = i + 1; j < n; j++) {
                        if (arr[j] < arr[minIndex]) {
                            minIndex = j;
                        }
                    }
                    // Swap the found minimum element with the first
                    // element of the unsorted part
                    int temp = arr[minIndex];
                    arr[minIndex] = arr[i];
                    arr[i] = temp;
                }
            }
            public void traversePreOrderWithoutRecursion() {
                Stack<Node> stack = new Stack<Node>();
                Node current = root;
                stack.push(root);
                while(!stack.isEmpty()) {
                    current = stack.pop();
                    visit(current.value);
                        
                    if(current.right != null) {
                        stack.push(current.right);
                    }
                    if(current.left != null) {
                        stack.push(current.left);
                    }
                }
            }
                        
            Output:
            selectionSort(int[] arr): O(n^2), O(arr^2), orange
            traversePreOrderWithoutRecursion: O(1), O(1), green
    """;
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
//        List<GroupInfo> children = group.getChildren();
//        for(GroupInfo child : children){
//            List<MethodInfo> methodInfo = child.getMethods();
//            for(MethodInfo method : methodInfo){
//                MethodInfo name = method;
//                Complexity compl = Complexity.fromString(method.getTimeComplexity().getLongComplexity());
//                res.put(name, compl);
//            }
//        }
        return res;
    }

    private static void parseResponse(GroupInfo groupInfo, String response) {
        try {
            ChatgptResponse chatgptResponse = new Gson().fromJson(response, ChatgptResponse.class);
            String content = chatgptResponse.choices.get(0).message.content;
            String[] items = content.split("\\n");
            for (String item : items) {
                String[] parts = content.split(":");
                String methodSignature = parts[0];
                String dataParts = parts[1];
                String shortTimeComplexity = dataParts.split(",")[0];
                String longTimeComplexity = dataParts.split(",")[1];
                String colorTimeComplexity = dataParts.split(",")[2];

                Complexity complexity = Complexity.fromString(shortTimeComplexity, longTimeComplexity, colorTimeComplexity);
                MethodInfo methodInfo = groupInfo.getMethodBySignature(methodSignature);
                methodInfo.setTimeComplexity(complexity);
            }
        } catch (Exception e) {
            System.err.println("Error parsing response from ChatGPT: " + e.getMessage());
        }
    }
}
