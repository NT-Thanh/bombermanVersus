package uet.oop.bomberman.gui;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.input.Keyboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GameMenu extends MenuBar {

    private Board _board;
    private Menu gameMe;
    private Menu levels;
    private Menu more;

    private MenuItem pauseItem ;
    private MenuItem resumeItem;
    private MenuItem commandItem;
    private MenuItem about;
    private MenuItem help;
    private MenuItem versus;
    private MenuItem[] levelItems;

    public GameMenu(Board board){

        _board = board;
        gameMe = new Menu("Game");
        levels = new Menu("Levels");
        more = new Menu("More");

        about = new MenuItem("About");
        help = new MenuItem("Help");
        commandItem = new MenuItem("Command");
        versus = new MenuItem("Versus");
        pauseItem = new MenuItem("Pause");
        resumeItem = new MenuItem("Resume");
        resumeItem.setEnabled(false);

        MenuShortcut pauseShortcut = new MenuShortcut(0x50);
        MenuShortcut resumeShortcut = new MenuShortcut(0x52);
        MenuShortcut commandShortcut = new MenuShortcut(0x43);

        resumeItem.setShortcut(resumeShortcut);
        pauseItem.setShortcut(pauseShortcut);
        commandItem.setShortcut(commandShortcut);

        levelItems = new MenuItem[5];
        for(int i = 0; i<5; i++){
            int j = i+1;
            levelItems[i] = new MenuItem("Level " + j);
            levels.add(levelItems[i]);

            if(i>0)
                levelItems[i].setEnabled(false);
        }

        levelItems[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _board.loadLevel(1);
            }
        });
        levelItems[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _board.loadLevel(2);
            }
        });
        levelItems[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _board.loadLevel(3);
            }
        });
        levelItems[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _board.loadLevel(4);
            }
        });
        levelItems[4].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _board.loadLevel(5);
            }
        });
        versus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _board.loadLevel(0);
            }
        });
        pauseItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _board.gamePause();
                resumeItem.setEnabled(true);
                pauseItem.setEnabled(false);
            }
        });
        resumeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _board.gameResume();
                resumeItem.setEnabled(false);
                pauseItem.setEnabled(true);
            }
        });
        commandItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = JOptionPane.showInputDialog(null, "Your command");
                if(s != null && s.equals("mqcc"))
                    _board.unlockLevels();
            }
        });
        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _board.gamePause();
                JOptionPane.showMessageDialog(null, "Use keyboard to play this game: " + "\n" + "Up/W --- Move up" + "\n" + "Down/S --- Move down" + "\n" + "Right/D --- Move Right" + "\n" + "Left/A --- Move left" + "\n" + "Space/X --- Place a bomb", "Help", JOptionPane.INFORMATION_MESSAGE);
                _board.gameResume();
            }
        });
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _board.gamePause();
                JOptionPane.showMessageDialog(null, "Base on following project: https://github.com/carlosflorencio/bomberman" + "\n" + "Rebuild by Tien Thanh and Xuan Hoang", "About", JOptionPane.INFORMATION_MESSAGE);
                _board.gameResume();
            }
        });

        more.add(help);
        more.add(about);
        gameMe.add(pauseItem);
        gameMe.add(resumeItem);
        gameMe.add(commandItem);
        gameMe.add(levels);
        gameMe.add(versus);

        this.add(gameMe);
        this.add(more);
    }
    public void update(){
        for(int i=0; i<5; i++){
            if((i+1) <= _board.get_currentLevel()){
                levelItems[i].setEnabled(true);
            }
        }
    }
}
