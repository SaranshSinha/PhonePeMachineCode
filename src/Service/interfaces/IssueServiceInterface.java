package Service.interfaces;

import exception.InvalidInputException;
import model.Agent;
import model.Issue;
import model.IssueType;

import java.util.List;
import java.util.Map;

public interface IssueServiceInterface {
    String createIssue(IssueType type, String subject, String description, String email) throws InvalidInputException;
    void assignIssue(String issueId, List<Agent> agents) throws InvalidInputException;
    void updateIssue(String issueId, String status, String resolution) throws InvalidInputException;
    void resolveIssue(String issueId, String resolution) throws InvalidInputException;
    List<Issue> searchIssuesByEmail(String email) throws InvalidInputException ;
    Map<String, List<Issue>> getAgentWorkHistory() throws InvalidInputException;
}