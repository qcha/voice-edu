package recorder;

import lombok.extern.slf4j.Slf4j;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.*;

import static recorder.Constants.*;

@Slf4j
public class AudioSplitter implements AutoCloseable {
    private int frameSize;
    private int attempt;
    private File storageDir;
    private InputStream audioInputStream;

    public AudioSplitter(int attempt, File allAudio) throws IOException {
        frameSize = AUDIO_FORMAT.getFrameSize();
        this.attempt = attempt;
        storageDir = allAudio.getParentFile();
        audioInputStream = new FileInputStream(allAudio);
    }

    public void split(int duration) throws IOException {
        if (storageDir.exists()) {

            log.debug("Start splitting.");
            byte[] buf = new byte[(int) (duration * FREQUENCY * frameSize)]; // the product is count of bytes in *time* seconds
            int bytes, i = 1;
            while ((bytes = audioInputStream.read(buf)) > 0) {
                try (ByteArrayInputStream bais = new ByteArrayInputStream(buf);
                     AudioInputStream ais = new AudioInputStream(bais, AUDIO_FORMAT, bytes / frameSize)) {
                    String child = String.format("%d%d.wav", attempt, i);
                    log.info("File " + child + " created");
                    AudioSystem.write(ais, AUDIO_TYPE, new File(i <= 99 ? PATH_TO_WAV_TRAIN : PATH_TO_WAV_TEST, child));
                    i++;
                }
            }

            log.debug("Splitting is ended.");
        }
    }

    @Override
    public void close() throws Exception {
        audioInputStream.close();
    }
}
