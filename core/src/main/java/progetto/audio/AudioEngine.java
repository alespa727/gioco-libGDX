package progetto.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;

import progetto.core.settings.model.ModelImpostazioni;

public class AudioEngine {

    private final Array<Music> playlist = new Array<>();
    private final Array<Sound> sounds = new Array<>();

    public AudioEngine() {
    }


    public void addSound(String path) {
        sounds.add(Gdx.audio.newSound(Gdx.files.internal(path)));
    }

    public void addMusic(String path) {
        int size = playlist.size;
        playlist.add(Gdx.audio.newMusic(Gdx.files.internal(path)));
        playlist.get(size).setLooping(true);
    }

    public void playMusic(int index) {
        playlist.get(index).setVolume(ModelImpostazioni.getMUSICA().getPosizione());
        playlist.get(index).play();
    }

    public void playSound(int index) {
        sounds.get(index).play(ModelImpostazioni.getSUONI().getPosizione());
    }

    public void stopMusic(int index) {
        playlist.get(index).stop();
    }

}
