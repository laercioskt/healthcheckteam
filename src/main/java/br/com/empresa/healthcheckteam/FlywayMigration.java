package br.com.empresa.healthcheckteam;

import br.com.empresa.healthcheckteam.db.migration.V3__CreateDefaultQuestionsAndAnswerOptions;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class FlywayMigration implements CommandLineRunner {

    @Autowired
    private V3__CreateDefaultQuestionsAndAnswerOptions V3__CreateDefaultQuestionsAndAnswerOptions;

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .javaMigrations(V3__CreateDefaultQuestionsAndAnswerOptions)
                .load();
        flyway.migrate();
    }
}
