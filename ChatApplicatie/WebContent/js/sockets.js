// TODO Replace fishy "::" in send and writeResponse methods with JSON
var webSocket;

function openSocket() {
	webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/ChatApplicatie/websocket");
	// Define behavior when a message is sent
	webSocket.onmessage = function(event) { writeResponse(event.data) };
}

function send(topicNo) {
	webSocket.send((new Date()).toLocaleTimeString() + "::" + topicNo + "::" + $("input#name" + topicNo).val() + "::" + $("input#reaction" + topicNo).val());
}

function writeResponse(message) {
	var msg = message.split("::");
	var date = msg[0];
	var topicNo = msg[1];
	var name = (msg[2] == "") ? "Anonymous" : msg[2];
	var reaction = msg[3];
	$('form#topic' + topicNo + 'form').after("<p class='reaction'>(" + date + ") " + name + ": " + reaction + "</p>");
}

function closeSocket() {
	webSocket.close();
}

// Open a WebSocket when the page is loaded
$(document).ready(function() {
	openSocket();
})

// Close the WebSocket (and remove the session from the Set) when a user closes the page
$(window).unload(function() {
	closeSocket();
})