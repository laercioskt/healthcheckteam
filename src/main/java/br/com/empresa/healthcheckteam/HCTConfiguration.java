package br.com.empresa.healthcheckteam;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class HCTConfiguration {

    @Bean
    public DataSource dataSource() throws URISyntaxException {
        HikariConfig config = new HikariConfig();
        URI dbUri = new URI(System.getenv("DATABASE_URL"));
        config.setJdbcUrl("jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath());
        config.setUsername(dbUri.getUserInfo().split(":")[0]);
        config.setPassword(dbUri.getUserInfo().split(":")[1]);
        return new HikariDataSource(config);
    }

    @Bean
    @DependsOn("dataSource")
    public Flyway flyway() throws URISyntaxException {
        Flyway load = Flyway.configure().baselineOnMigrate(true).dataSource(dataSource()).load();
        load.migrate();
        return load;
    }

}
