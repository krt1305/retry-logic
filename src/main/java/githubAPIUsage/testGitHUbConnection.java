package githubAPIUsage;

import com.mashape.unirest.http.Unirest;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class testGitHUbConnection {
    private static final String ISSUE_TRACKER_API_BASE_URL = "https://api.github.com/repos/authorjapps/zerocode/";


    @Test
    public void getIssueStatus() {

        String ISSUES_URL = "issues/";
        String githubIssueStatus = "CLOSED";
        try {
            githubIssueStatus = Unirest.get(ISSUE_TRACKER_API_BASE_URL.concat(ISSUES_URL).concat("396"))
                    .asJson()
                    .getBody()
                    .getObject()
                    .getString("state")
                    .toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Issue status is " + githubIssueStatus);

    }

    @Test
    public void getCommitComments() {
        String COMMENTS_URL = "/comments";
        String ISSUES_URL = "issues/";
        List<String> comments = new ArrayList<>();
        System.out.println("URL Is " + ISSUE_TRACKER_API_BASE_URL.concat(ISSUES_URL).concat("396").concat(COMMENTS_URL));
        String comment = null;
        try {
            comment = Unirest.get(ISSUE_TRACKER_API_BASE_URL.concat(ISSUES_URL).concat("396").concat(COMMENTS_URL))
                    .asJson()
                    .getBody().getArray().get(0).toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Comments are---");
        System.out.println(comment);
        for (String s : comments) {
            System.out.println(s);
        }

    }

    @Test
    public void test() {
        RestAssured.baseURI = ISSUE_TRACKER_API_BASE_URL;
        String COMMENTS_URL = "/comments";
        String ISSUES_URL = "issues/";
        RequestSpecification httpRequest = RestAssured.given();
        Response response = httpRequest.request(Method.GET, ISSUES_URL.concat("396").concat(COMMENTS_URL));
        String responseBody = response.getBody().asString();
        List<String> comments= Arrays.asList(responseBody);

        System.out.println("Response Body is =>  " + responseBody);
        System.out.println("Comment is "+comments.get(0));

    }
}
