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

    public void setState(int state) {
        if (state == 0) {
            this.indestructible = true;
        } else {
            this.indestructible = false;
        }
    }
}
