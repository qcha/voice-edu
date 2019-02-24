package recorder;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import java.io.File;

public final class Constants {
    private Constants() {

    }

    public final static String TMP_FILE_NAME_TEMPLATE = "all";

    public final static float FREQUENCY = 44100.0F;

    public final static AudioFormat AUDIO_FORMAT = new AudioFormat(FREQUENCY, 16, 2, true, false);
    public final static AudioFileFormat.Type AUDIO_TYPE = AudioFileFormat.Type.WAVE;

    public final static long TIMER = 420; // seconds
    public final static int DURATION = 5; // seconds

    private final static String FPS = File.separator;
    private final static String CSV_DIR = "src" + FPS + "main" + FPS + "resources" + FPS + "classification";
    public final static String SOURCE_DIR = "src" + FPS + "main" + FPS + "resources";
    public final static String PATH_TO_WAV_TRAIN = SOURCE_DIR + FPS + "allsounds" + FPS + "train";
    public final static String PATH_TO_CSV_TRAIN = CSV_DIR + FPS + "wav_data_train.csv";
    public final static String PATH_TO_WAV_TEST = SOURCE_DIR + FPS + "allsounds" + FPS + "test";
    public final static String PATH_TO_CSV_TEST = CSV_DIR + FPS + "wav_data_eval.csv";

    public final static int NEURONS = 50; //кол-во входных нейронов, зависит от структуры записи
}
