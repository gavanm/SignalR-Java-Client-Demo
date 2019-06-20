package au.gov.au.SignalRFrameworkJavaClient;

import java.util.Scanner;

import com.github.signalr4j.client.UpdateableCancellableFuture;
import com.github.signalr4j.client.hubs.HubConnection;
import com.github.signalr4j.client.hubs.HubProxy;
import com.github.signalr4j.client.hubs.SubscriptionHandler2;

public class Chatter {

    /*
        Relying heavily on the signalr4j fork of the original Microsoft SignalR Java client.
        https://github.com/signalr4j/signalr4j
        https://github.com/signalr4j/signalr4j/blob/master/src/test/java/com/github/signalr4j/client/tests/realtransport/HubConnectionTests.java
        This project uses Gradle instead of Maven, as the original signalr microsoft java client provided Gradle examples/config.

        Reading this blog earlier would have been a big help:
        https://whatheco.de/2014/03/20/getting-started-with-the-java-signalr-sdk/
     */


    public static String SERVER_ADDRESS = "127.0.0.1:54952";
    public static String SERVER_URL = "http://" + SERVER_ADDRESS + "/";
    public static String HUB_URL = "http://" + SERVER_ADDRESS + "/signalr";
    public static String CONNECTION_QUERYSTRING = "myVal=1";
    public static String HUB_NAME = "ChatHub";

    public static void main(String[] args)
        throws Exception {

        log("Starting...");

        Scanner reader = new Scanner(System.in); // Reading from System.in

        HubConnection connection = new HubConnection(SERVER_URL, true);

        HubProxy proxy = connection.createHubProxy(HUB_NAME);

        UpdateableCancellableFuture ucFuture = (UpdateableCancellableFuture) connection.start();
        ucFuture.get();

        log("Connected.");

        proxy.on("broadcastMessage",
                new SubscriptionHandler2<String, String>() {
                    @Override
                    public void run(String user, String message) {
                        System.out.println(user + " says " + message);
                    }
                },
                String.class,
                String.class
                );

        log("Created proxy for receiving messages");

        System.out.println("Enter Name (leave BLANK for 'Javier'):");
        String name = reader.nextLine();
        if(name.length()==0) name = "Javier";

        proxy.invoke("send", name, "* I have joined the chat *");

        System.out.printf("Hello %s, please enter your message, or type 'leave' to exit.%n", name);

        String message = "";
        while (!message.equals("leave")){
            message = reader.nextLine();

            if(!message.equals("leave")) proxy.invoke("send", name,message);
        }

        connection.disconnect();

        log("Disconnected.");

        return;
    }

    public static void log(String message) {
        System.out.println(message);
    }
}
