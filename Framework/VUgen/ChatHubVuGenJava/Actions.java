/*
 * LoadRunner Java script. (Build: _build_number_)
 * 
 * Script Description: 
 * 
 * List of Names from: http://listofrandomnames.com/
 */

import lrapi.lr;

import com.github.signalr4j.client.UpdateableCancellableFuture;
import com.github.signalr4j.client.hubs.HubConnection;
import com.github.signalr4j.client.hubs.HubProxy;
import com.github.signalr4j.client.hubs.SubscriptionHandler2;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Actions
{
	public String SERVER_ADDRESS = "";
	public String SERVER_URL = "";
	public String HUB_NAME = "";

	public ConcurrentLinkedQueue<String> messages = new ConcurrentLinkedQueue<String>();
	
	public int init() throws Throwable {
		lr.output_message("Init");
		
		SERVER_ADDRESS = lr.eval_string("{ServerAddress}");
		SERVER_URL = lr.eval_string("{Protocol}") + SERVER_ADDRESS + "/";
		HUB_NAME = lr.eval_string("{HubName}");
		
		lr.output_message(SERVER_ADDRESS);
		lr.output_message(SERVER_URL);
		lr.output_message(HUB_NAME);
		
		return 0;
	}//end of init


	public int action() throws Throwable {
		lr.output_message("Action");
		
		HubConnection connection = new HubConnection(SERVER_URL, true);

        HubProxy proxy = connection.createHubProxy(HUB_NAME);

        UpdateableCancellableFuture ucFuture = (UpdateableCancellableFuture) connection.start();
        ucFuture.get();

        lr.output_message("Connected");

        proxy.on("broadcastMessage",
                new SubscriptionHandler2<String, String>() {
                    @Override
                    public void run(String user, String message) {
                    	
                    	// Write the messages into a queue, which we can output later.
                    	// VuGen Java doesn't handle outputing from a different thread
                    	// for asynchronous operation. Need to poll / loop for the expected
                    	// value.
                    	
                        //lr.output_message(user + " says " + message);
                        //System.out.println(user + " says " + message);
                        messages.add(user + " says " + message);
                    }
                },
                String.class,
                String.class
                );

        lr.output_message("Proxy Created");

        for (int i = 0; i < 5; i++) {
        
        	proxy.invoke("send", lr.eval_string("{Name}"), lr.eval_string("{Quote}"));
        	lr.think_time(5.0);
        	
        	while(!messages.isEmpty()) {
        		lr.output_message(messages.poll());
        	}
        	
        }

        connection.disconnect();

        lr.output_message("Disconnected.");
		
		return 0;
	}//end of action


	public int end() throws Throwable {
		lr.output_message("End");
		return 0;
	}//end of end
}
