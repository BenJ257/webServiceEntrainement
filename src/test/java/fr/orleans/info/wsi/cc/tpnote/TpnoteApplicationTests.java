package fr.orleans.info.wsi.cc.tpnote;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.orleans.info.wsi.cc.tpnote.modele.FacadeQuizz;
import fr.orleans.info.wsi.cc.tpnote.modele.Question;
import fr.orleans.info.wsi.cc.tpnote.modele.ResultatVote;
import fr.orleans.info.wsi.cc.tpnote.modele.Utilisateur;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

import java.net.URI;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc

class TpnoteApplicationTests {




    @Autowired
    FacadeQuizz facadeQuizz;

    @Autowired
    MockMvc mvc;

    @Autowired
    DataTest dataTest;


    /**
     * Réinitialisation complète de la façade et
     * injection des datas par défaut
     */
    @BeforeEach
    public void reinitialiserFacade(){
        facadeQuizz.reinitFacade();

    }


    /**
     * Création d'un professeur avec l'adresse xxxx@univ-orleans.fr
     * @throws Exception
     * Code 201 retourné + Location
     */

    @Test
    public void testCreationCompte1() throws Exception {

        mvc.perform(post(URI.create("/api/quizz/utilisateur"))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("pseudo="+dataTest.emailProfBase()+"&password="+dataTest.motDePasseProfBase()))
                .andExpect(status().isCreated()).andExpect(header().exists("Location"));
    }


    /**
     * Création d'un étudiant avec l'adresse xxxx@etu.univ-orleans.fr
     * @throws Exception
     * Code 201 retourné + Location
     */

    @Test
    public void testCreationCompte2() throws Exception {

        mvc.perform(post(URI.create("/api/quizz/utilisateur"))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("pseudo="+dataTest.emailEtudiantBase()+"&password="+dataTest.motDePasseEtudiantBase()))
                .andExpect(status().isCreated()).andExpect(header().exists("Location"));
    }


    /**
     * Tentative de création d'un compte avec un mail déjà utilisé
     * @throws Exception
     * code 409 retourné
     */

    @Test
    public void testCreationCompte3() throws Exception {

        mvc.perform(post(URI.create("/api/quizz/utilisateur"))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("pseudo="+dataTest.emailProfBase()+"&password="+dataTest.motDePasseProfBase()))
                .andExpect(status().isCreated()).andExpect(header().exists("Location"));

        mvc.perform(post(URI.create("/api/quizz/utilisateur"))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("pseudo="+dataTest.emailProfBase()+"&password="+dataTest.motDePasseProfBase()+"1"))
                .andExpect(status().isConflict());
    }



    /**
     * Tentative de création d'un compte avec des données erronées
     * code 406 retourné
     * @throws Exception
     */

    @Test
    public void testCreationCompte4() throws Exception {

        mvc.perform(post(URI.create("/api/quizz/utilisateur"))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("pseudo="+dataTest.emailErrone()+"&password="+dataTest.motDePasseProfBase()))
                .andExpect(status().isNotAcceptable());
    }


    /**
     * Tentative de création d'un compte avec des données erronées
     * code 406 retourné
     * @throws Exception
     */

    @Test
    public void testCreationCompte5() throws Exception {

        mvc.perform(post(URI.create("/api/quizz/utilisateur"))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("pseudo="+dataTest.emailEtudiantBase()+"&password="+dataTest.mauvaisMotDePasse()))
                .andExpect(status().isNotAcceptable());
    }


    /**
     * Un utilisateur accède à son profil
     * Code attendu : 200
     * @throws Exception
     */
    @Test
    public void testGetProfil1() throws Exception {

        AtomicInteger identifiant = new AtomicInteger();
        mvc.perform(post(URI.create("/api/quizz/utilisateur"))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("pseudo="+dataTest.emailProfBase()+"&password="+dataTest.motDePasseProfBase()))
                .andExpect(status().isCreated()).andExpect(header().exists("Location"))
                .andDo((x) ->{
                    String[] t=x.getResponse().getHeader("Location").split("/");
                    identifiant.set(Integer.valueOf(t[t.length - 1]));
                });


        ObjectMapper objectMapper = new ObjectMapper();
        mvc.perform(get(URI.create("/api/quizz/utilisateur/"+identifiant.get()))
                .with(httpBasic(dataTest.emailProfBase(), dataTest.motDePasseProfBase())))
                .andExpect(status().isOk())
                .andDo((x)->
                {

                  Utilisateur utilisateur = objectMapper.readValue(x.getResponse().getContentAsString(),Utilisateur.class);
                  Assertions.assertEquals(dataTest.emailProfBase(),utilisateur.getEmailUtilisateur());
                });

    }

