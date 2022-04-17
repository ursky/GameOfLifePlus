package constants;

import world.World;

import javax.swing.*;
import java.awt.*;

public class BlankConstants {
    public World world;

    // thing constants
    public String name, type, imagePath, youngImagePath, deadImagePath;
    public int maxOffsprings, onScreenCoolDown, offScreenCoolDown;
    public float startingDensity, maxSize, minSizeToShow, metabolismRate, growAtHealth,
            maxGrowthRate, reproduceAtSize, reproduceAtHealth, reproductionPenalty;
    public float dispersalRange, maxShadeRange, shadePenalty, sproutTime, startHealth;

    // animal variables
    public float maxSpeed, maxAcceleration;

    // variables
    public float _metabolismRate, _maxGrowthRate, _shadePenalty, _sproutTime;

    public void update() {
        this._metabolismRate = this.metabolismRate / this.world.engine.currentFPS;
        this._maxGrowthRate = this.maxGrowthRate / this.world.engine.currentFPS;
        this._shadePenalty = this.shadePenalty / this.world.engine.currentFPS;
        this._sproutTime = this.sproutTime / this.world.engine.currentFPS;
    }

    public Image loadImage(String imagePath) {
        return new ImageIcon(imagePath).getImage();
    }

    public BlankConstants(World world) {
        this.world = world;
    }
}
