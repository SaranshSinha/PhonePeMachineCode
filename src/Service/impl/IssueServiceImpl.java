package Service.impl;

import Dao.AgentDao;
import Dao.IssueDao;
import exception.InvalidInputException;
import model.Agent;
import model.Issue;
import model.IssueType;
import Service.interfaces.AssignmentStrategy;
import Service.interfaces.IssueServiceInterface;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class IssueServiceImpl implements IssueServiceInterface {
    private final IssueDao issueDAO;
    private final AgentDao agentDAO;
    private final Map<String, List<Issue>> agentWorkHistory = new ConcurrentHashMap<>();
    private final AssignmentStrategy assignmentStrategy;
    private final AtomicInteger issueCounter = new AtomicInteger(0);

    public IssueServiceImpl(AssignmentStrategy assignmentStrategy) {
        this.agentDAO = new AgentDao();
        this.issueDAO = new IssueDao();
        this.assignmentStrategy = assignmentStrategy;
    }

    @Override
    public String createIssue(IssueType type, String subject, String description, String email) throws InvalidInputException {
        if (type == null || subject == null || description == null || email == null) {
            throw new InvalidInputException("All fields are required to create an issue.");
        }

        String issueId = "ISSUE-" + issueCounter.incrementAndGet();
        Issue issue = new Issue(issueId, type, subject, description, email);
        issueDAO.saveIssue(issue);
        return issueId;
    }

    @Override
    public void assignIssue(String issueId, List<Agent> agents) throws InvalidInputException {
        Issue issue = issueDAO.getIssueById(issueId);
        if (issue == null) {
            throw new InvalidInputException("Issue not found with ID: " + issueId);
        }

        if (!"CREATED".equals(issue.getStatus())) {
            throw new InvalidInputException("Issue cannot be assigned unless it's in CREATED state.");
        }

        Agent assignedAgent = assignmentStrategy.assignAgent(issue, agents);
        if (assignedAgent == null) {
            throw new InvalidInputException("No suitable agent found for the issue.");
        }

        issue.setAssignedAgentId(assignedAgent.getId());
        issue.setStatus("ASSIGNED");

        agentWorkHistory.putIfAbsent(assignedAgent.getId(), new ArrayList<>());
        agentWorkHistory.get(assignedAgent.getId()).add(issue);

        assignedAgent.setAvailable(false);
        issueDAO.saveIssue(issue);
    }

    @Override
    public void updateIssue(String issueId, String status, String resolution) throws InvalidInputException {
        if (issueId == null || issueId.isEmpty()) {
            throw new InvalidInputException("Issue ID cannot be null or blank.");
        }
        if (status == null || status.isEmpty()) {
            throw new InvalidInputException("Status cannot be null or blank.");
        }

        Issue issue = issueDAO.getIssueById(issueId);
        if (issue == null) {
            throw new InvalidInputException("No issue found with ID: " + issueId);
        }

        issue.setStatus(status);
        if (resolution != null && !resolution.isEmpty()) {
            issue.setResolution(resolution);
        }

        if ("RESOLVED".equalsIgnoreCase(status)) {
            String agentId = issue.getAssignedAgentId();
            if (agentId != null) {
                List<Issue> agentIssues = agentWorkHistory.get(agentId);

                boolean hasPendingIssues = agentIssues.stream()
                        .anyMatch(i -> !"RESOLVED".equalsIgnoreCase(i.getStatus()));
                if (!hasPendingIssues) {
                    agentIssues.forEach(i -> {
                        if (i.getAssignedAgentId().equals(agentId)) {
                            i.setStatus("RESOLVED");
                        }
                    });
                }
            }
        }
    }

    @Override
    public void resolveIssue(String issueId, String resolution) throws InvalidInputException {
        if (issueId == null || issueId.isEmpty()) {
            throw new InvalidInputException("Issue ID cannot be null or blank.");
        }
        if (resolution == null || resolution.isEmpty()) {
            throw new InvalidInputException("Resolution cannot be null or blank.");
        }

        Issue issue = issueDAO.getIssueById(issueId);
        if (issue == null) {
            throw new InvalidInputException("No issue found with ID: " + issueId);
        }

        if (!"ASSIGNED".equals(issue.getStatus()) && !"IN_PROGRESS".equals(issue.getStatus())) {
            throw new InvalidInputException("Cannot resolve an issue that is not in ASSIGNED or IN_PROGRESS state.");
        }

        issue.setStatus("RESOLVED");
        issue.setResolution(resolution);

        String agentId = issue.getAssignedAgentId();
        if (agentId != null) {
            List<Issue> agentIssues = agentWorkHistory.get(agentId);

            boolean hasPendingIssues = agentIssues.stream()
                    .anyMatch(i -> !"RESOLVED".equals(i.getStatus()));
            if (!hasPendingIssues) {
                agentIssues.forEach(i -> {
                    if (i.getAssignedAgentId().equals(agentId)) {
                        i.setStatus("RESOLVED");
                    }
                });
            }
        }
    }

    @Override
    public List<Issue> searchIssuesByEmail(String email) throws InvalidInputException {
        if (email == null || email.isEmpty()) {
            throw new InvalidInputException("Email cannot be null or blank.");
        }

        return issueDAO.getAllIssues().values().stream()
                .filter(issue -> email.equals(issue.getEmail()))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<Issue>> getAgentWorkHistory() {
        return agentWorkHistory;
    }
}
