$( function(){
	var scrollFunction = function(){
		var viewHeight = $('body')[0].clientHeight;
		var actualHeight = $('body')[0].scrollHeight;
		var yOffset = $('body')[0].scrollTop;
		if(viewHeight < actualHeight) {
			$('.scroll-indicator-bottom').css('opacity', Math.min(1, (actualHeight-(yOffset+viewHeight))/ 50.0));
			$('.scroll-indicator-top').css('opacity', Math.min(1, yOffset / 50.0));
		} else {
			$('.scroll-indicator-bottom').css('opacity', 0);
			$('.scroll-indicator-top').css('opacity', 0);
		}
	}

	$(window).resize(scrollFunction);
	$('body').scroll(scrollFunction);
	scrollFunction();
});