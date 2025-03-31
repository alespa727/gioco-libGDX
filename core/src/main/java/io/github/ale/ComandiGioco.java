package io.github.ale;

import com.badlogic.gdx.Input;

/**
<p>
    Questa classe contiene tutti i comandi che dovranno essere usati nel gioco.
<br>
    I comandi sono e devono essere TUTTI STATICI poiche' piu' facile ad accedervi.
<br>
    Gli attributi sono PRIVATI poiche' devono essere modificabili, solo,
    attraverso i metodi GETTER E SETTER.
</p>
*/

public final class ComandiGioco {
    // ATTRIBUTI -----------------------------------------------------------------------------

    /**<p>MOVIMENTI---------------------------------------------------------------------------------</p>*/
    // TASTO PER LA DIREZIONE BASE, MODIFICABILE, VERSO SOPRA (SU)
    private static int DIREZIONE_NORD = Input.Keys.W; //             <---------

    // TASTO PER LA DIREZIONE BASE, MODIFICABILE, VERSO SOTTO (GIU)
    private static int DIREZIONE_SUD = Input.Keys.S; //             <---------

    // TASTO PER LA DIREZIONE BASE, MODIFICABILE, VERSO DESTRA
    private static int DIREZIONE_EST = Input.Keys.D; //             <---------

    // TASTO PER LA DIREZIONE BASE, MODIFICABILE, VERSO SINISTRA
    private static int DIREZIONE_OVEST = Input.Keys.A; //             <---------
    // ---------------------------------------------------------------

    /**<p>TASTO PER L'ATTACCO BASE, MODIFICABILE----------------------------------------------------</p>*/
    private static int ATTACCO = Input.Keys.F;


    /**<p>TASTO PER USARE UN OGGETTO O INTERAGIRE, MODIFICABILE-------------------------------------</p>*/
    private static int USA = Input.Keys.E;


    /**<p>TASTO PER MUOVERSI PIU' VELOCEMENTE, MODIFICABILE-----------------------------------------</p>*/
    private static int CORRI = Input.Keys.SHIFT_LEFT;


    /**<p>TASTO PER FERMARE IL GIOCO, MODIFICABILE--------------------------------------------------</p>*/
    private static int FERMAGIOCO = Input.Keys.ESCAPE;


    /**<p>TASTO PER RIPRENDERE IL GIOCO, MODIFICABILE-----------------------------------------------</p>*/
    private static int RIPRENDIGIOCO = Input.Keys.SPACE;

    // COSTRUTTORI -----------------------------------------------------------------------------


    // METODI -----------------------------------------------------------------------------


    // GETTER E SETTER -----------------------------------------------------------------------------
    public static int getDirezioneNord() {
        return DIREZIONE_NORD;
    }

    public static void setDirezioneNord(int direzioneNord) {
        DIREZIONE_NORD = direzioneNord;
    }

    public static int getDirezioneSud() {
        return DIREZIONE_SUD;
    }

    public static void setDirezioneSud(int direzioneSud) {
        DIREZIONE_SUD = direzioneSud;
    }

    public static int getDirezioneEst() {
        return DIREZIONE_EST;
    }

    public static void setDirezioneEst(int direzioneEst) {
        DIREZIONE_EST = direzioneEst;
    }

    public static int getDirezioneOvest() {
        return DIREZIONE_OVEST;
    }

    public static void setDirezioneOvest(int direzioneOvest) {
        DIREZIONE_OVEST = direzioneOvest;
    }

    public static int getATTACCO() {
        return ATTACCO;
    }

    public static void setATTACCO(int ATTACCO) {
        ComandiGioco.ATTACCO = ATTACCO;
    }

    public static int getUSA() {
        return USA;
    }

    public static void setUSA(int USA) {
        ComandiGioco.USA = USA;
    }

    public static int getCORRI() {
        return CORRI;
    }

    public static void setCORRI(int CORRI) {
        ComandiGioco.CORRI = CORRI;
    }

    public static int getFERMAGIOCO() {
        return FERMAGIOCO;
    }

    public static void setFERMAGIOCO(int FERMAGIOCO) {
        ComandiGioco.FERMAGIOCO = FERMAGIOCO;
    }

    public static int getRIPRENDIGIOCO() {
        return RIPRENDIGIOCO;
    }

    public static void setRIPRENDIGIOCO(int RIPRENDIGIOCO) {
        ComandiGioco.RIPRENDIGIOCO = RIPRENDIGIOCO;
    }
}
