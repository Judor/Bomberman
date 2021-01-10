package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.game.World;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Box;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.Stone;
import fr.ubx.poo.model.decor.Tree;
import fr.ubx.poo.model.go.GameObject;



public class Monster extends GameObject implements Movable {
    Direction direction;
    private int lives;
    private boolean moveRequested = false;
    private boolean alive=true;


    public Monster(Game game, Position position){
        super(game,position);
        this.direction=Direction.S;
        this.lives=1;
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
        if (direction != this.direction)
            this.direction = direction;
        moveRequested = true;
    }

    public boolean canMove(Direction direction) {
        World world=game.getWorld();
        Position Pos = direction.nextPosition(getPosition());
        Decor nextDec= world.get(Pos);
        return Pos.inside(world.getDimension()) && !(nextDec instanceof Stone) && !(nextDec instanceof Tree) && !(nextDec instanceof Box);
    }

    public void doMove(Direction direction){
        if (alive){
            World world=game.getWorld();
            Position nextPos = direction.nextPosition(getPosition());
            setPosition(nextPos);
            Player player=game.getPlayer();

            if (this.getPosition().equals(player.getPosition())) {
                if (!player.indestructible()) {
                    player.getHurt(player.getPosition());
                }
            }
        }
    }

    public void update() {
        if (moveRequested)
            if (canMove(direction))
                doMove(direction);
        moveRequested = false;
    }

    public void getHurt(Position pos){
        if (pos.equals(this.getPosition())) {
        	lives--;
        	if (lives == 0) {
        		alive = false;
        	}
        }
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean isAlive() {
        return alive;
    }
}

