package br.com.empresa.healthcheckteam.backend.mock;

import br.com.empresa.healthcheckteam.backend.data.*;

import java.math.BigDecimal;
import java.util.*;

public class MockDataGenerator {
    private static int nextQuestaoId = 1;
    private static int nextTimeId = 1;
    private static int nextCategoryId = 1;
    private static int nextProductId = 1;
    private static final Random random = new Random(1);

    private static final String timesNames[] = new String[]{
            "Children's books", "Best sellers", "Romance", "Mystery",
            "Thriller", "Sci-fi", "Non-fiction", "Cookbooks"};

    private static final String questoesDescricoes[] = new String[]{
            "Children's books", "Best sellers", "Romance", "Mystery",
            "Thriller", "Sci-fi", "Non-fiction", "Cookbooks"};

    private static final String categoryNames[] = new String[]{
            "Children's books", "Best sellers", "Romance", "Mystery",
            "Thriller", "Sci-fi", "Non-fiction", "Cookbooks"};

    private static String[] word1 = new String[]{"The art of", "Mastering",
            "The secrets of", "Avoiding", "For fun and profit: ",
            "How to fail at", "10 important facts about",
            "The ultimate guide to", "Book of", "Surviving", "Encyclopedia of",
            "Very much", "Learning the basics of", "The cheap way to",
            "Being awesome at", "The life changer:", "The Vaadin way:",
            "Becoming one with", "Beginners guide to",
            "The complete visual guide to", "The mother of all references:"};

    private static String[] word2 = new String[]{"gardening",
            "living a healthy life", "designing tree houses", "home security",
            "intergalaxy travel", "meditation", "ice hockey",
            "children's education", "computer programming", "Vaadin TreeTable",
            "winter bathing", "playing the cello", "dummies", "rubber bands",
            "feeling down", "debugging", "running barefoot",
            "speaking to a big audience", "creating software", "giant needles",
            "elephants", "keeping your wife happy"};

    static List<Time> createTimes() {
        List<Time> times = new ArrayList<Time>();
        for (String name : timesNames) {
            Time c = createTime(name);
            times.add(c);
        }
        return times;

    }

    static List<Questao> createQuestoes() {
        List<Questao> questoes = new ArrayList<Questao>();
        for (String descricao : questoesDescricoes) {
            Questao q = createQuestao(descricao);
            questoes.add(q);
        }
        return questoes;

    }

    static List<Category> createCategories() {
        List<Category> categories = new ArrayList<Category>();
        for (String name : categoryNames) {
            Category c = createCategory(name);
            categories.add(c);
        }
        return categories;

    }

    static List<Product> createProducts(List<Category> categories) {
        List<Product> products = new ArrayList<Product>();
        for (int i = 0; i < 100; i++) {
            Product p = createProduct(categories);
            products.add(p);
        }

        return products;
    }

    private static Questao createQuestao(String descricao) {
        Questao p = new Questao();
        p.setId(nextQuestaoId++);
        p.setDescricao(descricao);
        return p;
    }

    private static Time createTime(String name) {
        Time p = new Time();
        p.setId(nextTimeId++);
        p.setTimeName(name);
        return p;
    }

    private static Category createCategory(String name) {
        Category c = new Category();
        c.setId(nextCategoryId++);
        c.setName(name);
        return c;
    }

    private static Product createProduct(List<Category> categories) {
        Product p = new Product();
        p.setId(nextProductId++);
        p.setProductName(generateName());

        p.setPrice(new BigDecimal((random.nextInt(250) + 50) / 10.0));
        p.setAvailability(Availability.values()[random.nextInt(Availability
                .values().length)]);
        if (p.getAvailability() == Availability.AVAILABLE) {
            p.setStockCount(random.nextInt(523));
        }

        p.setCategory(getCategory(categories, 1, 2));
        return p;
    }

    private static Set<Category> getCategory(List<Category> categories,
                                             int min, int max) {
        int nr = random.nextInt(max) + min;
        HashSet<Category> productCategories = new HashSet<Category>();
        for (int i = 0; i < nr; i++) {
            productCategories.add(categories.get(random.nextInt(categories
                    .size())));
        }

        return productCategories;
    }

    private static String generateName() {
        return word1[random.nextInt(word1.length)] + " "
                + word2[random.nextInt(word2.length)];
    }

}
