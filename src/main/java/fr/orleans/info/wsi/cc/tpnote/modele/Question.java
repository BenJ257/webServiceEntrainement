package fr.orleans.info.wsi.cc.tpnote.modele;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.orleans.info.wsi.cc.tpnote.modele.exceptions.ADejaVoteException;
import fr.orleans.info.wsi.cc.tpnote.modele.exceptions.NumeroPropositionInexistantException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class Question {
    private int idCreateur;
    private String idQuestion;
    private String libelleQuestion;
    private String[] reponsesPossibles;
    private Collection<Integer>[] votesParQuestion;
    private Collection votes;

    public Question() {
    }

    public Question(int idCreateur,String libelleQuestion, String ...reponsesPossibles) {
        this.idCreateur =idCreateur;
        this.idQuestion = UUID.randomUUID().toString();
        this.libelleQuestion = libelleQuestion;
        this.reponsesPossibles = reponsesPossibles;
        this.votesParQuestion = new Collection[this.reponsesPossibles.length];
        this.votes = new ArrayList();
        for (int i=0;i<this.reponsesPossibles.length;i++) {
            this.votesParQuestion[i]=new ArrayList<>();
        }
    }



    /**
     * Permet d'enregistrer un vote d'un utilisateur à une réponse
     * @param idUser : identifiant de l'utilisateur
     * @param idLibelleReponse : position dans le tableau de la réponse concernée
     * @throws ADejaVoteException : l'identifiant a déjà voté auparavant
     * @throws NumeroPropositionInexistantException : le numéro de la réponse est erroné
     */

    public void voterPourUneReponse(int idUser,int idLibelleReponse) throws ADejaVoteException, NumeroPropositionInexistantException {
        if (this.votes.contains(idUser))
            throw new ADejaVoteException();
        if (idLibelleReponse<0 || idLibelleReponse>=reponsesPossibles.length)
            throw new NumeroPropositionInexistantException();

        this.votes.add(idUser);
        this.votesParQuestion[idLibelleReponse].add(idUser);
    }

    public String getIdQuestion() {
        return idQuestion;
    }

    public String getLibelleQuestion() {
        return libelleQuestion;
    }

    public String[] getReponsesPossibles() {
        return reponsesPossibles;
    }


    /**
     * Permet de récupérer les statistiques de votes pour chaque réponse
     * @JsonIgnore: permet d'éviter la sérialisation des résultats lorsque
     * l'on retourne un objet Question
     * @return
     */
    @JsonIgnore
    public ResultatVote[] getResultats() {
        ResultatVote[] resultats = new ResultatVote[votesParQuestion.length];
        for (int i=0;i<votesParQuestion.length;i++) {
            resultats[i]= new ResultatVote(i,reponsesPossibles[i],votesParQuestion[i].size(),((double)votesParQuestion[i].size())/(double) votes.size());
        }
        return resultats;

    }
}
