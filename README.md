# TweetFeelingAnalysis

# Objective
Creation of an application used to analyze the behavior of Twitter users through their tweets.

# Context
The TweetFeelingAnalysis application is a Twitter user behavior analysis application. This application allows the user to perform a query and then display its result. A query is a search for tweets based on a keyword (name of a hashtag used on twitter) entered by a user. The result of a query is a display of tweets (retrieved by the Twitter API) with their classifications. The classification of a tweet is defined by the feeling of the words used, ie either a feeling (resp.) positive, or negative, or neutral.

**Note: The application allows user behavior to be defined only on the following 3 subjects: city, presidential election, French team**

# Application execution
Download the project and go to its root and run the command:
```
java --module-path lib --add-modules javafx.controls,javafx.fxml -jar app.jar
```

# Classifiers
## Simple Classifier (or Naive Classifier)
It is a classifier that defines the behavior of a tweet based on the keywords used. The naive approach verifies the existence of keywords (used in a tweet) in a database of negative/positive words.
##KNN Classify
It is a classifier that predicts the behavior of a tweet based on a learning base. KNNClassifier implements the KNN classification algorithm using the Levenstien distance. Using this distance finds the nearest neighbors of a tweet. The algorithm ends up deducing the class of the tweet from these neighbors.
## Bayes
Classifiers using Bayesian classification are class-based
NGram that can represent uni-grams as well as bi-grams. The classes
PresenceBayesClassifier and FrequencyBayesClassifier use the NGram class, it
therefore suffices to give them a list of integers representing the degrees of the n-grams to
treat.
