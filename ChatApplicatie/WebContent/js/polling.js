// Polling configuration
var friendsPollingInterval = 2000;
var sentFriendRequestsPollingInterval = 2000;
var receivedFriendRequestsPollingInterval = 2000;

///////////////////////////////////////////////////////////////////////////////
//STATUS///////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////

// Get the status (not really polling as this is only triggered when status is changed)
var getStatusxHRObject = new XMLHttpRequest();

function pollGetStatus() {
	getStatusxHRObject.open("GET", "Controller?action=getUser", true);
	getStatusxHRObject.onreadystatechange = getStatusData;
	getStatusxHRObject.send(null);
}

function getStatusData() {
	if(getStatusxHRObject.readyState == 4) {
		if(getStatusxHRObject.status == 200) {
			var serverResponse = JSON.parse(getStatusxHRObject.responseText);
			document.getElementById('status').textContent = serverResponse.status;
		}
	}
}

// Change the status (not really polling as this is only triggered manually)
var changeStatusxHRObject = new XMLHttpRequest();

function changeStatus() {
	// Get the new status from the select
	var newStatus = document.getElementById("statusSelect").value;
	// Post the new status
	changeStatusxHRObject.open("POST", "Controller?action=changeStatus", true);
	changeStatusxHRObject.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	changeStatusxHRObject.send("status=" + newStatus);
	// Refresh
	pollGetStatus();
}

///////////////////////////////////////////////////////////////////////////////
//FRIENDS//////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////

// Refresh the friends list (empty and refill) every 5 seconds
var getFriendsHRObject = new XMLHttpRequest();

function pollGetFriends() {
	// Get the friends list
	getFriendsHRObject.open("GET", "Controller?action=getFriends", true);
	getFriendsHRObject.onreadystatechange = getFriendsData;
	getFriendsHRObject.send(null);
}

function getFriendsData() {
	if(getFriendsHRObject.readyState == 4) {
		if(getFriendsHRObject.status == 200) {
			var serverResponse = JSON.parse(getFriendsHRObject.responseText);
			var friendsList = document.getElementById('friends');
			// Empty friends list
			friendsList.innerHTML = "";
			// Refill it with new data
			for(var i = 0; i < serverResponse.length; i ++) {
				// Add a friend to the friends list
				var friend = document.createElement("li");
				friend.id = serverResponse[i].username;
				// Add the name and the status
				friend.appendChild(document.createTextNode(serverResponse[i].username + " - " + serverResponse[i].status));
				// Add a form to remove the friend
				removeFriendForm = document.createElement("form");
				removeFriendForm.setAttribute("action", "Controller?action=removeFriend");
				removeFriendForm.setAttribute("method", "POST");
				removeFriendForm.setAttribute("id", "removeFriendForm");
				// Pass the username of the friend to remove
				var usernameInput = document.createElement("input");
				usernameInput.setAttribute("type", "hidden");
				usernameInput.setAttribute("name", "username");
				usernameInput.setAttribute("value", serverResponse[i].username);
				removeFriendForm.appendChild(usernameInput);
				// Add a "Remove" button
				var submitInput = document.createElement("input");
				submitInput.setAttribute("type", "image");
				submitInput.setAttribute("src", "images/delete.png");
				submitInput.setAttribute("value", "Remove");
				removeFriendForm.appendChild(submitInput);
				friend.appendChild(removeFriendForm);
				// Add a form to send a message to the friend
				selectConversationButton = document.createElement("button");
				selectConversationButton.innerHTML = "<img src='images/chat.png' alt='Chat'>";
				friend.appendChild(selectConversationButton);
				// Add the friend with all of its forms to the friends list
				friendsList.appendChild(friend);
			}
			// Rinse and repeat
			setTimeout("pollGetFriends()", friendsPollingInterval);
		}
	}
}

// Initiate the loop
pollGetFriends();

///////////////////////////////////////////////////////////////////////////////
//SENT FRIEND REQUESTS/////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////

//Refresh the sent friend requests list (empty and refill) every 5 seconds
var sentFriendRequestsxHRObject = new XMLHttpRequest();

// Get the sent friend requests list
function pollSentFriendRequests() {
	sentFriendRequestsxHRObject.open("GET", "Controller?action=getSentFriendRequests", true);
	sentFriendRequestsxHRObject.onreadystatechange = getSentFriendRequestsData;
	sentFriendRequestsxHRObject.send(null);
}

