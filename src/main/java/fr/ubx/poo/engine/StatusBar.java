/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.engine;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import static fr.ubx.poo.view.image.ImageResource.*;

public class StatusBar {
    public static final int height = 55;
    private final Text liveValue = new Text();
    private final Text bombsValue = new Text();
    private final Text rangeValue = new Text();
    private final Text keyValue = new Text();
    private final HBox level = new HBox();
    private final int gameLevel = 1;
    private final DropShadow ds = new DropShadow();


    public StatusBar(Group root, int sceneWidth, int sceneHeight) {
        // Status bar

        level.getStyleClass().add("level");
        level.getChildren().add(new ImageView(ImageFactory.getInstance().getDigit(gameLevel)));

        ds.setRadius(5.0);
        ds.setOffsetX(3.0);
        ds.setOffsetY(3.0);
        ds.setColor(Color.color(0.5f, 0.5f, 0.5f));


        HBox status = new HBox();
        status.getStyleClass().add("status");
        HBox live = statusGroup(ImageFactory.getInstance().get(HEART), this.liveValue);
        HBox bombs = statusGroup(ImageFactory.getInstance().get(BANNER_BOMB), bombsValue);
        HBox range = statusGroup(ImageFactory.getInstance().get(BANNER_RANGE), rangeValue);
        HBox key = statusGroup(ImageFactory.getInstance().get(KEY), keyValue);
        status.setSpacing(40.0);
        status.getChildren().addAll(live, bombs, range, key);

        HBox hBox = new HBox();
        hBox.getChildren().addAll(level, status);
        hBox.getStyleClass().add("statusBar");
        hBox.relocate(0, sceneHeight);
        hBox.setPrefSize(sceneWidth, height);
        root.getChildren().add(hBox);
    }

    private void updateLevel(int n) {
        if (n != gameLevel) {
            level.getChildren().clear();
            level.getChildren().add(new ImageView(ImageFactory.getInstance().getDigit(n)));
        }
    }

    private HBox statusGroup(Image kind, Text number) {
        HBox group = new HBox();
        ImageView img = new ImageView(kind);
        group.setSpacing(4);
        number.setEffect(ds);
        number.setCache(true);
        number.setFill(Color.BLACK);
        number.getStyleClass().add("number");
        group.getChildren().addAll(img, number);
        return group;
    }

    public void update(Game game) {
        updateLevel(game.getLevel()+1);
        liveValue.setText(String.valueOf(game.getPlayer().getLives()));
        rangeValue.setText(String.valueOf(game.getPlayer().getRange()));
        bombsValue.setText(String.valueOf(game.getPlayer().getBombs()));
        keyValue.setText(String.valueOf(game.getPlayer().getKeys()));
    }

}
