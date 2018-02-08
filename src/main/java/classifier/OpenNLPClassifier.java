package classifier;

import opennlp.tools.doccat.*;
import opennlp.tools.ml.naivebayes.NaiveBayesTrainer;
import opennlp.tools.util.*;

import java.io.File;
import java.io.IOException;

/**
 * @author Monica Shopova <monica.shopova@gmail.com>
 */

public class OpenNLPClassifier {
    private DoccatModel model;
    private String pathToTrainData;

    public OpenNLPClassifier(String pathToTrainData) {
        this.pathToTrainData = pathToTrainData;
    }

    public void trainModel() {
        try {
            InputStreamFactory dataIn = new MarkableFileInputStreamFactory(new File(pathToTrainData));
            ObjectStream<String> lineStream = new PlainTextByLineStream (dataIn, "UTF-8");
            ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

            TrainingParameters params = new TrainingParameters();
            params.put(TrainingParameters.CUTOFF_PARAM, Integer.toString(2));
            params.put(TrainingParameters.ITERATIONS_PARAM, Integer.toString(30));
            params.put(TrainingParameters.ALGORITHM_PARAM, NaiveBayesTrainer.NAIVE_BAYES_VALUE);

            model = DocumentCategorizerME.train("bg", sampleStream, params, new DoccatFactory());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean test(String category, String article) {
        String predictedCategory = classifyNewArticle(article);
        return category.equals(predictedCategory);
    }

    private String classifyNewArticle(String article) {
        DocumentCategorizerME bbTeamCategorizer = new DocumentCategorizerME(model);
        double[] outcomes = bbTeamCategorizer.categorize(article);
        String category = bbTeamCategorizer.getBestCategory(outcomes);
        System.out.println(category);

        return category;
    }
}
