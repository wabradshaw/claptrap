/**
 * Knockout Data class for a blog.
 */
function Joke(data, token){
	var self = this;
	self.data = data;
	self.setup = data.setup;
	self.punchline = data.punchline;
	
	self.token = token;
	self.vote = ko.observable(0);
	
	self.toggleGood = function(){
		self.vote(self.vote() == 1 ? 0 : 1);
		rateJoke();
		$('#good-rating').blur()
	}
	
	self.toggleBad = function(){
		self.vote(self.vote() == -1 ? 0 : -1);
		rateJoke();
		$('#bad-rating').blur()
	}

	function rateJoke(){
		$.ajax({
			type: 'POST',
			url: './rate/' + self.token,
			contentType: 'application/json',
			xhrFields: {
				withCredentials: true
			},			
			
			data: JSON.stringify({
			    vote: self.vote(),
			    joke: {
                    setup: self.setup,
                    punchline: self.punchline,
                    template: self.data.template,
                    nucleus: self.data.nucleus,
                    primarySetup: self.data.primarySetup,
                    secondarySetup: self.data.secondarySetup,
                    linguisticOriginal: self.data.linguisticOriginal,
                    linguisticReplacement: self.data.linguisticReplacement,
                    primaryRelationship: self.data.primaryRelationship,
                    secondaryRelationship: self.data.secondaryRelationship
				}
			}),			
			success: function(result){
				console.log("Rating logged");
			},
			error: function(){
				console.log("Could not access the logging server");
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
	self.token = new Date().getTime() + Math.random().toString(36).substring(2)
	
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