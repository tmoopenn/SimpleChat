package SimpleChatServer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;


public class Channel {
	
	//instance variables
	 /**
	   * The thread group associated with client threads. Each member of the
	   * thread group is a <code> ConnectionToClient </code>.
	   */
	private ArrayList<ConnectionToClient> clients;
	
	/**
	 * Name of the channel 
	 */
	private String channelName;
	
	/**
	 * Server to which the channel belongs
	 * 
	 */
	private EchoServer1 server;
	
	public Channel(String channelName, EchoServer1 thisServer, ArrayList<ConnectionToClient> myClients) {
		server = thisServer;
		this.channelName = channelName; 
		clients = myClients;
	}
	
	public Channel(String stringFromUser, EchoServer1 thisServer) {
		channelName = setupChannelName(stringFromUser);
		String[] users = parseChannelUsers(stringFromUser);
		Thread[] allClients = thisServer.getClientConnections();
		clients = new ArrayList<ConnectionToClient>(users.length);
		setupChannelUsers(users, allClients);

	}
	
	/**
	 * 
	 * @param users Array of type String of usernames 
	 * @param allClients Array of all ConnectionToClient clients
	 */
	private void setupChannelUsers(String[] users, Thread[] allClients) {
		 for (int i = 0; i<users.length; i++) {
			String user = users[i];
			for (int k =0; k < allClients.length; k++) {
				ConnectionToClient client = (ConnectionToClient) allClients[k];
				String username = (String) client.getInfo("id");
				if (user == null) 
					break; 
				else {
					if (user.equals(username)) {
					clients.add (client);
					break;
					}
				}
			}	
		}
	
	}
	
	
	/**
	 * Parses the string from the user for the name of the channel and returns it.
	 * @param stringFromUser
	 * @return names of the channel
	 */
	private String setupChannelName(String stringFromUser) {
		int comma = stringFromUser.indexOf(',');
		String channelName = stringFromUser.substring(0,comma);
		return channelName;
	}
	
	/**
	 * Takes the string from a user and parses it for the username of each user 
	 * within the string.
	 * @param stringFromUser 
	 * @return array of strings with usernames 
	 */
	private String[] parseChannelUsers(String stringFromUser) { //(stringFromUser: channelName, user1, user2...
		int index = stringFromUser.indexOf(","); //start after first comma, string before first comma should be channel name
		String[] users = new String[15];
		int count = 0;
		while (index<stringFromUser.lastIndexOf(",") ) {
			int end = stringFromUser.indexOf(",", index+1);
			String user = stringFromUser.substring(index+1, end);
			users[count] = user;
			count++;
			index = end;
		}
		if (index != stringFromUser.length()) //must be one more user eg. user1, user2, user 3 ->user 3 left out of while loop
			users[count] = stringFromUser.substring(index+1, stringFromUser.length());
		return users;
	}
	
	public void addClient (ConnectionToClient client) {
		clients.add(client);
	}
	
	public void removeClient (ConnectionToClient client){
		clients.remove(client);
		server.serverUI().display(client + "has been removed from " + channelName);
	}
	
	public void removeClient(String client) {
		for (int i=0; i<clients.size(); i++) {
			String username = (String) clients.get(i).getInfo("id");
			if (username.equals(client)) {
				clients.remove(clients.get(i));
				server.serverUI().display(client + " has been removed from " + channelName);
				return;
			}
		}
	}
	
	public String getChannelName() {
		return channelName;
	}
	
	public int numOfClients(){
		return clients.size();
	}
	
	public EchoServer1 getServer() {
		return server;
	}
	
	public Object[] enumerateClients() {
		Object[] newArray = clients.toArray();//Arrays.copyOf(clients, clients.length);
		return newArray;
	}
	
}
