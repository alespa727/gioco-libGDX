package progetto.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import progetto.Core;

/** Launcher dell'applicazione */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return; // This handles macOS support and helps on Windows.
        createApplication();
    }

    /** Crea l'applicazione LWJGL  */
    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new Core(), getDefaultConfiguration()); // Gli passa il Core del gioco e la configurazione presa dalla funzione
    }

    /** Configurazione di default */
    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        // File di configurazione
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        // Titolo del frame
        configuration.setTitle("Game");
        configuration.setDecorated(true);
        // V-sync
        configuration.useVsync(true);
        // RefreshRate adattato agli hz del monitor
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
        // Modalità finestra
        configuration.setWindowedMode(1280, 720);
        // Misura della finestra fissa
        configuration.setResizable(false);
        return configuration;
    }
}
