$(document).ready(function() {
	
	var interval;
	
	// Open the chat with the user whose button is clicked
	$(document).on("click", "ul#friends li button", function() {
		$("form#sendMessageForm input[type=text]").prop("disabled", false);
		clearInterval(interval);
		// Get the user to chat with
		var to = $(this).parent().attr("id");
		// Empty the chat box
		$("div#conversation").empty();
		// Load the conversation
		getConversation(to);
		// Set the hidden input field's value to the person to chat with
		$("form#sendMessageForm input[name=username]").val(to);
		// Add polling
		interval = setInterval(function() {
			getConversation($("form#sendMessageForm input[name=username]").val());
		}, 100);
	})
	
	function getConversation(username) {
		// Retrieve the conversation
		$.getJSON("Controller?action=getConversations", function(conversations) {
			// Filter out the conversation with the right person
			$.each(conversations, function() {
				// If the conversation matches...
				if(this.user2.username == username) {
					// Empty the chat box
					$("div#conversation").empty();
					for(var i = 0; i < this.messages.length; i ++) {
						// ...show the messages it contains
						$("div#conversation").append("</p>" + this.messages[i] + "</p>");
					}
				}
			})
		})
	}
	
	$("form#sendMessageForm").submit(function(event) {
		event.preventDefault();
		$.post($(this).attr("action"), {
			message: "(" + (new Date()).toLocaleTimeString() + ")" + " " + $("h2#username").text() + ": " + $(this).find("input[name='message']").val(),
			username: $(this).find("input[name='username']").val()
		})
		// Clear the input field after a message is sent
		$(this).find("input[name=message]").val("");
	})
	
})

/*

$.getJSON is shorthand for
$.ajax({
	dataType: "json",
	url: url,
	data: data,
	success: success
});

*/