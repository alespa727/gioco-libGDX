package progetto.gameplay.entity.types.living;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import progetto.gameplay.entity.skills.SkillSet;
import progetto.gameplay.entity.types.EntityInstance;
import progetto.utils.TerminalCommand;

import java.util.List;

import static com.badlogic.gdx.net.HttpRequestBuilder.json;

public class HumanoidInstances extends EntityInstance {

    // === Attributi specifici dell'umano ===
    public transient final SkillSet skillset;
    public List<String> skillNames;
    public final float speed;
    public final float maxHealth;
    public final float health;

    // === Costruttore ===
    public HumanoidInstances(Humanoid e) {
        super(e);
        this.skillset = e.getSkillset();
        this.maxHealth = e.getMaxHealth();
        this.speed = e.getMaxSpeed();
        this.health = e.getHealth();
        this.skillNames = skillset.getSkillNames();
        json.setOutputType(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);
        json.setUsePrototypes(false); // evita riferimenti strani tra oggetti
        String jsonString = json.prettyPrint(this);
        System.out.println(jsonString);
    }

    @Override
    public String toString() {
        return "HumanoidInstances{" +
            super.toString() +
            "skillset=" + skillset +
            ", speed=" + speed +
            ", maxHealth=" + maxHealth +
            ", health=" + health +
            '}';
    }
}
