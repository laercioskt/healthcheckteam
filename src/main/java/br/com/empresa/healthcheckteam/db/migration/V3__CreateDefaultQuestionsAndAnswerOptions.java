package br.com.empresa.healthcheckteam.db.migration;

import br.com.empresa.healthcheckteam.backend.data.Question;
import br.com.empresa.healthcheckteam.backend.data.Question.QuestionBuilder;
import br.com.empresa.healthcheckteam.backend.defaultdata.DefaultDataGenerator;
import br.com.empresa.healthcheckteam.backend.repository.QuestionRepository;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

@ConditionalOnProperty(value="springconfig", havingValue="production")
@Component
public class V3__CreateDefaultQuestionsAndAnswerOptions extends BaseJavaMigration {

    private static final Logger LOGGER = getLogger(V3__CreateDefaultQuestionsAndAnswerOptions.class);

    @Autowired
    QuestionRepository questionRepository;

    @Override
    public void migrate(Context context) {
        LOGGER.info("Starting migration");
        DefaultDataGenerator.defaultQuestions.forEach((question, options) -> {
            QuestionBuilder builder = new QuestionBuilder().withDescription(question);
            Arrays.stream(options).forEach(builder::withAnswerOption);
            Question savedQuestion = questionRepository.save(builder.build());
            LOGGER.info(format("Saved question %s", savedQuestion));
        });
        LOGGER.info("End migration");
    }

}
