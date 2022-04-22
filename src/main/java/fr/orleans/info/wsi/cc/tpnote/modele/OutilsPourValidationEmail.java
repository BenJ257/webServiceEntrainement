package fr.orleans.info.wsi.cc.tpnote.modele;

import java.util.regex.Pattern;

public class OutilsPourValidationEmail {

    /**
     * Permet de vérifier la validité d'une adresse mail
     * @param emailAddress
     * @return statut de la validité (true si OK, false sinon)
     */
        public static boolean patternMatches(String emailAddress) {
            String regexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
            return Pattern.compile(regexPattern)
                    .matcher(emailAddress)
                    .matches();
        }


}
