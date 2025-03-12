package io.github.ale.entity.enemy.umani;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Segment;

import io.github.ale.entity.abstractEntity.EntityConfig;
import io.github.ale.entity.abstractEntity.movement.EntityMovementManager;
import io.github.ale.entity.enemy.abstractEnemy.Nemico;

public final class Finn extends Nemico{

    public Finn(EntityConfig config){
        super(config);
        create();
    }
    /**
     * inizializza il nemico
     */
    
    @Override
    public void create() {
        obbiettivo = new Vector2();
        obbiettivoDrawCoord = new Vector2();
        range = new Rectangle(getX(), getY(), 2f, 2f);
        linea = new Segment(new Vector3(0, 0, 0), new Vector3(0, 0, 0));
        movement = new EntityMovementManager();
        getEnemyStates().setInRange(false);
        playerCircle = new Circle(0, 0, 0.5f);
        areaInseguimento = new Circle(getX()+getSize().getWidth()/2, getY()+getSize().getHeight()/2, 5f);
    }
}
