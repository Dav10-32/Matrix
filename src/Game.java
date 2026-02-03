import java.util.ArrayList;
import java.util.List;

public class Game {
    public static final int SIZE = 8;
    private char[][] board = new char[SIZE][SIZE];

    private Nio nio;
    private boolean caughtNio = false;
    private boolean nioEscape = false;

    private List<Agent> agents = new ArrayList<>();
    private final Object lock = new Object();
    private boolean turnoNio = true;

    private int xPhone = 4;
    private int yPhone = 4;

    public Game() {
        initMatrix();
        initEntities();
        printMatrix(); // imprimir tablero inicial
    }

    private void initMatrix() {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                board[i][j] = '.';
        board[xPhone][yPhone] = 'T';
    }

    private void initEntities() {
        this.nio = new Nio(7, 2, this); // posición inicial de Nio
        agents.add(new Agent(2, 3, this)); // un agente
    }

    public void start() throws InterruptedException {

        Thread tNio = new Thread(this.nio);
        tNio.start();

        for (Agent a : agents) {
            new Thread(a).start();
        }

        while (!nioEscape && !caughtNio) {

            // TURNO NIO
            turnoNio = true;
            synchronized (lock) { lock.notifyAll(); }
            Thread.sleep(300);

            if (nio.getX() == xPhone && nio.getY() == yPhone) {
                nioEscape = true;
                break;
            }

            // TURNO AGENTES
            turnoNio = false;
            synchronized (lock) { lock.notifyAll(); }
            Thread.sleep(300);

            for (Agent a : agents) {
                if (a.getX() == nio.getX() && a.getY() == nio.getY()) {
                    caughtNio = true;
                    break;
                }
            }

            printMatrix();
            System.out.println("----- FIN DE TURNO -----\n");
        }

        System.out.println(nioEscape
                ? "¡Nio ha escapado por el teléfono!"
                : "¡Los agentes han atrapado a Nio!");
        System.exit(0);
    }

    public void printMatrix() {
        System.out.println("----- TURNO -----");
        for (char[] fila : board) {
            for (char c : fila) {
                System.out.print(c + " ");
            }
            System.out.println();
        }
    }

    public Object getLock() { return lock; }
    public boolean isTurnoNio() { return turnoNio; }
    public void updatePosition(int ox, int oy, int nx, int ny, char c) {
        if (board[ox][oy] != 'T') board[ox][oy] = '.';
        board[nx][ny] = c;
    }

    public Nio getNio() { return nio; }
    public int getTelefonoX() { return xPhone; }
    public int getTelefonoY() { return yPhone; }
    public boolean isNioEscape() { return nioEscape; }
    public boolean isCaughtNio() { return caughtNio; }

    public static void main(String[] args) throws InterruptedException {
        new Game().start();
    }

    public void setNioEscape() {
        this.nioEscape = true;
    }

    public void setCaughtNio() {
        this.caughtNio = true;
    }
}
