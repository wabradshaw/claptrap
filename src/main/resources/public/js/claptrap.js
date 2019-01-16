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
			
			data: JSON.stringify({
			    vote: self.vote,
			    joke: {
                    setup: self.setup,
                    punchline: self.punchline,
                    template: self.data.setupTemplate,
                    nucleus: self.data.spec.nucleus,
                    primarySetup: self.data.spec.primarySetup.substitution.spelling,
                    secondarySetup: self.data.spec.secondarySetup.substitution.spelling,
                    linguisticOriginal: self.data.spec.linguisticSub.original.spelling,
                    linguisticReplacement: self.data.spec.linguisticSub.substitution.spelling,
                    primaryRelationship: self.data.spec.primarySetup.relationship,
                    secondaryRelationship: self.data.spec.secondarySetup.relationship
				}
			}),
			
			success: function(result){
				alert("TODO");
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