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

function Link(type, descriptor, addDet){
	var self = this;
	self.type = type;
	self.descriptor = descriptor;
	self.addDet = addDet;
}

var primaryLinks = [
	new Link("SYNONYM", "is the same as", false),
	new Link("IS_A", "is more specific than", false),
	new Link("INCLUDES", "is less specific than", false)
];

var secondaryLinks = [
	new Link("SYNONYM", "is the same as", false),
	new Link("IS_A", "is more specific than", false),
	new Link("INCLUDES", "is less specific than", false),
	new Link("NEAR_SYNONYM", "is similar to", false),
	new Link("HAS_A", "has", false),
	new Link("PART_OF", "is part of", false),
	new Link("IN", "can be found in", false),
	new Link("ON", "can be found on", false),
	new Link("IN", "comes from", false),
	new Link("PROPERTY", "can be described as", false),
	new Link("ACTION", "(e.g. 'runs')", false),
	new Link("ACTION_CONTINUOUS", "can be (e.g. 'shining')", true),
	new Link("RECIPIENT_ACTION", "is something someone can", false),
	new Link("RECIPIENT_ACTION_PAST", "could have been", true)
];

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
			url: './service/rate/relation/' + self.token,
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
	self.detNucleus = addDet(self.nucleus);
	self.linguisticOriginal = joke.data.linguisticOriginal;
	self.linguisticReplacement = joke.data.linguisticReplacement;
	self.detLinguisticReplacement = addDet(self.linguisticReplacement);
	
	self.primarySetup = ko.observable(joke.data.primarySetup || joke.data.nucleus)
	self.primaryLink = ko.observable(primaryLinks.find(link => link.type == (joke.data.primaryRelationship || "SYNONYM")))
	self.secondarySetup = ko.observable(joke.data.secondarySetup)
	self.secondaryLink = ko.observable(secondaryLinks.find(link => link.type == joke.data.secondaryRelationship))
	
	self.primaryOptions = primaryLinks
	self.secondaryOptions = secondaryLinks
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
	
	self.combined = encodeURI(self.setup + " " + self.punchline);
	self.shareFacebook = "https://www.facebook.com/sharer.php?u=https://claptrapapp.com&quote=" + self.combined;
	self.shareTwitter = "https://twitter.com/share?url=https://claptrapapp.com&amp;text=" + self.combined + "&hashtags=claptrapapp";
	self.shareReddit = "https://reddit.com/submit?url=https://claptrapapp.com&amp;title=" + self.combined;
	self.shareEmail = "mailto:?Subject=" + encodeURI(self.setup) + "&Body=" + encodeURI(self.punchline) + "%0D%0A%0D%0A" + "Generated by the joke telling AI at https://claptrapapp.com" + "%0D%0A";
	
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
			url: './service/rate/' + self.token,
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
	self.suggestedJokeSpec = ko.observable();
	self.token = new Date().getTime() + Math.random().toString(36).substring(2);
	
	self.mode = ko.observable('Joke');
	
	self.generateJoke = function(){
		$.get("./service/joke", function(data){
			console.log(data);
			self.showJoke();			
			self.currentJoke(new Joke(data, self.token));
			self.suggestedJoke(self.currentJoke());
			self.suggestedJokeSpec(new JokeSpec(self.currentJoke()));			
		});
	}
	
	self.regenerateJoke = function(){
		$.get("./service/joke/custom?nucleus=" + self.suggestedJokeSpec().nucleus
				+ "&linguisticOriginal=" + self.suggestedJokeSpec().linguisticOriginal
				+ "&linguisticSubstitute=" + self.suggestedJokeSpec().linguisticReplacement
				+ "&primarySetup=" + self.suggestedJokeSpec().primarySetup()
				+ "&primaryRelationship=" + self.suggestedJokeSpec().primaryLink().type
				+ "&secondarySetup=" + self.suggestedJokeSpec().secondarySetup()
				+ "&secondaryRelationship=" + self.suggestedJokeSpec().secondaryLink().type
				, function(data){
			console.log(data);
			self.suggestedJoke(new Joke(data, self.token));
			self.showPreview();
		});
	}
	
	self.submitJoke = function(){
		if(self.currentJoke() != self.suggestedJoke()){
			$.ajax({
				type: 'POST',
				url: './service/suggest/' + self.token,
				contentType: 'application/json',
				xhrFields: {
					withCredentials: true
				},			
				
				data: JSON.stringify({
					oldJoke: {
						setup: self.currentJoke().setup,
						punchline: self.currentJoke().punchline,
						template: self.currentJoke().data.template,
						nucleus: self.currentJoke().data.nucleus,
						primarySetup: self.currentJoke().data.primarySetup,
						secondarySetup: self.currentJoke().data.secondarySetup,
						linguisticOriginal: self.currentJoke().data.linguisticOriginal,
						linguisticReplacement: self.currentJoke().data.linguisticReplacement,
						primaryRelationship: self.currentJoke().data.primaryRelationship,
						secondaryRelationship: self.currentJoke().data.secondaryRelationship
					},
					newJoke: {
						setup: self.suggestedJoke().setup,
						punchline: self.suggestedJoke().punchline,
						template: self.suggestedJoke().data.template,
						nucleus: self.suggestedJoke().data.nucleus,
						primarySetup: self.suggestedJoke().data.primarySetup,
						secondarySetup: self.suggestedJoke().data.secondarySetup,
						linguisticOriginal: self.suggestedJoke().data.linguisticOriginal,
						linguisticReplacement: self.suggestedJoke().data.linguisticReplacement,
						primaryRelationship: self.suggestedJoke().data.primaryRelationship,
						secondaryRelationship: self.suggestedJoke().data.secondaryRelationship
					}
				}),			
				success: function(result){
					console.log("Suggestion logged");
				},
				error: function(){
					console.log("Could not access the logging server");
				}
			});
		}
		self.currentJoke(self.suggestedJoke());		
		self.showJoke();
	}
	
	self.showJoke = function(){
		self.mode('Joke');
		refreshWindow();
	}
	
	self.showExplanation = function(){
		self.mode('Explanation');
		refreshWindow();
	}
	
	self.showImprove = function(){
		self.mode('Improve');
		refreshWindow();
	}
	
	self.showPreview = function(){
		self.mode('Preview');
		refreshWindow();
	}
	
}

/**
 * Register the ContentViewModel.
 */
$( document ).ready(function() {
	var jokeModel = new JokingViewModel();
	ko.applyBindings(jokeModel);
	jokeModel.generateJoke();
});