package progetto.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.graphics.glutils.HdpiMode;
import org.lwjgl.system.windows.DISPLAY_DEVICE;
import progetto.core.Core;

/** Launcher dell'applicazione */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return; // Classe di LibGDX
        createApplication();
    }

    /**
     * Crea l'applicazione LWJGL
     */
    private static void createApplication() {
        new Lwjgl3Application(new Core(), getDefaultConfiguration());
    }

    /**
     * Configurazione di default di LWJGL3
     */
    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Game");
        config.setDecorated(false);
        int width = 1280; //1920;
        int height = 720; //1080;
        config.setWindowedMode(width, height);
        config.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate-1);
        return config;
    }
}