    /**
     * Un utilisateur essaie d'accéder à un profil qui n'est pas le sien
     * Code attendu : 403
     * @throws Exception
     */
    @Test
    public void testGetProfil2() throws Exception {
        creationCompteProfesseur();

        AtomicInteger identifiant = new AtomicInteger();
        mvc.perform(post(URI.create("/api/quizz/utilisateur"))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("pseudo="+dataTest.emailEtudiantBase()+"&password="+dataTest.motDePasseEtudiantBase()))
                .andExpect(status().isCreated()).andExpect(header().exists("Location"))
                .andDo((x) ->{
                    String[] t=x.getResponse().getHeader("Location").split("/");
                    identifiant.set(Integer.valueOf(t[t.length - 1]));
                });



        mvc.perform(get(URI.create("/api/quizz/utilisateur/"+identifiant.get()))
                        .with(httpBasic(dataTest.emailProfBase(), dataTest.motDePasseProfBase())))
                .andExpect(status().isForbidden());
    }



    private void creationCompteProfesseur() throws Exception {
        mvc.perform(post(URI.create("/api/quizz/utilisateur"))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("pseudo="+dataTest.emailProfBase()+"&password="+dataTest.motDePasseProfBase()))
                .andExpect(status().isCreated()).andExpect(header().exists("Location"));

    }



    private void creationCompteEtudiant() throws Exception {
        mvc.perform(post(URI.create("/api/quizz/utilisateur"))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("pseudo="+dataTest.emailEtudiantBase()+"&password="+dataTest.motDePasseEtudiantBase()))
                .andExpect(status().isCreated()).andExpect(header().exists("Location"));

    }




    /**
     * Création d'une question par un professeur
     * Code attendu : 201
     * Location attendue
     * @throws Exception
     */

