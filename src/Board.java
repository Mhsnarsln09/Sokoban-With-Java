import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JPanel;
import java.util.Timer;
import java.util.TimerTask;

public class Board extends JPanel {

    private final int OFFSET = 30;
    private final int SPACE = 20;
   
    private ArrayList<Wall> walls;
    private ArrayList<Baggage> baggs;
    private ArrayList<BaseArea> areas;

    private Player player;
    private int w = 0;
    private int h = 0;

    private final int LEFT_COLLISION = 1;
    private final int RIGHT_COLLISION = 2;
    private final int TOP_COLLISION = 3;
    private final int BOTTOM_COLLISION = 4;
    private int moveCount = 0;
    private boolean isCompleted = false;

    private Timer timer;
    private int timeElapsed = 0;
    private String gameFrame
            = "    ######\n"
            + "    ##   #\n"
            + "    ##$  #\n"
            + "  ####  $##\n"
            + "  ##  $ $ #\n"
            + "#### # ## #   ######\n"
            + "##   # ## #####  ..#\n"
            + "## $  $          ..#\n"
            + "###### ### #@##  ..#\n"
            + "    ##     #########\n"
            + "    ########\n";

    public Board() {

        initBoard();
    }

    private void initBoard() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        initWorld();
    }

    public int getBoardWidth() {
        return this.w;
    }

    public int getBoardHeight() {
        return this.h;
    }

    private void initWorld() {

        walls = new ArrayList<>();
        baggs = new ArrayList<>();
        areas = new ArrayList<>();

        int x = OFFSET;
        int y = OFFSET;

        Wall wall;
        Baggage b;
        BaseArea a;

        for (int i = 0; i < gameFrame.length(); i++) {

            char item = gameFrame.charAt(i);

            switch (item) {

                case '\n':
                    y += SPACE;

                    if (this.w < x) {
                        this.w = x;
                    }

                    x = OFFSET;
                    break;

                case '#':
                    wall = new Wall(x, y);
                    walls.add(wall);
                    x += SPACE;
                    break;

                case '$':
                    b = new Baggage(x, y);
                    baggs.add(b);
                    x += SPACE;
                    break;

                case '.':
                    a = new BaseArea(x, y);
                    areas.add(a);
                    x += SPACE;
                    break;

                case '@':
                    player = new Player(x, y);
                    x += SPACE;
                    break;

                case ' ':
                    x += SPACE;
                    break;

                default:
                    break;
            }

            h = y;
        }

    }

    private void buildWorld(Graphics g) {

        g.setColor(new Color(40, 40, 40));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        ArrayList<Actor> world = new ArrayList<>();

        world.addAll(walls);
        world.addAll(areas);
        world.addAll(baggs);
        world.add(player);

        for (int i = 0; i < world.size(); i++) {

            Actor item = world.get(i);

            if (item instanceof Player || item instanceof Baggage) {

                g.drawImage(item.getImage(), item.x() + 2, item.y() + 2, this);
            } else {

                g.drawImage(item.getImage(), item.x(), item.y(), this);
            }

            g.setColor(new Color(255, 255, 244));
            g.drawString("Moves:" + "    " + moveCount, 375, 20);g.drawString("Time:" + "    " + timeElapsed + " seconds", 375, 40);
            g.drawString("Up : W, ↑", 100, 300);
            g.drawString("Left : A, ←", 25, 330);
            g.drawString("Down : S, ↓", 100, 330);
            g.drawString("Right : D, →", 175, 330);
            if (isCompleted) {
                g.drawString("Completed", 25, 20);

            }

        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        buildWorld(g);
    }

    private enum Direction {
        LEFT, RIGHT, UP, DOWN, RESTART
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if(moveCount == 0){
                startTimer();
            }

            Direction direction = mapKeyToDirection(e.getKeyCode());
            if (direction != null) {
                switch (direction) {
                    case LEFT:
                        moveToLeft();
                        break;
                    case RIGHT:
                        moveToRight();
                        break;
                    case UP:
                        moveToTop();
                        break;
                    case DOWN:
                        moveToBottom();
                        break;
                    case RESTART:
                        restartLevel();
                        break;
                }
            }
            if (isCompleted) {
                return;
            }
            repaint();
        }

        private Direction mapKeyToDirection(int keyCode) {
            return switch (keyCode) {
                case KeyEvent.VK_LEFT, KeyEvent.VK_A -> Direction.LEFT;
                case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> Direction.RIGHT;
                case KeyEvent.VK_UP, KeyEvent.VK_W -> Direction.UP;
                case KeyEvent.VK_DOWN, KeyEvent.VK_S -> Direction.DOWN;
                case KeyEvent.VK_R, KeyEvent.VK_N -> Direction.RESTART;
                default -> null;
            };
        }
    }


    private boolean checkWallCollision(Actor actor, int type) {

        switch (type) {

            case LEFT_COLLISION:

                for (int i = 0; i < walls.size(); i++) {

                    Wall wall = walls.get(i);

                    if (actor.isLeftCollision(wall)) {

                        return true;
                    }
                }

                return false;

            case RIGHT_COLLISION:

                for (int i = 0; i < walls.size(); i++) {

                    Wall wall = walls.get(i);

                    if (actor.isRightCollision(wall)) {
                        return true;
                    }
                }

                return false;

            case TOP_COLLISION:

                for (int i = 0; i < walls.size(); i++) {

                    Wall wall = walls.get(i);

                    if (actor.isTopCollision(wall)) {

                        return true;
                    }
                }

                return false;

            case BOTTOM_COLLISION:

                for (int i = 0; i < walls.size(); i++) {

                    Wall wall = walls.get(i);

                    if (actor.isBottomCollision(wall)) {

                        return true;
                    }
                }

                return false;

            default:
                break;
        }

        return false;
    }

    private boolean checkBagCollision(int type) {

        switch (type) {

            case LEFT_COLLISION:

                for (int i = 0; i < baggs.size(); i++) {

                    Baggage bag = baggs.get(i);

                    if (player.isLeftCollision(bag)) {

                        for (int j = 0; j < baggs.size(); j++) {

                            Baggage item = baggs.get(j);

                            if (!bag.equals(item)) {

                                if (bag.isLeftCollision(item)) {
                                    return true;
                                }
                            }

                            if (checkWallCollision(bag, LEFT_COLLISION)) {
                                return true;
                            }
                        }

                        bag.move(-SPACE, 0);
                        isCompleted();
                    }
                }

                return false;

            case RIGHT_COLLISION:

                for (int i = 0; i < baggs.size(); i++) {

                    Baggage bag = baggs.get(i);

                    if (player.isRightCollision(bag)) {

                        for (int j = 0; j < baggs.size(); j++) {

                            Baggage item = baggs.get(j);

                            if (!bag.equals(item)) {

                                if (bag.isRightCollision(item)) {
                                    return true;
                                }
                            }

                            if (checkWallCollision(bag, RIGHT_COLLISION)) {
                                return true;
                            }
                        }

                        bag.move(SPACE, 0);
                        isCompleted();
                    }
                }
                return false;

            case TOP_COLLISION:

                for (int i = 0; i < baggs.size(); i++) {

                    Baggage bag = baggs.get(i);

                    if (player.isTopCollision(bag)) {

                        for (int j = 0; j < baggs.size(); j++) {

                            Baggage item = baggs.get(j);

                            if (!bag.equals(item)) {

                                if (bag.isTopCollision(item)) {
                                    return true;
                                }
                            }

                            if (checkWallCollision(bag, TOP_COLLISION)) {
                                return true;
                            }
                        }

                        bag.move(0, -SPACE);
                        isCompleted();
                    }
                }

                return false;

            case BOTTOM_COLLISION:

                for (int i = 0; i < baggs.size(); i++) {

                    Baggage bag = baggs.get(i);

                    if (player.isBottomCollision(bag)) {

                        for (int j = 0; j < baggs.size(); j++) {

                            Baggage item = baggs.get(j);

                            if (!bag.equals(item)) {

                                if (bag.isBottomCollision(item)) {
                                    return true;
                                }
                            }

                            if (checkWallCollision(bag, BOTTOM_COLLISION)) {

                                return true;
                            }
                        }

                        bag.move(0, SPACE);
                        isCompleted();
                    }
                }

                break;

            default:
                break;
        }

        return false;
    }

    public void moveToLeft() {
        if (checkWallCollision(player,
                LEFT_COLLISION)) {
            return;
        }

        if (checkBagCollision(LEFT_COLLISION)) {
            return;
        }

        player.move(-SPACE, 0);
        moveCount += 1;
    }

    public void moveToRight() {

        if (checkWallCollision(player, RIGHT_COLLISION)) {
            return;
        }

        if (checkBagCollision(RIGHT_COLLISION)) {
            return;
        }

        player.move(SPACE, 0);
        moveCount += 1;
    }

    public void moveToTop() {
        if (checkWallCollision(player, TOP_COLLISION)) {
            return;
        }

        if (checkBagCollision(TOP_COLLISION)) {
            return;
        }

        player.move(0, -SPACE);
        moveCount += 1;
    }

    public void moveToBottom() {
        if (checkWallCollision(player, BOTTOM_COLLISION)) {
            return;
        }

        if (checkBagCollision(BOTTOM_COLLISION)) {
            return;
        }

        player.move(0, SPACE);
        moveCount += 1;
    }
    public void isCompleted() {

        int nOfBags = baggs.size();
        int finishedBags = 0;

        for (int i = 0; i < nOfBags; i++) {

            Baggage bag = baggs.get(i);

            for (int j = 0; j < nOfBags; j++) {

                BaseArea area = areas.get(j);

                if (bag.x() == area.x() && bag.y() == area.y()) {

                    finishedBags += 1;
                }
            }
        }

        if (finishedBags == nOfBags) {

            isCompleted = true;
            repaint();
        }
    }

    private void restartLevel() {

        areas.clear();
        baggs.clear();
        walls.clear();

        moveCount = 0;

        timer.cancel();
        timeElapsed = 0;

        initWorld();

        if (isCompleted) {
            isCompleted = false;
        }
    }



    private void startTimer() {
        timer = new Timer();
          timer.scheduleAtFixedRate(new TimerTask() {
              @Override
              public void run() {
                  if (!isCompleted) {
                      timeElapsed++;
                      repaint();
                  }
              }
          }, 1000, 1000);

      }

}

