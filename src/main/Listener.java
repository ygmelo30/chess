package main;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class Listener {
    // variables
    private boolean check = false;

    private boolean moveType = false;

    // setting up the clips
    URL filPathCheckMusic = new URI(getClass().getResource("/Music/pokémon-black-&-white-critical-health-music-(hq)-made-with-Voicemod.wav").toString()).toURL();

    URL filePathRegularMusic = new URI(getClass().getResource("/Music/mixkit-classical-8-715.wav").toString()).toURL();


    URL filePathRegularMove = new URI(getClass().getResource("/Music/move-self.wav").toString()).toURL();
    URL filePathCaptureMove = new URI(getClass().getResource("/Music/capture.wav").toString()).toURL();

    Clip clip = AudioSystem.getClip();

    Clip moveSound = AudioSystem.getClip();
    AudioInputStream aisMove = AudioSystem.getAudioInputStream(filePathRegularMove);
    AudioInputStream ais = AudioSystem.getAudioInputStream(filePathRegularMusic);



    public Listener() throws LineUnavailableException, UnsupportedAudioFileException, IOException, URISyntaxException {
        startMusic();
    }

    // getters and setters

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        if(this.check != check) {
            this.check = check;
            changeMusic();
        }
    }

    public boolean isMoveType() {
        return moveType;
    }

    public void setMoveType(boolean moveType) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
            this.moveType = moveType;
            playMoveType();

    }

    // methods
    private void changeMusic() throws LineUnavailableException, IOException, UnsupportedAudioFileException {

        if (check) {
            playCheckMusic();
        } else {
            playRegularMusic();
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            }
        });
    }

    private void startMusic() throws LineUnavailableException, IOException {
        clip.open(ais);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    private void playRegularMusic() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        if(clip.isOpen() || clip.isActive() || clip.isRunning()) {
            clip.close();
        }
        ais = AudioSystem.getAudioInputStream(filePathRegularMusic);

        clip.open(ais);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    private void playCheckMusic() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        if(clip.isOpen() || clip.isActive() || clip.isRunning()) {
            clip.close();
        }
        ais = AudioSystem.getAudioInputStream(filPathCheckMusic);

        clip.open(ais);
        clip.loop(Clip.LOOP_CONTINUOUSLY);

    }

    private void playMoveType() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (!moveType) {
            playRegularMove();
        } else {
            playCaptureMove();
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            }
        });
    }

    private void playRegularMove() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if(moveSound.isOpen() || moveSound.isActive() || moveSound.isRunning()) {
            moveSound.close();
        }
        aisMove = AudioSystem.getAudioInputStream(filePathRegularMove);

        moveSound.open(aisMove);
        moveSound.start();
    }

    private void playCaptureMove() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if(moveSound.isOpen() || moveSound.isActive() || moveSound.isRunning()) {
            moveSound.close();
        }
        aisMove = AudioSystem.getAudioInputStream(filePathCaptureMove);

        moveSound.open(aisMove);
        moveSound.start();
    }
}
