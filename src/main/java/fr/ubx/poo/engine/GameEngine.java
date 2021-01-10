/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.engine;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.PositionNotFoundException;
import fr.ubx.poo.model.go.character.Bomb;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.view.sprite.Sprite;
import fr.ubx.poo.view.sprite.SpriteBomb;
import fr.ubx.poo.view.sprite.SpriteFactory;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public final class GameEngine {
    private static AnimationTimer gameLoop;
    private final String windowTitle;
    private final Game game;
    private StatusBar statusBar;
    private Pane layer;
    private Input input;
    private Stage stage;
    private final Player player;
    private final List<Monster> monsters;
    private final List<Bomb> bombs= new ArrayList<>();
    private boolean monsterDead=false;
    private int iDeadMonster=0;
    private Sprite spritePlayer;
    private final List<Sprite> sprites = new ArrayList<>();
    private final List<SpriteBomb> spriteBomb=new ArrayList<>();
    private final List<Sprite> spriteMonster=new ArrayList<>();


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

        // Create Player sprites and make player indestructible for 3 first secs
        spritePlayer = SpriteFactory.createPlayer(layer, player);
        player.mrIndestructible();

        // Create Monsters and their sprites
        monsters.clear();
        monsters.addAll(game.getWorld().findMonster(game));
        iDeadMonster=0;
        monsterDead=false;
        spriteMonster.clear();  //We clear the monsters from previous levels.
        monsters.forEach(monster -> spriteMonster.add(SpriteFactory.createMonster(layer, monster))); //Create the monsters sprites
        spriteMonster.forEach(sprite -> sprite.setState(game.getLevel() + 1));//Set the sprites for the adequate level
        MonstersMoveAutomatically(); //Make Monsters move by themselves
    }

    protected final void buildAndSetGameLoop() {
        gameLoop = new AnimationTimer() {
            public void handle(long now) {
                // Check keyboard actions
                processInput();

                // Do actions
                update();

                // Graphic update
                render();
                statusBar.update(game);
            }
        };
    }

    private void processInput() {
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
        //Let's check if player has a bomb in his inventory,decrement it, and create a new bomb and a new sprite associated to it
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
                processInput();
            }
        }.start();
    }

    private void update() {
    	if (game.isChangedLevel()) {
    		game.setChangedLevel(false);
    		initialize(stage,game);
    	}
    	//Update Monsters
        //If a monster is dead, monsterDead will become true, and iDeadMonster will be the "id" of the dead monster that we'll have to delete
        monsters.forEach(Monster::update);
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
        bombs.forEach(Bomb::update);
        bombs.removeIf(Bomb::getBoomed);
    }

    private void MonstersMoveAutomatically(){
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
            	monsters.forEach(Monster::RandomMove);
            }
        }, 1, (3- game.getLevel())* 1000L); //The higher the level, the faster the monsters
    }

    private void render() {
    	if(this.game.getWorld().isAffichage()) {
    		sprites.forEach(Sprite::remove);
    		sprites.clear();
    		game.getWorld().forEach((Pos,dec) -> sprites.add(SpriteFactory.createDecor(layer,Pos,dec)));
    		game.getWorld().setAffichage(false);
    	}
    	//Remove the sprites of the dead monster
        if (monsterDead) {
            for (int i = 0; i < spriteMonster.size(); i++) {
                if (i == iDeadMonster) {
                    spriteMonster.get(i).remove();
                    spriteMonster.remove(i);
                    monsterDead = false;
                    iDeadMonster = 0;
                }
            }
        }
        //Remove the sprites of exploded bombs
        for (int i = 0; i < spriteBomb.size(); i++) {
            if (spriteBomb.get(i).getBomb().getBoomed()) {
                spriteBomb.get(i).remove();
                spriteBomb.remove(i);
                game.getWorld().setAffichage(true);
            }
        }
        //Render all the sprites
        sprites.forEach(Sprite::render);
        //Render the monsters sprites
        spriteMonster.forEach(Sprite::render);
        //Render the bombs sprites
        spriteBomb.forEach(Sprite::render);

        //We check if the player is in his indestructible period. If it's the case, his sprite going to be golden
        if (player.indestructible()) {
            spritePlayer.setState(0);
        }
        else {
            spritePlayer.setState(1);
        }
        spritePlayer.render();
    }

    public void start() {
        gameLoop.start();
    }

}
