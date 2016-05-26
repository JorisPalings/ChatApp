package database;

import java.util.HashSet;

import domain.Person;

public class PersonDatabase {
	
	public HashSet<Person> persons;

	public PersonDatabase() {
		this.persons = new HashSet<>();
		this.persons.add(new Person("Joris", "t"));
		this.persons.add(new Person("Sander", "t"));
		this.persons.add(new Person("Nick", "t"));
		this.persons.add(new Person("Brent", "t"));
		this.persons.add(new Person("Tom", "t"));
		this.persons.add(new Person("Maarten", "t"));
		this.persons.add(new Person("Lars", "t"));
		this.getUser("Joris").sendFriendRequest(this.getUser("Sander"));
		this.getUser("Sander").acceptFriendRequest(this.getUser("Joris"));
		this.getUser("Joris").sendFriendRequest(this.getUser("Nick"));
		this.getUser("Nick").acceptFriendRequest(this.getUser("Joris"));
	}
	
	public HashSet<Person> getPersons() {
		return persons;
	}

	public void setPersons(HashSet<Person> persons) {
		this.persons = persons;
	}

	public Person getUser(String username) {
		for(Person p: this.getPersons()) {
			if(p.getUsername().equals(username)) {
				return p;
			}
		}
		throw new IllegalArgumentException("No such user (" + username + ")");
	}
	
	public void addUser(String username, String password) {
		if(username == null || username.trim().equals("")) {
			throw new IllegalArgumentException("Username is null or an empty String");
		}
		if(password == null) {
			throw new IllegalArgumentException("Password is null or an empty String");
		}
		this.persons.add(new Person(username, password));
	}
	
}
