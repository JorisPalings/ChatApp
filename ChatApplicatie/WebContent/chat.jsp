<%@ page language="java" contentType="text/html; charset= UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Chat</title>
	<link rel="stylesheet" href="css/stylesheet.css">
	<script src="js/jquery-2.2.3.min.js"></script>
	<script src="js/chat.js"></script>
	<script src="js/polling.js"></script>
</head>
<body>

	<nav id="chatNav">
		<h1>Le chat!</h1>
		
		<div class="right">
			<h2 id="username">${user.username}</h2>
	
			<p id="status">${user.status}</p>
		
			<form id="changeStatus" action="Controller?action=changeStatus" method="POST">
				<select id="statusSelect" name="status" onchange="changeStatus()">
					<option value="ONLINE">
						Online
					</option>
					<option value="AWAY">
						Away
					</option>
					<option value="DONOTDISTURB">
						Do not disturb
					</option>
					<option value="OFFLINE">
						Offline
					</option>
				</select>
			</form>
		
			<a href="Controller?action=logout">Log out</a>
		</div>
	</nav>
	
	<main id="chatScreen">
		<aside>
			<c:forEach var="error" items="${errors}">
				<p class="error"><c:out value="${error}" /></p>
			</c:forEach>
			
			<form id="addFriendForm" action="Controller?action=sendFriendRequest" method="POST" autocomplete="off">
				<input id="username" name="username" placeholder="Add a friend" type="text"></input>
			</form>
			
			<h3>Sent friend requests</h3>
			<ul id="sentFriendRequests">
			</ul>
		
			<h3>Received friend requests</h3>
			<ul id="receivedFriendRequests">
			</ul>
		
			<h3>Friends</h3>
			<ul id="friends">
			</ul>
		</aside>
		
		<div id="chat">
			<div id="conversation">
			</div>
			
			<form id="sendMessageForm" action="Controller?action=sendMessage" method="POST" autocomplete="off">
				<input name="message" placeholder="Type a message here" type="text" disabled></input>
				<input name="username" type="hidden"></input>
			</form>
		</div>
	</main>
</body>
</html>