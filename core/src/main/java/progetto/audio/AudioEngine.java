package progetto.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

import progetto.core.settings.model.ModelImpostazioni;

import java.util.HashMap;

public class AudioEngine {

    private final Array<Music> playlist = new Array<>();
    private final HashMap<String, Sound> sounds = new HashMap<>();
    private final Array<String> loaded = new Array<>();

    public AudioEngine() {
    }

    public void addSound(String path) {
        if (!loaded.contains(path, false)){
            loaded.add(path);
            FileHandle file = Gdx.files.internal(path);
            System.out.println(file.exists());
            sounds.put(path, Gdx.audio.newSound(file));
            sounds.get(path).setLooping(0, true);
        }
        playSound(path);
    }

    public void addMusic(String path) {
        if (!loaded.contains(path, false)) {
            loaded.add(path);
            int size = playlist.size;
            playlist.add(Gdx.audio.newMusic(Gdx.files.internal(path)));
            playlist.get(size).setLooping(true);
        }
    }

    public void playMusic(boolean loop) {
        playlist.get(playlist.size-1).setVolume(ModelImpostazioni.getMUSICA().getPosizione());
        playlist.get(playlist.size-1).setLooping(loop);
        playlist.get(playlist.size-1).play();
    }

    public void playSound(String path) {
        sounds.get(path).play(ModelImpostazioni.getSUONI().getPosizione());
    }

    public void stopMusic() {
        for (Music music: playlist) {
            if (music.isPlaying()) {
                music.stop();
            }
        }
    }

}
