package fr.orleans.info.wsi.cc.tpnote.modele;

import fr.orleans.info.wsi.cc.tpnote.DataTest;
import fr.orleans.info.wsi.cc.tpnote.DataTestImpl;
import fr.orleans.info.wsi.cc.tpnote.modele.exceptions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestFacadeQuizz {

    FacadeQuizz instance;
    DataTest data;

    public TestFacadeQuizz(){
        data = new DataTestImpl();
    }



    /**
     * Initialisation de la façade
     */
    @BeforeEach
    public void initialiseInstance(){

        instance = new FacadeQuizz();

    }


    /**
     * Tentative de création d'un compte avec un email déjà pris
     */


    @Test
    public void testCreerUtilisateur1()  {

        String email = data.emailProfBase();
        String motDePasse = data.motDePasseProfBase();

        String email1 = data.emailProfBase();
        String motDePasse1 = data.motDePasseProfBase()+"2";
        Assertions.assertDoesNotThrow(()->this.instance.creerUtilisateur(email,motDePasse));
        Assertions.assertThrows(EmailDejaUtiliseException.class,() ->this.instance.creerUtilisateur(email1,motDePasse1));
    }


    /**
     * Tentative de création d'un compte avec un mot de passe blank
     */
    @Test
    public void testCreerUtilisateur2()  {

        String email = data.emailProfBase();
        String motDePasse = "     ";

        Assertions.assertThrows(MotDePasseObligatoireException.class,() ->this.instance.creerUtilisateur(email,motDePasse));
    }


    /**
     * Tentative de création d'un compte avec un mot de passe nul
     */
    @Test
    public void testCreerUtilisateur3()  {

        String email = data.emailProfBase();
        String motDePasse = null;

        Assertions.assertThrows(MotDePasseObligatoireException.class,() ->this.instance.creerUtilisateur(email,motDePasse));
    }


    /**
     * Tentative d'une création de compte avec un email non valide
     */

    @Test
    public void testCreerUtilisateur4()  {
        String[]composante = data.emailProfBase().split("@");
        String email = composante[0]+composante[1];
        String motDePasse = data.motDePasseProfBase();

        Assertions.assertThrows(EmailNonValideException.class,() ->this.instance.creerUtilisateur(email,motDePasse));
    }

    /**
     * Création d'un compte valide
     * @throws MotDePasseObligatoireException
     * @throws EmailNonValideException
     * @throws EmailDejaUtiliseException
     */

    @Test
    public void testCreerUtilisateur6() throws MotDePasseObligatoireException, EmailNonValideException, EmailDejaUtiliseException {

        String email = data.emailProfBase();
        String motDePasse = data.motDePasseProfBase();
        Integer id = this.instance.creerUtilisateur(email,motDePasse);
        Assertions.assertNotNull(id,"le compte devrait être créé");
    }


    /**
     * Récupération correcte de l'identifiant d'un email existant
     * @throws MotDePasseObligatoireException
     * @throws EmailNonValideException
     * @throws EmailDejaUtiliseException
     * @throws EmailInexistantException
     */
    @Test
    public void testGetIdUserByEmail1() throws MotDePasseObligatoireException, EmailNonValideException, EmailDejaUtiliseException, EmailInexistantException {
        String email = data.emailProfBase();
        String motDePasse = data.motDePasseProfBase();
        Integer id = this.instance.creerUtilisateur(email,motDePasse);
        Assertions.assertEquals(id,instance.getIdUserByEmail(email));
    }


    /**
     * Tentative de récupération d'un identifiant pour un email inexistant
     * @throws MotDePasseObligatoireException
     * @throws EmailNonValideException
     * @throws EmailDejaUtiliseException
     * @throws EmailInexistantException
     */

    @Test
    public void testGetIdUserByEmail2() throws MotDePasseObligatoireException, EmailNonValideException, EmailDejaUtiliseException, EmailInexistantException {
        String email = data.emailProfBase();
        String motDePasse = data.motDePasseProfBase();
        String email1= data.emailEtudiantBase();
        this.instance.creerUtilisateur(email,motDePasse);
        Assertions.assertThrows(EmailInexistantException.class,() ->instance.getIdUserByEmail(email1));
    }

    /**
     * Création correcte d'une question
     * @throws MotDePasseObligatoireException
     * @throws EmailNonValideException
     * @throws EmailDejaUtiliseException
     * @throws LibelleQuestionNonRenseigneException
     * @throws AuMoinsDeuxReponsesException
     */

    @Test
    public void testCreerQuestion1() throws MotDePasseObligatoireException, EmailNonValideException, EmailDejaUtiliseException, LibelleQuestionNonRenseigneException, AuMoinsDeuxReponsesException {
        String email = data.emailProfBase();
        String motDePasse = data.motDePasseProfBase();
        int id = this.instance.creerUtilisateur(email,motDePasse);
        String idQuestion = this.instance.creerQuestion(id, data.libelleQuestion(), data.bonnesReponses());
        Assertions.assertNotNull(idQuestion);
    }

    /**
     * Tentative de création d'une question avec un nombre de réponses insuffisant
     * @throws MotDePasseObligatoireException
     * @throws EmailNonValideException
     * @throws EmailDejaUtiliseException
     * @throws LibelleQuestionNonRenseigneException
     * @throws AuMoinsDeuxReponsesException
     */

    @Test
    public void testCreerQuestion2() throws MotDePasseObligatoireException, EmailNonValideException, EmailDejaUtiliseException, LibelleQuestionNonRenseigneException, AuMoinsDeuxReponsesException {
        String email = data.emailProfBase();
        String motDePasse = data.motDePasseProfBase();
        int id = this.instance.creerUtilisateur(email,motDePasse);
        Assertions.assertThrows(AuMoinsDeuxReponsesException.class,()-> this.instance.creerQuestion(id, data.libelleQuestion(), data.mauvaisesReponses()));
    }


    /**
     * Tentative d'une création de question avec un libellé blank
     * @throws MotDePasseObligatoireException
     * @throws EmailNonValideException
     * @throws EmailDejaUtiliseException
     * @throws LibelleQuestionNonRenseigneException
     * @throws AuMoinsDeuxReponsesException
     */
    @Test
    public void testCreerQuestion3() throws MotDePasseObligatoireException, EmailNonValideException, EmailDejaUtiliseException, LibelleQuestionNonRenseigneException, AuMoinsDeuxReponsesException {
        String email = data.emailProfBase();
        String motDePasse = data.motDePasseProfBase();
        int id = this.instance.creerUtilisateur(email,motDePasse);
        Assertions.assertThrows(LibelleQuestionNonRenseigneException.class,()-> this.instance.creerQuestion(id, data.mauvaisLibelleQuestion(), data.bonnesReponses()));
    }


    /**
     * Récupération d'une question existante par son identifiant
     * @throws MotDePasseObligatoireException
     * @throws EmailNonValideException
     * @throws EmailDejaUtiliseException
     * @throws LibelleQuestionNonRenseigneException
     * @throws AuMoinsDeuxReponsesException
     * @throws QuestionInexistanteException
     */

    @Test
    public void testGetQuestionById1() throws MotDePasseObligatoireException, EmailNonValideException, EmailDejaUtiliseException, LibelleQuestionNonRenseigneException, AuMoinsDeuxReponsesException, QuestionInexistanteException {
        String email = data.emailProfBase();
        String motDePasse = data.motDePasseProfBase();
        int id = this.instance.creerUtilisateur(email,motDePasse);
        String idQuestion = this.instance.creerQuestion(id, data.libelleQuestion(), data.bonnesReponses());
        Assertions.assertNotNull(this.instance.getQuestionById(idQuestion));
    }


    /**
     * Tentative de récupération d'une question avec un identifiant erroné
     * @throws MotDePasseObligatoireException
     * @throws EmailNonValideException
     * @throws EmailDejaUtiliseException
     * @throws LibelleQuestionNonRenseigneException
     * @throws AuMoinsDeuxReponsesException
     * @throws QuestionInexistanteException
     */

    @Test
    public void testGetQuestionById2() throws MotDePasseObligatoireException, EmailNonValideException, EmailDejaUtiliseException, LibelleQuestionNonRenseigneException, AuMoinsDeuxReponsesException, QuestionInexistanteException {
        Assertions.assertThrows(QuestionInexistanteException.class,() ->this.instance.getQuestionById(data.identifiantQuestionBidon()));
    }


    /**
     * Vote correct d'un étudiant à une question existante et pour une réponse existante
     * @throws MotDePasseObligatoireException
     * @throws EmailNonValideException
     * @throws EmailDejaUtiliseException
     * @throws LibelleQuestionNonRenseigneException
     * @throws AuMoinsDeuxReponsesException
     * @throws QuestionInexistanteException
     * @throws NumeroPropositionInexistantException
     * @throws ADejaVoteException
     */
    @Test
    public void testVoterReponse1() throws MotDePasseObligatoireException, EmailNonValideException, EmailDejaUtiliseException, LibelleQuestionNonRenseigneException, AuMoinsDeuxReponsesException, QuestionInexistanteException, NumeroPropositionInexistantException, ADejaVoteException {
        String email = data.emailProfBase();
        String motDePasse = data.motDePasseProfBase();
        int id = this.instance.creerUtilisateur(email,motDePasse);
        String idQuestion = this.instance.creerQuestion(id, data.libelleQuestion(), data.bonnesReponses());

        String emailEtu = data.emailEtudiantBase();
        String motDePasseEtu = data.motDePasseEtudiantBase();
        int idEtu = this.instance.creerUtilisateur(emailEtu,motDePasseEtu);
        Assertions.assertDoesNotThrow(()->instance.voterReponse(idEtu,idQuestion,0));
    }

    /**
     * Tentative de double vote.
     * @throws MotDePasseObligatoireException
     * @throws EmailNonValideException
     * @throws EmailDejaUtiliseException
     * @throws LibelleQuestionNonRenseigneException
     * @throws AuMoinsDeuxReponsesException
     * @throws QuestionInexistanteException
     * @throws NumeroPropositionInexistantException
     * @throws ADejaVoteException
     */
    @Test
    public void testVoterReponse2() throws MotDePasseObligatoireException, EmailNonValideException, EmailDejaUtiliseException, LibelleQuestionNonRenseigneException, AuMoinsDeuxReponsesException, QuestionInexistanteException, NumeroPropositionInexistantException, ADejaVoteException {
        String email = data.emailProfBase();
        String motDePasse = data.motDePasseProfBase();
        int id = this.instance.creerUtilisateur(email,motDePasse);
        String idQuestion = this.instance.creerQuestion(id, data.libelleQuestion(), data.bonnesReponses());

        String emailEtu = data.emailEtudiantBase();
        String motDePasseEtu = data.motDePasseEtudiantBase();
        int idEtu = this.instance.creerUtilisateur(emailEtu,motDePasseEtu);
        instance.voterReponse(idEtu,idQuestion,0);
        Assertions.assertThrows(ADejaVoteException.class,()->instance.voterReponse(idEtu,idQuestion,1));
    }


    /**
     * Tentative de vote avec un mauvais identifiant de réponse
     * @throws MotDePasseObligatoireException
     * @throws EmailNonValideException
     * @throws EmailDejaUtiliseException
     * @throws LibelleQuestionNonRenseigneException
     * @throws AuMoinsDeuxReponsesException
     * @throws QuestionInexistanteException
     * @throws NumeroPropositionInexistantException
     * @throws ADejaVoteException
     */
    @Test
    public void testVoterReponse3() throws MotDePasseObligatoireException, EmailNonValideException, EmailDejaUtiliseException, LibelleQuestionNonRenseigneException, AuMoinsDeuxReponsesException, QuestionInexistanteException, NumeroPropositionInexistantException, ADejaVoteException {
        String email = data.emailProfBase();
        String motDePasse = data.motDePasseProfBase();
        int id = this.instance.creerUtilisateur(email,motDePasse);
        String idQuestion = this.instance.creerQuestion(id, data.libelleQuestion(), data.bonnesReponses());

        String emailEtu = data.emailEtudiantBase();
        String motDePasseEtu = data.motDePasseEtudiantBase();
        int idEtu = this.instance.creerUtilisateur(emailEtu,motDePasseEtu);
        Assertions.assertThrows(NumeroPropositionInexistantException.class,()->instance.voterReponse(idEtu,idQuestion,-1));
    }

    /**
     * Tentative de vote avec un mauvais identifiant de réponse
     * @throws MotDePasseObligatoireException
     * @throws EmailNonValideException
     * @throws EmailDejaUtiliseException
     * @throws LibelleQuestionNonRenseigneException
     * @throws AuMoinsDeuxReponsesException
     * @throws QuestionInexistanteException
     * @throws NumeroPropositionInexistantException
     * @throws ADejaVoteException
     */
    @Test
    public void testVoterReponse4() throws MotDePasseObligatoireException, EmailNonValideException, EmailDejaUtiliseException, LibelleQuestionNonRenseigneException, AuMoinsDeuxReponsesException, QuestionInexistanteException, NumeroPropositionInexistantException, ADejaVoteException {
        String email = data.emailProfBase();
        String motDePasse = data.motDePasseProfBase();
        int id = this.instance.creerUtilisateur(email,motDePasse);
        String idQuestion = this.instance.creerQuestion(id, data.libelleQuestion(), data.bonnesReponses());

        String emailEtu = data.emailEtudiantBase();
        String motDePasseEtu = data.motDePasseEtudiantBase();
        int idEtu = this.instance.creerUtilisateur(emailEtu,motDePasseEtu);
        Assertions.assertThrows(NumeroPropositionInexistantException.class,()->instance.voterReponse(idEtu,idQuestion,12));
    }


    /**
     * Tentative de vote avec un mauvais identifiant de question
     * @throws MotDePasseObligatoireException
     * @throws EmailNonValideException
     * @throws EmailDejaUtiliseException
     * @throws LibelleQuestionNonRenseigneException
     * @throws AuMoinsDeuxReponsesException
     * @throws QuestionInexistanteException
     * @throws NumeroPropositionInexistantException
     * @throws ADejaVoteException
     */

    @Test
    public void testVoterReponse5() throws MotDePasseObligatoireException, EmailNonValideException, EmailDejaUtiliseException, LibelleQuestionNonRenseigneException, AuMoinsDeuxReponsesException, QuestionInexistanteException, NumeroPropositionInexistantException, ADejaVoteException {
        String email = data.emailProfBase();
        String motDePasse = data.motDePasseProfBase();
        this.instance.creerUtilisateur(email,motDePasse);
        String emailEtu = data.emailEtudiantBase();
        String motDePasseEtu = data.motDePasseEtudiantBase();
        int idEtu = this.instance.creerUtilisateur(emailEtu,motDePasseEtu);
        Assertions.assertThrows(QuestionInexistanteException.class,()->instance.voterReponse(idEtu, data.identifiantQuestionBidon(), 1));
    }



    @Test
    public void testGetUtilisateurByEmail1() throws MotDePasseObligatoireException, EmailNonValideException, EmailDejaUtiliseException, UtilisateurInexistantException {
        String email = data.emailProfBase();
        String motDePasse = data.motDePasseProfBase();
        this.instance.creerUtilisateur(email,motDePasse);
        Assertions.assertNotNull(this.instance.getUtilisateurByEmail(email));
    }

    @Test
    public void testGetUtilisateurByEmail2() throws MotDePasseObligatoireException, EmailNonValideException, EmailDejaUtiliseException, UtilisateurInexistantException {
        String email = data.emailProfBase();
        Assertions.assertThrows(UtilisateurInexistantException.class,()->this.instance.getUtilisateurByEmail(email));
    }



    @Test
    public void testReInit() throws MotDePasseObligatoireException, EmailNonValideException, EmailDejaUtiliseException, LibelleQuestionNonRenseigneException, AuMoinsDeuxReponsesException {
        String email = data.emailProfBase();
        String motDePasse = data.motDePasseProfBase();
        int id = this.instance.creerUtilisateur(email,motDePasse);
        this.instance.creerQuestion(id, data.libelleQuestion(), data.bonnesReponses());
        instance.reinitFacade();
        Assertions.assertEquals(0,Utilisateur.ID);
        Assertions.assertDoesNotThrow(()->this.instance.creerUtilisateur(email,motDePasse));

    }



    @Test
    public void testGetResultatDunVote1() throws MotDePasseObligatoireException, EmailNonValideException, EmailDejaUtiliseException, LibelleQuestionNonRenseigneException, AuMoinsDeuxReponsesException, QuestionInexistanteException, NumeroPropositionInexistantException, ADejaVoteException {
        String email = data.emailProfBase();
        String motDePasse = data.motDePasseProfBase();
        int id = this.instance.creerUtilisateur(email,motDePasse);
        String idQuestion = this.instance.creerQuestion(id, data.libelleQuestion(), data.bonnesReponses());

        String emailEtu = data.emailEtudiantBase();
        String motDePasseEtu = data.motDePasseEtudiantBase();
        int idEtu = this.instance.creerUtilisateur(emailEtu,motDePasseEtu);
        instance.voterReponse(idEtu,idQuestion,0);
        ResultatVote[] resultats = instance.getResultats(idQuestion);
        Assertions.assertTrue(resultats.length==(data.bonnesReponses().length));
    }



    @Test
    public void testGetResultatDunVote2() throws MotDePasseObligatoireException, EmailNonValideException, EmailDejaUtiliseException, LibelleQuestionNonRenseigneException, AuMoinsDeuxReponsesException, QuestionInexistanteException, NumeroPropositionInexistantException, ADejaVoteException {
        String idQuestion = data.identifiantQuestionBidon();

        Assertions.assertThrows(QuestionInexistanteException.class,()->instance.getResultats(idQuestion));

    }
}
