import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import static java.awt.Font.PLAIN;


public class GamePanel extends JPanel implements MouseListener, ActionListener {
        static final int SCREEN_WIDTH =600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNIT = (SCREEN_WIDTH * SCREEN_HEIGHT / UNIT_SIZE);
    static final int DELAY = 110;
    boolean running = false;
    Timer timer;
    Random random;
    static int[][] board = new int[SCREEN_WIDTH / UNIT_SIZE][SCREEN_HEIGHT / UNIT_SIZE];
    static int generation = 0;
    static int aliveCells=0;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        this.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {

            }

            public void mousePressed(MouseEvent e) {

                if (board[getMousePosition().x / UNIT_SIZE][getMousePosition().y / UNIT_SIZE] == 0)
                    board[getMousePosition().x / UNIT_SIZE][getMousePosition().y / UNIT_SIZE] = 1;
                else
                    board[getMousePosition().x / UNIT_SIZE][getMousePosition().y / UNIT_SIZE] = 0;
            }

            public void mouseReleased(MouseEvent e) {

            }

            public void mouseEntered(MouseEvent e) {

            }

            public void mouseExited(MouseEvent e) {

            }
        });
        startGame();
    }

    public void startGame() {
        running = false;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.setColor(Color.darkGray);
        for (int i = 1; i < SCREEN_WIDTH / UNIT_SIZE; i++) {
            g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_WIDTH);
        }
        for (int i = 1; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
            g.drawLine(0, i * UNIT_SIZE, SCREEN_HEIGHT, i * UNIT_SIZE);
        }
        g.setColor(Color.white);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 1)
                    g.fillRect(i * UNIT_SIZE, j * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            }
        }
        g.setColor(Color.orange);
        g.setFont(new Font("Bauhaus 93", PLAIN, (SCREEN_HEIGHT / 20)));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Current generation : " + generation, (SCREEN_WIDTH - metrics1.stringWidth("Current generation : " + generation)) / 2, SCREEN_WIDTH * 19 / 20);
        g.setColor(Color.GREEN);
        g.setFont(new Font("Bauhaus 93", PLAIN, (SCREEN_HEIGHT / 20)));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Alive cells : " + aliveCells, (SCREEN_WIDTH - metrics2.stringWidth("Alive cells : " + aliveCells)) / 2, SCREEN_WIDTH * 17 / 20);
    }

    public void gameOfLife(int[][] board) {
        generation++;
        aliveCells=0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                encode(board, i, j);
            }
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                update(board, i, j);
            }
        }
    }

    void encode(int[][] board, int i, int j) {
        int aliveCount = 0;
        int neighVal;

        for (int r = -1; r <= 1; r++) {
            for (int c = -1; c <= 1; c++) {
                int neighR = i + r;
                int neighC = j + c;

                if (neighR >= 0 && neighR < board.length && neighC >= 0 && neighC < board[i].length) {
                    neighVal = board[neighR][neighC];
                    if ((r == 0 && c == -1) || r == -1)
                        neighVal = neighVal / 10;
                    if (neighVal == 1 && !(r == 0 && c == 0))
                        aliveCount++;
                }
            }
        }
        if(board[i][j]==1)
            aliveCells++;
        board[i][j] = board[i][j] * 10 + aliveCount;
    }

    void update(int[][] board, int i, int j) {
        int neighCount = board[i][j] % 10;
        int isAlive = board[i][j] / 10;

        if (isAlive == 1) {
            if (neighCount < 2 || neighCount > 3)
                board[i][j] = 0;
            else
                board[i][j] = 1;
        } else if (isAlive == 0) {
            if (neighCount == 3)
                board[i][j] = 1;
            else
                board[i][j] = 0;
        }
    }

    void clearBoard(int[][] board) {
        running = false;
        generation = 0;
        for (int i = 0; i < SCREEN_WIDTH / UNIT_SIZE; i++) {
            for (int j = 0; j < SCREEN_HEIGHT / UNIT_SIZE; j++) {
                board[i][j] = 0;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            gameOfLife(board);
        }
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (board[getMousePosition().x / UNIT_SIZE][getMousePosition().y / UNIT_SIZE] == 0)
        {
            board[getMousePosition().x / UNIT_SIZE][getMousePosition().y / UNIT_SIZE] = 1;
        }
        else {
            board[getMousePosition().x / UNIT_SIZE][getMousePosition().y / UNIT_SIZE] = 0;
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_SPACE:
                    running = running ? false : true;
                    break;
                case KeyEvent.VK_R:
                   clearBoard(board);
                    break;
            }
        }
    }
}


