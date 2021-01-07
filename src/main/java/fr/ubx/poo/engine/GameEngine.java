/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.engine;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.World;
import fr.ubx.poo.model.decor.Doornextopened;
import fr.ubx.poo.view.sprite.Sprite;
import fr.ubx.poo.view.sprite.SpriteBomb;
import fr.ubx.poo.view.sprite.SpriteFactory;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.PositionNotFoundException;
import fr.ubx.poo.model.go.character.*;
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
    private final Player player;
    private Monster[] monster;
    private Bomb bomb;
    private final List<Sprite> sprites = new ArrayList<>();
    private StatusBar statusBar;
    private Pane layer;
    private Input input;
    private Stage stage;
    private Sprite spritePlayer;
    private List<SpriteBomb> spriteBomb=new ArrayList<>();
    private List<Sprite> spriteMonster=new ArrayList<>();
    private int nbMonster;
    private int nbBomb=0;



    public GameEngine(final String windowTitle, Game game, final Stage stage) {
        this.windowTitle = windowTitle;
        this.game = game;
        this.player = game.getPlayer();
        this.monster = game.getMonster();
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
        statusBar = new StatusBar(root, sceneWidth, sceneHeight, game);
        
        try {
			player.setPosition(game.getWorld().findPlayer());
		} catch (PositionNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("aaaaaaaaaaaaaaaa");
		}
        
        // Create decor sprites
        game.getWorld().forEach( (pos,d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d)));
        // Create Player sprites
        spritePlayer = SpriteFactory.createPlayer(layer, player);
        // Create Monsters sprites
        nbMonster=game.getWorld().nbMonsters();
        for(int i=0;i<nbMonster;i++) {
        	spriteMonster.add(SpriteFactory.createMonster(layer,monster[i]));
        }
        
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
        if(input.isBomb()){
            if (player.getBombs()>0){
                player.decBomb();
                nbBomb+=1;
                bomb=new Bomb(game,player.getPosition());
                spriteBomb.add((SpriteBomb) SpriteFactory.createBomb(layer,bomb));

            }
        }
        if(input.isKey()){
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
    	if (game.isChangelevel()) {
    		
    		game.setChangelevel(false);
    		initialize(stage,game);
    	}
    	
    	
        player.update(now);
        for (int i = 0; i < nbMonster; i++) {
            monster[i].update(now);
        }

        if (!player.isAlive()){
            gameLoop.stop();
        showMessage("You looser ! ", Color.RED);
    }
        if (player.isWinner()) {
            gameLoop.stop();
            showMessage("Congrats,it's a wrap ! ", Color.BLUE);
        }
    }

    private void MonstersMoveAutomatically(){
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (int i = 0; i < nbMonster; i++) {
                    monster[i].RandomMove();
                }
            }
        }, 2000,1300);
    }

    private void render() {    	
    	
    	if(this.game.getWorld().isAffichage()) {
    		sprites.forEach(Sprite::remove);
    		sprites.clear();
    		game.getWorld().forEach((Pos,dec) -> sprites.add(SpriteFactory.createDecor(layer,Pos,dec)));
    		game.getWorld().setAffichage(false);
    	}
        sprites.forEach(Sprite::render);
        for (int i = 0; i < nbMonster; i++) {
            if (!monster[i].isAlive()){
                spriteMonster.get(i).remove();
                spriteMonster.remove(i);
                nbMonster=nbMonster-1;
            }
        }
        spriteMonster.forEach(Sprite::render);
        spritePlayer.render();
        for(int i=0;i<spriteBomb.size();i++){
            if (spriteBomb.get(i).getBoom()){
                spriteBomb.get(i).remove();
                spriteBomb.remove(i);
            }
        }
        spriteBomb.forEach(Sprite::render);
    }

    public void start() {
        gameLoop.start();
    }



}
