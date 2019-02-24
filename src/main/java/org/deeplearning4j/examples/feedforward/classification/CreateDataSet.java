package org.deeplearning4j.examples.feedforward.classification;

import lombok.extern.slf4j.Slf4j;
import org.deeplearning4j.examples.feedforward.classification.soundProcessing.*;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static recorder.Constants.FREQUENCY;
import static recorder.Constants.NEURONS;

@Slf4j
public class CreateDataSet {

    private static float sampleRate = FREQUENCY; //Частота дискретизации у каждой записи своя - если ошибку выдает - то подписывает, какую надо выставить
    private static Set<String> numOutputs = new HashSet<>(); //кол-во классов (голосов) кол-во голосов для распознования (в CSV файле)

    public static int getNumOutputs() {
        return numOutputs.size();
    }

    /*public CreateDataSet() throws IOException, UnsupportedAudioFileException {
        //  Обучающая выборка

        System.out.println("Create Train Set from Folder: ");
        System.out.println(PATH_TO_WAV_TRAIN);
        createCSVFile(PATH_TO_CSV_TRAIN, PATH_TO_WAV_TRAIN);

        System.out.println("Create Test Set from Folder: ");
        System.out.println(SOURCE_DIR + "\\allsound\\test");
        String pathToWAVtest = SOURCE_DIR + "\\allsound\\test";
        String pathToCSVtest = SOURCE_DIR + "\\classification\\wav_data_eval.csv";
        createCSVFile(pathToCSVtest, pathToWAVtest);
    }*/

    public static void createCSVFile(String pathToCSV, String pathToWAV)
            throws UnsupportedAudioFileException, IOException {
        log.info("Create Set from Folder: {}", pathToWAV);
        File folder = new File(pathToWAV);
        try (FileWriter writer = new FileWriter(pathToCSV)) {
            writer.write("");
            File[] folderEntries = folder.listFiles();
            for (File entry : folderEntries) {
                int userKey = Character.getNumericValue(entry.getName().charAt(0));
                numOutputs.add(String.valueOf(userKey - 1));
                log.info("take " + entry.getName() + " - class of speaker is #" + (userKey - 1));
                double[] audioSample = convertFileToDoubleArray(entry);
                double[] q = extractFeatures(audioSample);

                writer.append(String.valueOf(userKey - 1));

                for (int j = 1; j < q.length; j++) {
                    writer.append(",");
                    writer.append((String.valueOf(q[j])).substring(0, 14));
                }
                writer.append("\n");
            }
        }
        log.info("File saved to {}", pathToCSV);
    }


    private static double[] convertFileToDoubleArray(File voiceSampleFile)
            throws UnsupportedAudioFileException, IOException {

        AudioInputStream sample = AudioSystem.getAudioInputStream(voiceSampleFile);
        AudioFormat format = sample.getFormat();
        float diff = Math.abs(format.getSampleRate() - sampleRate);
        if (diff > 5 * Math.ulp(0.0f)) {
            throw new IllegalArgumentException("The sample rate for this file is different than org.deeplearning4j.examples.feedforward.classification.Recognito's " +
                    "defined sample rate : [" + format.getSampleRate() + "]");
        }
        return FileHelper.readAudioInputStream(sample);
    }

    private static double[] extractFeatures(double[] voiceSample) {

        AutocorrellatedVoiceActivityDetector voiceDetector = new AutocorrellatedVoiceActivityDetector();
        Normalizer normalizer = new Normalizer();
        FeaturesExtractor<double[]> lpcExtractor = new LpcFeaturesExtractor(sampleRate, NEURONS + 1); //poles = 51 - это количество выходных данных у одного трека
        //то есть входящих нейронов в итоге будет 50
        voiceDetector.removeSilence(voiceSample, sampleRate);
        normalizer.normalize(voiceSample, sampleRate);
        double[] lpcFeatures = lpcExtractor.extractFeatures(voiceSample);

        return lpcFeatures;
    }

}
