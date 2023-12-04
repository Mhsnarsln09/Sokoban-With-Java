import java.awt.Image;
import javax.swing.ImageIcon;

public class Wall extends Actor{
    private Image image;

    public Wall(int x, int y) {
        super(x, y);

        initWall();
    }

    private void initWall() {

        ImageIcon wallIcon = new ImageIcon("src/assets/wall.png");
        image = wallIcon.getImage();
        setImage(image);
    }
}
