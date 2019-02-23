package recorder;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;

public final class Constants {
    private Constants() {

    }

    public final static String TMP_FILE_NAME_TEMPLATE = "all";

    public final static float FREQUENCY = 44100.0F;

    public final static AudioFormat AUDIO_FORMAT = new AudioFormat(FREQUENCY, 16, 2, true, false);
    public final static AudioFileFormat.Type AUDIO_TYPE = AudioFileFormat.Type.WAVE;

    public final static long TIMER = 420; // seconds
    public final static int DURATION = 5; // seconds

    public final static String SOURCE_DIR = System.getProperty("user.dir") + "\\src\\main\\resources";
    public final static String PATH_TO_WAV_TRAIN = SOURCE_DIR + "\\wave";
    public final static String PATH_TO_CSV_TRAIN = SOURCE_DIR + "\\classification\\wav_data_train.csv";
}
