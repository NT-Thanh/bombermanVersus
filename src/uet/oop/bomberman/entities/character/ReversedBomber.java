package uet.oop.bomberman.entities.character;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.tile.item.BombItem;
import uet.oop.bomberman.entities.tile.item.FlameItem;
import uet.oop.bomberman.entities.tile.item.Item;
import uet.oop.bomberman.entities.tile.item.SpeedItem;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.level.Coordinates;

import java.util.Iterator;
import java.util.List;

public class ReversedBomber extends Bomber {
    private List<Bomb> _bombs;

    private int bombRate = BOMBRATE;
    private int bombRadius = BOMBRADIUS;
    private double bomberSpeed = BOMBERSPEED;

    public ReversedBomber(int x, int y, Board board){
        super(x, y, board);
        _bombs = _board.getBombs();
    }

    @Override
    public void update() {
        clearBombs();
        if (!_alive) {
            afterKill();
            return;
        }

        if (_timeBetweenPutBombs < -7500) _timeBetweenPutBombs = 0;
        else _timeBetweenPutBombs--;

        animate();

        calculateMove();

        detectPlaceBomb();
    }

    @Override
    public void render(Screen screen) {
        calculateXOffset();
        if(_alive){
            chooseSprite();
        }
        else
            _sprite = Sprite.balloom_dead;
        screen.renderEntity((int) _x, (int) _y - _sprite.SIZE, this);
    }

    @Override
    protected void calculateMove() {
        int xa = 0, ya = 0;
        if(_input.up) ya--;
        if(_input.down) ya++;
        if(_input.left) xa--;
        if(_input.right) xa++;

        if(xa != 0 || ya != 0)  {
            move(xa * bomberSpeed, ya * bomberSpeed);
            _moving = true;
        } else _moving = false;
    }

    @Override
    protected void detectPlaceBomb() {
        if(_input.l && _timeBetweenPutBombs < 0 && bombRate>0){
            int xt = Coordinates.pixelToTile(_x + _sprite.getSize() / 2);
            int yt = Coordinates.pixelToTile((_y - _sprite.getSize() / 2));

            placeBomb(xt, yt);
            bombRate--;
            _timeBetweenPutBombs = 20;
        }
    }

    private void clearBombs() {
        Iterator<Bomb> bs = _bombs.iterator();

        Bomb b;
        while (bs.hasNext()) {
            b = bs.next();
            if (b.isRemoved()) {
                bs.remove();
                bombRate++;
            }
        }

    }

    @Override
    public void resetPowerups(){
        bombRate = BOMBRATE;
        bomberSpeed = BOMBERSPEED;
        bombRadius = BOMBRADIUS;
    }

    @Override
    public void addPowerup(Item i) {
        if(i.isRemoved())
            return;

        if(i instanceof BombItem){
            bombRate++;
        }
        else if(i instanceof FlameItem){
            bombRadius++;
        }
        else if(i instanceof SpeedItem){
            bomberSpeed+=0.3;
        }
        _board.getGame().soundEffect.playItemGet();
    }

    private void chooseSprite(){
        switch(_direction) {
            case 0:
            case 1:
                _sprite = Sprite.movingSprite(Sprite.balloom_right1, Sprite.balloom_right2, Sprite.balloom_right3, _animate, 60);
                break;
            case 2:
            case 3:
                _sprite = Sprite.movingSprite(Sprite.balloom_left1, Sprite.balloom_left2, Sprite.balloom_left3, _animate, 60);
                break;
        }
//        switch (_direction) {
//            case 0:
//                _sprite = Sprite.player_up;
//                if (_moving) {
//                    _sprite = Sprite.movingSprite(Sprite.player_up_1, Sprite.player_up_2, _animate, 20);
//                }
//                break;
//            case 1:
//                _sprite = Sprite.player_right;
//                if (_moving) {
//                    _sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, _animate, 20);
//                }
//                break;
//            case 2:
//                _sprite = Sprite.player_down;
//                if (_moving) {
//                    _sprite = Sprite.movingSprite(Sprite.player_down_1, Sprite.player_down_2, _animate, 20);
//                }
//                break;
//            case 3:
//                _sprite = Sprite.player_left;
//                if (_moving) {
//                    _sprite = Sprite.movingSprite(Sprite.player_left_1, Sprite.player_left_2, _animate, 20);
//                }
//                break;
//            default:
//                _sprite = Sprite.player_right;
//                if (_moving) {
//                    _sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, _animate, 20);
//                }
//                break;
//        }
    }
}
