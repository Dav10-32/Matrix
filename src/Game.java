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
    private boolean nioMovido = false;
    private boolean todosAgentesMovidos = false;
    private int agentesEnEspera = 0;

    private int xPhone = 4;
    private int yPhone = 4;

    public Game() {
        initMatrix();
        initEntities();
        printMatrix();
    }

    private void initMatrix() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = '.';
            }
        }
        board[xPhone][yPhone] = 'T';
    }

    private void initEntities() {
        this.nio = new Nio(7, 0, this);
        agents.add(new Agent(2, 3, this));
    }

    public void start() throws InterruptedException {
        Thread tNio = new Thread(this.nio);
        tNio.start();

        for (Agent a : agents) {
            new Thread(a).start();
        }

        while (!nioEscape && !caughtNio) {
            synchronized (lock) {
                // TURNO DE NIO
                System.out.println("\n=== TURNO DE NIO ===");
                turnoNio = true;
                nioMovido = false;
                todosAgentesMovidos = false;
                agentesEnEspera = agents.size();

                lock.notifyAll();

                while (!nioMovido) {
                    lock.wait();
                }

                if (nio.getX() == xPhone && nio.getY() == yPhone) {
                    nioEscape = true;
                    break;
                }

                // TURNO DE AGENTES
                System.out.println("\n=== TURNO DE AGENTES ===");
                turnoNio = false;
                lock.notifyAll();

                while (!todosAgentesMovidos) {
                    lock.wait();
                }

                for (Agent a : agents) {
                    if (a.getX() == nio.getX() && a.getY() == nio.getY()) {
                        caughtNio = true;
                        break;
                    }
                }
            }

            printMatrix();
            Thread.sleep(300);
        }

        printMatrix();
        if (nioEscape) {
            System.out.println("\n¡Nio ha escapado por el teléfono!");
        } else {
            System.out.println("\n¡Los agentes han atrapado a Nio!");
        }
        System.exit(0);
    }

    public void printMatrix() {
        System.out.println("\n----- TABLERO -----");
        char[][] tempBoard = new char[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                tempBoard[i][j] = '.';
            }
        }

        tempBoard[xPhone][yPhone] = 'T';
        tempBoard[nio.getX()][nio.getY()] = 'N';

        for (Agent a : agents) {
            if (a.getX() == nio.getX() && a.getY() == nio.getY()) {
                tempBoard[a.getX()][a.getY()] = 'X';
            } else if (a.getX() == xPhone && a.getY() == yPhone) {
                tempBoard[a.getX()][a.getY()] = 'X';
            } else {
                tempBoard[a.getX()][a.getY()] = 'A';
            }
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(tempBoard[i][j] + " ");
            }
            System.out.println();
        }
    }

    public Object getLock() {
        return lock;
    }

    public boolean isTurnoNio() {
        return turnoNio;
    }

    public void updatePosition(int ox, int oy, int nx, int ny, char c) {
        synchronized (lock) {
            board[ox][oy] = '.';
            board[nx][ny] = c;
        }
    }

    public void nioMovido() {
        synchronized (lock) {
            nioMovido = true;
            lock.notifyAll();
        }
    }

    public void agenteMovido() {
        synchronized (lock) {
            agentesEnEspera--;
            if (agentesEnEspera == 0) {
                todosAgentesMovidos = true;
                lock.notifyAll();
            }
        }
    }

    public Nio getNio() {
        return nio;
    }

    public int getTelefonoX() {
        return xPhone;
    }

    public int getTelefonoY() {
        return yPhone;
    }

    public boolean isNioEscape() {
        return nioEscape;
    }

    public boolean isCaughtNio() {
        return caughtNio;
    }

    public static void main(String[] args) throws InterruptedException {
        new Game().start();
    }
}