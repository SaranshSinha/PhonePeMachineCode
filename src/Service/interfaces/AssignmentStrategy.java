package Service.interfaces;

import model.Agent;
import model.Issue;

import java.util.List;

public interface AssignmentStrategy {
    Agent assignAgent(Issue issue, List<Agent> agents);
}