package Snake;

//import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGameGUI extends JFrame {
    private static final int CELL_SIZE = 50;
    private static final int BOARD_SIZE = 10;

    private LinkedList<Point> snake;
    private Point food;
    private Direction direction;
    private boolean gameOver;

    public SnakeGameGUI() {
        setTitle("Snake Game");
        setSize(BOARD_SIZE * CELL_SIZE, BOARD_SIZE * CELL_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        snake = new LinkedList<>();
        snake.add(new Point(0, 0));
        direction = Direction.RIGHT;
        placeFood();

        GamePanel gamePanel = new GamePanel();
        add(gamePanel);

        Timer timer = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameOver) {
                    move();
                    checkCollisions();
                    gamePanel.repaint();
                }
            }
        });

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        direction = Direction.UP;
                        break;
                    case KeyEvent.VK_DOWN:
                        direction = Direction.DOWN;
                        break;
                    case KeyEvent.VK_LEFT:
                        direction = Direction.LEFT;
                        break;
                    case KeyEvent.VK_RIGHT:
                        direction = Direction.RIGHT;
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        setFocusable(true);
        timer.start();
    }

    private void move() {
        Point head = snake.getFirst();
        Point newHead = new Point(head.x + direction.x, head.y + direction.y);

        // Move snake
        snake.addFirst(newHead);

        // Check if snake eats food
        if (newHead.equals(food)) {
            placeFood();
        } else {
            // Remove the last segment if not eating
            snake.removeLast();
        }
    }

    private void placeFood() {
        Random rand = new Random();
        int x, y;

        do {
            x = rand.nextInt(BOARD_SIZE);
            y = rand.nextInt(BOARD_SIZE);
        } while (snake.contains(new Point(x, y)));

        food = new Point(x, y);
    }

    private void checkCollisions() {
        Point head = snake.getFirst();

        // Check for self-collision
        if (snake.subList(1, snake.size()).contains(head)) {
            gameOver = true;
            return;
        }

        // Check for wall collision
        if (head.x < 0 || head.x >= BOARD_SIZE || head.y < 0 || head.y >= BOARD_SIZE) {
            gameOver = true;
        }
    }

    private class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
    
            // Draw boundary
            g.setColor(Color.BLACK);
            g.drawRect(0, 0, BOARD_SIZE * CELL_SIZE, BOARD_SIZE * CELL_SIZE);
    
            // Draw snake
            for (Point point : snake) {
                g.setColor(Color.GREEN);
                g.fillRect(point.x * CELL_SIZE, point.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
    
            // Draw food
            g.setColor(Color.RED);
            g.fillRect(food.x * CELL_SIZE, food.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    
            // Draw game over message
            if (gameOver) {
                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString("Game Over! Your score: " + (snake.size() - 1), 50, getHeight() / 2);
            }
        }
    }
    

  
    private enum Direction {
        UP(0, -1),
        DOWN(0, 1),
        LEFT(-1, 0),
        RIGHT(1, 0);

        int x, y;

        Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SnakeGameGUI().setVisible(true);
            }
        });
    }
}
