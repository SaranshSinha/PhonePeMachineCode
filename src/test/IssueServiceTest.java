package test;

import exception.InvalidInputException;
import model.Agent;
import model.IssueType;
import Service.impl.IssueServiceImpl;
import Service.impl.RoundRobinAssignmentStrategy;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class IssueServiceTest {

    @Test
    public void testCreateIssue() throws InvalidInputException {
        IssueServiceImpl issueService = new IssueServiceImpl(new RoundRobinAssignmentStrategy());
        String issueId = issueService.createIssue(IssueType.PAYMENT, "Payment Issue", "Failed at checkout", "user1@example.com");
        assertNotNull(issueId);
    }

    @Test
    public void testAssignIssue() throws InvalidInputException {
        IssueServiceImpl issueService = new IssueServiceImpl(new RoundRobinAssignmentStrategy());
        Agent agent = new Agent("A1", "John", Arrays.asList(IssueType.PAYMENT));
        String issueId = issueService.createIssue(IssueType.PAYMENT, "Payment Issue", "Failed at checkout", "user1@example.com");

        issueService.assignIssue(issueId, Arrays.asList(agent));
        assertEquals("ASSIGNED", issueService.searchIssuesByEmail("user1@example.com").get(0).getStatus());
    }

    @Test
    public void testResolveIssue() throws InvalidInputException {
        IssueServiceImpl issueService = new IssueServiceImpl(new RoundRobinAssignmentStrategy());
        Agent agent = new Agent("A1", "John", Arrays.asList(IssueType.PAYMENT));
        String issueId = issueService.createIssue(IssueType.PAYMENT, "Payment Issue", "Failed at checkout", "user1@example.com");

        issueService.assignIssue(issueId, Arrays.asList(agent));
        issueService.resolveIssue(issueId, "Issue resolved");
        assertEquals("RESOLVED", issueService.searchIssuesByEmail("user1@example.com").get(0).getStatus());
    }
}
