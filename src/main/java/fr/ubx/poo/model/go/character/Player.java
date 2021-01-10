/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.game.World;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.game.Game;

public class Player extends GameObject implements Movable {

    private boolean alive = true;
    private Direction direction;
    private boolean moveRequested = false;
    private int lives ;
    private int range = 1;
    private int bombs = 1;
    private int keys = 0;
    private boolean winner;
    private boolean noDamages =false;

    public Player(Game game, Position position) {
        super(game, position);
        this.direction = Direction.S;
        this.lives = game.getInitPlayerLives();
    }

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
        }
        moveRequested = true;
    }

    @Override
    public boolean canMove(Direction direction) {
        World world = game.getWorld();
    	Position Pos = direction.nextPosition(getPosition());
        Position nextNextPos = direction.nextPosition(Pos);
    	Decor nextDec= world.get(Pos);

    	if (! (Pos.inside(world.getDimension())) || nextDec instanceof Stone  || nextDec instanceof Tree || nextDec instanceof Doornextclosed){
    	  return false;
    	}
    	if (nextDec instanceof Box)
    		return nextNextPos.inside(world.getDimension()) && world.isEmpty(nextNextPos);
        return true;
    }

    public void doMove(Direction direction) {
        World world = game.getWorld();
        Position nextPos = direction.nextPosition(getPosition());
        Position nextNextPos = direction.nextPosition(nextPos);
        Decor nextDec = world.get(nextPos);
        setPosition(nextPos);

        if (nextDec instanceof Heart) {
            setLives(lives + 1);
            world.clear(nextPos);
            world.setAffichage(true);
        }
        if (nextDec instanceof Key) {
            setKeys(keys + 1);
            world.clear(nextPos);
            world.setAffichage(true);
        }
        if (nextDec instanceof Bombnumberdec) {
            if (bombs > 0) {
                setBombs(bombs - 1);
                world.clear(nextPos);
                world.setAffichage(true);
            }
        }
        if (nextDec instanceof Bombnumberinc) {
            setBombs(bombs + 1);
            world.clear(nextPos);
            world.setAffichage(true);
        }
        if (nextDec instanceof Bombrangeinc) {
            setRange(range + 1);
            world.clear(nextPos);
            world.setAffichage(true);
        }
        if (nextDec instanceof Bombrangedec) {
            if (range > 1) {
                setRange(range - 1);
                world.clear(nextPos);
                world.setAffichage(true);
            }
        }
        if (nextDec instanceof Box) {
            world.clear(nextPos);
            world.setAffichage(true);
            world.set(nextNextPos, new Box());
        }
        
        if (nextDec instanceof Doornextopened) {
        	game.setChangedLevel(true);
            world.setLevelUp(true);
        	game.setLevel(game.getLevel()+1);
        }
        
        if (nextDec instanceof Doorprevopened) {
        	game.setChangedLevel(true);
            world.setLevelP(true);
        	game.setLevel(game.getLevel()-1);
        }
        
        if (nextDec instanceof Bomberwoman) {
            winner = true;
        }

        if(!noDamages) {
            List<Monster> monsters = game.getMonsters();
            monsters.forEach(monster -> getHurt(monster.getPosition()));
        }
    }

    public void getHurt(Position pos){
        if (pos.equals(this.getPosition())) {
        	lives--;
        	if (lives == 0) {
                alive = false;
            }
        }
    }

    public void update() {
        if (moveRequested)
            if (canMove(direction))
                doMove(direction);
        moveRequested = false;
    }

    public void doorOpening() {
        World world=game.getWorld();
    	Position nextPos = direction.nextPosition(getPosition());
    	Decor nextDec = world.get(nextPos);
    	if (nextDec instanceof Doornextclosed) {
            if (keys != 0) {
                world.clear(nextPos);
                world.setAffichage(true);
                world.set(nextPos, new Doornextopened());
                keys--;
            }
        }
    }
    public boolean indestructible(){
        return noDamages;
    }

    public void mrIndestructible(){
        noDamages =true;
        TimerTask noDamages3S = new TimerTask() {
            public void run() {
                noDamages =false;
            }
        };
        Timer tMrIrr = new Timer();
        tMrIrr.schedule(noDamages3S,3000);
    }

    public Direction getDirection() {
        return direction;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int L) {
        this.lives=L;
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
