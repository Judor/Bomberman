package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Entity;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.game.Game;

public class Monster extends GameObject implements Movable {
    Direction direction;
    private int lives=1;
    private boolean moveRequested = false;


    public Monster(Game game, Position position){
        super(game,position);
        this.direction=Direction.S;
        this.lives=1;
    }

    public int getLives() {
        return lives;
    }
    public void setLives(int L) {
        this.lives=L;
    }

    public Direction getDirection() {
        return direction;
    }

    public void RandomMove(){
        int Random = (int)(Math.random()*4);
        switch (Random){
            case 0:
                requestMove(Direction.N) ;
                break;
            case 1 :
                requestMove(Direction.S);
                break;
            case 2 :
                requestMove(Direction.E) ;
                break;
            case 3 :
                requestMove(Direction.W );
                break;
        }
    }

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
        }
        moveRequested = true;
    }

    public boolean canMove(Direction direction) {
        Position Pos = direction.nextPosition(getPosition());
        Position nextnextPos = direction.nextPosition(Pos);
        Decor nextdec= game.getWorld().get(Pos);
        if (! (Pos.inside(game.getWorld().dimension)) || nextdec instanceof Stone  || nextdec instanceof Tree ||  nextdec instanceof Box){
            return false;
        }
        return true;

    }

    public void doMove(Direction direction){
        Position nextPos = direction.nextPosition(getPosition());
        Position nextnextPos = direction.nextPosition(nextPos);
        Decor nextdec = game.getWorld().get(nextPos);
        setPosition(nextPos);
        /* On veut tester ici si le monstre est sur la meme case que la joueur
        if (Entity.getPosition() = nextPos) {
            player.setLives() = player.getLives()-1;
        }
        Si oui on baisse la vie du joueur.
        */
        if( nextdec instanceof Heart ) {
            setLives(lives+1);
            game.getWorld().clear(nextPos);
            game.getWorld().setAffichage(true);
        }
    }

    public void update(long now) {
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
            }
        }
        moveRequested = false;
    }
}

