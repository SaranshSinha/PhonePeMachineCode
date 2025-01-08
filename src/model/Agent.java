package model;

import java.util.List;

public class Agent {
    private String id;
    private String name;
    private List<IssueType> supportedIssueTypes;
    private boolean isAvailable = true;

    public Agent(String id, String name, List<IssueType> supportedIssueTypes) {
        this.id = id;
        this.name = name;
        this.supportedIssueTypes = supportedIssueTypes;
    }

    public String getId() {
        return id;
    }

    public List<IssueType> getSupportedIssueTypes() {
        return supportedIssueTypes;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return name + " (" + id + ")";
    }
}
