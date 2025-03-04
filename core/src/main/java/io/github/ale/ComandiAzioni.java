package io.github.ale;

public class ComandiAzioni {
    Float coord;
    Azioni azione;


    public ComandiAzioni(Azioni azione, Float coord){
        this.azione = azione;
        this.coord = coord;
    }
    public Azioni getAzione() {
        return azione;
    }

    public void setAzione(Azioni azione) {
        this.azione = azione;
    }

    public Float getCoord() {
        return coord;
    }

    public void setCoord(Float coord) {
        this.coord = coord;
    }

}
