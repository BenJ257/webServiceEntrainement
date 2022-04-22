package fr.orleans.info.wsi.cc.tpnote.modele;

import fr.orleans.info.wsi.cc.tpnote.modele.exceptions.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FacadeQuizz {

    private List<Utilisateur> utilisateurs = new ArrayList<>();
    private List<Question> questions = new ArrayList<>();

    /**
     *
     * @param email : email valide
     * @param password : mot de passe utilisateur non vide et chiffré (lors de son intégration au web-service)
     * @return identifiant entier
     * @throws EmailDejaUtiliseException : email déjà utilisé
     * @throws EmailNonValideException : email n'est pas de la bonne forme
     * @throws MotDePasseObligatoireException : le mot de passe est Blank ou nul
     */

    public int creerUtilisateur(String email,String password) throws EmailDejaUtiliseException, EmailNonValideException, MotDePasseObligatoireException {
        int id;

        if(utilisateurs.stream().anyMatch(utilisateur -> utilisateur.getEmailUtilisateur().equals(email))) {
            throw new EmailDejaUtiliseException();
        }
        else if(!OutilsPourValidationEmail.patternMatches(email)) {
            throw new EmailNonValideException();
        }
        else if(password == null) {
            throw new MotDePasseObligatoireException();
        }
        else if(password.isBlank()) {
            throw new MotDePasseObligatoireException();
        }
        else {
            Utilisateur utilisateur = new Utilisateur(email, password);
            id = utilisateur.getIdUtilisateur();
            utilisateurs.add(utilisateur);
        }

        return id;
    }

    /**
     * Permet de récupérer l'identifiant int d'un utilisateur par son E-mail
     * @param email
     * @return identifiant int
     */

    public int getIdUserByEmail(String email) throws EmailInexistantException {
        /*boolean trouveEmail = false;
        int id = 0;

        for(Utilisateur u : utilisateurs) {
            if(u.getEmailUtilisateur().equals(email)) {
                trouveEmail = true;
                id = u.getIdUtilisateur();
            }
        }
        if(!trouveEmail) {
            throw new EmailInexistantException();
        }*/

        return utilisateurs.stream()
                .filter(utilisateur -> utilisateur.getEmailUtilisateur().equals(email))
                .findFirst()
                .orElseThrow(EmailInexistantException::new)
                .getIdUtilisateur();

        //return id;
    }

    /**
     * Permet à un professeur de créer une question
     * @param idUser id du professeur (on suppose qu'uniquement les professeurs pourront accéder à cette fonctionnalité donc
     *               pas besoin de vérifier s'il s'agit d'un professeur ou s'il s'agit d'un utilisateur existant)
     * @param libelleQuestion : libellé de la question
     * @param libellesReponses : libellés des réponses possibles
     * @return identifiant aléatoire chaîne de caractère (UUID)
     * @throws AuMoinsDeuxReponsesException : au moins deux réponses sont attendues
     * @throws LibelleQuestionNonRenseigneException : le libellé doit être obligatoirement non vide (non blank)
     */

    public String creerQuestion(int idUser, String libelleQuestion, String... libellesReponses) throws AuMoinsDeuxReponsesException, LibelleQuestionNonRenseigneException {
        String id;

        if(libellesReponses.length < 2) {
            throw new AuMoinsDeuxReponsesException();
        }
        else if(libelleQuestion.isBlank()) {
            throw new LibelleQuestionNonRenseigneException();
        }
        else {
            Question question = new Question(idUser, libelleQuestion, libellesReponses);
            id = question.getIdQuestion();
            questions.add(question);
        }

        return id;
    }


    /**
     * Permet de récupérer une question par son identifiant
     * @param idQuestion : id de la question concernée
     * @return l'objet Question concerné
     * @throws QuestionInexistanteException : l'identifiant donné ne correspond à aucune question
     */

    public Question getQuestionById(String idQuestion) throws QuestionInexistanteException {
        /*Question question = null;
        for(Question q : questions) {
            if(q.getIdQuestion().equals(idQuestion)) {
                question = q;
            }
        }
        if(question == null) {
            throw new QuestionInexistanteException();
        }

        return question;*/

        return questions.stream()
                .filter(question -> question.getIdQuestion().equals(idQuestion))
                .findFirst()
                .orElseThrow(QuestionInexistanteException::new);
    }

    /**
     * Permet à un étudiant de voter pour une réponse
     * @param idUser : identifiant entier de l'étudiant en question (là encore on suppose que l'idUser est correct et que c'est bien un étudiant. Cette
     *               vérification est déléguée au contrôleur REST)
     * @param idQuestion : identifiant de la question concernée
     * @param numeroProposition : numéro de la proposition (les réponses possibles sont stockées dans un tableau donc le
     *                          numéro correspond à l'indice dans le tableau)
     * @throws ADejaVoteException : l'étudiant concerné a déjà voté à cette question (éventuellement pour une autre réponse)
     * @throws NumeroPropositionInexistantException : le numéro de la proposition n'est pas un indice correct du tableau des propositions
     * de la question
     * @throws QuestionInexistanteException : la question identifiée n'existe pas
     */

    public void voterReponse(int idUser,String idQuestion, int numeroProposition) throws ADejaVoteException,
            NumeroPropositionInexistantException, QuestionInexistanteException {
        Question question = questions.stream()
                .filter(q -> q.getIdQuestion().equals(idQuestion))
                .findFirst()
                .orElseThrow(QuestionInexistanteException::new);

        question.voterPourUneReponse(idUser, numeroProposition);
    }


    /**
     * Vous devez dans la fonction ci-dessous vider toutes vos structures de données.
     * Pensez à remettre à 0 vos éventuels compteurs statiques (probablement dans la classe utilisateur)
     */

    public void reinitFacade(){
        Utilisateur.resetCompteur();
        utilisateurs.clear();
        questions.clear();
    }


    /**
     * Permet de récupérer un utilisateur par son email
     * @param username
     * @return
     */
    public Utilisateur getUtilisateurByEmail(String username) throws UtilisateurInexistantException {
        return utilisateurs.stream()
                .filter(utilisateur -> utilisateur.getEmailUtilisateur().contains(username))
                .findFirst()
                .orElseThrow(UtilisateurInexistantException::new);
    }


    /**
     * Permet de récupérer le résultat d'un vote à une question
     * @param idQuestion
     * @return
     * @throws QuestionInexistanteException
     */

    public ResultatVote[] getResultats(String idQuestion) throws QuestionInexistanteException {
        /*Question question = questions.stream()
                .filter(question1 -> question1.getIdQuestion().equals(idQuestion))
                .findFirst()
                .orElseThrow(QuestionInexistanteException::new);
        return question.getResultats();*/

        return questions.stream()
                .filter(question -> question.getIdQuestion().equals(idQuestion))
                .findFirst()
                .orElseThrow(QuestionInexistanteException::new)
                .getResultats();
    }
}
