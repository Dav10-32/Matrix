public class Nio implements Runnable {

    private int xPosition, yPosition;
    private Game game;
    private boolean movedThisTurn = false;

    public Nio(int xPosition, int yPosition, Game game) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.game = game;
    }

    @Override
    public void run() {
        while (!game.isNioEscape() && !game.isCaughtNio()) {
            synchronized (game.getLock()) {
                while (!game.isTurnoNio()) {
                    movedThisTurn = false; // reset para el próximo turno
                    try { game.getLock().wait(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                }

                if (!movedThisTurn) {
                    mover();
                    System.out.println("Nio se movió a (" + xPosition + "," + yPosition + ")");
                    movedThisTurn = true;

                    if (xPosition == game.getTelefonoX() && yPosition == game.getTelefonoY()) {
                        game.updatePosition(xPosition, yPosition, xPosition, yPosition, 'N');
                        game.getNio().game.setNioEscape();
                    }
                }
            }

            try { Thread.sleep(200); } catch (InterruptedException ignored) {}
        }
    }

    private void mover() {
        int tx = game.getTelefonoX();
        int ty = game.getTelefonoY();

        int nx = xPosition;
        int ny = yPosition;

        if (tx > xPosition) nx++;
        else if (tx < xPosition) nx--;

        if (ty > yPosition) ny++;
        else if (ty < yPosition) ny--;

        game.updatePosition(xPosition, yPosition, nx, ny, 'N');
        xPosition = nx;
        yPosition = ny;
    }

    public int getX() { return xPosition; }
    public int getY() { return yPosition; }
}
