package fr.orleans.info.wsi.cc.tpnote;

import org.springframework.stereotype.Component;

@Component
public class DataTestImpl implements DataTest {

    @Override
    public String emailProfBase(){
        return "yohan.boichut@univ-orleans.fr";
    }


    @Override
    public  String motDePasseProfBase(){
        return "babar";
    }


    @Override
    public  String emailEtudiantBase(){
        return "gerard.menvussaa@etu.univ-orleans.fr";
    }


    @Override
    public  String motDePasseEtudiantBase(){
        return "celestine";
    }

    @Override
    public  String libelleQuestion(){
        return "Quelle est la couleur du cheval blanc d'Henry IV ?";
    }


    @Override
    public  String mauvaisLibelleQuestion(){
        return "         ";
    }




    @Override
    public  String identifiantQuestionBidon(){
        return "identifiantBidon";
    }
    @Override
    public  String[] bonnesReponses(){
        return new String[]{"Blanc","Noir","Rouge"};
    }


    @Override
    public  String[] mauvaisesReponses(){
        return new String[]{"Rouge"};
    }

    @Override
    public String emailErrone() {
        return "yohan.boichutuniv-orleans.fr";
    }

    @Override
    public String mauvaisMotDePasse() {
        return "       ";
    }


}
