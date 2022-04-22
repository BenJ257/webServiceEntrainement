package fr.orleans.info.wsi.cc.tpnote.modele;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Utilisateur {

    private int idUtilisateur;
    private String emailUtilisateur;
    private String motDePasseUtilisateur;
    public String[] roles;

    static int ID=0;

    public String getEmailUtilisateur() {
        return emailUtilisateur;
    }

    public void setEmailUtilisateur(String emailUtilisateur) {
        this.emailUtilisateur = emailUtilisateur;
    }


    /**
     * @JsonIgnore permet de ne pas sortir le mot de passe lors de la récupération d'un profil
     * @return
     */
    @JsonIgnore
    public String getMotDePasseUtilisateur() {
        return motDePasseUtilisateur;
    }

    public void setMotDePasseUtilisateur(String motDePasseUtilisateur) {
        this.motDePasseUtilisateur = motDePasseUtilisateur;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public Utilisateur() {
    }

    public Utilisateur(String emailUtilisateur, String motDePasseUtilisateur) {
        this.emailUtilisateur = emailUtilisateur;
        this.motDePasseUtilisateur = motDePasseUtilisateur;
        this.idUtilisateur = ID;
        String s = (this.emailUtilisateur.split("@"))[1];
        switch (s){
            case "etu.univ-orleans.fr":{
                roles= new String[]{"ETUDIANT"};
                break;
            }
            case "univ-orleans.fr" : {
                roles= new String[]{"PROFESSEUR"};
                break;
            }
            default: {
                roles= new String[0];
            }
        }
        ID++;
    }


    public String[] getRoles(){
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    /**
     * Permet de réinitialiser le compteur
     */
    public static void resetCompteur(){
        ID=0;
    }
}