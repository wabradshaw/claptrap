# Claptrap
A joke telling AI that frequently misses the mark. It uses a basic understanding of how words related to each other to generate its own unique jokes.

Jokes like:

> What do you call a laptop that is sleepy?
> A nap-top!

Don't expect Oscar Wilde. The important thing is that Claptrap understood that *nap* and *sleepy* have similar meanings, while *nap-top* and *laptop* sound alike, and how to fit these things together into a joke.

# Thanks
This project wouldn't exist without the help of a few people, despite the fact they don't know it exists. Firstly, I'd like to thank Kim Binsted, Graeme Ritchie and Helen Pain. Their work on [JAPE (Joking Analysis and Production Engine)](http://www2.hawaii.edu/~binsted/papers/Binstedthesis.pdf), [STANDUP](https://homepages.abdn.ac.uk/g.ritchie/pages/papers/IEEE_IS_2006.pdf) and the [Joking Computer](http://joking.abdn.ac.uk/home.shtml) served as the basis for this design. In particular Helen was my advisor in my first foray into computational humour. She introduced me to the system, gave me the opportunity to work with it, then put up with months of me complaining about the quality of the jokes. Thanks also to [the teams behind STANDUP and the Joking Computer](http://joking.abdn.ac.uk/people7.shtml), it might have been Kim & Graeme's brainchild, but it wouldn't have got where it did without all of the researchers in Edinburgh, Aberdeen & Dundee.

Finally, I'd like to thank the [Datamuse API](https://www.datamuse.com/api/), [WordNet](https://wordnet.princeton.edu/) and the [CMU pronunciation dictionary](http://www.speech.cs.cmu.edu/cgi-bin/cmudict). They supplied data that Claptrap used to tell jokes at the start. Unfortunately, while they do a great job at providing a general dictionary, the jokes it led to weren't funny. As such I created a targeted dictionary by hand which has much better results. DataMuse has been left as an option in the code to show possibilities.
