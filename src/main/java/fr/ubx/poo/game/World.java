/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;

import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.character.Monster;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class World {
    private final List<Map<Position, Decor>> grid = new ArrayList<>();
    private final List<WorldEntity[][]> raw = new ArrayList<>();
    public final List<Dimension> dimension = new ArrayList<>();
    private boolean affichage;
    private boolean levelP=false;
    private boolean levelUp=false;
    private int actualLevel = 0;

    public World(List<WorldEntity[][]> raw) {
    	for (WorldEntity[][] raw1 : raw) {
    		this.raw.add(raw1);
    		Dimension d = new Dimension(raw1.length, raw1[0].length);
            dimension.add (new Dimension(raw1.length, raw1[0].length));
            grid.add(WorldBuilder.build(raw1, d));
    	}
        
    }


    public Position findPlayer() throws PositionNotFoundException {
    	Position pos;
    	if (isLevelP()) pos = findEntity(WorldEntity.DoorNextClosed);
    	else {
    		if (isLevelUp()) pos = findEntity(WorldEntity.DoorPrevOpened);
    		else pos = findEntity(WorldEntity.Player);
    	}
    	setLevelP(false);
    	setLevelUp(false);
    	if (pos==null) throw new PositionNotFoundException("Player");
    	return pos;
    }
    
    
    public Position findEntity(WorldEntity w) {
        for (int x = 0; x < getDimension().width; x++) {
            for (int y = 0; y < getDimension().height; y++) {
                if (raw.get(actualLevel)[y][x] == w ) {
                    return new Position(x, y);
                }
            }
        }
        return null;
    }

    
    public List<Monster> findMonster(Game a){
    	List<Monster> monsters=new ArrayList<>();
        for (int x = 0; x < getDimension().width; x++) {
            for (int y = 0; y < getDimension().height; y++) {
                if (raw.get(actualLevel)[y][x] == WorldEntity.Monster) {
                    monsters.add(new Monster(a, new Position(x,y)));
                }
            }
        }
        return monsters;
    }

    
    public Dimension getDimension() {
		return dimension.get(actualLevel);
	}

	public Decor get(Position position) {
        return grid.get(actualLevel).get(position);
    }

    public void set(Position position, Decor decor) {
        grid.get(actualLevel).put(position, decor);
    }

    public void clear(Position position) {
        grid.get(actualLevel).remove(position);
    }

    public void forEach(BiConsumer<Position, Decor> fn) {
        grid.get(actualLevel).forEach(fn);
    }

    public boolean isEmpty(Position position) {
        return grid.get(actualLevel).get(position) == null;
    }

	public boolean isAffichage() {
		return affichage;
	}

	public void setAffichage(boolean affichage) {
		this.affichage = affichage;
	}

	public boolean isLevelP() {
		return levelP;
	}

	public void setLevelP(boolean levelP) {
		this.levelP = levelP;
	}

	public boolean isLevelUp() {
		return levelUp;
	}

	public void setLevelUp(boolean levelUp) {
		this.levelUp = levelUp;
	}

	public void setActualLevel(int actualLevel) {
		this.actualLevel = actualLevel;
	}

}
