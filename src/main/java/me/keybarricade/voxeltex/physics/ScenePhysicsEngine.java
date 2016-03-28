package me.keybarricade.voxeltex.physics;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import me.keybarricade.voxeltex.global.Time;
import me.keybarricade.voxeltex.scene.AbstractScene;

import javax.vecmath.Vector3f;

public class ScenePhysicsEngine {

    /**
     * The scene this physics engine is attached to.
     */
    private AbstractScene scene;

    /**
     * Bullet engine physics world instance.
     */
    private DynamicsWorld bulletDynamicsWorld;

    /**
     * Constructor.
     *
     * @param scene Scene.
     */
    public ScenePhysicsEngine(AbstractScene scene) {
        this.scene = scene;
    }

    /**
     * Get the scene this physics engine is attached to.
     *
     * @return Attached scene.
     */
    public AbstractScene getScene() {
        return this.scene;
    }

    public void setUp() {
        // Create a DBVT broadphase to broadly check collisions using the AABB technique which ensures good performance
        BroadphaseInterface broadphase = new DbvtBroadphase();

        // Set up a collision configuration and dispatcher
        CollisionConfiguration collisionConfig = new DefaultCollisionConfiguration();
        CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfig);

        // Configure a constraint solver
        ConstraintSolver solver = new SequentialImpulseConstraintSolver();

        // Create and configure the Bullet physics dynamic world
        this.bulletDynamicsWorld = new DiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfig);
        this.bulletDynamicsWorld.setGravity(new Vector3f(0, -9.81f, 0));
    }

    /**
     * Update the physics engine state.
     * This will simulate a physics step, and will apply the physics to the game objects in the attached scene.
     */
    public void update() {
        // Simulate the next physics step
        bulletDynamicsWorld.stepSimulation(Time.deltaTimeFloat);

        // TODO: Apply physics to all game objects?
    }

    /**
     * Add a rigidbody to the physics world.
     *
     * @param rigidbody Rigidbody to add.
     */
    public void addRigidbody(RigidBody rigidbody) {
        this.bulletDynamicsWorld.addRigidBody(rigidbody);
    }
}
