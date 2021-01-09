/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.engine;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.model.decor.Explosion;
import fr.ubx.poo.view.sprite.*;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.PositionNotFoundException;
import fr.ubx.poo.model.go.character.*;
import fr.ubx.poo.view.sprite.SpriteMonster;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.*;


public final class GameEngine {
    private static AnimationTimer gameLoop;
    private final String windowTitle;
    private final Game game;
    private StatusBar statusBar;
    private Pane layer;
    private Input input;
    private Stage stage;
    private final Player player;
    private List<Monster> monsters;
    private List<Bomb> bombs= new ArrayList<>();
    private boolean monsterDead=false;
    private int iDeadMonster=0;
    private Sprite spritePlayer;
    private final List<Sprite> sprites = new ArrayList<>();
    private List<SpriteBomb> spriteBomb=new ArrayList<>();
    private List<Sprite> spriteMonster=new ArrayList<>();


    public GameEngine(final String windowTitle, Game game, final Stage stage) {
        this.windowTitle = windowTitle;
        this.game = game;
        this.player = game.getPlayer();
        this.monsters = game.getMonsters();
        initialize(stage, game);
        buildAndSetGameLoop();
    }

    private void initialize(Stage stage, Game game) {
        this.stage = stage;
        Group root = new Group();
        layer = new Pane();

        int height = game.getWorld().getDimension().height;
        int width = game.getWorld().getDimension().width;
        int sceneWidth = width * Sprite.size;
        int sceneHeight = height * Sprite.size;
        Scene scene = new Scene(root, sceneWidth, sceneHeight + StatusBar.height);
        scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());

        stage.setTitle(windowTitle);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        input = new Input(scene);
        root.getChildren().add(layer);
        statusBar = new StatusBar(root, sceneWidth, sceneHeight);
        
        try {
			player.setPosition(game.getWorld().findPlayer());
		} catch (PositionNotFoundException positionNotFoundException) {
			positionNotFoundException.printStackTrace();
			System.out.println("No Player found !");
		}
        // Create decor sprites
        game.getWorld().forEach( (pos,d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d)));

        // Create Player sprites
        spritePlayer = SpriteFactory.createPlayer(layer, player);

        // Create Monsters and their sprites
        monsters.clear();
        monsters.addAll(game.getWorld().findMonster(game));
        iDeadMonster=0;
        monsterDead=false;
        spriteMonster.clear();  //We clear the monsters from previous levels.
        monsters.forEach(monster->monster.setLives(game.getLevel()+1)); // The higher the level, the tougher the monsters
        monsters.forEach(monster -> spriteMonster.add(SpriteFactory.createMonster(layer, monster))); //Create the monsters sprites
        MonstersMoveAutomatically(); //Make Monsters move by themselves
    }

    protected final void buildAndSetGameLoop() {
        gameLoop = new AnimationTimer() {
            public void handle(long now) {
                // Check keyboard actions
                processInput(now);

                // Do actions
                update(now);

                // Graphic update
                render();
                statusBar.update(game);
            }
        };
    }

    private void processInput(long now) {
        if (input.isExit()) {
            gameLoop.stop();
            Platform.exit();
            System.exit(0);
        }
        if (input.isMoveDown()) {
            player.requestMove(Direction.S);
        }
        if (input.isMoveLeft()) {
            player.requestMove(Direction.W);
        }
        if (input.isMoveRight()) {
            player.requestMove(Direction.E);
        }
        if (input.isMoveUp()) {
            player.requestMove(Direction.N);
        }
        //Let's check if player has a bomb in his inventory,decrement it, create a new bomb and a new sprite associated to it
        if (input.isBomb()) {
            if (player.getBombs() > 0) {
                player.decBomb();
                Bomb b = new Bomb(game, player.getPosition());
                bombs.add(b);
                spriteBomb.add((SpriteBomb) SpriteFactory.createBomb(layer,b));
            }
        }
        if (input.isKey()) {
            player.doorOpening();
        }
        input.clear();
    }

    private void showMessage(String msg, Color color) {
        Text waitingForKey = new Text(msg);
        waitingForKey.setTextAlignment(TextAlignment.CENTER);
        waitingForKey.setFont(new Font(60));
        waitingForKey.setFill(color);
        StackPane root = new StackPane();
        root.getChildren().add(waitingForKey);
        Scene scene = new Scene(root, 400, 200, Color.WHITE);
        stage.setTitle(windowTitle);
        stage.setScene(scene);
        input = new Input(scene);
        stage.show();
        new AnimationTimer() {
            public void handle(long now) {
                processInput(now);
            }
        }.start();
    }

    private void update(long now) {
    	if (game.isChangedLevel()) {
    		game.setChangedLevel(false);
    		initialize(stage,game);
    	}
    	//Update Monsters
        //If a monster is dead,  monsterDead will become true, and iDeadMonster will be the "id" of the dead monster that we'll have to delete
        monsters.forEach(monster -> monster.update(now));
        iDeadMonster=0;
        for (int i=0;i< monsters.size();i++) {
        	if (!monsters.get(i).isAlive()){
                iDeadMonster=i;
        	    monsterDead=true;
        		monsters.remove(monsters.get(i));
        	}
        }
        //Update Player & See if he won or if he died
        player.update();
        if (!player.isAlive()){
            gameLoop.stop();
            showMessage("You looser ! ", Color.RED);
        }
        if (player.isWinner()) {
            gameLoop.stop();
            showMessage("Congrats! ", Color.BLUE);
        }
        //Update bombs
        bombs.forEach(b -> b.update());
        bombs.removeIf(b -> b.getBoomed());
    }

    private void MonstersMoveAutomatically(){
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
            	monsters.forEach(monster -> monster.RandomMove());
            }
        }, 1, (3- game.getLevel())*1000); //The higher the level, the faster the monsters
    }

    private void render() {
    	if(this.game.getWorld().isAffichage()) {
    		sprites.forEach(Sprite::remove);
    		sprites.clear();
    		game.getWorld().forEach((Pos,dec) -> sprites.add(SpriteFactory.createDecor(layer,Pos,dec)));
    		game.getWorld().setAffichage(false);
    	}
    	//Remove the sprites of dead monsters
        if (monsterDead) {
            for (int i = 0; i < spriteMonster.size(); i++) {
                if(i==iDeadMonster){
                    spriteMonster.get(i).remove();
                    spriteMonster.remove(i);
                    monsterDead=false;
                    iDeadMonster=0;
                }
            }
        }
        //Remove the sprites of exploded bombs
        for(int i=0;i<spriteBomb.size();i++){
            if (spriteBomb.get(i).getBomb().getBoomed()){
                spriteBomb.get(i).remove();
                spriteBomb.remove(i);
        		game.getWorld().setAffichage(true);
            }
        }
        //Render all the sprites
        sprites.forEach(Sprite :: render);
        spriteMonster.forEach(Sprite::render);
        spriteBomb.forEach(Sprite::render);
        spritePlayer.render();
    }

    public void start() {
        gameLoop.start();
    }

}
