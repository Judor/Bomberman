/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.game.Game;

public class Player extends GameObject implements Movable {

    private final boolean alive = true;
    Direction direction;
    private boolean moveRequested = false;
    private int lives = 1;
    private int ranges = 1;
    private int bombs = 0;
    private int keys = 0;
    
    private boolean winner;

    public Player(Game game, Position position) {
        super(game, position);
        this.direction = Direction.S;
        this.lives = game.getInitPlayerLives();
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

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
        }
        moveRequested = true;
    }

    @Override
    public boolean canMove(Direction direction) {
    	Position Pos = direction.nextPosition(getPosition());
        Position nextnextPos = direction.nextPosition(Pos);
    	Decor nextdec= game.getWorld().get(Pos);
    	if (! (Pos.inside(game.getWorld().dimension)) || nextdec instanceof Stone  || nextdec instanceof Tree){
    	  return false;
    	}
    	if (nextdec instanceof Box) {
    		return nextnextPos.inside(game.getWorld().dimension) && game.getWorld().isEmpty(nextnextPos);
    	}
        return true;
    }

    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Position nextnextPos = direction.nextPosition(nextPos);
        Decor nextdec= game.getWorld().get(nextPos);
        setPosition(nextPos);
        
        if(nextdec instanceof Heart ) {
        	setLives(lives+1);
        	game.getWorld().clear(nextPos);
        	game.getWorld().setAffichage(true);
        }
        if(nextdec instanceof Key ) {
        	setKeys(keys+1);
        	game.getWorld().clear(nextPos);
        	game.getWorld().setAffichage(true);
        }
        if(nextdec instanceof Bombnumberdec ) {
        	if(bombs>0) {
	        	setBombs(bombs-1);
	        	game.getWorld().clear(nextPos);
	        	game.getWorld().setAffichage(true);
        	}
        }
        if(nextdec instanceof Bombnumberinc ) {
        	setBombs(bombs+1);
        	game.getWorld().clear(nextPos);
        	game.getWorld().setAffichage(true);
        }
        if(nextdec instanceof Bombrangeinc  ) {
        	setRanges(ranges+1);
        	game.getWorld().clear(nextPos);
        	game.getWorld().setAffichage(true);
        }
        if(nextdec instanceof Bombrangedec) {
        	if(ranges>1) {
        		setRanges(ranges-1);
            	game.getWorld().clear(nextPos);
            	game.getWorld().setAffichage(true);
        	}
        	 
        }
        if(nextdec instanceof Box  ) {
         	game.getWorld().clear(nextPos);
         	game.getWorld().setAffichage(true);
         	game.getWorld().set(nextnextPos, new Box());
         }
        if(nextdec instanceof Bomberwoman ) {
            winner=true;
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

    public boolean isWinner() {
        return winner;
    }

    public boolean isAlive() {
        return alive;
    }

	public int getRanges() {
		return ranges;
	}

	public void setRanges(int ranges) {
		this.ranges = ranges;
	}

	public int getBombs() {
		return bombs;
	}

	public void setBombs(int bombs) {
		this.bombs = bombs;
	}

	public int getKeys() {
		return keys;
	}

	public void setKeys(int keys) {
		this.keys = keys;
	}

}