function getSentFriendRequestsData() {
	if(sentFriendRequestsxHRObject.readyState == 4) {
		if(sentFriendRequestsxHRObject.status == 200) {
			var serverResponse = JSON.parse(sentFriendRequestsxHRObject.responseText);
			var sentFriendRequestsList = document.getElementById('sentFriendRequests')
			// Empty sent friend requests list
			sentFriendRequestsList.innerHTML = "";
			// Refill it with new data
			for(var i = 0; i < serverResponse.length; i ++) {
				// Add a friend request to the sent friend requests list
				var sentFriendRequest = document.createElement("li");
				// Add the name
				sentFriendRequest.appendChild(document.createTextNode(serverResponse[i].username));
				sentFriendRequestsList.appendChild(sentFriendRequest);
				// Add a form to cancel the request
				cancelFriendRequestForm = document.createElement("form");
				cancelFriendRequestForm.setAttribute("action", "Controller?action=cancelFriendRequest");
				cancelFriendRequestForm.setAttribute("method", "POST");
				cancelFriendRequestForm.setAttribute("id", "cancelFriendRequestForm");
				// Pass the username of the request to cancel
				var usernameInput = document.createElement("input");
				usernameInput.setAttribute("type", "hidden");
				usernameInput.setAttribute("name", "username");
				usernameInput.setAttribute("value", serverResponse[i].username);
				cancelFriendRequestForm.appendChild(usernameInput);
				// Add a "Cancel" button
				var submitInput = document.createElement("input");
				submitInput.setAttribute("type", "image");
				submitInput.setAttribute("src", "images/delete.png");
				cancelFriendRequestForm.appendChild(submitInput);
				sentFriendRequest.appendChild(cancelFriendRequestForm);
				sentFriendRequestsList.appendChild(sentFriendRequest);
			}
			// Rinse and repeat
			setTimeout("pollSentFriendRequests()", sentFriendRequestsPollingInterval);
		}
	}
}

// Initiate the loop
pollSentFriendRequests();

///////////////////////////////////////////////////////////////////////////////
//RECEIVED FRIEND REQUESTS/////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////

// Refresh the sent friend requests list (empty and refill) every 5 seconds
var receivedFriendRequestsxHRObject = new XMLHttpRequest();

// Get the sent friend requests list
function pollReceivedFriendRequests() {
	receivedFriendRequestsxHRObject.open("GET", "Controller?action=getReceivedFriendRequests", true);
	receivedFriendRequestsxHRObject.onreadystatechange = getReceivedFriendRequestsData;
	receivedFriendRequestsxHRObject.send(null);
}

function getReceivedFriendRequestsData() {
	if(receivedFriendRequestsxHRObject.readyState == 4) {
		if(receivedFriendRequestsxHRObject.status == 200) {
			var serverResponse = JSON.parse(receivedFriendRequestsxHRObject.responseText);
			var receivedFriendRequestsList = document.getElementById('receivedFriendRequests')
			// Empty sent friend requests list
			receivedFriendRequestsList.innerHTML = "";
			// Refill it with new data
			for(var i = 0; i < serverResponse.length; i ++) {
				var receivedFriendRequest = document.createElement("li");
				receivedFriendRequest.appendChild(document.createTextNode(serverResponse[i].username));
				receivedFriendRequestsList.appendChild(receivedFriendRequest);
				// Add a form to accept the request
				acceptFriendRequestForm = document.createElement("form");
				acceptFriendRequestForm.setAttribute("action", "Controller?action=acceptFriendRequest");
				acceptFriendRequestForm.setAttribute("method", "POST");
				acceptFriendRequestForm.setAttribute("id", "acceptFriendRequestForm");
				// Pass the username of the request to accept
				var usernameInput = document.createElement("input");
				usernameInput.setAttribute("type", "hidden");
				usernameInput.setAttribute("name", "username");
				usernameInput.setAttribute("value", serverResponse[i].username);
				acceptFriendRequestForm.appendChild(usernameInput);
				// Add an "Accept" button
				var submitInput = document.createElement("input");
				submitInput.setAttribute("type", "image");
				submitInput.setAttribute("src", "images/accept.png");
				acceptFriendRequestForm.appendChild(submitInput);
				receivedFriendRequest.appendChild(acceptFriendRequestForm);
				receivedFriendRequestsList.appendChild(receivedFriendRequest);
				// Add a form to reject the request
				rejectFriendRequestForm = document.createElement("form");
				rejectFriendRequestForm.setAttribute("action", "Controller?action=rejectFriendRequest");
				rejectFriendRequestForm.setAttribute("method", "POST");
				rejectFriendRequestForm.setAttribute("id", "rejectFriendRequestForm");
				// Pass the username of the request to accept
				var usernameInput = document.createElement("input");
				usernameInput.setAttribute("type", "hidden");
				usernameInput.setAttribute("name", "username");
				usernameInput.setAttribute("value", serverResponse[i].username);
				rejectFriendRequestForm.appendChild(usernameInput);
				// Add a "Reject" button
				var submitInput = document.createElement("input");
				submitInput.setAttribute("type", "image");
				submitInput.setAttribute("src", "images/delete.png");
				rejectFriendRequestForm.appendChild(submitInput);
				receivedFriendRequest.appendChild(rejectFriendRequestForm);
				receivedFriendRequestsList.appendChild(receivedFriendRequest);
			}
			// Rinse and repeat
			setTimeout("pollReceivedFriendRequests()", receivedFriendRequestsPollingInterval);
		}
	}
}

// Initiate the loop
pollReceivedFriendRequests();