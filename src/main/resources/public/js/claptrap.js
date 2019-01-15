/**
 * Knockout Data class for a blog.
 */
function Joke(setup, punchline){
	var self = this;
	self.setup = setup;
	self.punchline = punchline;
}

/**
 * Main Knockout view model containing the trip history.
 */
function JokingViewModel(){
	var self = this;
	self.currentJoke = ko.observable()
	
	self.generateJoke = function(){
		$.get("./joke?sweary=false", function(data){
			console.log(data);
			self.currentJoke(new Joke(data.setup, data.punchline));
		});
	}
}

/**
 * Register the ContentViewModel.
 */
$( document ).ready(function() {
	ko.applyBindings(new JokingViewModel());
});