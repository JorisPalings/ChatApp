package database;

import java.util.HashMap;

import domain.Person;
import domain.Status;

public class UserService {
	
	public PersonDatabase personDatabase;
	
	public UserService() {
		this.personDatabase = new PersonDatabase();
	}
	
	public PersonDatabase getPersonDatabase() {
		return this.personDatabase;
	}
	
	public Person getAuthenticatedUser(String username, String password) {
		Person authenticatedUser = null;
		if(personDatabase.getUser(username).isPasswordCorrect(password)) {
			authenticatedUser = personDatabase.getUser(username);
		}
		return authenticatedUser;
	}
	
	public boolean doesPersonExist(String username) {
		boolean exists = false;
		for(Person p: getPersonDatabase().getPersons()) {
			if(p.getUsername().equals(username)) {
				exists = true;
			}
		}
		return exists;
	}
	
	public void addUser(String username, String password) {
		getPersonDatabase().addUser(username, password);
	}
	
	public HashMap<String, Integer> getStatuses() {
		HashMap<String, Integer> statuses = new HashMap<String, Integer>();
		for(Status s: Status.values()) {
			statuses.put(s.getStatusString(), 0);
		}
		for(Person p: this.getPersonDatabase().getPersons()) {
			statuses.put(p.getStatus(), statuses.get(p.getStatus()) + 1);
		}
		return statuses;
	}

}
