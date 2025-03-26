package io.github.ale.music;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.ArrayMap;

public class MusicPlayer {
    private final ArrayMap<String, Music> playlist;

    public MusicPlayer(String path) {
        playlist = new ArrayMap<>(5);
        // Initialize playlist without calling overridable methods
        if (path != null) {
            initializePlaylist(path);
        }
    }

    private void initializePlaylist(String path) {
        new Thread(() -> {
                add(path);
                System.out.println("Canzone caricata!");
                playlist.get(path).setLooping(true);
                playlist.get(path).setVolume(0.5f);
                playlist.get(path).play();
        }).start();
    }

    public void addAsincrono(String path) {
        new Thread(() -> {
            add(path);
            System.out.println("Canzone caricata!");
        }).start();
    }

    public void add(String path) {
        if (!playlist.containsKey(path)) {
            playlist.put(path, Gdx.audio.newMusic(Gdx.files.internal(path)));
        } else {
            System.err.println("Musica già caricata: " + path);
        }
    }

    public void playfromthestart() {
        for (int i = 0; i < playlist.size; i++) {
            if (playlist.get(playlist.getKeyAt(i)).isPlaying())
                playlist.get(playlist.getKeyAt(i)).setPosition(0f);
        }
    }

    public void play(String path) {
        Music music = playlist.get(path);
        if (music != null) {
            music.play();
        } else {
            System.err.println("Musica non trovata: " + path);
        }
    }

    public void play(int index) {
        playlist.getValueAt(index).play();
    }

    public void pause(String path) {
        playlist.get(path).pause();
    }

    public void stop() {
        for (int i = 0; i < playlist.size; i++) {
            Music music = playlist.getValueAt(i);
            if (music != null) {
                music.stop();
            }
        }
    }

    public void setLooping(String path, boolean looping) {
        playlist.get(path).setLooping(looping);
    }

    public void setLooping(int index, boolean looping) {
        playlist.getValueAt(index).setLooping(looping);
    }

    public void setVolume(float volume) {
        for (int i = 0; i < playlist.size; i++) {
            playlist.getValueAt(i).setVolume(volume);
        }
    }

    public void empty() {
        for (int i = 0; i < playlist.size; i++) {
            Music music = playlist.getValueAt(i);
            if (music != null) {
                music.dispose();
            }
        }
        playlist.clear();
    }

    public void debugPlaylist() {
        System.out.println("Canzoni:");
        for (int i = 0; i < playlist.size; i++) {
            System.out.println("Key: " + playlist.getKeyAt(i) + ", Value: " + playlist.getValueAt(i));
        }
    }
}
