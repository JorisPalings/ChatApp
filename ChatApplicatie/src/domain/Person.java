package domain;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Person {

	private String username, password;
	private byte[] salt;
	private HashSet<Person> friends, sentFriendRequests, receivedFriendRequests;
	private Status status;
	private ArrayList<Conversation> conversations;
	
	public Person() {};
	
	public Person(String username, String password) {
		this.setUsername(username);
		try {
			this.setSalt();
			this.setHashedPassword(password);
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		this.friends = new HashSet<>();
		this.sentFriendRequests = new HashSet<>();
		this.receivedFriendRequests = new HashSet<>();
		this.setStatus(Status.OFFLINE);
		this.conversations = new ArrayList<Conversation>();
	}
	
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
	private byte[] getSalt() {
		return salt;
	}
	
	@JsonIgnore
	public HashSet<Person> getFriends() {
		return friends;
	}
	
	@JsonIgnore
	public HashSet<Person> getSentFriendRequests() {
		return sentFriendRequests;
	}
	
	@JsonIgnore
	public HashSet<Person> getReceivedFriendRequests() {
		return receivedFriendRequests;
	}
	
	public String getStatus() {
		return status.getStatusString();
	}
	
	@JsonIgnore
	public ArrayList<Conversation> getConversations() {
		return this.conversations;
	}

	public void setUsername(String username) {
		if(username == null || username.trim().equals("")) {
			throw new IllegalArgumentException("Username is null or an empty String");
		}
		this.username = username;
	}
	
	public void setSalt() {
		SecureRandom random = new SecureRandom();
		byte seed[] = random.generateSeed(20);
		this.salt = seed;
	}
	
	public void setHashedPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		this.password = hashPassword(password);
	}
	
	private String hashPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest crypt = MessageDigest.getInstance("SHA-1");
		crypt.reset();
		byte[] seed = this.getSalt();
		crypt.update(seed);
		crypt.update(password.getBytes("UTF-8"));
		return new BigInteger(1, crypt.digest()).toString(16);
	}
	
	public void setStatus(Status status) {
		if(status == null) {
			throw new IllegalArgumentException("Status is null");
		}
		this.status = status;
	}

	public boolean isPasswordCorrect(String password) {
		if(password == null) {
			throw new IllegalArgumentException("Password is null");
		}
		boolean isCorrect = false;
		try {
			isCorrect = hashPassword(password).equals(this.getPassword());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return isCorrect;
	}
	
	public void sendFriendRequest(Person person) {
		if(person == null) {
			throw new IllegalArgumentException("Person is null");
		}
		if(this.getFriends().contains(person)) {
			throw new IllegalArgumentException("Person is already a friend");
		}
		this.getSentFriendRequests().add(person);
		person.getReceivedFriendRequests().add(this);
	}
	
	public void cancelFriendRequest(Person person) {
		if(person == null) {
			throw new IllegalArgumentException("Person is null");
		}
		if(!(this.getSentFriendRequests().contains(person))) {
			throw new IllegalArgumentException("No request for that user");
		}
		this.getSentFriendRequests().remove(person);
		person.getReceivedFriendRequests().remove(this);
	}
	
	public void acceptFriendRequest(Person person) {
		if(person == null) {
			throw new IllegalArgumentException("Person is null");
		}
		this.getFriends().add(person);
		person.getFriends().add(this);
		this.getReceivedFriendRequests().remove(person);
		person.getSentFriendRequests().remove(this);
	}
	
	public void rejectFriendRequest(Person person) {
		if(person == null) {
			throw new IllegalArgumentException("Person is null");
		}
		this.getReceivedFriendRequests().remove(person);
		person.getSentFriendRequests().remove(this);
	}
	
	public void removeFriend(Person person) {
		if(person == null) {
			throw new IllegalArgumentException("Person is null");
		}
		if(!(this.getFriends().contains(person))) {
			throw new IllegalArgumentException("Person is not a friend");
		}
		this.getFriends().remove(person);
		person.getFriends().remove(this);
	}
	
	public Conversation getConversationWith(Person person) {
		Conversation conversation = null;
		for(Conversation c: this.getConversations()) {
			if(c.getUser2().equals(person)) {
				conversation = c;
			}
		}
		return conversation;
	}
	
	public void startConversationWith(Person person) {
		if(this.getConversationWith(person) != null) {
			throw new IllegalArgumentException("Already in a conversation with that person");
		}
		this.getConversations().add(new Conversation(this, person));
		person.getConversations().add(new Conversation(person, this));
	}
	
	public void sendMessage(Person person, String message) {
		if(person == null) {
			throw new IllegalArgumentException("Person is null");
		}
		if(!(this.getFriends().contains(person))) {
			throw new IllegalArgumentException("Person is not a friend");
		}
		if(message == null || message.equals("")) {
			throw new IllegalArgumentException("Message is null or an empty String");
		}
		if(this.getConversationWith(person) == null) {
			this.startConversationWith(person);
		}
		this.getConversationWith(person).sendMessage(message);
		person.getConversationWith(this).sendMessage(message);
	}
	
}
