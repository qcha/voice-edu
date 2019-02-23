package qcha.voicerecorder;

import lombok.extern.slf4j.Slf4j;

import javax.sound.sampled.LineUnavailableException;
import java.io.File;

import static qcha.voicerecorder.Constants.DURATION;

@Slf4j
public class AudioController {
    private WaveAudioRecording waveAudioRecording;
    private File allAudio;
    private int attempt;

    public AudioController(int attempt, String dir) throws LineUnavailableException {
        log.debug("Temp file is created.");
        allAudio = new File(dir, Constants.TMP_FILE_NAME_TEMPLATE + "_" + attempt + ".wav");
        waveAudioRecording = new WaveAudioRecording(allAudio);
        this.attempt = attempt;
    }

    public void startRecord() {
        waveAudioRecording.start();
    }

    public void stopRecord() {
        try (AudioSplitter splitter = new AudioSplitter(attempt, allAudio)) {
            waveAudioRecording.close();
            splitter.split(DURATION);
        } catch (Exception e) {
            log.error("Ошибка при закрытии потока записи.");
            throw new RuntimeException(e);
        }

        if (allAudio.delete()) {
            log.debug("Temp file is deleted.");
        } else {
            log.error("Can't delete temp file.");
        }
    }
}