    @Test
    public void testCreationQuestion1() throws Exception {

        creationCompteProfesseur();
        Question question = new Question(0, dataTest.libelleQuestion(), dataTest.bonnesReponses());

        ObjectMapper objectMapper= new ObjectMapper();

        mvc.perform(post(URI.create("/api/quizz/question"))
                        .with(httpBasic(dataTest.emailProfBase(), dataTest.motDePasseProfBase()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(question)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }


    /**
     * Tentative de création d'une question par un étudiant
     * Code attendu : 403 (Forbidden)
     * @throws Exception
     */

    @Test
    public void testCreationQuestion2() throws Exception {
        creationCompteEtudiant();
        Question question = new Question(0, dataTest.libelleQuestion(), dataTest.bonnesReponses());

        ObjectMapper objectMapper= new ObjectMapper();

        mvc.perform(post(URI.create("/api/quizz/question"))
                        .with(httpBasic(dataTest.emailEtudiantBase(), dataTest.motDePasseEtudiantBase()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(question)))
                .andExpect(status().isForbidden());
    }


    /**
     * Tentative de création d'une question par un professeur mais avec des attributs non corrects
     * Code attendu : 406
     * @throws Exception
     */

    @Test
    public void testCreationQuestion3() throws Exception {


        creationCompteProfesseur();
        Question question = new Question(0, dataTest.libelleQuestion(), dataTest.mauvaisesReponses());

        ObjectMapper objectMapper= new ObjectMapper();

        mvc.perform(post(URI.create("/api/quizz/question"))
                        .with(httpBasic(dataTest.emailProfBase(), dataTest.motDePasseProfBase()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(question)))
                .andExpect(status().isNotAcceptable());

    }



    /**
     * Tentative de création d'une question par un professeur mais avec des attributs non corrects
     * Code attendu : 406
     * @throws Exception
     */

    @Test
    public void testCreationQuestion4() throws Exception {
        creationCompteProfesseur();
        Question question = new Question(0, dataTest.mauvaisLibelleQuestion(), dataTest.bonnesReponses());

        ObjectMapper objectMapper= new ObjectMapper();

        mvc.perform(post(URI.create("/api/quizz/question"))
                        .with(httpBasic(dataTest.emailProfBase(), dataTest.motDePasseProfBase()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(question)))
                .andExpect(status().isNotAcceptable());
    }


    /**
     * Vote correct d'un étudiant à une question existante
     * Code attendu : 202 (accepted)
     * @throws Exception
     */

    @Test
    public void testVoter1() throws Exception {

        creationCompteProfesseur();
        creationCompteEtudiant();
        Question question = new Question(0, dataTest.libelleQuestion(), dataTest.bonnesReponses());
        AtomicReference<String> identifiantQuestion = new AtomicReference<>("");

        ObjectMapper objectMapper= new ObjectMapper();

        // Création de la question par le professeur et récupération de l'identifiant de la question générée
        mvc.perform(post(URI.create("/api/quizz/question"))
                        .with(httpBasic(dataTest.emailProfBase(), dataTest.motDePasseProfBase()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(question)))
                .andExpect(status().isCreated())
                .andDo((v) -> {
                    identifiantQuestion.set(v.getResponse().getHeader("Location"));
                });
        String[] idDecompose = identifiantQuestion.get().split("/");
        String idRecupere = idDecompose[idDecompose.length-1];



        // Vote de l'étudiant
        mvc.perform(put(URI.create("/api/quizz/question/"+idRecupere+"/vote"))
                        .with(httpBasic(dataTest.emailEtudiantBase(), dataTest.motDePasseEtudiantBase()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("idReponse=1"))
                .andExpect(status().isAccepted());

    }



    /**
     * Vote non correct d'un étudiant à une question existante
     * Code attendu : 406
     * @throws Exception
     */

    @Test
    public void testVoter2() throws Exception {

        creationCompteProfesseur();
        creationCompteEtudiant();

        Question question = new Question(0, dataTest.libelleQuestion(), dataTest.bonnesReponses());
        AtomicReference<String> identifiantQuestion = new AtomicReference<>("");

        ObjectMapper objectMapper= new ObjectMapper();

        // Création de la question par le professeur et récupération de l'identifiant de la question générée
        mvc.perform(post(URI.create("/api/quizz/question"))
                        .with(httpBasic(dataTest.emailProfBase(), dataTest.motDePasseProfBase()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(question)))
                .andExpect(status().isCreated())
                .andDo((v) -> {
                    identifiantQuestion.set(v.getResponse().getHeader("Location"));
                });
        String[] idDecompose = identifiantQuestion.get().split("/");
        String idRecupere = idDecompose[idDecompose.length-1];



        // Vote de l'étudiant
        mvc.perform(put(URI.create("/api/quizz/question/"+idRecupere+"/vote"))
                        .with(httpBasic(dataTest.emailEtudiantBase(), dataTest.motDePasseEtudiantBase()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("idReponse=12"))
                .andExpect(status().isNotAcceptable());
    }




    /**
     * Un étudiant tente de voter deux fois
     * Code attendu : 409
     * @throws Exception
     */

    @Test
    public void testVoter3() throws Exception {
        creationCompteProfesseur();
        creationCompteEtudiant();


        Question question = new Question(0, dataTest.libelleQuestion(), dataTest.bonnesReponses());
        AtomicReference<String> identifiantQuestion = new AtomicReference<>("");

        ObjectMapper objectMapper= new ObjectMapper();

        // Création de la question par le professeur et récupération de l'identifiant de la question générée
        mvc.perform(post(URI.create("/api/quizz/question"))
                        .with(httpBasic(dataTest.emailProfBase(), dataTest.motDePasseProfBase()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(question)))
                .andExpect(status().isCreated())
                .andDo((v) -> {
                    identifiantQuestion.set(v.getResponse().getHeader("Location"));
                });
        String[] idDecompose = identifiantQuestion.get().split("/");
        String idRecupere = idDecompose[idDecompose.length-1];



        // Vote de l'étudiant
        mvc.perform(put(URI.create("/api/quizz/question/"+idRecupere+"/vote"))
                        .with(httpBasic(dataTest.emailEtudiantBase(), dataTest.motDePasseEtudiantBase()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("idReponse=1"))
                .andExpect(status().isAccepted());


        // Vote de l'étudiant
        mvc.perform(put(URI.create("/api/quizz/question/"+idRecupere+"/vote"))
                        .with(httpBasic(dataTest.emailEtudiantBase(), dataTest.motDePasseEtudiantBase()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("idReponse=0"))
                .andExpect(status().isConflict());

    }




    /**
     * Un professeur tente de répondre à une question
     * Code attendu : 403
     * @throws Exception
     */

    @Test
    public void testVoter4() throws Exception {

        creationCompteProfesseur();

        Question question = new Question(0, dataTest.libelleQuestion(), dataTest.bonnesReponses());
        AtomicReference<String> identifiantQuestion = new AtomicReference<>("");

        ObjectMapper objectMapper= new ObjectMapper();

        // Création de la question par le professeur et récupération de l'identifiant de la question générée
        mvc.perform(post(URI.create("/api/quizz/question"))
                        .with(httpBasic(dataTest.emailProfBase(), dataTest.motDePasseProfBase()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(question)))
                .andExpect(status().isCreated())
                .andDo((v) -> {
                    identifiantQuestion.set(v.getResponse().getHeader("Location"));
                });
        String[] idDecompose = identifiantQuestion.get().split("/");
        String idRecupere = idDecompose[idDecompose.length-1];



        // Vote du professeur
        mvc.perform(put(URI.create("/api/quizz/question/"+idRecupere+"/vote"))
                        .with(httpBasic(dataTest.emailProfBase(), dataTest.motDePasseProfBase()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("idReponse=1"))
                .andExpect(status().isForbidden());

    }


    /**
     * Un professeur récupère une question existante
     * @throws Exception
     */


    @Test
    public void testGetQuestion1() throws Exception {

        creationCompteProfesseur();

        Question question = new Question(0, dataTest.libelleQuestion(), dataTest.bonnesReponses());
        AtomicReference<String> identifiantQuestion = new AtomicReference<>("");
        AtomicReference<Question> questionRecuperee= new AtomicReference<>();

        ObjectMapper objectMapper= new ObjectMapper();

        // Création de la question par le professeur et récupération de l'identifiant de la question générée
        mvc.perform(post(URI.create("/api/quizz/question"))
                        .with(httpBasic(dataTest.emailProfBase(), dataTest.motDePasseProfBase()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(question)))
                .andExpect(status().isCreated())
                .andDo((v) -> {
                    identifiantQuestion.set(v.getResponse().getHeader("Location"));
                });
        String[] idDecompose = identifiantQuestion.get().split("/");
        String idQuestion = idDecompose[idDecompose.length-1];

        mvc.perform(get(URI.create("/api/quizz/question/"+ idQuestion))
                .with(httpBasic(dataTest.emailProfBase(), dataTest.motDePasseProfBase())).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo((x)->{questionRecuperee.set(objectMapper.readValue(x.getResponse().getContentAsString(), Question.class));});

        Question res = questionRecuperee.get();
        if (Objects.nonNull(res)) {
            Assertions.assertEquals(idQuestion,res.getIdQuestion());
        }
        else
            Assertions.fail("Question non récupérée");

    }


    /**
     * Un étudiant récupère une question existante
     * @throws Exception
     */
    @Test
    public void testGetQuestion2() throws Exception {
        creationCompteProfesseur();
        creationCompteEtudiant();

        Question question = new Question(0, dataTest.libelleQuestion(), dataTest.bonnesReponses());
        AtomicReference<String> identifiantQuestion = new AtomicReference<>("");
        AtomicReference<Question> questionRecuperee= new AtomicReference<>();

        ObjectMapper objectMapper= new ObjectMapper();

        // Création de la question par le professeur et récupération de l'identifiant de la question générée
        mvc.perform(post(URI.create("/api/quizz/question"))
                        .with(httpBasic(dataTest.emailProfBase(), dataTest.motDePasseProfBase()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(question)))
                .andExpect(status().isCreated())
                .andDo((v) -> {
                    identifiantQuestion.set(v.getResponse().getHeader("Location"));
                });
        String[] idDecompose = identifiantQuestion.get().split("/");
        String idQuestion = idDecompose[idDecompose.length-1];


        mvc.perform(get(URI.create("/api/quizz/question/"+ idQuestion))
                        .with(httpBasic(dataTest.emailEtudiantBase(), dataTest.motDePasseEtudiantBase())).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo((x)->{questionRecuperee.set(objectMapper.readValue(x.getResponse().getContentAsString(), Question.class));});

        Question res = questionRecuperee.get();
        if (Objects.nonNull(res)) {
            Assertions.assertEquals(idQuestion,res.getIdQuestion());
        }
        else
            Assertions.fail("Question non récupérée");

    }


    /**
     * Un utilisateur tente de récupérer une question inexistante
     * @throws Exception
     * Code attendu : 404
     */

    @Test
    public void testGetQuestion3() throws Exception {

        creationCompteProfesseur();

        String idQuestion = dataTest.identifiantQuestionBidon();
        mvc.perform(get(URI.create("/api/quizz/question/"+ idQuestion))
                        .with(httpBasic(dataTest.emailProfBase(), dataTest.motDePasseProfBase()))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());

    }

    /**
     * Un prof récupère les résultats d'une question
     * Code attendu : 200
     * Contenu attendu : un tableau avec les trois objets ResultatVote
     * @throws Exception
     */
    @Test
    public void testGetResultats1() throws Exception {

        creationCompteProfesseur();
        Question question = new Question(0, dataTest.libelleQuestion(), dataTest.bonnesReponses());
        AtomicReference<String> identifiantQuestion = new AtomicReference<>("");

        ObjectMapper objectMapper= new ObjectMapper();

        // Création de la question par le professeur et récupération de l'identifiant de la question générée
        mvc.perform(post(URI.create("/api/quizz/question"))
                        .with(httpBasic(dataTest.emailProfBase(), dataTest.motDePasseProfBase()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(question)))
                .andExpect(status().isCreated())
                .andDo((v) -> {
                    String[] r = v.getResponse().getHeader("Location").split("/");
                    identifiantQuestion.set(r[r.length-1]);
                });
        String[] idDecompose = identifiantQuestion.get().split("/");
        String idRecupere = idDecompose[idDecompose.length-1];

        mvc.perform(get(URI.create("/api/quizz/question/"+idRecupere+"/vote"))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .with(httpBasic(dataTest.emailProfBase(), dataTest.motDePasseProfBase()))
                )
                .andExpect(status().isOk())
                .andDo((x)->{
                    Assertions.assertEquals(dataTest.bonnesReponses().length,objectMapper.readValue(x.getResponse().getContentAsString(), ResultatVote[].class).length);
                });

    }


    /**
     * Un étudiant tente de récupérer les résultats d'une question
     * Code attendu : 403
     * @throws Exception
     */
    @Test
    public void testGetResultats2() throws Exception {

        creationCompteProfesseur();
        creationCompteEtudiant();

        Question question = new Question(0, dataTest.libelleQuestion(), dataTest.bonnesReponses());
        AtomicReference<String> identifiantQuestion = new AtomicReference<>("");

        ObjectMapper objectMapper= new ObjectMapper();

        // Création de la question par le professeur et récupération de l'identifiant de la question générée
        mvc.perform(post(URI.create("/api/quizz/question"))
                        .with(httpBasic(dataTest.emailProfBase(), dataTest.motDePasseProfBase()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(question)))
                .andExpect(status().isCreated())
                .andDo((v) -> {
                    String[] r = v.getResponse().getHeader("Location").split("/");
                    identifiantQuestion.set(r[r.length-1]);
                });
        String[] idDecompose = identifiantQuestion.get().split("/");
        String idRecupere = idDecompose[idDecompose.length-1];

        mvc.perform(get(URI.create("/api/quizz/question/"+idRecupere+"/vote"))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .with(httpBasic(dataTest.emailEtudiantBase(), dataTest.motDePasseEtudiantBase()))
                )
                .andExpect(status().isForbidden());

    }

    /**
     * Un prof tente de récupérer les résultats d'une question inexistante
     * Code attendu : 404
     * @throws Exception
     */
    @Test
    public void testGetResultats3() throws Exception {

        creationCompteProfesseur();


        String idRecupere = dataTest.identifiantQuestionBidon();
        mvc.perform(get(URI.create("/api/quizz/question/"+idRecupere+"/vote"))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .with(httpBasic(dataTest.emailProfBase(), dataTest.motDePasseProfBase()))
                )
                .andExpect(status().isNotFound());

    }


}
