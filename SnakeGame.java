package Snake;
import java.util.*;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class SnakeGame {
    public static void main(String[] args) {
        new SnakeGame().startGame();
    }

    private static final int BOARD_SIZE = 10;
    private static final char EMPTY_CELL = '-';
    private static final char SNAKE_BODY = 'o';
    private static final char FOOD_CELL = '*';

    private LinkedList<Point> snake;
    private Point food;
    private Direction direction;
    private boolean gameOver;

    public SnakeGame() {
        snake = new LinkedList<>();
        snake.add(new Point(0, 0));
        direction = Direction.RIGHT;
        placeFood();
    }

    private void startGame() {
        Scanner scanner = new Scanner(System.in);

        while (!gameOver) {
            displayBoard();
            System.out.print("Enter a direction (W/A/S/D): ");
            char input = scanner.next().toUpperCase().charAt(0);
            updateDirection(input);
            move();
            checkCollisions();
        }

        System.out.println("Game Over! Your score: " + (snake.size() - 1));
        scanner.close();
    }

    private void displayBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Point currentPoint = new Point(i, j);
                if (snake.contains(currentPoint)) {
                    System.out.print(SNAKE_BODY + " ");
                } else if (currentPoint.equals(food)) {
                    System.out.print(FOOD_CELL + " ");
                } else {
                    System.out.print(EMPTY_CELL + " ");
                }
            }
            System.out.println();
        }
    }

    private void updateDirection(char input) {
        switch (input) {
            case 'W':
                direction = Direction.UP;
                break;
            case 'A':
                direction = Direction.LEFT;
                break;
            case 'S':
                direction = Direction.DOWN;
                break;
            case 'D':
                direction = Direction.RIGHT;
                break;
        }
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
            return;
        }
    }

    private enum Direction {
        UP(-1, 0),
        DOWN(1, 0),
        LEFT(0, -1),
        RIGHT(0, 1);

        int x, y;

        Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static class Point {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Point point = (Point) obj;
            return x == point.x && y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    
}
