package me.keybarricade.game.scene;

import me.keybarricade.game.component.animator.ObjectDecayAnimatorComponent;
import me.keybarricade.game.level.LevelBuilder;
import me.keybarricade.game.level.LevelManager;
import me.keybarricade.game.prefab.SandSurfacePrefab;
import me.keybarricade.voxeltex.component.follow.SmoothTopDownFollowComponent;
import me.keybarricade.voxeltex.component.overlay.gui.GuiPanelComponent;
import me.keybarricade.voxeltex.component.overlay.gui.menu.ToggleableMenuComponent;
import me.keybarricade.voxeltex.component.transform.RectangleTransform;
import me.keybarricade.voxeltex.component.transform.RectangleTransformAnchor;
import me.keybarricade.voxeltex.component.transform.VerticalTransformAnchorType;
import me.keybarricade.voxeltex.gameobject.GameObject;
import me.keybarricade.voxeltex.light.Light;
import me.keybarricade.voxeltex.prefab.camera.MouseLookCameraPrefab;
import me.keybarricade.voxeltex.prefab.gui.GuiButtonPrefab;
import me.keybarricade.voxeltex.prefab.gui.GuiLabelPrefab;
import me.keybarricade.voxeltex.prefab.light.LightPrefab;
import me.keybarricade.voxeltex.scene.Scene;
import me.keybarricade.voxeltex.util.Color;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class GameScene extends Scene {

    /**
     * Level base object.
     */
    private GameObject levelBase;

    /**
     * Camera prefab in this scene.
     */
    private MouseLookCameraPrefab cameraPrefab;

    /**
     * Smooth top down follow component used to follow the player.
     */
    private SmoothTopDownFollowComponent smoothCameraFollow;

    /**
     * Level manager.
     */
    private LevelManager levelManager;

    @Override
    public void load() {
        // Load the super
        super.load();

        // Load the level manager and the level data
        this.levelManager = new LevelManager();
        this.levelManager.load();

        // Create the menu
        createMenu();

        // Create and add the sand surface prefab
        addGameObject(new SandSurfacePrefab());

        // Add a sun
        LightPrefab sunLight = new LightPrefab("Sun", Light.LIGHT_TYPE_DIRECTIONAL, new Color(0xFDDC5C).toVector3f(), 0.3f);
        sunLight.getTransform().getRotation().set(90, 45, 90).normalize();
        sunLight.getTransform().getPosition().set(-5, 1, -3);
        addGameObject(sunLight);

        // Create the base level object
        this.levelBase = new GameObject("LevelBase");
        addGameObject(this.levelBase);

        // Create the camera prefab
        this.cameraPrefab = new MouseLookCameraPrefab();
        this.cameraPrefab.getTransform().setPosition(new Vector3f(0.5f, 1.50f, 5.0f));
        this.cameraPrefab.addComponent(this.smoothCameraFollow = new SmoothTopDownFollowComponent());
        addGameObject(this.cameraPrefab);

        // Load the level
        loadLevel();
    }

    /**
     * Create the toggleable menu and add it to the scene
     */
    private void createMenu() {
        // Create the base menu panel
        GameObject menuPanel = new GameObject("OverlayTest");
        menuPanel.addComponent(new RectangleTransform(
                new Vector2f(0, 0),
                new Vector2f(350, 215),
                new RectangleTransformAnchor(0.5f, 0.6f, 0.5f, 0.6f)
        ));
        menuPanel.addComponent(new GuiPanelComponent());
        addGameObject(menuPanel);

        // Create the menu label
        GuiLabelPrefab menuLabel = new GuiLabelPrefab("Button", "Menu");
        menuLabel.getRectangleTransform().setVerticalAnchorPreset(VerticalTransformAnchorType.TOP);
        menuLabel.getRectangleTransform().setPositionTop(-(20 + 16)); // TODO: Invert this when stretched?
        menuLabel.setColor(Color.WHITE);
        menuPanel.addChild(menuLabel);

        // Create a restart button
        // TODO: Properly implement this restart feature!
        GuiButtonPrefab restartButton = new GuiButtonPrefab("RestartButton", "Restart") {
            @Override
            public void onClick() {
                // Call the super
                super.onClick();

                // Load the main menu
                //getEngine().getSceneManager().loadScene(new GameScene());
                unloadLevel();
                loadLevel();
            }
        };
        restartButton.getRectangleTransform().setVerticalAnchorPreset(VerticalTransformAnchorType.TOP);
        restartButton.getRectangleTransform().setPositionTop(-(20 + 16 + (40 + 8))); // TODO: Invert this when stretched?
        menuPanel.addChild(restartButton);

        // Create a main menu button
        GuiButtonPrefab mainMenuButton = new GuiButtonPrefab("MainMenuButton", "Main Menu") {
            @Override
            public void onClick() {
                // Call the super
                super.onClick();

                // Load the main menu
                getEngine().getSceneManager().loadScene(new MainMenuScene());
            }
        };
        mainMenuButton.getRectangleTransform().setVerticalAnchorPreset(VerticalTransformAnchorType.TOP);
        mainMenuButton.getRectangleTransform().setPositionTop(-(20 + 16 + (40 + 8) * 2)); // TODO: Invert this when stretched?
        menuPanel.addChild(mainMenuButton);

        // Create an exit button
        GuiButtonPrefab exitButton = new GuiButtonPrefab("ExitButton", "Exit") {
            @Override
            public void onClick() {
                // Call the super
                super.onClick();

                // Exit
                System.out.println("Exit button pressed.");
                System.exit(0);
            }
        };
        exitButton.getRectangleTransform().setVerticalAnchorPreset(VerticalTransformAnchorType.TOP);
        exitButton.getRectangleTransform().setPositionTop(-(20 + 16 + (40 + 8) * 3)); // TODO: Invert this when stretched?
        menuPanel.addChild(exitButton);

        // Create a toggleable menu controller
        GameObject menuController = new GameObject("MenuController");
        menuController.addComponent(new ToggleableMenuComponent(menuPanel));
        addGameObject(menuController);
    }

    /**
     * Load the level.
     */
    private void loadLevel() {
        // Create a level builder to build the level with
        LevelBuilder builder = new LevelBuilder(this.levelManager.getLevel(0), this.levelBase);

        // Build the level
        builder.build(0.5f);

        // Set the camera target to the player
        this.smoothCameraFollow.setTarget(builder.getPlayer());
    }

    /**
     * Unload the level.
     */
    private void unloadLevel() {
        // Create a variable to calculate the spawn delays
        float delay = 0.0f;

        // Reset the camera target
        this.smoothCameraFollow.setTarget(null);

        // Loop through all children of the level base, and make them decay
        for(int i = 0, size = this.levelBase.getChildCount(false); i < size; i++)
            this.levelBase.getChild(i).addComponent(new ObjectDecayAnimatorComponent(delay += 0.01f));
    }
}