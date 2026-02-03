public class Nio extends Thread {
    private boolean caught = false;
    private int xPosition, yPosition;
    private Game game;

    public Nio(int xPosition, int yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }
}