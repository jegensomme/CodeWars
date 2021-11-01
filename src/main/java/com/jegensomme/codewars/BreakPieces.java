package com.jegensomme.codewars;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * https://www.codewars.com/kata/527fde8d24b9309d9b000c4e
 */
class BreakPieces {

    public static String[] process(String shape) {
        BreakPieces breakPieces = new BreakPieces(shape);
        return breakPieces.process();
    }

    private final int[][] map;

    private Direction dir = null;
    private int currentX = -1, currentY = -1;
    private int startX = -1, startY = -1;
    private final List<int[]> nodesToCut = new ArrayList<>();

    private BreakPieces(String shape) {
        map = Arrays.stream(shape.split("\n"))
                .map(String::chars)
                .map(IntStream::toArray)
                .toArray(int[][]::new);
    }

    private String[] process() {
        List<String> result = new ArrayList<>();
        System.out.println(printFigure(map));
        while (!toSingleLine().strip().isEmpty()) {
            int i = 0;
            while (startX == -1 && startY == -1 && i < map.length) {
                for (int j = 0; j < map[i].length; j++) {
                    if (map[i][j] == '+') {
                        startX = i;
                        startY = j;
                        break;
                    }
                }
                i++;
            }
            assert startX != -1 && startY != -1;
            currentX = startX;
            currentY = startY;
            do {
                chooseDirection();
                drawLine();
            } while (currentX != startX || currentY != startY);
            nodesToCut.add(new int[] { startX, startY });
            result.add(cut());
            startX = -1;
            startY = -1;
            dir = null;
            nodesToCut.clear();
        }
        return result.toArray(String[]::new);
    }

    private void drawLine() {
        do {
            switch (dir) {
                case UP:
                    currentX--;
                    break;
                case DOWN:
                    currentX++;
                    break;
                case RIGHT:
                    currentY++;
                    break;
                case LEFT:
                    currentY--;
                    break;
            }
        } while (map[currentX][currentY] != '+');
    }

    private String cut() {
        if (nodesToCut.size() < 4) {
            throw new IllegalStateException("must be at least 4 nodes to cut");
        }
        int top = nodesToCut.stream().mapToInt(n -> n[0]).min().getAsInt();
        int bottom = nodesToCut.stream().mapToInt(n -> n[0]).max().getAsInt();
        int left = nodesToCut.stream().mapToInt(n -> n[1]).min().getAsInt();
        int right = nodesToCut.stream().mapToInt(n -> n[1]).max().getAsInt();
        int[][] figure = new int[bottom - top + 1][right - left + 1];
        for (int[] line : figure) {
            Arrays.fill(line, ' ');
        }
        for (int i = 0; i < nodesToCut.size() - 1; i++) {
            int x1 = nodesToCut.get(i)[0];
            int y1 = nodesToCut.get(i)[1];
            int x2 = nodesToCut.get(i + 1)[0];
            int y2 = nodesToCut.get(i + 1)[1];
            figure[x1 - top][y1 - left] = map[x1][y1];
            if (x1 == startX && y1 == startY || getBondCount(x1, y1) < 2) {
                map[x1][y1] = ' ';
            }
            boolean needToCut = true;
            if (!isEmpty(x1, y1)) {
                for (int j = i + 1; j < nodesToCut.size(); j++) {
                    if (getBondCount(nodesToCut.get(j)[0], nodesToCut.get(j)[1]) > 2) {
                        needToCut = false;
                        break;
                    }
                }
            }
            if (x1 == x2) {
                for (int y = Math.min(y1, y2) + 1; y < Math.max(y1, y2); y++) {
                    figure[x1 - top][y - left] = map[x1][y] == '+' ? '-' : map[x1][y];
                    if (needToCut) {
                        map[x1][y] = ' ';
                    }
                }
            } else {
                for (int x = Math.min(x1, x2) + 1; x < Math.max(x1, x2); x++) {
                    figure[x - top][y1 - left] = map[x][y1] == '+' ? '-' : map[x][y1];
                    if (needToCut) {
                        map[x][y1] = ' ';
                    }
                }
            }
        }
        return printFigure(figure);
    }

    private int getBondCount(int x, int y) {
        return (isEmpty(x, y + 1) ? 0 : 1)
                + (isEmpty(x, y - 1) ? 0 : 1)
                + (isEmpty(x + 1, y) ? 0 : 1)
                + (isEmpty(x - 1, y) ? 0 : 1);
    }

    private boolean isEmpty(int x, int y) {
        return x >= map.length || x < 0 || y >= map[x].length || y < 0 || map[x][y] == ' ';
    }

    private Direction tryRightOrLeft() {
        return startY > currentY && !isEmpty(currentX, currentY + 1) ? Direction.RIGHT
                : startY < currentY && !isEmpty(currentX, currentY - 1) ? Direction.LEFT
                : !isEmpty(currentX, currentY + 1) ? Direction.RIGHT
                : !isEmpty(currentX, currentY - 1) ? Direction.LEFT
                : dir;
    }

    private Direction tryUpOrDown() {
        return startX > currentX && !isEmpty(currentX + 1, currentY) ? Direction.DOWN
                : startX < currentX && !isEmpty(currentX - 1, currentY) ? Direction.UP
                : !isEmpty(currentX + 1, currentY) ? Direction.DOWN
                : !isEmpty(currentX - 1, currentY) ? Direction.UP
                : dir;
    }

    private Direction chooseRightOrLeft() {
        return startY > currentY && !isEmpty(currentX, currentY + 1) || isEmpty(currentX, currentY - 1)
                ? Direction.RIGHT : Direction.LEFT;
    }

    private Direction chooseUpOrDown() {
        return startX > currentX && !isEmpty(currentX + 1, currentY) || isEmpty(currentX - 1, currentY)
                ? Direction.DOWN : Direction.UP;
    }

    private void chooseDirection() {
        Direction oldDir = dir;
        if (startX == currentX && startY == currentY) {
            dir = Direction.RIGHT;
        } else if (dir == Direction.UP) {
            dir = isEmpty(currentX - 1, currentY) ? chooseRightOrLeft()
                    : startX >= currentX ? tryRightOrLeft() : dir;
        } else if (dir == Direction.DOWN) {
            dir = isEmpty(currentX + 1, currentY) ? chooseRightOrLeft()
                    : startX <= currentX ? tryRightOrLeft() : dir;
        } else if (dir == Direction.RIGHT) {
            dir = isEmpty(currentX, currentY + 1) ? chooseUpOrDown()
                    : startY <= currentY ? tryUpOrDown() : dir;
        } else if (dir == Direction.LEFT) {
            dir = isEmpty(currentX, currentY - 1) ? chooseUpOrDown()
                    : startY >= currentY ? tryUpOrDown() : dir;
        }
        if (oldDir != dir) {
            nodesToCut.add(new int[] { currentX, currentY });
        }
    }

    private String toSingleLine() {
        return Arrays.stream(map)
                .flatMapToInt(Arrays::stream)
                .mapToObj(Character::toString)
                .collect(Collectors.joining());
    }

    private String printFigure(int[][] map) {
        return Arrays.stream(map)
                .map(line -> Arrays.stream(line)
                        .mapToObj(Character::toString)
                        .collect(Collectors.joining())
                        .replaceAll("\\s+$", ""))
                .collect(Collectors.joining("\n"));
    }

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
}