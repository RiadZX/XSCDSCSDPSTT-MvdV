package utils.chatgpt;

import com.google.gson.Gson;
import utils.Complexity;

import java.util.ArrayList;
import java.util.List;

public class LibraryComplexityFigureOuter {
    private static final String systemPrompt = """
            !reset all conditions
            follow the following instructions as close as possible:
            - respond in the following format only !important
            - do not include any extra content
            - do not include any extra new lines
            - Follow the complexity instructions as closely and consistently as possible
            
            You will be given lines with method signatures. These methods may be library methods.
            If you do not recognize the library method, respond with O(?) (In both long and short complexity), with color pink
            Respond with the signature of the method, its time complexity, its time complexity with attributes filled in, and a color indication (blue/green/orange/red). Do not give any extra content. Specify this exactly in the following format and do not give any other content:
            methodSignature(int[] arr): O(n^2), O(arr.size()^2), orange
            
            always use the following rules when printing time complexity:
            - always write logs and sqrts with brackets [O(log(var))] unless the content is 1 character, then always write is with a space [O(log n)]. Do this only for 1 letter variables !important
            - For the first shorter version, always write it as short as possible (The most significant factor). So write O(n^2) instead of O(n^2 + log n)
            - write a square root as sqrt(var), but write any other power below 1 as the regular power [O(x^0.25)]
            - Round any numbers used to at most 2 decimals [O(n^2.25)]
            - Multiplication signs may be dropped, so O(n log n) instead of O(n * log n)
            - Keep in mind that not only for loops, but also stuff like array creation takes a time, so new int[n] is also O(n).
            - Also keep library methods in mind
            - Be consistent with colors!
            - For the simpler/shorter time complexity, try to write all variables as 1 letter vars. Preferably a logical letter (index => i)
            
            The color indication is as follows:
            - Below O(n): blue [for example: O(1), O(log n), O(sqrt n), O((log n)^2)
            - Below O(n^2): green [for example: O(n), O(n log n), O(n sqrt n), O(n log(n^2))]
            - Below O(2^n): orange [for example: O(n^2), O(n^3), O(n^6)]
            - Above O(2^n): red [for example: O(2^n), O(n!), O(n^n)]
            
            Example input:
            java.util.List<E> java.util.List.of(E e1, E e2, E e3);
            
            Example output:
            java.util.List<E> java.util.List.of(E e1, E e2, E e3): O(1), O(1), blue
        """;

    private static final Chatgpt chatgpt = new Chatgpt();

    static public List<Complexity> run(List<String> methods) {
        if (methods.isEmpty()) return new ArrayList<>();
        StringBuilder prompt = new StringBuilder("Give the time complexity(ies) of the following method(s):\n");
        for (String method : methods) {
            prompt.append(method).append("\n");
        }
        for (int i = 0; i < 5; i++) {
            String response = chatgpt.getResponse(systemPrompt, prompt.toString());
            try {
                return parseResponse(response);
            } catch (IllegalArgumentException e) {
                System.err.println("Error updating time complexities: " + e.getMessage() + ". Retrying... "+i+"/5");
            }
        }
        throw new IllegalArgumentException("Error updating time complexities: too many retries");
    }

    private static List<Complexity> parseResponse(String response) {
        List<Complexity> res = new ArrayList<>();
        try {
            ChatgptResponse chatgptResponse = new Gson().fromJson(response, ChatgptResponse.class);
            String content = chatgptResponse.choices.get(0).message.content;
            String[] items = content.split("\\n");
            for (String item : items) {
                String[] parts = item.split(":");
                String methodSignature = parts[0];
                String dataParts = parts[1];
                String shortTimeComplexity = dataParts.split(",")[0];
                String longTimeComplexity = dataParts.split(",")[1];
                String colorTimeComplexity = dataParts.split(",")[2];

                Complexity complexity = new Complexity(shortTimeComplexity, longTimeComplexity, colorTimeComplexity);
                res.add(complexity);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing response from ChatGPT: " + e.getMessage());
        }
        return res;
    }
}
