/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.view.image;

import fr.ubx.poo.game.Direction;
import javafx.scene.image.Image;

import static fr.ubx.poo.view.image.ImageResource.*;

public final class ImageFactory {
    private final Image[] images;

    private final ImageResource[] playDirectionsIndestructible = new ImageResource[]{
            // Direction { N, E, S, W }
            PLAYER_UP_INDESTRUCTIBLE, PLAYER_RIGHT_INDESTRUCTIBLE, PLAYER_DOWN_INDESTRUCTIBLE, PLAYER_LEFT_INDESTRUCTIBLE,
    };
    private final ImageResource[] playDirections = new ImageResource[]{
            // Direction { N, E, S, W }
            PLAYER_UP, PLAYER_RIGHT, PLAYER_DOWN, PLAYER_LEFT,
    };

    private final ImageResource[] monDirections_1 = new ImageResource[]{
            // Direction { N, E, S, W }
            MONSTER_UP, MONSTER_RIGHT, MONSTER_DOWN, MONSTER_LEFT,
    };
    private final ImageResource[] monDirections_2 = new ImageResource[]{
            // Direction { N, E, S, W }
            MONSTER_UP, MONSTER_RIGHT_2, MONSTER_DOWN_2, MONSTER_LEFT_2,
    };
    private final ImageResource[] monDirections_3 = new ImageResource[]{
            // Direction { N, E, S, W }
            MONSTER_UP_3, MONSTER_RIGHT_3, MONSTER_DOWN_3, MONSTER_LEFT_3,
    };

    private final ImageResource[] digits = new ImageResource[]{
            DIGIT_0, DIGIT_1, DIGIT_2, DIGIT_3, DIGIT_4,
            DIGIT_5, DIGIT_6, DIGIT_7, DIGIT_8, DIGIT_9,
    };

    private final ImageResource[] Bombs = new ImageResource[]{
            BOMB_4, BOMB_3, BOMB_2, BOMB_1
    };

    private ImageFactory() {
        images = new Image[ImageResource.values().length];
    }

    /**
     * Point d'accès pour l'instance unique du singleton
     */
    public static ImageFactory getInstance() {
        return Holder.instance;
    }

    private Image loadImage(String file) {
        return new Image(getClass().getResource("/images/" + file).toExternalForm());
    }

    public void load() {
        for (ImageResource img : ImageResource.values()) {
            images[img.ordinal()] = loadImage(img.getFileName());
        }
    }

    public Image get(ImageResource img) {
        return images[img.ordinal()];
    }

    public Image getDigit(int i) {
        if (i < 0 || i > 9)
            throw new IllegalArgumentException();
        return get(digits[i]);
    }

    public Image getPlayer(Direction direction,boolean indestructible) {
        if (indestructible){
            return get(playDirectionsIndestructible[direction.ordinal()]);
        }
        return get(playDirections[direction.ordinal()]);
    }
    public Image getMonster(Direction direction,int level) {
        if (level==1) {
            return get(monDirections_1[direction.ordinal()]);
        }
        else if(level==2){
            return get(monDirections_2[direction.ordinal()]);
            }
        else return get(monDirections_3[direction.ordinal()]);
    }
    public Image getBomb(int i){
        return get(Bombs[i]);
    }

    /**
     * Holder
     */
    private static class Holder {
        /**
         * Instance unique non préinitialisée
         */
        private final static ImageFactory instance = new ImageFactory();
    }

}
