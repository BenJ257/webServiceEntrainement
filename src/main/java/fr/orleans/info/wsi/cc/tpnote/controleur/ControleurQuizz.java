package fr.orleans.info.wsi.cc.tpnote.controleur;

import fr.orleans.info.wsi.cc.tpnote.modele.FacadeQuizz;
import fr.orleans.info.wsi.cc.tpnote.modele.Question;
import fr.orleans.info.wsi.cc.tpnote.modele.ResultatVote;
import fr.orleans.info.wsi.cc.tpnote.modele.Utilisateur;
import fr.orleans.info.wsi.cc.tpnote.modele.exceptions.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;

@RestController
@RequestMapping("/api/quizz")
public class ControleurQuizz {

    final
    FacadeQuizz facadeQuizz;

    public ControleurQuizz(FacadeQuizz facadeQuizz) {
        this.facadeQuizz = facadeQuizz;
    }

    /*
    POST /api/quizz/utilisateur
        - ne nécessite aucune authentification
        - deux paramètres dans le body de la requête:
            - pseudo doit être un email valide
            - password doit être un mot de passe non vide
        - Codes :
            - 201 quand l'utilisateur est bien créé
            - 406 quand les informations dans les paramètres sont incorrectes
            - 409 quand l'adresse mail est déjà utilisée
     */
    @PostMapping("/utilisateur")
    public ResponseEntity<String> inscription(@RequestParam String pseudo, @RequestParam String password) {
        try {
            int idUtilisateur = facadeQuizz.creerUtilisateur(pseudo, password);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(idUtilisateur).toUri();
            return ResponseEntity.created(location).body("L'utilisateur a bien été crée.");
        } catch (EmailDejaUtiliseException e) {
            return ResponseEntity.status(409).build();
        } catch (MotDePasseObligatoireException | EmailNonValideException e) {
            return ResponseEntity.status(406).build();
        }
    }

    /*
    GET /api/quizz/utilisateur/{idUtilisateur}
    - pour tous les utilisateurs authentifiés
    - Codes :
        - 200 quand l'utilisateur est bien récupéré avec un objet Utilisateur dans
          le body de la réponse
        - 403 quand un utilisateur essaie de récupérer un profil qui n'est pas le sien
     */
    @GetMapping("/utilisateur/{idUtilisateur}")
    public ResponseEntity<Utilisateur> getProfilUtilisateur(Principal principal, @PathVariable int idUtilisateur) {
        String email = principal.getName();
        try {
            int id = facadeQuizz.getIdUserByEmail(email);
            if (id == idUtilisateur) {
                Utilisateur utilisateur = facadeQuizz.getUtilisateurByEmail(email);
                return ResponseEntity.ok(utilisateur);
            } else {
                return ResponseEntity.status(403).build();
            }
        } catch (EmailInexistantException | UtilisateurInexistantException e) {
            return ResponseEntity.status(403).build();
        }
    }

    /*
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
    */
    @PostMapping("/question")
    public ResponseEntity<String> creerQuestion(@RequestBody Question question, Principal principal) {
        String email = principal.getName();
        try {
            int idUtilisateur = facadeQuizz.getIdUserByEmail(email);
            String idQuestion = facadeQuizz.creerQuestion(idUtilisateur, question.getLibelleQuestion(), question.getReponsesPossibles());

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{idQuestion}")
                    .buildAndExpand(idQuestion).toUri();
            return ResponseEntity.created(location).body("La question a été créée");
        } catch (EmailInexistantException e) {
            return ResponseEntity.status(403).build();
        } catch (LibelleQuestionNonRenseigneException | AuMoinsDeuxReponsesException e) {
            return ResponseEntity.status(406).build();
        }
    }

    /*
    GET /api/quizz/question/{idQuestion}
    - requête authentifiée disponible pour toutes les personnes authentifiées
    - retourne dans le body l'objet Question correspondant à l'identifiant
    - Codes :
        - 200 : si la question existe
        - 404 : si aucune question ne correspond à cet identifiant
    */
    @GetMapping("/question/{idQuestion}")
    public ResponseEntity<Question> getQuestion(@PathVariable String idQuestion) {
        try {
            Question question = facadeQuizz.getQuestionById(idQuestion);
            return ResponseEntity.ok(question);
        } catch (QuestionInexistanteException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /*
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
    */
    @PutMapping("/question/{idQuestion}/vote")
    public ResponseEntity<String> repondreQuestion(@PathVariable String idQuestion, @RequestParam int idReponse, Principal principal) {
        String email = principal.getName();
        try {
            int idUtilisateur = facadeQuizz.getIdUserByEmail(email);
            facadeQuizz.voterReponse(idUtilisateur, idQuestion, idReponse);
            return ResponseEntity.status(202).body("Votre réponse a bien été envoyée");
        } catch (EmailInexistantException e) {
            return ResponseEntity.status(403).build();
        } catch (QuestionInexistanteException e) {
            return ResponseEntity.status(404).build();
        } catch (NumeroPropositionInexistantException e) {
            return ResponseEntity.status(406).build();
        } catch (ADejaVoteException e) {
            return ResponseEntity.status(409).build();
        }
    }

    /*
    GET /api/quizz/question/{idQuestion}/vote
    - requête authentifiée uniquement disponible pour les professeurs (rôle PROFESSEUR)
    - contient dans le body de la réponse le résultat des votes à la question idQuestion
    - Codes :
        - 200 : le résultat a bien été récupéré
        - 404 : l'identifiant idQuestion ne correspond à aucune ressource existante
        - 403 : si la personne authentifiée n'a pas accès à cette URI
     */
    @GetMapping("/question/{idQuestion}/vote")
    public ResponseEntity<ResultatVote[]> getResultatsVote(@PathVariable String idQuestion) {
        try {
            ResultatVote[] resultatVote = facadeQuizz.getResultats(idQuestion);
            return ResponseEntity.status(200).body(resultatVote);
        } catch (QuestionInexistanteException e) {
            return ResponseEntity.status(404).build();
        }
    }
}
