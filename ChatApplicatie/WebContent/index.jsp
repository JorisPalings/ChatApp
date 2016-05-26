<%@ page language="java" contentType="text/html; charset= UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Le chat</title>
	<link rel="stylesheet" href="css/stylesheet.css">
	<script src="js/angular-1.5.5.min.js"></script>
	<script src="js/jquery-2.2.3.min.js"></script>
	<script src="js/modal.js"></script>
	<script src="js/smoothScroll.js"></script>
	<script src="js/status.js"></script>
	<script src="js/sockets.js"></script>
</head>
<body>

	<main>
	
		<nav id="indexNav">
			<h1><a class="smoothScroll" href="#splash">Le chat!</a></h1>
			<ul>
				<li><a class="launchModal" href="#login">Log in</a></li>
				<li><a class="launchModal" href="#register">Register</a></li>
			</ul>
		</nav>
	
		<section id="splash">
		
			<video poster="images/chat.jpg" autoplay loop muted>
				<source src="video/chat.webm" type='video/webm; codecs="vp8.0, vorbis"'>
				<source src="video/chat.ogv" type='video/ogg; codecs="theora, vorbis"'>
				<source src="video/chat.mp4" type='video/mp4; codecs="avc1.4D401E, mp4a.40.2"'>
				<p>Your browser is outdated and does not support HTML5 video!</p>
			</video>
			
			<c:forEach var="error" items="${errors}">
				<p class="error">
					<c:out value="${error}" />
				</p>
			</c:forEach>
	
			<p id="statuses" ng-app="app" ng-controller="controller">
				There are currently {{online == 1 ? "1 user" : online + " users"}} online, {{away == 1 ? "1 user" : away + " users"}} away, {{donotdisturb == 1 ? "1 user" : donotdisturb + " users"}} on do not disturb, and {{offline == 1 ? "1 user" : offline + " users"}} offline of a total of {{online + away + donotdisturb + offline == 1 ? "1 user" : online + away + donotdisturb + offline + " users"}}.
			</p>
	
			<div id="darkOverlay"></div>
	
			<form class="modal" id="loginForm" action="Controller?action=login" method="POST">
				<fieldset>
				<legend>Log in</legend>
				<label id="username" for="username">Username:</label>
				<input id="username" name="username" type="text" placeholder="Username" />
				<label id="password" for="password">Password:</label>
				<input id="password" name="password" type="password" placeholder="Password" />
				<input id="submit" value="Log in" type="submit" />
				</fieldset>
			</form>
	
			<form class="modal" id="registerForm" action="Controller?action=register" method="POST">
				<fieldset>
				<legend>Register</legend>
				<label id="username" for="username">Username:</label>
				<input id="username" name="username" type="text" placeholder="Username" />
				<label id="password" for="password">Password:</label>
				<input id="password" name="password" type="password" placeholder="Password" />
				<input id="submit" value="Register" type="submit" />
				</fieldset>
			</form>
			
			<a class="smoothScroll" href="#topics">
				<img id="scroll" src="images/scroll.png" alt="Scroll down">
			</a>
			
		</section>
		
		<section id="topics">
		
			<h2>Today's topics</h2>
			
			<form id=topic0form>
				<h3 id="topic0">What's your favorite color?</h3>
				<label for="name0">Name:</label>
				<input type="text" id="name0" name="name0" />
				<label for="reaction0">Message:</label>
				<input type="text" id="reaction0" name="reaction0" />
				<button type="button" onclick="send(0);">Send</button>
			</form>
			
			<p class="reaction">(10:15:36 AM) Nick: I prefer blue myself.</p>
			<p class="reaction">(8:12:04 AM) Joris: Orange!</p>

			<form id="topic1form">
				<h3 id="topic1">Who's your favorite artist?</h3>
				<label for="name1">Name:</label>
				<input type="text" id="name1" name="name1" />
				<label for="reaction1">Message:</label>
				<input type="text" id="reaction1" name="reaction1" />
				<button type="button" onclick="send(1);">Send</button>
			</form>
			
			<p class="reaction">(4:26:56 PM) Sander: I like Tides of Nebula.</p>
			
		</section>
		
	</main>
</body>
</html>