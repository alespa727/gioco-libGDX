package progetto.gameplay.entity.types.abstractEntity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class EntityConfig {

    // === Identificatori e descrizione ===
    public int id;
    public String nome, descrizione;

    // === Posizione e dimensioni ===
    public float x, y;
    public float width, height;
    public float offsetX, offsetY;
    public Vector2 direzione;

    // === Stato e comportamento ===
    public boolean isAlive;
    public boolean inCollisione;
    public boolean isMoving;

    // === Statistiche ===
    public float hp;
    public float speed;
    public float attackdmg;

    // === Grafica ===
    public Texture img;
    public float imageWidth, imageHeight;

    // === Costruttori ===
    public EntityConfig() {
    }

    public EntityConfig(EntityConfig config) {
        this.id = config.id;
        this.x = config.x;
        this.y = config.y;
        this.img = config.img;
        this.width = config.width;
        this.height = config.height;
        this.direzione = config.direzione;
        this.nome = config.nome;
        this.descrizione = config.descrizione;
        this.isAlive = config.isAlive;
        this.offsetX = config.offsetX;
        this.offsetY = config.offsetY;
        this.inCollisione = config.inCollisione;
        this.isMoving = config.isMoving;
        this.hp = config.hp;
        this.speed = config.speed;
        this.attackdmg = config.attackdmg;
        this.imageWidth = config.imageWidth;
        this.imageHeight = config.imageHeight;
    }
}
