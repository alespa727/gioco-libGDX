package progetto.manager.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import org.fusesource.jansi.Ansi;
import progetto.core.game.GameScreen;

import java.util.Scanner;

public class TerminalCommand extends Thread {
    private final Scanner scanner;
    private final GameScreen gameScreen;
    private boolean running;

    public TerminalCommand(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        scanner = new Scanner(System.in);
        running = true;
    }

    @Override
    public void run() {
        while (running) {
            String input = scanner.nextLine();  // Legge l'input dal terminale
            processCommand(input);  // Elabora il comando
        }
    }

    public void stopRunning() {
        running = false;
    }

    private void processCommand(String command) {
        // Elenco dei comandi e delle azioni associate
        String[] tokens = command.split(" ");
        if(tokens[0].isEmpty()){
            return;
        }
        switch (tokens[0]) {
            case "teleport":
                try {
                    if (tokens.length > 3) {
                        throw new RuntimeException();
                    }
                    int x = Integer.parseInt(tokens[1]);
                    int y = Integer.parseInt(tokens[2]);
                    printMessage("Teleporting the player...");
                    sleep(500);
                    teleportPlayer(x, y);
                    printMessage("Command executed!");
                }catch (Exception e) {
                    switch (tokens.length) {
                        case 1:
                            printError("Inserire coordinate x e y");
                            break;
                        case 2:
                            printError("Inserire coordinate y");
                            break;
                        default:
                            printError("Sintassi non corretta");
                            break;
                    }
                }
                break;
            case "debug":
                try{
                    if (tokens.length > 2) {
                        throw new RuntimeException();
                    }
                    String state = tokens[1];
                    switch (state){
                        case "true":
                            printMessage("Enabling debug mode...");
                            sleep(500);
                            DebugWindow.setDebugMode(true);
                            break;
                        case "false":
                            printMessage("Disabling debug mode...");
                            sleep(500);
                            DebugWindow.setDebugMode(false);
                            break;
                        default:
                            printError("Sintassi non corretta");
                    }
                }catch (Exception e) {
                    if (tokens.length == 1) {
                        printError("Inserire lo stato");
                    } else {
                        printError("Sintassi non corretta");
                    }
                }
                break;
            case "kill":
                try{
                    if (tokens.length == 1) {
                        printMessage("Killing the player...");
                        sleep(500);
                        printMessage("Player killed.");
                        killPlayer();
                    }
                }catch (Exception e) {
                    printError("Sintassi non corretta");
                }
                break;
            case "god":
                try {
                    String state = tokens[1];
                    switch (state){
                        case "true":
                            printMessage("Enabling god mode...");
                            sleep(500);
                            printMessage("God mode enabled.");
                            setGodMode(true);
                            break;
                        case "false":
                            printMessage("Disabling god mode...");
                            sleep(500);
                            printMessage("God mode disabled.");
                            setGodMode(false);
                            break;
                        default:
                            printError("Sintassi non corretta");
                    }
                }catch (Exception e) {
                    printError("Sintassi non corretta");
                }
                break;
            case "exit":
                Gdx.app.exit();
            default:
                printError("Comando non trovato: " + command);
        }
    }

    private void setGodMode(boolean state) {
        gameScreen.getEntityManager().player().getHumanStates().setInvulnerable(state);
        System.out.println(gameScreen.getEntityManager().player().getHumanStates().isInvulnerable());
    }

    private void teleportPlayer(int x, int y) {
        // Teletrasporta il giocatore
        gameScreen.getEntityManager().player().teleport(new Vector2(x, y));
    }

    private void killPlayer() {
        // Uccide il player
        gameScreen.getEntityManager().player().kill();
    }

    public static void printMessage(String message) {
        // Messaggio da console (Blu)
        System.out.println(Ansi.ansi().fg(Ansi.Color.BLUE).a(message).reset());
    }

    public static void printError(String message) {
        // Messaggio di errore (Rosso)
        Gdx.app.log("Error", Ansi.ansi().fg(Ansi.Color.RED).a(message).reset().toString());
    }

    public static void printWarning(String message) {
        // Warning (Giallo)
        Gdx.app.log("Warning", Ansi.ansi().fg(Ansi.Color.YELLOW).a(message).reset().toString());
    }

}
