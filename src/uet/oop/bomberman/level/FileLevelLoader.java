package uet.oop.bomberman.level;

import com.sun.xml.internal.fastinfoset.util.CharArray;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.ReversedBomber;
import uet.oop.bomberman.entities.character.enemy.*;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Portal;
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.entities.tile.destroyable.Brick;
import uet.oop.bomberman.entities.tile.item.BombItem;
import uet.oop.bomberman.entities.tile.item.FlameItem;
import uet.oop.bomberman.entities.tile.item.SpeedItem;
import uet.oop.bomberman.exceptions.LoadLevelException;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileLevelLoader extends LevelLoader {

	/**
	 * Ma trận chứa thông tin bản đồ, mỗi phần tử lưu giá trị kí tự đọc được
	 * từ ma trận bản đồ trong tệp cấu hình
	 */
	private static char[][] _map;
	
	public FileLevelLoader(Board board, int level) throws LoadLevelException {
		super(board, level);
	}
	
	@Override
	public void loadLevel(int level) {
		// TODO: đọc dữ liệu từ tệp cấu hình /levels/Level{level}.txt
		// TODO: cập nhật các giá trị đọc được vào _width, _height, _level, _map
		String pa = "/levels/Level" + level + ".txt";
		try {
			InputStream is = this.getClass().getResourceAsStream(pa);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			String line = br.readLine();
			int vt1 = 0, vt2 = 0;
			for(int i=0; i<line.length(); i++){
				if(line.charAt(i) == ' ' && vt1 == 0) vt1 = i;
				else if(line.charAt(i) == ' ') vt2 = i;
			}
			_level = Integer.parseInt(line.substring(0, vt1));
			_height = Integer.parseInt(line.substring(vt1+1, vt2));
			_width = Integer.parseInt(line.substring(vt2+1, line.length()));

			System.out.println(_level + ", " + _height + ", " + _width);
			_map = new char[_height][_width];
			for(int i=0; i<_height; i++){
				_map[i] = br.readLine().toCharArray();
			}
			is.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void createEntities() {
		// TODO: tạo các Entity của màn chơi
		// TODO: sau khi tạo xong, gọi _board.addEntity() để thêm Entity vào game

		// TODO: phần code mẫu ở dưới để hướng dẫn cách thêm các loại Entity vào game
		// TODO: hãy xóa nó khi hoàn thành chức năng load màn chơi từ tệp cấu hình
		for (int x = 0; x < _height; x++) {
			for (int y = 0; y < _width; y++) {
				int pos = y + x * _width;
				if(_map[x][y] == '#'){
					_board.addEntity(pos, new Wall(y, x, Sprite.wall));
				}
				else if(_map[x][y] == '*'){
					_board.addEntity(pos,
							new LayeredEntity(y, x,
									new Grass(y, x, Sprite.grass),
									new Brick(y, x, Sprite.brick)
							)
					);
				}
				else if(_map[x][y] == 'x'){
					LayeredEntity layer = new LayeredEntity(y, x,
							new Grass(y, x, Sprite.grass),
							new Brick(y, x, Sprite.brick));
					layer.addBeforeTop(new Portal(y, x, _board, Sprite.portal));
					_board.addEntity(pos, layer);
				}
				// Add Characters
				else if(_map[x][y] == 'p'){
					_board.addCharacter( new Bomber(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board) );
					Screen.setOffset(0, 0);
					_board.addEntity(pos, new Grass(y, x, Sprite.grass));
				}
				else if(_map[x][y] == 'r'){
					_board.addCharacter( new ReversedBomber(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board) );
					Screen.setOffset(0, 0);
					_board.addEntity(pos, new Grass(y, x, Sprite.grass));
				}
				// Add enemys
				else if(_map[x][y] == '1'){
					_board.addCharacter( new Balloon(Coordinates.tileToPixel(y), Coordinates.tileToPixel(x) + Game.TILES_SIZE, _board));
					_board.addEntity(pos, new Grass(y, x, Sprite.grass));
				}
				else if(_map[x][y] == '2'){
					_board.addCharacter( new Oneal(Coordinates.tileToPixel(y), Coordinates.tileToPixel(x) + Game.TILES_SIZE, _board));
					_board.addEntity(pos, new Grass(y, x, Sprite.grass));
				}
				else if(_map[x][y] == '3'){
					_board.addCharacter( new Doll(Coordinates.tileToPixel(y), Coordinates.tileToPixel(x) + Game.TILES_SIZE, _board));
					_board.addEntity(pos, new Grass(y, x, Sprite.grass));
				}
				else if(_map[x][y] == '4'){
					_board.addCharacter( new Minvo(Coordinates.tileToPixel(y), Coordinates.tileToPixel(x) + Game.TILES_SIZE, _board));
					_board.addEntity(pos, new Grass(y, x, Sprite.grass));
				}
				else if(_map[x][y] == '5'){
					_board.addCharacter( new Kondoria(Coordinates.tileToPixel(y), Coordinates.tileToPixel(x) + Game.TILES_SIZE, _board));
					_board.addEntity(pos, new Grass(y, x, Sprite.grass));
				}
				// Add powerups
				else if(_map[x][y] == 'b'){
					LayeredEntity layer = new LayeredEntity(y, x,
							new Grass(y, x, Sprite.grass),
							new Brick(y, x, Sprite.brick));
					layer.addBeforeTop(new BombItem(y, x, Sprite.powerup_bombs));
					_board.addEntity(pos, layer);
				}
				else if(_map[x][y] == 'f'){
					LayeredEntity layer = new LayeredEntity(y, x,
							new Grass(y ,x, Sprite.grass),
							new Brick(y, x, Sprite.brick));
					layer.addBeforeTop(new FlameItem(y, x, Sprite.powerup_flames));
					_board.addEntity(pos, layer);
				}
				else if(_map[x][y] == 's'){
					LayeredEntity layer = new LayeredEntity(y, x,
							new Grass(y, x, Sprite.grass),
							new Brick(y, x, Sprite.brick));
					layer.addBeforeTop(new SpeedItem(y, x, Sprite.powerup_speed));
					_board.addEntity(pos, layer);
				}
				else
					_board.addEntity(pos, new Grass(y, x, Sprite.grass));
			}
		}

//		// thêm Bomber
//		int xBomber = 1, yBomber = 1;
//		_board.addCharacter( new Bomber(Coordinates.tileToPixel(xBomber), Coordinates.tileToPixel(yBomber) + Game.TILES_SIZE, _board) );
//		Screen.setOffset(0, 0);
//		_board.addEntity(xBomber + yBomber * _width, new Grass(xBomber, yBomber, Sprite.grass));
//
//		// thêm Enemy
//		int xE = 2, yE = 1;
//		_board.addCharacter( new Balloon(Coordinates.tileToPixel(xE), Coordinates.tileToPixel(yE) + Game.TILES_SIZE, _board));
//		_board.addEntity(xE + yE * _width, new Grass(xE, yE, Sprite.grass));
//
//		// thêm Brick
//		int xB = 3, yB = 1;
//		_board.addEntity(xB + yB * _width,
//				new LayeredEntity(xB, yB,
//					new Grass(xB, yB, Sprite.grass),
//					new Brick(xB, yB, Sprite.brick)
//				)
//		);
//
//		// thêm Item kèm Brick che phủ ở trên
//		int xI = 1, yI = 2;
//		_board.addEntity(xI + yI * _width,
//				new LayeredEntity(xI, yI,
//					new Grass(xI ,yI, Sprite.grass),
//					new SpeedItem(xI, yI, Sprite.powerup_flames),
//					new Brick(xI, yI, Sprite.brick)
//				)
//		);
	}

}
