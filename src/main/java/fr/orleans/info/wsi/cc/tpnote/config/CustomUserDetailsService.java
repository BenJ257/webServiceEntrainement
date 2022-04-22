package fr.orleans.info.wsi.cc.tpnote.config;

import fr.orleans.info.wsi.cc.tpnote.controleur.ControleurQuizz;
import fr.orleans.info.wsi.cc.tpnote.modele.FacadeQuizz;
import fr.orleans.info.wsi.cc.tpnote.modele.Utilisateur;
import fr.orleans.info.wsi.cc.tpnote.modele.exceptions.UtilisateurInexistantException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FacadeQuizz facadeQuizz;

    //public CustomUserDetailsService() {}

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            Utilisateur utilisateur = facadeQuizz.getUtilisateurByEmail(email);
            return User.builder()
                    .username(utilisateur.getEmailUtilisateur())
                    .password(passwordEncoder.encode(utilisateur.getMotDePasseUtilisateur()))
                    .roles(utilisateur.getRoles())
                    .build();
        } catch (UtilisateurInexistantException e) {
            throw new UsernameNotFoundException("L'email " + email + " n'a pas été trouvé.");
        }
    }
}
