
import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.webhook.Payload;
import com.github.seratch.jslack.api.webhook.WebhookResponse;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import skipDefects.Issue;

import javax.net.ssl.SSLProtocolException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;


public class TestRetryLogic {

    String urlSlackWebHook = "https://hooks.slack.com/services/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";

    @Test(enabled = false, dataProvider = "countryData")
    public void testAssertionFailureAndCaptureTime(String countryName) {

        Instant start = Instant.now();
        RestAssured.baseURI = "https://restcountries.eu/rest/v2/name/" + countryName;
        RequestSpecification httpRequest = RestAssured.given();
        Response response = httpRequest.request(Method.GET);
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toSeconds();
        System.out.println("Time elapsed in seconds-- " + timeElapsed);
        int statusCode = response.getStatusCode();
        System.out.println("Response is " + response.getBody().asString());
        Assert.assertEquals(statusCode, 200);

        // https://javarevisited.blogspot.com/2012/04/how-to-measure-elapsed-execution-time.html#ixzz6LYrIYUiT


    }

    @Test(dataProvider = "employeeData", enabled = false)
    public void testEmployeeDataProvider(String employeeId) {
        RestAssured.baseURI = "http://dummy.restapiexample.com/api/v1/employee/" + employeeId;
        RequestSpecification httpRequest = RestAssured.given();
        Response response = httpRequest.request(Method.GET);
        int statusCode = response.getStatusCode();
        System.out.println("Response is " + response.getBody().asString());
        Assert.assertEquals(statusCode, 200);


    }

    @Test(dataProvider = "countryData", enabled = false)
    public void testCountryDataProvider(String countryName) {
        RestAssured.baseURI = "https://restcountries.eu/rest/v2/name/" + countryName;
        RequestSpecification httpRequest = RestAssured.given();
        Response response = httpRequest.request(Method.GET);
        int statusCode = response.getStatusCode();
        System.out.println("Response is " + response.getBody().asString());
        Assert.assertEquals(statusCode, 200);


    }

    @Test(enabled = false)
    public void testSocketTimeOutException() throws Exception {
        String myUrl = "https://google.com/";
        String results = null;
        results = crunchifyCallURL(myUrl);
        System.out.println("Results is " + results);
    }

    @Test(enabled = false) // no config found case- default logic
    public void testSSLProtocolException() throws SSLProtocolException {

        throw new SSLProtocolException("SSL protocol Exception");

    }

    @Issue("JRA-9")
    @Test(enabled = false)
    public void testDefect() {

    }

    @Issue("JRA-9")
    @Test(enabled = true)
    public void unknownHostException() throws IOException {
        String hostname = "http://locaihost";
        URL url = new URL(hostname);
        HttpURLConnection con = null;
        con = (HttpURLConnection) url.openConnection();
        con.getResponseCode();

    }

    @Test(enabled = false)
    public void authenticationFailureException() {
        String hostname = "http://dummy.restapiexample.com/api/v1/employee/1900000";
        URL url = null;
        try {
            url = new URL(hostname);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            con.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("Response code is " + con.getResponseCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Assert.assertEquals(con.getResponseCode(), 200);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Test(enabled = false)
    public void connectionException() throws InterruptedException, IOException {
        Socket socket = null;
        try {
            Thread.sleep(3000);
            socket = new Socket("localhost", 3333);
            PrintWriter outWriter = new PrintWriter(socket.getOutputStream(), true);
            outWriter.println("Hello Mr. Server!");
        } finally {

            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

       /* ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        AsyncRetryExecutor executor = new AsyncRetryExecutor(scheduler).
                retryOn(Exception.class).
                withExponentialBackoff(500, 2).withMaxRetries(2);
        executor.
                getWithRetry(() -> new Socket("localhost", 3333)).
                thenAccept(socket -> System.out.println("Connected! " + socket));*/
    }


    @Test(enabled = false)
    public void testAPIGatewayException() {
        //https://httpstat.us/504?sleep=5000
        RestAssured.baseURI = "https://httpstat.us/504?sleep=5000";
        RequestSpecification httpRequest = RestAssured.given();
        Response response = httpRequest.request(Method.GET);
        int statusCode = response.getStatusCode();
        System.out.println("Response is " + response.getBody().asString());
        Assert.assertEquals(statusCode, 200);
    }

    private String crunchifyCallURL(String crunchifyURL) throws Exception {
        URL crunchURL = null;
        BufferedReader crunchReader = null;
        StringBuilder crunchBuilder;

        try {
            // create the HttpURLConnection
            crunchURL = new URL(crunchifyURL);
            HttpURLConnection connection = (HttpURLConnection) crunchURL.openConnection();

            // Let's make GET call
            connection.setRequestMethod("GET");

            // Current Timeout 10 milliseconds - to generate Timeout Error
            connection.setReadTimeout(10);
            connection.connect();

            // Simply read result and print line
            crunchReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            crunchBuilder = new StringBuilder();

            String eachLine = null;
            while ((eachLine = crunchReader.readLine()) != null) {
                crunchBuilder.append(eachLine + "\n");
            }
            return crunchBuilder.toString();

        } catch (Exception et) {
            et.printStackTrace();
            throw et;
        } finally {
            if (crunchReader != null) {

                try {
                    crunchReader.close();

                } catch (IOException ioException) {
                    ioException.printStackTrace();

                }
            }
        }
    }

    @DataProvider(name = "employeeData")
    public Object[][] getData() {
        return new Object[][]
                {
                        {"1"},
                        {"20002"},
                        {"2"}
                };

    }

    @DataProvider(name = "countryData")
    public Object[][] getCountryData() {
        return new Object[][]
                {
                        {"1"},
                        {"Colombia"},
                        {"Estonia"}

                };

    }

    @AfterSuite
    public void detectDeadlock() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] threadIds = threadMXBean.findDeadlockedThreads();
        boolean deadlock = threadIds != null && threadIds.length > 0;
        System.out.println("Deadlocks found " + deadlock);

      /*  WebHookToken token = WebHookToken.fromString("<token>");
        MessageRequest message = MessageRequest.builder()
                .text("Hello, Slack!")
                .username("roboslack")
                .channel("#your-channel")
                .build();
        ResponseCode response =
                SlackWebHookService.with(token).sendMessage(message);*/

     /*   Slack slack = Slack.getInstance();
        ChatPostMessageResponse response = slack.methods(token).chatPostMessage(req -> req
                .channel("C1234567")
                .text("Write one, post anywhere"));
        if (response.isOk()) {
            MessageChangedEvent.Message postedMessage = response.getMessage();
        } else {
            String errorCode = response.getError(); // e.g., "invalid_auth", "channel_not_found"
        }*/

        //https://app.slack.com/client/T0130GYN79R/C012YV1U2G5
        String message = "sample message";
        Payload payload = Payload.builder().
                channel("#general").username("Bot").iconEmoji(":rocket").text(message).build();
        WebhookResponse webhookResponse = null;
        try {
            webhookResponse = Slack.getInstance().send(urlSlackWebHook, payload);
            System.out.println("Slack code ->" + webhookResponse.getCode());
            System.out.println("Slack body ->" + webhookResponse.getBody());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}

