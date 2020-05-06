import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import skipDefects.Issue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;


public class TestRetryLogic {

    @Test(enabled = true)
    public void testAssertionFailureAndCaptureTime() {

        Instant start = Instant.now();
        RestAssured.baseURI = "https://restcountries.eu/rest/v2/name/1";
        System.out.println("URI " + RestAssured.baseURI);
        //Define the specification of request. Server is specified by baseURI above.
        RequestSpecification httpRequest = RestAssured.given();
        //Makes calls to the server using Method type.
        Response response = httpRequest.request(Method.GET);
        // CODE HERE
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toSeconds();
        System.out.println("Time elapsed in seconds-- " + timeElapsed);
        //Checks the Status Code
        int statusCode = response.getStatusCode();
        System.out.println("Response is " + response.getBody().asString());
        Assert.assertEquals(statusCode, 200);

        // https://javarevisited.blogspot.com/2012/04/how-to-measure-elapsed-execution-time.html#ixzz6LYrIYUiT


    }

    @Test(dataProvider = "employeeData", enabled = false)
    public void testEmployeeDataProvider(String employeeId) {
        RestAssured.baseURI = "http://dummy.restapiexample.com/api/v1/employee/" + employeeId;
        System.out.println("URI " + RestAssured.baseURI);

        //Define the specification of request. Server is specified by baseURI above.
        RequestSpecification httpRequest = RestAssured.given();

        //Makes calls to the server using Method type.
        Response response = httpRequest.request(Method.GET);

        //Checks the Status Code
        int statusCode = response.getStatusCode();
        System.out.println("Response is " + response.getBody().asString());
        Assert.assertEquals(statusCode, 200);


    }


    @Test(dataProvider = "countryData", enabled = false)
    public void testCountryDataProvider(String countryName) {
        RestAssured.baseURI = "https://restcountries.eu/rest/v2/name/" + countryName;
        System.out.println("URI " + RestAssured.baseURI);

        //Define the specification of request. Server is specified by baseURI above.
        RequestSpecification httpRequest = RestAssured.given();

        //Makes calls to the server using Method type.
        Response response = httpRequest.request(Method.GET);

        //Checks the Status Code
        int statusCode = response.getStatusCode();
        System.out.println("Response is " + response.getBody().asString());
        Assert.assertEquals(statusCode, 200);


    }

    @Test(enabled = false)
    public void testSocketTimeOutException() {
        String myUrl = "https://google.com/";
        try {
            String results = crunchifyCallURL(myUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Issue("JRA-9")
    @Test(enabled = false)
    public void testDefect() {

    }

    @Issue("JRA-9")
    @Test(enabled = false)
    public void unknownHostException() throws IOException {
        String hostname = "http://locaihost";
        URL url = new URL(hostname);
        HttpURLConnection con = null;
        con = (HttpURLConnection) url.openConnection();
        con.getResponseCode();

    }

    @Test(enabled = false)
    public void authenticationFailureException() throws IOException {
        String hostname = "http://dummy.restapiexample.com/api/v1/employee/1900000";
        URL url = new URL(hostname);
        HttpURLConnection con = null;
        con = (HttpURLConnection) url.openConnection();
        con.getResponseCode();
        System.out.println("Response code is " + con.getResponseCode());


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

}

