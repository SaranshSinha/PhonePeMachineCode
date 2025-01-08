import Service.interfaces.AssignmentStrategy;
import Service.interfaces.IssueServiceInterface;
import exception.InvalidInputException;
import model.Agent;
import model.IssueType;
import Service.impl.IssueServiceImpl;
import Service.impl.RoundRobinAssignmentStrategy;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InvalidInputException {
        // Define agents and strategy
        Agent agent1 = new Agent("A1", "Ram", Arrays.asList(IssueType.PAYMENT, IssueType.INSURANCE));
        Agent agent2 = new Agent("A2", "Rahul", Arrays.asList(IssueType.INSURANCE, IssueType.GOLD));
        Agent agent3 = new Agent("A3", "Saransh", Arrays.asList(IssueType.PAYMENT, IssueType.MUTUAL_FUND));

        //TODO: Add a DAO layer for all CRUD related operations
        List<Agent> agents = Arrays.asList(agent1, agent2, agent3);

        AssignmentStrategy strategy = new RoundRobinAssignmentStrategy();
        IssueServiceInterface issueService = new IssueServiceImpl(strategy);

        // 1. Create issues
        String issueId1 = issueService.createIssue(IssueType.PAYMENT, "Failed Payment", "Payment failed at checkout.", "user1@example.com");
        String issueId2 = issueService.createIssue(IssueType.INSURANCE, "Policy Update Issue", "Unable to update policy.", "user2@example.com");
        String issueId3 = issueService.createIssue(IssueType.MUTUAL_FUND, "Gold Investment Issue", "Error in investment statement.", "user1@example.com");

        System.out.println("Created issues:");
        System.out.println("Issue 1 ID: " + issueId1);
        System.out.println("Issue 2 ID: " + issueId2);
        System.out.println("Issue 3 ID: " + issueId3);

        // 2. Assign issues to agents
        issueService.assignIssue(issueId1, agents);
        issueService.assignIssue(issueId2, agents);
        issueService.assignIssue(issueId3, agents);


        // 3. Update issue status
        issueService.updateIssue(issueId1, "IN_PROGRESS", null);
        issueService.updateIssue(issueId2, "IN_PROGRESS", null);
        issueService.updateIssue(issueId3, "IN_PROGRESS", null);
        issueService.resolveIssue(issueId1, "Refund processed successfully");
        issueService.resolveIssue(issueId2, "Policy updated successfully");

        System.out.println("\nIssue statuses:");
        System.out.println("Issue 1 status: " + issueService.searchIssuesByEmail("user1@example.com").get(1).getStatus());
        System.out.println("Issue 3 status: " + issueService.searchIssuesByEmail("user1@example.com").get(0).getStatus());
        System.out.println("Issue 2 status: " + issueService.searchIssuesByEmail("user2@example.com").get(0).getStatus());

        // 4. Agent's assigned issues
        System.out.println("\nAgent assigned issues:");
        System.out.println("Agent A1's assigned issues: " + issueService.getAgentWorkHistory().get("A1"));
        System.out.println("Agent A2's assigned issues: " + issueService.getAgentWorkHistory().get("A2"));
        System.out.println("Agent A3's assigned issues: " + issueService.getAgentWorkHistory().get("A3"));

        // 5. Agent work history
        System.out.println("\nAgent work history:");
        issueService.getAgentWorkHistory().forEach((agentId, issues) -> {
            System.out.println("Agent ID: " + agentId);
            issues.forEach(issue -> System.out.println("  Worked on issue: " + issue.getId() + ", Status: " + issue.getStatus()));
        });
    }
}