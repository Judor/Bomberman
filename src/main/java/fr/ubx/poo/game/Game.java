/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;


import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.model.go.character.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class Game {
    private final World world;
    private final Player player;
    private final List<Monster> monsters;
    public int initPlayerLives;
    private int level=0;
    private int levels;
    private String prefix;
    private boolean changedLevel=false;
    

    public Game(String worldPath) {
        loadConfig(worldPath);
        world=new World(loadGame(worldPath));
        Position positionPlayer;
        monsters = world.findMonster(this);
        
        try {
            positionPlayer = world.findPlayer();
            player = new Player(this, positionPlayer);
            
        } catch (PositionNotFoundException e) {
            System.err.println("Position not found : " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    public int getInitPlayerLives() {
        return initPlayerLives;
    }

    private void loadConfig(String path) {
        try (InputStream input = new FileInputStream(new File(path, "config.properties"))) {
            Properties prop = new Properties();
            // load the configuration file
            prop.load(input);
            initPlayerLives = Integer.parseInt(prop.getProperty("lives", "3"));
            prefix=prop.getProperty("prefix");
            levels=Integer.parseInt(prop.getProperty("levels"));
        } catch (IOException ex) {
            System.err.println("Error loading configuration");
        }
    }
    
    
    private List<WorldEntity[][]> loadGame(String path){
    	List<WorldEntity[][]> game = new ArrayList<>();
    	for (int i=1; i<levels+1; i++) {
    		game.add(loadConf(path,i));
    	}
    	return game;
    }
    
    
    
    private WorldEntity[][] loadConf(String path, int level) {
    	 WorldEntity[][] World = null ;
        try  { 
        	BufferedReader fd = new BufferedReader(new FileReader(path+"/" +prefix+level+".txt"));
        	
        	String line;
    	    int j=0;
    	    fd.mark(1000);
    	    int x = (int) fd.lines().count();
    	    fd.reset();
    	    line = fd.readLine();
    	    fd.reset();
    	    int y = line.length();
    	    World = new WorldEntity[x][y];
    	    while ((line = fd.readLine()) != null) {
    			for (int i=0; i<y; i++) {    				
    				Optional<WorldEntity> v= WorldEntity.fromCode(line.charAt(i));
    				if(v.isPresent()) {
    					World[j][i]=v.get();
    				}
   
    			}
    			j++;
    		}
    	    fd.close();
        } catch (IOException ex) {
            System.err.println("Error loading level");
        }
       
        return World;
    }

    public World getWorld() {
        return world;
    }

    public Player getPlayer() {
        return this.player;
    }

	public boolean isChangedLevel() {
		return changedLevel;
	}

	public void setChangedLevel(boolean changedLevel) {
		this.changedLevel = changedLevel;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int l) {
		world.setActualLevel(l);
		this.level=l;
	}
	public List<Monster> getMonsters() {
		return monsters;
	}


}
