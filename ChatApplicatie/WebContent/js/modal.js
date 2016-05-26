$(document).ready(function() {
	
	var openForm;
	
	$("nav ul li a.launchModal").click(function() {
		openForm = $(this).attr("href").slice(1) + "Form";
		console.log(openForm);
		$("div#darkOverlay").fadeIn();
		$("form#" + openForm).fadeIn();
		$("form#" + openForm + " input:first").select();
	})
	
	$("div#darkOverlay").click(function() {
		$(this).fadeOut();
		$("form#" + openForm).fadeOut();
	})
	
})