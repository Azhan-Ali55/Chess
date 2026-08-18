package util;

import javafx.scene.media.AudioClip;
import java.util.Random;

public class SoundManager {
    private static final Random random = new Random();

    private static final AudioClip[] moveSounds = {
            loadSound("/sounds/move_1.mp3"),
            loadSound("/sounds/move_2.mp3"),
            loadSound("/sounds/move_3.mp3"),
            loadSound("/sounds/move_4.mp3")
    };
    private static final AudioClip captureSound = loadSound("/sounds/piece-capture.mp3");
    private static final AudioClip checkSound = loadSound("/sounds/check.mp3");
    private static final AudioClip checkmateSound = loadSound("/sounds/checkmate.mp3");

    private static AudioClip loadSound(String path) {
        var resource = SoundManager.class.getResource(path);
        if (resource == null) {
            throw new RuntimeException("Sound not found: " + path);
        }
        System.out.println("Loaded sound: " + resource);
        return new AudioClip(resource.toExternalForm());
    }

    public static void playMoveSound() {
        int index = random.nextInt(moveSounds.length);
        moveSounds[index].play();
    }

    public static void playCaptureSound() {
        captureSound.play();
    }
    public static void playCheckSound() { checkSound.play(); }
    public static void playCheckmateSound() { checkmateSound.play(); }
}