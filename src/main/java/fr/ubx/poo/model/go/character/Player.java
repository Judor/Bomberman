/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.game.Game;

public class Player extends GameObject implements Movable {

    private boolean alive = true;
    Direction direction;
    private boolean moveRequested = false;
    private int lives = 1;
    private int range = 1;
    private int bombs = 1;
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
    	if (! (Pos.inside(game.getWorld().dimension)) || nextdec instanceof Stone  || nextdec instanceof Tree || nextdec instanceof Doornextclosed){
    	  return false;
    	}
    	if (nextdec instanceof Box)
    		return nextnextPos.inside(game.getWorld().dimension) && game.getWorld().isEmpty(nextnextPos);
        return true;
    }

    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Position nextnextPos = direction.nextPosition(nextPos);
        Decor nextdec = game.getWorld().get(nextPos);
        setPosition(nextPos);

        if (nextdec instanceof Heart) {
            setLives(lives + 1);
            game.getWorld().clear(nextPos);
            game.getWorld().setAffichage(true);
        }
        if (nextdec instanceof Key) {
            setKeys(keys + 1);
            game.getWorld().clear(nextPos);
            game.getWorld().setAffichage(true);
        }
        if (nextdec instanceof Bombnumberdec) {
            if (bombs > 0) {
                setBombs(bombs - 1);
                game.getWorld().clear(nextPos);
                game.getWorld().setAffichage(true);
            }
        }
        if (nextdec instanceof Bombnumberinc) {
            setBombs(bombs + 1);
            game.getWorld().clear(nextPos);
            game.getWorld().setAffichage(true);
        }
        if (nextdec instanceof Bombrangeinc) {
            setRange(range + 1);
            game.getWorld().clear(nextPos);
            game.getWorld().setAffichage(true);
        }
        if (nextdec instanceof Bombrangedec) {
            if (range > 1) {
                setRange(range - 1);
                game.getWorld().clear(nextPos);
                game.getWorld().setAffichage(true);
            }
        }
        if (nextdec instanceof Box) {
            game.getWorld().clear(nextPos);
            game.getWorld().setAffichage(true);
            game.getWorld().set(nextnextPos, new Box());
        }
        if (nextdec instanceof Bomberwoman) {
            winner = true;
        }
        

        Monster[] monster = game.getMonster();
        int nbMonster = game.getNbMonster();
        for (int i = 0; i < nbMonster; i++) {
            if (monster[i].getPosition().equals(this.getPosition()))
                if (monster[i].isAlive()) {
                getHurt();
            }

        }
    }
	public void update(long now) {
        if (moveRequested)
            if (canMove(direction))
                doMove(direction);
        moveRequested = false;
    }


    public void getHurt(){
        lives=lives-1;
        if (lives == 0)
            alive = false;
    }
    
    
    public void doorOpening() {
    	Position nextpos = direction.nextPosition(getPosition());
    	Decor nextdec = game.getWorld().get(nextpos);
    	if (nextdec instanceof Doornextclosed) {
            if (keys != 0) {
                game.getWorld().clear(nextpos);
                game.getWorld().setAffichage(true);
                game.getWorld().set(nextpos, new Doornextopened());
                keys--;
            }
        }
    }
    
    
    
    public void decBomb(){
        this.bombs=this.bombs-1;
    }
    public void incBomb(){
        this.bombs+=1;
    }

    public boolean isWinner() {
        return winner;
    }

    public boolean isAlive() {
        return alive;
    }

	public int getRange() {
		return range;
	}

	public void setRange(int ranges) {
		this.range = ranges;
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
