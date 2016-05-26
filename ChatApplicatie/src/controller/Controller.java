// TODO Catch no such user exceptions and display them (e.g. sendFriendRequest)

package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import database.UserService;
import domain.Person;
import domain.Status;

@WebServlet("/Controller")
public class Controller extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	public UserService model = new UserService();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		processRequest(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		processRequest(request, response);
	}

	private void processRequest(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String action = (String) request.getParameter("action");
		ArrayList<String> errors = new ArrayList<>();
		String destination = "index.jsp";
		String responseJSON = "";
		
		if(isAuthenticationNeeded(action)) {
			if(!isAuthenticated(request)) {
				errors.add("You need to be logged in to view that page!");
				request.setAttribute("errors", errors);
			} else if(action.equals("getUser")) {
				responseJSON = getUser(request, response);
			} else if(action.equals("changeStatus")) {
				destination = changeStatus(request, response);
			} else if(action.equals("getFriends")) {
				responseJSON = getFriends(request, response);
			} else if(action.equals("getSentFriendRequests")) {
				responseJSON = getSentFriendRequests(request, response);
			} else if(action.equals("getReceivedFriendRequests")) {
				responseJSON = getReceivedFriendRequests(request, response);
			} else if(action.equals("sendFriendRequest")) {
				destination = sendFriendRequest(request, response);
			} else if(action.equals("cancelFriendRequest")) {
				destination = cancelFriendRequest(request, response);
			} else if(action.equals("acceptFriendRequest")) {
				destination = acceptFriendRequest(request, response);
			} else if(action.equals("rejectFriendRequest")) {
				destination = rejectFriendRequest(request, response);
			} else if(action.equals("removeFriend")) {
				destination = removeFriend(request, response);
			} else if(action.equals("sendMessage")) {
				responseJSON = sendMessage(request, response);
			} else if(action.equals("getConversations")) {
				responseJSON = getConversations(request, response);
			} else if(action.equals("getStatuses")) {
				responseJSON = getStatuses(request, response);
			}
		} else {
			if(action == null) {
				destination = "index.jsp";
			} else if(action.equals("login")) {
				destination = login(request, response);
			} else if(action.equals("logout")) {
				destination = logout(request, response);
			} else if(action.equals("register")) {
				destination = register(request, response);
			} 
		}
		
		// Don't redirect
		if(action != null && action.equals("getUser")
				|| action != null && action.equals("getFriends") 
				|| action != null && action.equals("getSentFriendRequests")
				|| action != null && action.equals("getReceivedFriendRequests")
				|| action != null && action.equals("sendMessage")
				|| action != null && action.equals("getConversations")
				|| action != null && action.equals("getStatuses")) {
			response.getWriter().write(responseJSON);
		// Redirect
		} else {
			RequestDispatcher view = request.getRequestDispatcher(destination);
			view.forward(request, response);
		}
	}
	
	private boolean isAuthenticated(HttpServletRequest request) {
		return request.getSession(false) != null;
	}
	
	private boolean isAuthenticationNeeded(String action) {
		if(action == null 
				|| action.equals("login") 
				|| action.equals("logout") 
				|| action.equals("register")) {
			return false;
		} else {
			return true;
		}
	}
	
	private String login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = ((String) request.getParameter("username")).trim();
		String password = ((String) request.getParameter("password")).trim();
		ArrayList<String> errors = new ArrayList<>();
		
		try {
			Person authenticatedUser = model.getAuthenticatedUser(username, password);
			if(authenticatedUser != null) {
				authenticatedUser.setStatus(Status.ONLINE);
				HttpSession session = request.getSession();
				session.setAttribute("user", authenticatedUser);
				
				return "chat.jsp";
			} else {
				errors.add("Incorrect username/password");
				request.setAttribute("errors", errors);
				
				return "index.jsp";
			}
		} catch(Exception e) {
			errors.add("User not found.");
			request.setAttribute("errors", errors);
		}
		return "index.jsp";
	}
		
	private String logout(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Person authenticatedUser = (Person) session.getAttribute("user");
		authenticatedUser.setStatus(Status.OFFLINE);
		session.invalidate();
		
		return "index.jsp";
	}
	
	private String register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = ((String) request.getParameter("username")).trim();
		String password = ((String) request.getParameter("password")).trim();
		ArrayList<String> errors = new ArrayList<>();
		
		if(model.doesPersonExist(username)) {
			errors.add("That username is already taken. Try another one.");
			request.setAttribute("errors", errors);
			
			return "index.jsp";
		} else {
			try {
				model.addUser(username, password);
				Person authenticatedUser = model.getAuthenticatedUser(username, password);
				authenticatedUser.setStatus(Status.ONLINE);
				HttpSession session = request.getSession();
				session.setAttribute("user", authenticatedUser);
				
				return "chat.jsp";
			} catch(Exception e) {
				errors.add("Invalid username/password.");
				request.setAttribute("errors", errors);
			}
			return "index.jsp";
		}
	}
	
	private String getUser(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
		HttpSession session = request.getSession();
		Person authenticatedUser = (Person) session.getAttribute("user");

		Object user = model.getPersonDatabase().getUser(authenticatedUser.getUsername());
		
		ObjectMapper mapper = new ObjectMapper();
		String userJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(user);
		
		return userJSON;
	}
	
	private String changeStatus(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Person authenticatedUser = (Person) session.getAttribute("user");
		
		String status = (String) request.getParameter("status");
		
		model.getPersonDatabase().getUser(authenticatedUser.getUsername())
			.setStatus(Status.valueOf(status));
		
		return "chat.jsp";
	}
	
	private String getFriends(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		Person authenticatedUser = (Person) session.getAttribute("user");
		
		Object[] friends = model.getPersonDatabase().getUser(authenticatedUser.getUsername())
			.getFriends().toArray();
		
		ObjectMapper mapper = new ObjectMapper();
		String friendsJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(friends);
		
		return friendsJSON;
	}
	
	private String getSentFriendRequests(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		Person authenticatedUser = (Person) session.getAttribute("user");
		
		Object[] sentFriendRequests = model.getPersonDatabase().getUser(authenticatedUser.getUsername())
			.getSentFriendRequests().toArray();
		
		ObjectMapper mapper = new ObjectMapper();
		String sentFriendRequestsJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(sentFriendRequests);
		
		return sentFriendRequestsJSON;
	}
	
	private String getReceivedFriendRequests(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		Person authenticatedUser = (Person) session.getAttribute("user");
		
		Object[] receivedFriendRequests = model.getPersonDatabase().getUser(authenticatedUser.getUsername())
			.getReceivedFriendRequests().toArray();
		
		ObjectMapper mapper = new ObjectMapper();
		String receivedFriendRequestsJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(receivedFriendRequests);
		
		return receivedFriendRequestsJSON;
	}
	
	private String sendFriendRequest(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Person authenticatedUser = (Person) session.getAttribute("user");
		ArrayList<String> errors = new ArrayList<>();

		String friendUsername = ((String) request.getParameter("username")).trim();
		
		try {
			Person friend = model.getPersonDatabase().getUser(friendUsername);
				
			model.getPersonDatabase().getUser(authenticatedUser.getUsername())
				.sendFriendRequest(friend);
		} catch(Exception e) {
			errors.add("That user doesn't exist!");
			request.setAttribute("errors", errors);
			
			return "chat.jsp";
		}
			
		return "chat.jsp";
	}
	
	private String cancelFriendRequest(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Person authenticatedUser = (Person) session.getAttribute("user");

		String friendUsername = ((String) request.getParameter("username")).trim();
		Person friend = model.getPersonDatabase().getUser(friendUsername);
			
		model.getPersonDatabase().getUser(authenticatedUser.getUsername())
			.cancelFriendRequest(friend);
		
		return "chat.jsp";
	}
	
	private String acceptFriendRequest(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Person authenticatedUser = (Person) session.getAttribute("user");

		String friendUsername = ((String) request.getParameter("username")).trim();
		Person friend = model.getPersonDatabase().getUser(friendUsername);
			
		model.getPersonDatabase().getUser(authenticatedUser.getUsername())
			.acceptFriendRequest(friend);
		
		return "chat.jsp";
	}
	
	private String rejectFriendRequest(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Person authenticatedUser = (Person) session.getAttribute("user");

		String friendUsername = ((String) request.getParameter("username")).trim();
		Person friend = model.getPersonDatabase().getUser(friendUsername);
			
		model.getPersonDatabase().getUser(authenticatedUser.getUsername())
			.rejectFriendRequest(friend);
		
		return "chat.jsp";
	}
	
	private String removeFriend(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Person authenticatedUser = (Person) session.getAttribute("user");

		String friendUsername = ((String) request.getParameter("username")).trim();
		Person friend = model.getPersonDatabase().getUser(friendUsername);
			
		model.getPersonDatabase().getUser(authenticatedUser.getUsername())
			.removeFriend(friend);
		
		return "chat.jsp";
	}
	
	private String sendMessage(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
		HttpSession session = request.getSession();
		Person authenticatedUser = (Person) session.getAttribute("user");
		Person from = model.getPersonDatabase().getUser(authenticatedUser.getUsername());

		String friend = ((String) request.getParameter("username")).trim();
		Person to = model.getPersonDatabase().getUser(friend);

		String message = ((String) request.getParameter("message")).trim();
		
		from.sendMessage(to, message);
		
		Object[] conversations = model.getPersonDatabase().getUser(authenticatedUser.getUsername())
				.getConversations().toArray();
			
		ObjectMapper mapper = new ObjectMapper();
		String conversationsJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(conversations);
		
		// Not actually ever used
		return conversationsJSON;
	}
	
	private String getConversations(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
		HttpSession session = request.getSession();
		Person authenticatedUser = (Person) session.getAttribute("user");
		
		Object[] conversations = model.getPersonDatabase().getUser(authenticatedUser.getUsername())
			.getConversations().toArray();
		
		ObjectMapper mapper = new ObjectMapper();
		String conversationsJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(conversations);
		
		return conversationsJSON;
	}
	
	private String getStatuses(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String statusesJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(model.getStatuses());
		
		return statusesJSON;
	}
	
}
