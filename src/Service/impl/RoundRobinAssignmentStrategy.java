package Service.impl;

import model.Agent;
import model.Issue;
import Service.interfaces.AssignmentStrategy;

import java.util.List;

public class RoundRobinAssignmentStrategy implements AssignmentStrategy {
    private int lastAssignedIndex = -1;

    @Override
    public Agent assignAgent(Issue issue, List<Agent> agents) {
        if (agents == null || agents.isEmpty()) {
            return null;
        }

        int agentCount = agents.size();
        for (int i = 0; i < agentCount; i++) {
            lastAssignedIndex = (lastAssignedIndex + 1) % agentCount;
            Agent agent = agents.get(lastAssignedIndex);

            if (agent.isAvailable() && agent.getSupportedIssueTypes().contains(issue.getType())) {
                return agent;
            }
        }

        return null; // No suitable agent found
    }
}
