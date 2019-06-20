package au.gov.ato.dis.signalrchatjavaclient;

import java.util.Scanner;

import com.microsoft.signalr.Action2;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.*;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * 
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        System.out.println("Enter the URL of the SignalR Chat you want to join (leave BLANK for 'http://localhost:41877/chatHub').");
        Scanner reader = new Scanner(System.in); // Reading from System.in

        try {
            
            String input;
            String name;
            input = reader.nextLine();
            if (input.length()==0) { input = "http://localhost:41877/chatHub";}

            HubConnection hubConnection = HubConnectionBuilder.create(input).build();
            //HubConnection hubConnection = HubConnectionBuilder.create("http://localhost:50933/signalr/hubs").build();
        
            System.out.println("Built Hub Connection");

            // Receive Message
            //Action2<String, String> actionReceive = (user, message) -> System.out.println(user + " says " + message);
            //hubConnection.on("broadcastMessage", actionReceive, String.class, String.class);

            //This is a blocking call
            hubConnection.start().blockingAwait();

            System.out.println("Enter Name (leave BLANK for 'Javier'):");
            name = reader.nextLine();
            if(name.length()==0) { name = "Javier";}


            System.out.println("Hello " + name + ", please enter your message, or type 'leave' to exit.");
    
            while (!input.equals("leave")){
                input = reader.nextLine();
                hubConnection.send("SendMessage", "Javier",input);
            }
    
            hubConnection.stop();
        } catch (Throwable t) {
            System.out.println(t.toString());
        } finally {
            reader.close();
        }
    }
}
