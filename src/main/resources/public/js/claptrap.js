function getLinguisticDescriptor(spec){
	var nucleus = spec.nucleus;
	var original = spec.linguisticOriginal;
	var splitNucleus = nucleus.replace(original, "-" + original + "-")
							  .replace(/--/g,"-")
							  .replace(/ -|- /g," ")
							  .replace(/^-|-$/g,"");
	var formattedResult = spec.punchline.replace(/A |An /,"")
									    .replace("!","");
	return splitNucleus + " sounds like " + formattedResult
}

function getSemanticDescriptor(original, sub, relationship){
	switch(relationship) {
		case "SYNONYM": return original + " is the same as "+ sub;
		case "NEAR_SYNONYM": return original + " is similar to "+ sub;
		case "IS_A": return original + " is a type of "+ sub;
		case "INCLUDES": return sub + " is a type of "+ original;
		case "HAS_A": return original + " has "+ sub;
		case "PART_OF": return sub + " has a "+ original;
		case "IN": return original + " can be found in "+ sub;
		case "ON": return original + " can be found on "+ sub;
		case "FROM": return original + " comes from "+ sub;
		case "PROPERTY": return original + " are "+ sub;
		case "ACTION": return original + " can "+ sub;
		case "ACTION_CONTINUOUS": return original + " can be "+ sub;
		case "RECIPIENT_ACTION": return "someone can " + sub + " " + original;
		case "RECIPIENT_ACTION": return "someone could have " + sub + " " + original;
		default: return original + " is related to " + sub;
	}
}

/**
 * Knockout Data class for a relationship within a joke.
 */ 
function Relationship(token, descriptor, original, substitution, relationship, position) {
	var self = this;
	self.token = token;
	self.descriptor = descriptor;
	self.original = original;
	self.substitution = substitution;
	self.relationship = relationship;
	
	self.wrong = ko.observable(false);
	
	self.toggleRelationship = function(){
		self.wrong(!self.wrong());
		$(":focus").blur();
		console.log("todo");		
	}
}
 
/**
 * Knockout Data class for a joke.
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

	self.relationships = ko.observableArray([]);
	self.relationships.push(new Relationship(self.token, getLinguisticDescriptor(self.data), self.data.nucleus, self.data.linguisticReplacement, "RHYME", "linguistic"));
	if(!self.data.primarySetup.endsWith(self.data.nucleus)){
		self.relationships.push(new Relationship(self.token, getSemanticDescriptor(self.data.nucleus, self.data.primarySetup, self.data.primaryRelationship), self.data.linguisticReplacement, self.data.secondarySetup, self.data.secondaryRelationship, "primary"));
	}
	self.relationships.push(new Relationship(self.token, getSemanticDescriptor(self.data.linguisticReplacement, self.data.secondarySetup, self.data.secondaryRelationship), self.data.linguisticReplacement, self.data.secondarySetup, self.data.secondaryRelationship, "secondary"));
	
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
 * Main Knockout view model.
 */
function JokingViewModel(){
	var self = this;
	self.currentJoke = ko.observable();
	self.token = new Date().getTime() + Math.random().toString(36).substring(2);
	
	self.mode = ko.observable('Joke');
	
	self.generateJoke = function(){
		$.get("./joke?sweary=false", function(data){
			console.log(data);
			self.showJoke();
			self.currentJoke(new Joke(data, self.token));
		});
	}
	
	self.showJoke = function(){
		self.mode('Joke');
	}
	
	self.showExplanation = function(){
		self.mode('Explanation');
	}
	
}

/**
 * Register the ContentViewModel.
 */
$( document ).ready(function() {
	ko.applyBindings(new JokingViewModel());
});