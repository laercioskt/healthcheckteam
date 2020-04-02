package br.com.empresa.healthcheckteam.backend.mock;

import br.com.empresa.healthcheckteam.backend.DataService;
import br.com.empresa.healthcheckteam.backend.data.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Mock data model. This implementation has very simplistic locking and does not
 * notify users of modifications.
 */
public class MockDataService extends DataService {

    private static MockDataService INSTANCE;

    private List<Assessment> assessments;
    private List<Answer> answers;
    private List<Questao> questoes;
    private List<Time> times;
    private List<Product> products;
    private List<Category> categories;
    private int nextAssessmentId = 0;
    private int nextAnswerId = 0;
    private int nextQuestaoId = 0;
    private int nextTimeId = 0;
    private int nextProductId = 0;
    private int nextCategoryId = 0;

    private MockDataService() {
        assessments = MockDataGenerator.createAssessments();
        answers = MockDataGenerator.createAnswers();
        questoes = MockDataGenerator.createQuestoes();
        times = MockDataGenerator.createTimes();
        categories = MockDataGenerator.createCategories();
        products = MockDataGenerator.createProducts(categories);
        nextProductId = products.size() + 1;
        nextCategoryId = categories.size() + 1;
    }

    public synchronized static DataService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MockDataService();
        }
        return INSTANCE;
    }

    @Override
    public Collection<Answer> getAllAnswers() {
        return Collections.unmodifiableList(answers);
    }

    @Override
    public void updateAnswer(Answer a) {
        if (a.getId() < 0) {
            // New answer
            a.setId(nextAnswerId++);
            answers.add(a);
            return;
        }
        for (int i = 0; i < answers.size(); i++) {
            if (answers.get(i).getId() == a.getId()) {
                answers.set(i, a);
                return;
            }
        }

        throw new IllegalArgumentException("No answer with id " + a.getId() + " found");
    }

    @Override
    public void deleteAnswer(int answerId) {
        Answer a = getAnswerById(answerId);
        if (a == null) {
            throw new IllegalArgumentException("Answer with id " + answerId + " not found");
        }
        answers.remove(a);
    }

    @Override
    public Answer getAnswerById(int answerId) {
        for (int i = 0; i < answers.size(); i++) {
            if (answers.get(i).getId() == answerId) {
                return answers.get(i);
            }
        }
        return null;
    }

    @Override
    public Collection<Assessment> getAllAssessments() {
        return Collections.unmodifiableList(assessments);
    }

    @Override
    public void updateAssessment(Assessment a) {
        if (a.getId() < 0) {
            // New assessment
            a.setId(nextAssessmentId++);
            assessments.add(a);
            return;
        }
        for (int i = 0; i < assessments.size(); i++) {
            if (assessments.get(i).getId() == a.getId()) {
                assessments.set(i, a);
                return;
            }
        }

        throw new IllegalArgumentException("No assessment with id " + a.getId() + " found");
    }

    @Override
    public void deleteAssessment(int assessmentId) {
        Assessment a = getAssessmentById(assessmentId);
        if (a == null) {
            throw new IllegalArgumentException("Assessment with id " + assessmentId + " not found");
        }
        assessments.remove(a);
    }

    @Override
    public Assessment getAssessmentById(int assessmentId) {
        for (int i = 0; i < assessments.size(); i++) {
            if (assessments.get(i).getId() == assessmentId) {
                return assessments.get(i);
            }
        }
        return null;
    }

    @Override
    public Collection<Questao> getAllQuestoes() {
        return Collections.unmodifiableList(questoes);
    }

    @Override
    public void updateQuestao(Questao q) {
        if (q.getId() < 0) {
            // New product
            q.setId(nextQuestaoId++);
            questoes.add(q);
            return;
        }
        for (int i = 0; i < questoes.size(); i++) {
            if (questoes.get(i).getId() == q.getId()) {
                questoes.set(i, q);
                return;
            }
        }

        throw new IllegalArgumentException("No questao with id " + q.getId() + " found");
    }

    @Override
    public void deleteQuestao(int questaoId) {
        Questao q = getQuestaoById(questaoId);
        if (q == null) {
            throw new IllegalArgumentException("Questao with id " + questaoId + " not found");
        }
        questoes.remove(q);
    }

    @Override
    public Questao getQuestaoById(int questaoId) {
        for (int i = 0; i < questoes.size(); i++) {
            if (questoes.get(i).getId() == questaoId) {
                return questoes.get(i);
            }
        }
        return null;
    }

    @Override
    public Collection<Time> getAllTimes() {
        return Collections.unmodifiableList(times);
    }

    @Override
    public void updateTime(Time t) {
        if (t.getId() < 0) {
            // New product
            t.setId(nextTimeId++);
            times.add(t);
            return;
        }
        for (int i = 0; i < times.size(); i++) {
            if (times.get(i).getId() == t.getId()) {
                times.set(i, t);
                return;
            }
        }

        throw new IllegalArgumentException("No time with id " + t.getId() + " found");
    }

    @Override
    public void deleteTime(int timeId) {
        Time t = getTimeById(timeId);
        if (t == null) {
            throw new IllegalArgumentException("Time with id " + timeId + " not found");
        }
        times.remove(t);
    }

    @Override
    public Time getTimeById(int timeId) {
        for (int i = 0; i < times.size(); i++) {
            if (times.get(i).getId() == timeId) {
                return times.get(i);
            }
        }
        return null;
    }

    @Override
    public synchronized List<Product> getAllProducts() {
        return Collections.unmodifiableList(products);
    }

    @Override
    public synchronized List<Category> getAllCategories() {
        return Collections.unmodifiableList(categories);
    }

    @Override
    public synchronized void updateProduct(Product p) {
        if (p.getId() < 0) {
            // New product
            p.setId(nextProductId++);
            products.add(p);
            return;
        }
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == p.getId()) {
                products.set(i, p);
                return;
            }
        }

        throw new IllegalArgumentException("No product with id " + p.getId()
                + " found");
    }

    @Override
    public synchronized Product getProductById(int productId) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == productId) {
                return products.get(i);
            }
        }
        return null;
    }

    @Override
    public void updateCategory(Category category) {
        if (category.getId() < 0) {
            category.setId(nextCategoryId++);
            categories.add(category);
        }
    }

    @Override
    public void deleteCategory(int categoryId) {
        if (categories.removeIf(category -> category.getId() == categoryId)) {
            getAllProducts().forEach(product -> {
                product.getCategory().removeIf(category -> category.getId() == categoryId);
            });
        }
    }

    @Override
    public synchronized void deleteProduct(int productId) {
        Product p = getProductById(productId);
        if (p == null) {
            throw new IllegalArgumentException("Product with id " + productId
                    + " not found");
        }
        products.remove(p);
    }
}
