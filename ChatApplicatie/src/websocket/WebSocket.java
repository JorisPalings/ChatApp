package websocket;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@ServerEndpoint("/websocket")
public class WebSocket {
	
	private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());
	
	@OnOpen
	public void onOpen(Session session) {
		
		sessions.add(session);
	}
	
	@OnMessage
	public void onMessage(String message, Session session) throws JsonParseException, JsonMappingException, IOException {
		
		sendMessageToAll(message);
		
	}
	
	private void sendMessageToAll(String message) {

		for(Session s: sessions){
			try {
				s.getBasicRemote().sendText(message);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@OnError
	public void onError(Session session, Throwable error) {
		
		// Chrome triggers onError rather than onClose when the window is closed
		sessions.remove(session);
		
	}
	
	@OnClose
	public void onClose(Session session) {
		
		sessions.remove(session);
		
	}
	
}