package Dao;

import model.Agent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AgentDao {
    private final Map<String, Agent> agentStorage = new ConcurrentHashMap<>();
    private static AgentDao instance;

    private AgentDao() {}

    public static AgentDao getInstance() {
        if (instance == null) {
            instance = new AgentDao();

            return instance;
        }
        return instance;
    }

    public void saveAgent(Agent agent) {
        agentStorage.put(agent.getId(), agent);
    }

    public Agent getAgentById(String agentId) {
        return agentStorage.get(agentId);
    }

    public Map<String, Agent> getAllAgents() {
        return agentStorage;
    }

    public void deleteAgent(String agentId) {
        agentStorage.remove(agentId);
    }
}
