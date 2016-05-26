package domain;

import java.util.LinkedList;

public class Conversation {
	
	private Person user1, user2;
	private LinkedList<String> messages;
	
	public Conversation(Person user1, Person user2) {
		this.setUser1(user1);
		this.setUser2(user2);
		this.setMessages(new LinkedList<String>());
	}
	
	public Person getUser1() {
		return user1;
	}

	public Person getUser2() {
		return user2;
	}

	public LinkedList<String> getMessages() {
		return messages;
	}

	private void setUser1(Person user1) {
		this.user1 = user1;
	}

	private void setUser2(Person user2) {
		this.user2 = user2;
	}

	private void setMessages(LinkedList<String> messages) {
		this.messages = messages;
	}
	
	public void sendMessage(String message) {
		this.messages.add(message);
	}

}