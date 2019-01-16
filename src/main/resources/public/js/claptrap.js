/**
 * Knockout Data class for a blog.
 */
function Joke(data, token){
	var self = this;
	self.data = data;
	self.setup = data.setup;
	self.punchline = data.punchline;
	
	self.token = token;
	self.vote = 0;
	
	self.toggleGood = function(){
		self.vote = self.vote == 1 ? 0 : 1;
		rateJoke();
	}
	
	self.toggleBad = function(){
		self.vote = self.vote == -1 ? 0 : -1;
		rateJoke();
	}

	function rateJoke(){
		$.ajax({
			type: 'POST',
			url: './rate/' + self.token,
			contentType: 'application/json',
			xhrFields: {
				withCredentials: true
			},			
			
			data: {},
			
			success: function(result){
				alert("Done");
			},
			error: function(){
				alert("Could not access the server");
			}
		});
	}	
}

/**
 * Main Knockout view model containing the trip history.
 */
function JokingViewModel(){
	var self = this;
	self.currentJoke = ko.observable()
	self.token = "Test"
	
	self.generateJoke = function(){
		$.get("./joke?sweary=false", function(data){
			console.log(data);
			self.currentJoke(new Joke(data, self.token));
		});
	}
}

/**
 * Register the ContentViewModel.
 */
$( document ).ready(function() {
	ko.applyBindings(new JokingViewModel());
});