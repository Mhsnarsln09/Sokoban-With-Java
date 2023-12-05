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

    private GameLogic gameLogic;
    private Player player;
    private int w = 0;
    private int h = 0;

    private boolean isCompleted = false;

    private Timer timer;
    private int timeElapsed = 0;

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

        String gameFrame = "    ######\n"
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

        gameLogic = new GameLogic(SPACE, walls, baggs, player, this::isCompleted);

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
            String formattedTime = formatTime(timeElapsed);
            g.drawString("Moves:" + "    " + gameLogic.moveCount, 375, 20);g.drawString("Time:" + "    " + formattedTime, 375, 40);
            g.drawString("Up : W,  ↑", 100, 300);
            g.drawString("Left : A,  ←", 25, 330);
            g.drawString("Down : S,  ↓", 100, 330);
            g.drawString("Right : D,  →", 175, 330);
            if (isCompleted) {
                g.drawString("Finish", 25, 20);
                g.drawString("New Game [ N ]", 375, 300);
                g.drawString("CONGRATULATION", 175, 375);
            }else if(gameLogic.moveCount>0) {
                g.drawString("Restart [ R ]", 375, 300);
            } else {
                g.drawString("Start [ W,  ↑ ]", 375, 300);
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
            Direction direction = mapKeyToDirection(e.getKeyCode());

            if(gameLogic.moveCount == 0 && direction == Direction.UP){
                startTimer();
            }
            if (direction != null) {
                switch (direction) {
                    case LEFT:
                        gameLogic.moveToLeft();
                        break;
                    case RIGHT:
                        gameLogic.moveToRight();
                        break;
                    case UP:
                        gameLogic.moveToTop();
                        break;
                    case DOWN:
                        gameLogic.moveToBottom();
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

        gameLogic.moveCount = 0;

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

    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }

}

