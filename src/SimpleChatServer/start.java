package SimpleChatServer;

import java.io.IOException;

public class start extends ServerNotConnectedCommand{
	// TODO move messages to serverStarted
	public start(String str, EchoServer1 server){
		super(str, server);
	}//end start
	
	public void doCmd(){
		if (getServer().isClosed())
			getServer().serverStarted();		
		else 
			System.out.println("Server is not closed");
		
	}//end doCmb()
	
}//end class
