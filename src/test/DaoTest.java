package test;

import Dao.IssueDao;
import model.Issue;
import model.IssueType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DaoTest {
    @Test
    public void testSaveAndRetrieveIssue() {
        IssueDao issueDAO = IssueDao.getInstance();
        Issue issue = new Issue("ISSUE-1", IssueType.PAYMENT, "Payment Issue", "Details", "user1@example.com");

        issueDAO.saveIssue(issue);
        Issue retrieved = issueDAO.getIssueById("ISSUE-1");

        assertNotNull(retrieved);
        assertEquals("ISSUE-1", retrieved.getId());
    }
}
