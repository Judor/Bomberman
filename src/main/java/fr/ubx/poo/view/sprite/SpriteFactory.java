/*
 * Copyright (c) 2020. Laurent Réveillère
 */
package fr.ubx.poo.view.sprite;

import static fr.ubx.poo.view.image.ImageResource.*;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.go.character.*;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.layout.Pane;

import javax.sql.ConnectionPoolDataSource;


public final class SpriteFactory {

    public static Sprite createDecor(Pane layer, Position position, Decor decor) {
        ImageFactory factory = ImageFactory.getInstance();
        if (decor instanceof Stone)
            return new SpriteDecor(layer, factory.get(STONE), position);
        if (decor instanceof Tree)
            return new SpriteDecor(layer, factory.get(TREE), position);
        if (decor instanceof Box)
            return new SpriteDecor(layer, factory.get(BOX), position);
        if (decor instanceof Bomberwoman)
            return new SpriteDecor(layer, factory.get(BOMBERWOMAN), position);
        if (decor instanceof Bombnumberdec)
            return new SpriteDecor(layer, factory.get(BOMBNUMBERDEC), position);
        if (decor instanceof Bombnumberinc)
            return new SpriteDecor(layer, factory.get(BOMBNUMBERINC), position);
        if (decor instanceof Bombrangedec)
            return new SpriteDecor(layer, factory.get(BOMBRANGEDEC), position);
        if (decor instanceof Bombrangeinc)
            return new SpriteDecor(layer, factory.get(BOMBRANGEINC), position);
        if (decor instanceof Doornextopened)
            return new SpriteDecor(layer, factory.get(DOORNEXTOPENED), position);
        if (decor instanceof Doornextclosed)
            return new SpriteDecor(layer, factory.get(DOORNEXTCLOSED), position);
        if (decor instanceof Doorprevopened)
            return new SpriteDecor(layer, factory.get(DOORPREVOPENED), position);
        if (decor instanceof Key)
            return new SpriteDecor(layer, factory.get(KEY), position);
        if (decor instanceof Heart)
            return new SpriteDecor(layer, factory.get(HEART), position);
        if(decor instanceof Explosion)
            return new SpriteDecor(layer,factory.get(EXPLOSION),position);

        throw new RuntimeException("Unsupported sprite for decor " + decor);
    }

    public static Sprite createPlayer(Pane layer, Player player) {
        return new SpritePlayer(layer, player);
    }

    public static Sprite createMonster(Pane layer, Monster monster) {
        return new SpriteMonster(layer, monster);
    }
    
    public static Sprite createBomb(Pane layer, Bomb bomb){
        return new SpriteBomb(layer,bomb);
    }

    public static Sprite createExplosion(Pane layer,Position position){return new SpriteDecor(layer,ImageFactory.getInstance().get(EXPLOSION),position);}

}
