package fr.ubx.poo.view.sprite;

import fr.ubx.poo.game.Position;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class SpriteExplosion extends Sprite {
    private Position position;

    public SpriteExplosion(Pane layer, Image image, Position position) {
        super(layer, image);
        this.position = position;
    }


    @Override
    public void updateImage() {

    }

    @Override
    public Position getPosition() {
        return null;
    }
}

