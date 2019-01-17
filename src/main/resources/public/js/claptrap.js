function stripDet(word){
	return word.replace(/^(a|an|some|the) /i,"");
}

function addDet(word){
	if(/^([A-Z]|one )/.test(word)){
		return word;
	} else {
		return `${AvsAnSimple.query(word)} ${word}`;
	}
}

function getLinguisticDescriptor(spec){
	var nucleus = spec.nucleus;
	var original = spec.linguisticOriginal;
	var splitNucleus = nucleus.replace(original, "-" + original + "-")
							  .replace(/--/g,"-")
							  .replace(/ -|- /g," ")
							  .replace(/^-|-$/g,"");
	var formattedResult = stripDet(spec.punchline).replace("!","");
	return splitNucleus + " sounds like " + formattedResult
}

function getSemanticDescriptor(original, sub, relationship){
	var originalDet = addDet(original);
	
	switch(relationship) {
		case "SYNONYM": return `${originalDet} is the same as ${sub}`;
		case "NEAR_SYNONYM": return `${originalDet} is similar to ${sub}`;
		case "IS_A": return `${originalDet} is a type of ${sub}`;
		case "INCLUDES": return `${sub} is a type of ${originalDet}`;
		case "HAS_A": return `${originalDet} has ${sub}`;
		case "PART_OF": return `${sub} has ${originalDet}`;
		case "IN": return `${originalDet} can be found in ${sub}`;
		case "ON": return `${originalDet} can be found on ${sub}`;
		case "FROM": return `${originalDet} comes from ${sub}`;
		case "PROPERTY": return `${originalDet} is ${sub}`;
		case "ACTION": return `${originalDet} can perform the action of ${stripDet(sub)}`;
		case "ACTION_CONTINUOUS": return `${originalDet} can be ${stripDet(sub)}`;
		case "RECIPIENT_ACTION": return `someone can ${stripDet(sub)} ${originalDet}`;
		case "RECIPIENT_ACTION_PAST": return `someone could have ${stripDet(sub)} ${originalDet}`;
		default: return `${originalDet} is related to ${sub}`;
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
	self.position = position;
	
	self.wrong = ko.observable(false);
	
	self.toggleRelationship = function(){
		self.wrong(!self.wrong());
		$(":focus").blur();
		$.ajax({
			type: 'POST',
			url: './rate/relation/' + self.token,
			contentType: 'application/json',
			xhrFields: {
				withCredentials: true
			},			
			
			data: JSON.stringify({
			    wrong: self.wrong(),
			    relationship: {
                    original: self.original,
                    substitution: self.substitution,
                    relationship: self.relationship,
                    position: self.position
				}
			}),			
			success: function(result){
				console.log("Relation rating logged");
			},
			error: function(){
				console.log("Could not access the logging server");
			}
		});	
	}
}

function JokeSpec(joke){
	var self = this;
	self.nucleus = joke.data.nucleus;
	self.linguisticOriginal = joke.data.linguisticOriginal;
	self.linguisticReplacement = joke.data.linguisticReplacement;
	
	self.primarySetup = ko.observable(joke.data.primarySetup || joke.data.nucleus)
	self.primaryRelationship = ko.observable(joke.data.primaryRelationship || "SYNONYM")
	self.secondarySetup = ko.observable(joke.data.secondarySetup)
	self.secondaryRelationship = ko.observable(joke.data.secondaryRelationship)
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
	if(self.data.primarySetup && !self.data.primarySetup.endsWith(self.data.nucleus)){
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
	self.suggestedJoke = ko.observable();
	self.token = new Date().getTime() + Math.random().toString(36).substring(2);
	
	self.mode = ko.observable('Joke');
	
	self.generateJoke = function(){
		$.get("./joke", function(data){
			console.log(data);
			self.showJoke();			
			self.currentJoke(new Joke(data, self.token));
			self.suggestedJoke(new JokeSpec(self.currentJoke()));			
		});
	}
	
	self.regenerateJoke = function(){
		$.get("./joke/custom?nucleus=" + self.suggestedJoke().nucleus
				+ "&linguisticOriginal=" + self.suggestedJoke().linguisticOriginal
				+ "&linguisticSubstitute=" + self.suggestedJoke().linguisticReplacement
				+ "&primarySetup=" + self.suggestedJoke().primarySetup()
				+ "&primaryRelationship=" + self.suggestedJoke().primaryRelationship()
				+ "&secondarySetup=" + self.suggestedJoke().secondarySetup()
				+ "&secondaryRelationship=" + self.suggestedJoke().secondaryRelationship()
				, function(data){
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
	
	self.showImprove = function(){
		self.mode('Improve');
	}
}

/**
 * Register the ContentViewModel.
 */
$( document ).ready(function() {
	ko.applyBindings(new JokingViewModel());
});