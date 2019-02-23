package qcha.voicerecorder;

import lombok.extern.slf4j.Slf4j;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

import static qcha.voicerecorder.Constants.AUDIO_FORMAT;
import static qcha.voicerecorder.Constants.AUDIO_TYPE;

@Slf4j
public class WaveAudioRecording extends Thread implements AutoCloseable {
    private TargetDataLine dataLine;
    private AudioInputStream audioStream;
    private File file;

    {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (file.exists()) {
                    log.error("File will not be saved.");
                    close();
                }
            } catch (Exception e) {
                throw new RuntimeException();
            }
            file.delete();
        }));
    }

    public WaveAudioRecording(File allAudio) throws LineUnavailableException {
        this.file = allAudio;
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, AUDIO_FORMAT);

        dataLine = (TargetDataLine) AudioSystem.getLine(info);
        dataLine.open(AUDIO_FORMAT);
    }

    @Override
    public void run() {
        log.debug("Start recording.");
        dataLine.start();
        audioStream = new AudioInputStream(dataLine);

        try {
            AudioSystem.write(audioStream, AUDIO_TYPE, file);
        } catch (IOException e) {
            log.error("Ошибка записи в файл {}", file);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        log.debug("Stop recording.");
        dataLine.stop();
        dataLine.close();
        audioStream.close();
    }
}
