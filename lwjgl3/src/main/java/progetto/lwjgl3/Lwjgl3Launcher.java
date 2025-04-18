package progetto.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import progetto.core.Core;

/** Launcher dell'applicazione */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return; // This handles macOS support and helps on Windows.
        createApplication();
    }

    /**
     * Crea l'applicazione LWJGL
     */
    private static void createApplication() {
        new Lwjgl3Application(new Core(), getDefaultConfiguration());
    }

    /** Configurazione di default */
    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        // File di configurazione
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        // Titolo del frame
        configuration.setTitle("Game");
        configuration.setDecorated(false);
        // V-sync
        configuration.useVsync(true);
        // RefreshRate adattato agli hz del monitor
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
        // Modalità finestra
        //configuration.setWindowedMode(1920, 1080);
        //configuration.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
        configuration.setWindowedMode(1280, 720);
        // Misura della finestra fissa
        configuration.setResizable(false);
        return configuration;
    }
}
