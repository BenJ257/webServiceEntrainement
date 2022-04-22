package fr.orleans.info.wsi.cc.tpnote;

import fr.orleans.info.wsi.cc.tpnote.modele.FacadeQuizz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class TpnoteApplication {


    public static void main(String[] args) {
        SpringApplication.run(TpnoteApplication.class, args);
    }

}
