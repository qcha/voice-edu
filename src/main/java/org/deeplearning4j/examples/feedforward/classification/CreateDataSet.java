package org.deeplearning4j.examples.feedforward.classification;

import org.deeplearning4j.examples.feedforward.classification.soundProcessing.*;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class CreateDataSet {

    public CreateDataSet() throws IOException, UnsupportedAudioFileException {
        float sampleRate = 44100.0f; //Частота дискретизации у каждой записи своя - если ошибку выдает - то подписывает, какую надо выставить
        //  Обучающая выборка
        String sourceDir = System.getProperty("user.dir") + "\\src\\main\\resources";

        System.out.println("Create Train Set from Folder: ");
        System.out.println(sourceDir + "\\wave");
        String pathToWAVtrain = sourceDir + "\\wave";
        String pathToCSVtrain = sourceDir + "\\classification\\wav_data_train.csv";
        createCSVFile(sampleRate, pathToCSVtrain, pathToWAVtrain);
/*
        System.out.println("Create Test Set from Folder: ");
        System.out.println(sourceDir + "\\wave\\test");
        String pathToWAVtest = sourceDir + "\\wave\\test";
        String pathToCSVtest = sourceDir + "\\classification\\wav_data_eval.csv";
        createCSVFile(sampleRate, pathToCSVtest, pathToWAVtest);*/
    }

    private void createCSVFile(float sampleRate, String pathToCSV, String pathToWAV)
            throws UnsupportedAudioFileException, IOException {
        File folder = new File(pathToWAV);
        try (FileWriter writer = new FileWriter(pathToCSV)) {
            writer.write("");
            File[] folderEntries = folder.listFiles();
            for (File entry : folderEntries) {
                int userKey = Character.getNumericValue(entry.getName().charAt(0));
                System.out.println("take " + entry.getName() + " - class of speaker is #" + String.valueOf(userKey - 1));
                double[] audioSample = convertFileToDoubleArray(entry, sampleRate);
                double[] q = extractFeatures(audioSample, sampleRate);

                writer.append(String.valueOf(userKey - 1));

                for (int j = 1; j < q.length; j++) {
                    writer.append(",");
                    writer.append((String.valueOf(q[j])).substring(0, 14));
                }
                writer.append("\n");
            }
        }
    }


    private double[] convertFileToDoubleArray(File voiceSampleFile, float sampleRate)
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

    private double[] extractFeatures(double[] voiceSample, float sampleRate) {

        AutocorrellatedVoiceActivityDetector voiceDetector = new AutocorrellatedVoiceActivityDetector();
        Normalizer normalizer = new Normalizer();
        FeaturesExtractor<double[]> lpcExtractor = new LpcFeaturesExtractor(sampleRate, 51); //poles = 500 - это количество выходных данных у одного трека
        //то есть входящих нейронов в итоге будет 500
        voiceDetector.removeSilence(voiceSample, sampleRate);
        normalizer.normalize(voiceSample, sampleRate);
        double[] lpcFeatures = lpcExtractor.extractFeatures(voiceSample);

        return lpcFeatures;
    }

}
