import java.awt.Image;
import javax.swing.ImageIcon;
public class GoalWizard extends Actor {
    public GoalWizard(int x, int y) {
        super(x, y);

        initArea();
    }

    private void initArea() {

        ImageIcon goalWizardIcon = new ImageIcon("src/assets/magician.png");
        Image image = goalWizardIcon.getImage();
        setImage(image);
    }
}
