public class Nio implements Runnable {
    private int xPosition, yPosition;
    private Game game;

    public Nio(int xPosition, int yPosition, Game game) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.game = game;
    }

    @Override
    public void run() {
        try {
            while (!game.isNioEscape() && !game.isCaughtNio()) {
                synchronized (game.getLock()) {
                    while (!game.isTurnoNio()) {
                        game.getLock().wait();
                    }

                    mover();
                    System.out.println("Nio se movió a (" + xPosition + "," + yPosition + ")");
                    game.nioMovido();

                    game.getLock().wait();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void mover() {
        int tx = game.getTelefonoX();
        int ty = game.getTelefonoY();

        int nx = xPosition;
        int ny = yPosition;

        if (tx > xPosition) {
            nx++;
        } else if (tx < xPosition) {
            nx--;
        }

        if (ty > yPosition) {
            ny++;
        } else if (ty < yPosition) {
            ny--;
        }

        if (nx < 0) nx = 0;
        if (nx >= Game.SIZE) nx = Game.SIZE - 1;
        if (ny < 0) ny = 0;
        if (ny >= Game.SIZE) ny = Game.SIZE - 1;

        game.updatePosition(xPosition, yPosition, nx, ny, 'N');
        xPosition = nx;
        yPosition = ny;
    }

    public int getX() {
        return xPosition;
    }

    public int getY() {
        return yPosition;
    }
}