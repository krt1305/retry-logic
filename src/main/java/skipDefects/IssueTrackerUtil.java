package skipDefects;

import com.mashape.unirest.http.Unirest;

public class IssueTrackerUtil {
    private static final String ISSUE_TRACKER_API_BASE_URL = "https://jira.atlassian.com/rest/api/latest/issue/";
    private static final String issueID="383";

    public static IssueStatus getStatus(String issueID) {
        String githubIssueStatus = null;
        try {
            githubIssueStatus = Unirest.get(ISSUE_TRACKER_API_BASE_URL.concat(issueID))
                    .asJson()
                    .getBody().getObject()
                    .getJSONObject("fields").getJSONObject("status")
                    .getString("name")
                    .toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return IssueStatus.valueOf(githubIssueStatus);
    }

}
