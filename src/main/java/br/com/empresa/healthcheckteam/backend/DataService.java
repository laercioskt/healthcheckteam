package br.com.empresa.healthcheckteam.backend;

import br.com.empresa.healthcheckteam.backend.data.*;
import br.com.empresa.healthcheckteam.backend.mock.MockDataService;

import java.io.Serializable;
import java.util.Collection;

/**
 * Back-end service interface for retrieving and updating product data.
 */
public abstract class DataService implements Serializable {

    public abstract Collection<Assessment> getAllAssessments();

    public abstract void updateAssessment(Assessment a);

    public abstract void deleteAssessment(int assessmentId);

    public abstract Assessment getAssessmentById(int assessmentId);

    public abstract Collection<Answer> getAllAnswers();

    public abstract void updateAnswer(Answer a);

    public abstract void deleteAnswer(int answerId);

    public abstract Answer getAnswerById(int answerId);

    public abstract Collection<Questao> getAllQuestoes();

    public abstract void updateQuestao(Questao q);

    public abstract void deleteQuestao(int questaoId);

    public abstract Questao getQuestaoById(int questaoId);

    public abstract Collection<Time> getAllTimes();

    public abstract void updateTime(Time t);

    public abstract void deleteTime(int timeId);

    public abstract Time getTimeById(int timeId);

    public abstract Collection<Product> getAllProducts();

    public abstract Collection<Category> getAllCategories();

    public abstract void updateProduct(Product p);

    public abstract void deleteProduct(int productId);

    public abstract Product getProductById(int productId);

    public abstract void updateCategory(Category category);

    public abstract void deleteCategory(int categoryId);

    public static DataService get() {
        return MockDataService.getInstance();
    }

}
