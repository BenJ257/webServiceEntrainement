Entraînement TP noté


Vous devez développer un web-service qui permet à un professeur de
poser une question et de proposer plusieurs réponses.
Les étudiants peuvent participer en répondant à la question.
Vous avez toutes les caractéristiques du web-service demandé ci-dessous.


POST /api/quizz/utilisateur
    - ne nécessite aucune authentification
    - deux paramètres dans le body de la requête:
        - pseudo doit être un email valide
        - password doit être un mot de passe non vide
    - Codes :
        - 201 quand l'utilisateur est bien créé
        - 406 quand les informations dans les paramètres sont incorrectes
        - 409 quand l'adresse mail est déjà utilisée



GET /api/quizz/utilisateur/{idUtilisateur}
    - pour tous les utilisateurs authentifiés
    - Codes :
        - 200 quand l'utilisateur est bien récupéré avec un objet Utilisateur dans
          le body de la réponse
        - 403 quand un utilisateur essaie de récupérer un profil qui n'est pas le sien

POST /api/quizz/question
    - requête authentifiée uniquement disponible pour les professeurs (rôle PROFESSEUR)
    - Requiert dans le body de la requête :
        - une structure Json de la classe Question contenant au moins les champs :
            * libelleQuestion : le libellé de la question
            * reponsesPossibles : un tableau de réponses
    - Codes :
        - 201 : si la question a pu être créée sans erreur + Location de la ressource créée
        - 406 : si les attributs de l'objets envoyés ne sont pas conformes aux attentes (voir
        la fonction creerQuestion dans FacadeQuizz.java
        - 403 : si la personne authentifiée n'a pas accès à cette URI



GET /api/quizz/question/{idQuestion}
    - requête authentifiée disponible pour toutes les personnes authentifiées
    - retourne dans le body l'objet Question correspondant à l'identifiant
    - Codes :
        - 200 : si la question existe
        - 404 : si aucune question ne correspond à cet identifiant


PUT /api/quizz/question/{idQuestion}/vote
    - requête authentifiée uniquement disponible pour les étudiants (rôle ETUDIANT)
    - contient dans le body de la requête un paramètre idReponse qui permettra
    à un étudiant de voter pour la réponse concernée
    - Codes :
        - 202 : le vote a été accepté
        - 409 : l'étudiant a déjà voté pour cette question
        - 406 : l'identifiant idReponse n'est pas correct
        - 404 : l'identifiant idQuestion ne correspond à aucune ressource existante
        - 403 : si la personne authentifiée n'a pas accès à cette URI


GET /api/quizz/question/{idQuestion}/vote
    - requête authentifiée uniquement disponible pour les professeurs (rôle PROFESSEUR)
    - contient dans le body de la réponse le résultat des votes à la question idQuestion
    - Codes :
        - 200 : le résultat a bien été récupéré
        - 404 : l'identifiant idQuestion ne correspond à aucune ressource existante
        - 403 : si la personne authentifiée n'a pas accès à cette URI




Outils proposés :

- Pour tester vos implémentations, vous avez deux classes disponibles dans
test/java.
    * TpnoteApplicationTests est une classe de tests fonctionnels existants qui vous permet
    de vérifier que votre web-service accomplit bien sa mission
    * TestFacadeQuizz est une classe de tests fonctionnels existants qui vous permet de vérifier
    que la façade que vous avez implémentée remplit bien sa mission également.
    * Pour lancer les tests deux choix :
        ** maven test
        ** Exécuter à la main chaque classe de tests avec la petite flèche verte

- Vous avez une classe OutilsPourValidationEmail dont la fonction
patternMatches vérifie la bonne forme d'une adresse email
- Les classes métiers Utilisateur, ResultatVote et Question sont dans leur version finale,
vous n'avez donc pas à y toucher.




Travail demandé :

- En fonction de la description du Web-service rest précédemment donnée,
    1. complétez les deux classes ci-dessous :
        * fr.orleans.info.wsi.cc.tpnote.modele.FacadeQuizz
        * fr.orleans.info.wsi.cc.tpnote.controleur.ControleurQuizz
    2. Configurez la sécurité pour avoir une authentification basique gérant les rôles PROFESSEUR
    et ETUDIANT. Pour information, les rôles sont attribués aux utilisateurs dans la classe Utilisateur



