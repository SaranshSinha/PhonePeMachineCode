package Dao;

import model.Issue;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IssueDao {
    private final Map<String, Issue> issueStorage = new ConcurrentHashMap<>();
    private static IssueDao instance;

    private IssueDao() {

    }

    public static IssueDao getInstance() {
        if (instance == null) {
            instance = new IssueDao();
            return instance;
        }
        return instance;
    }

    public void saveIssue(Issue issue) {
        issueStorage.put(issue.getId(), issue);
    }

    public Issue getIssueById(String issueId) {
        return issueStorage.get(issueId);
    }

    public Map<String, Issue> getAllIssues() {
        return issueStorage;
    }

    public void deleteIssue(String issueId) {
        issueStorage.remove(issueId);
    }
}
