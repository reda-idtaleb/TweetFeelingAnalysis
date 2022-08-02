# TweetFeelingAnalysis

# Objectif
Création d'une application qui sert à analyser le comportement des utilisateurs de Twitter à travers leurs tweets.

# Contexte 
L'application TweetFeelingAnalysis est une application d'annalyse de comportement des utilisateurs Twitter. Cette application permet à l'utilisateur d'effectuer une requête puis afficher son résultat. Une reqûete est une recherche de tweets à partir d'un mot clé(nom d'un hasghtag utilisé sur twitter) saisi par un utilisateur. Le résultat d'une requéte est un affichage des tweets(récupérés par l'API Twitter) avec leurs classifications. La classification d'un tweet est définit par le sentiment des mots utilisés, c'est à dire soit un sentiment(resp.) positif, soit négatif, soit neutre.

**Note: L'application permet de définir le comportement des utilisateurs que sur les 3 sujets suivants: vlille, éléction présidentielles, équipe de france**

# Execution de l'application
Téléchargez le projet et situez vous dans sa racine et executez la commande:
```
java --module-path lib --add-modules javafx.controls,javafx.fxml -jar app.jar
``` 

# Les classifieurs
## Simple Classifier(ou Naive Classifier)
C'est un classifieur qui définit le comportement d'un tweet en se basant sur les mots clés utilisés. L'approche naïve vérifie l'existence des mots clés(utilisés dans un tweet) dans une base des mots négatifs/positifs.
## KNN Classifier
C'est un classifieur qui prédit le comportement d'un tweet en se basant sur une base d'apprentissage. KNNClassifier implémente l'algorithme de classification KNN en utilisant la distance de Levenstien. L'utilisation de cette distance permet de trouver les plus proches voisins d'un tweet. L'algorithme termine par déduire la classe du tweet à partir de ces voisins.
## Bayes
Les classifieurs utilisant la classification bayesienne sont basés sur une classe
NGramme qui peut représenter des uni-grammes comme des bi-grammes. Les classes
PresenceBayesClassifier et FrequencyBayesClassifier utilisent la classe NGramme, il
suffit donc de leur donner une liste d'entiers représentant les degrés des n-grammes à
traiter.