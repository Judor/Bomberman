package fr.ubx.poo.view.sprite;

import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Pane;

public class SpriteBomb extends SpriteGameObject{
    private final ColorAdjust effect = new ColorAdjust();

    public SpriteBomb(Pane layer, Monster monster) {
        super(layer, null, monster);
        updateImage();
    }


    @Override
    public void updateImage() {

    }
}
