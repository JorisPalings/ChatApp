$(function() {
	$('a.smoothScroll').click(function(event) {
		$('html, body').stop().animate({
			scrollTop: $($(this).attr('href')).offset().top
		}, 1000);
		event.preventDefault(); // Stops the page from jumping to the anchor.
	});
});