package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Entity;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.game.Game;

public class Bomb extends GameObject implements Movable {
    private boolean moveRequested = false;
    private int bombRange;
    public Bomb(Game game, Position position){
        super(game,position);
        this.bombRange=1;
    }
    public void bombRange(int range){
        this.bombRange=range;
    }

    @Override
    public boolean canMove(Direction direction) {
        return false;
    }

    @Override
    public void doMove(Direction direction) {

    }
    public void kaboom(){

    }





}
