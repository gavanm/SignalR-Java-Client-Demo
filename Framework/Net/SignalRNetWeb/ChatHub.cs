using System;
using System.Web;
using Microsoft.AspNet.SignalR;

namespace SignalRNetWeb
{
    public class ChatHub : Hub
    {
        public void Send(string name, string message)
        {
            // Call the broadcastMessage method to update clients.
            Clients.All.broadcastMessage(name, message);
        }

        //public void Hello()
        //{
        //    Clients.All.hello();
        //}
    }
}