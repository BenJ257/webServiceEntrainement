package fr.orleans.info.wsi.cc.tpnote.modele;

public class ResultatVote {
    private int idProposition;
    private String libelleProposition;
    private int nbReponses;
    private double pourcentage;

    public ResultatVote() {
    }

    public ResultatVote(int idProposition, String libelleProposition, int nbReponses, double pourcentage) {
        this.idProposition = idProposition;
        this.libelleProposition = libelleProposition;
        this.nbReponses = nbReponses;
        this.pourcentage = pourcentage;
    }

    public void setIdProposition(int idProposition) {
        this.idProposition = idProposition;
    }

    public void setLibelleProposition(String libelleProposition) {
        this.libelleProposition = libelleProposition;
    }

    public void setNbReponses(int nbReponses) {
        this.nbReponses = nbReponses;
    }

    public void setPourcentage(double pourcentage) {
        this.pourcentage = pourcentage;
    }

    public int getIdProposition() {
        return idProposition;
    }

    public String getLibelleProposition() {
        return libelleProposition;
    }

    public int getNbReponses() {
        return nbReponses;
    }

    public double getPourcentage() {
        return pourcentage;
    }
}
