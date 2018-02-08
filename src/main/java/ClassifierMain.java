import classifier.OpenNLPClassifier;

import java.util.ArrayList;
import java.util.List;

import static utils.FilesUtils.*;

/**
 * @author Monica Shopova <monica.shopova@gmail.com>
 */
public class ClassifierMain {

    private final static String PATH_TO_DOCS = "resources/docs/";
    private final static String PATH_TO_TRAIN_FOLDER = "resources/train/";
    private final static String PATH_TO_TEST_FOLDER = "resources/test/";
    private final static String PATH_TO_BBTEAM_TRAIN_DATA = "bbteam_train_data.txt";

    public static void prepareTrainData() {
        createFile(PATH_TO_TRAIN_FOLDER, PATH_TO_BBTEAM_TRAIN_DATA);
        List<String> documentsPaths = getFilesNames(PATH_TO_DOCS);
        String documentContent;
        String category;
        String contentToWrite;
        createFile(PATH_TO_TRAIN_FOLDER, PATH_TO_BBTEAM_TRAIN_DATA);

        for (String documentPath : documentsPaths) {
            documentContent = readFile(documentPath);
            if (documentContent.length() == 0) {
                continue;
            }
            category = extractCategory(documentPath);
            contentToWrite = category + " " + documentContent;
            writeToFileMultiple(PATH_TO_TRAIN_FOLDER, PATH_TO_BBTEAM_TRAIN_DATA, contentToWrite);
        }
    }

    public static String extractCategory(String documentPath) {
        String fileName = documentPath.substring(documentPath.lastIndexOf('/') + 1);
        String category = fileName.substring(0, fileName.indexOf('_'));
        return category;
    }

    public static void main(String[] args) {
        prepareTrainData();
        OpenNLPClassifier bbTeamClassifier = new OpenNLPClassifier(PATH_TO_TRAIN_FOLDER + PATH_TO_BBTEAM_TRAIN_DATA);
        bbTeamClassifier.trainModel();

        List<String> testDocumentsPaths = getFilesNames(PATH_TO_TEST_FOLDER);
        List<Boolean> testResult = new ArrayList<>();

        String testDocumentContent;
        String category;

        for (String testDocumentPath : testDocumentsPaths) {
            testDocumentContent = readFile(testDocumentPath);
            if (testDocumentContent.length() == 0) {
                continue;
            }
            category = extractCategory(testDocumentPath);
            testResult.add(bbTeamClassifier.test(category, testDocumentContent));
        }

        long truthyResults = testResult.stream().filter(el -> el).count();
        Double precision = (double) truthyResults / testResult.size();
        System.out.println("precision is: " + precision);
        System.out.println(testResult.toString());
    }
}
