package fr.ubx.poo.view.sprite;

import fr.ubx.poo.model.go.character.*;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Pane;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class SpriteBomb extends SpriteGameObject {
    int state;

    public SpriteBomb(Pane layer, Bomb bomb,int state) {
        super(layer, null, bomb);
        updateImage();
        this.state=state;
    }

    public void updateImage() {
        setImage(ImageFactory.getInstance().getBomb(state));
    }
}
