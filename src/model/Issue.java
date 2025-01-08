package model;

public class Issue {
    private String id;
    private IssueType type;
    private String subject;
    private String description;
    private String email;
    private String status = "CREATED";
    private String assignedAgentId;
    private String resolution;

    public Issue(String id, IssueType type, String subject, String description, String email) {
        this.id = id;
        this.type = type;
        this.subject = subject;
        this.description = description;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public IssueType getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignedAgentId() {
        return assignedAgentId;
    }

    public void setAssignedAgentId(String assignedAgentId) {
        this.assignedAgentId = assignedAgentId;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    @Override
    public String toString() {
        return "Issue{id='" + id + "', type=" + type + ", status=" + status + "}";
    }

    public String getEmail() {
        return email;
    }
}
