package me.keybarricade.game.prefab;

import me.keybarricade.game.asset.GameResourceBundle;
import me.keybarricade.game.component.animator.ObjectDecayAnimatorComponent;
import me.keybarricade.game.component.animator.ObjectSpawnAnimatorComponent;
import me.keybarricade.voxeltex.component.rigidbody.RigidbodyComponent;
import me.keybarricade.voxeltex.material.Material;
import me.keybarricade.voxeltex.math.vector.Vector3fFactory;
import me.keybarricade.voxeltex.prefab.primitive.CubePrefab;
import org.joml.Vector3f;

public class BoxPrefab extends CubePrefab {

    /**
     * Constructor.
     */
    public BoxPrefab(Vector3f position, boolean dummy, float spawnDelay, float decayDelay) {
        // Construct the parent
        super("BoxPrefab", Vector3fFactory.one());

        // Set a random box material
        setMaterial(getRandomMaterial());

        // Set the position
        getTransform().setPosition(position);

        // Add the animator components
        if(spawnDelay >= 0)
            addComponent(new ObjectSpawnAnimatorComponent(spawnDelay, !dummy ? new RigidbodyComponent(true) : null));

        // Add a decay animation
        if(decayDelay >= 0.0f)
            addComponent(new ObjectDecayAnimatorComponent(decayDelay));
    }

    /**
     * Get a random box material.
     *
     * @return Box material.
     */
    private Material getRandomMaterial() {
        // Get a random value
        float r = (float) Math.random();

        // 3/4th chance to use the regular box
        if(r < 0.75f)
            return GameResourceBundle.getInstance().MATERIAL_BOX0;

        else {
            // Re-randomize the value
            r = (float) Math.random();

            // Pick the correct material
            if(r < 0.15f)
                return GameResourceBundle.getInstance().MATERIAL_BOX1;
            else if(r < 0.3f)
                return GameResourceBundle.getInstance().MATERIAL_BOX2;
            else if(r < 0.4f)
                return GameResourceBundle.getInstance().MATERIAL_BOX3;
            else if(r < 0.5f)
                return GameResourceBundle.getInstance().MATERIAL_BOX4;
            else if(r < 0.55f)
                return GameResourceBundle.getInstance().MATERIAL_BOX5;
            else if(r < 0.6f)
                return GameResourceBundle.getInstance().MATERIAL_BOX6;
            else if(r < 0.7f)
                return GameResourceBundle.getInstance().MATERIAL_BOX7;
            else if(r < 0.8f)
                return GameResourceBundle.getInstance().MATERIAL_BOX8;
            else if(r < 0.95f)
                return GameResourceBundle.getInstance().MATERIAL_BOX9;
            else
                return GameResourceBundle.getInstance().MATERIAL_BOX10;
        }
    }
}
