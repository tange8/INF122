package main;

import java.util.ArrayList;
import java.util.List;

public class ScenarioPrompt {
    private String title;
    private String description;
    private ArrayList<String> options;

    public ScenarioPrompt(String title, String description, ArrayList<String> options) {
        this.title = title;
        this.description = description;
        this.options = options;
    }

    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public ArrayList<String> getOptions() {
        return options;
    }

    public String getPrompt() {
        StringBuilder prompt = new StringBuilder();
        prompt.append("--- ");
        prompt.append(title);
        prompt.append(" --- \n");
        prompt.append(description);
        prompt.append("\n");
        prompt.append("What will you do?");
        int i = 1;
        for (String option : options) {
            prompt.append(i);
            prompt.append(": ");
            prompt.append(option);
            prompt.append("\n");
            i++;
        }
        return prompt.toString();
    }
}
