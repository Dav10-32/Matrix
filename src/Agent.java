public class Agent implements Runnable {

    private int x, y;
    private Game game;

    public Agent(int x, int y, Game game) {
        this.x = x;
        this.y = y;
        this.game = game;
    }

    @Override
    public void run() {
        while (!game.isNioEscape() && !game.isCaughtNio()) {
            synchronized (game.getLock()) {
                while (game.isTurnoNio()) {
                    try { game.getLock().wait(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                }

                mover();
                System.out.println("Agente se movió a (" + x + "," + y + ")");
                if (x == game.getNio().getX() && y == game.getNio().getY()) {
                    game.setCaughtNio();
                }
            }

            try { Thread.sleep(200); } catch (InterruptedException ignored) {}
        }
    }

    private void mover() {
        Nio nio = game.getNio();

        int nx = x;
        int ny = y;

        if (nio.getX() > x) nx++;
        else if (nio.getX() < x) nx--;

        if (nio.getY() > y) ny++;
        else if (nio.getY() < y) ny--;

        game.updatePosition(x, y, nx, ny, 'A');
        x = nx;
        y = ny;
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
