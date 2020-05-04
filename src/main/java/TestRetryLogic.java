import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import skipDefects.Issue;
import skipDefects.MethodIssueStatusListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;


public class TestRetryLogic {

    @Test(enabled = false)
    public void testAssertionFailure() {
        Assert.assertFalse(true);

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
    public void testDefect()
    {

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
}
