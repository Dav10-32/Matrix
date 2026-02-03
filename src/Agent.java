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
        try {
            while (!game.isNioEscape() && !game.isCaughtNio()) {
                synchronized (game.getLock()) {
                    while (game.isTurnoNio()) {
                        game.getLock().wait();
                    }

                    mover();
                    System.out.println("Agente se movió a (" + x + "," + y + ")");
                    game.agenteMovido();

                    game.getLock().wait();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void mover() {
        Nio nio = game.getNio();

        int nx = x;
        int ny = y;

        if (nio.getX() > x) {
            nx++;
        } else if (nio.getX() < x) {
            nx--;
        }

        if (nio.getY() > y) {
            ny++;
        } else if (nio.getY() < y) {
            ny--;
        }

        if (nx < 0) nx = 0;
        if (nx >= Game.SIZE) nx = Game.SIZE - 1;
        if (ny < 0) ny = 0;
        if (ny >= Game.SIZE) ny = Game.SIZE - 1;

        game.updatePosition(x, y, nx, ny, 'A');
        x = nx;
        y = ny;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}