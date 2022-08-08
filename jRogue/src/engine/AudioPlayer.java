package engine;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class AudioPlayer {
    Clip clip;

    public AudioPlayer(String fileName) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File file = new File("./jRogue/src/sounds/" + fileName).getAbsoluteFile();
        AudioInputStream ais = AudioSystem.getAudioInputStream(file);
        clip = AudioSystem.getClip();
        clip.open(ais);

        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(-20.0f);
    }

    public void play() {
        clip.start();
    }

    public boolean isPlaying() {
        if (clip.isRunning()) {
            return true;
        } else {
            return false;
        }
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }

    public void close() {
        clip.close();
    }
}
