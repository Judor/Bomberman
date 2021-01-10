/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.view.sprite;

import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.layout.Pane;

public class SpritePlayer extends SpriteGameObject {
    private boolean indestructible = false;

    public SpritePlayer(Pane layer, Player player) {
        super(layer, null, player);
        updateImage();
    }

    public void updateImage() {
        Player player = (Player) go;
        setImage(ImageFactory.getInstance().getPlayer(player.getDirection(), indestructible));
    }

    public void setState(int state) {                   //Used in GameEngine. If he's in the firsts secs of a Level, the state is going  be 0. The rest of the time, it's 1
        this.indestructible = state == 0;
    }
}
