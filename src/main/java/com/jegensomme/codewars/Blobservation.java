package com.jegensomme.codewars;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Comparator.comparingInt;

/**
 * https://www.codewars.com/kata/5abab55b20746bc32e000008
 */
public class Blobservation {
    private final int high;
    private final int width;
    private final int[][] map;
    private List<List<Integer>> blobs = List.of();

    public Blobservation(int high) {
        this(high, high);
    }

    public Blobservation(int high, int width) {
        this.high = high;
        this.width = width;
        map = new int[high][width];
    }

    public void populate(List<Map<String,Integer>> blobs) {
        int[][] newMap = new int[high][width];
        for (int i = 0; i < map.length; i++) {
            System.arraycopy(map[i], 0, newMap[i], 0, map[i].length);
        }
        for (Map<String, Integer> blob : blobs) {
            int x = blob.get("x");
            int y = blob.get("y");
            int size = blob.get("size");
            if (x < 0 || x >= high || y < 0 || y >= width || size < 0 || size > 20) {
                throw new IllegalArgumentException();
            }
            newMap[x][y] += size;
        }
        for (int i = 0; i < map.length; i++) {
            System.arraycopy(newMap[i], 0, map[i], 0, map[i].length);
        }
        updateBlobs();
    }

    public void move() {
        move(1);
    }

    public void move(int iterations) {
        if (iterations <= 0) throw new IllegalArgumentException();
        while (iterations != 0) {
            if (blobs.size() <= 1) {
                return;
            }
            int[][] newMap = new int[high][width];
            int min = blobs.stream().mapToInt(b -> b.get(2)).min().orElse(0);
            assert min != 0;
            for (List<Integer> blob : blobs) {
                final int x = blob.get(0);
                final int y = blob.get(1);
                final int size = blob.get(2);
                if (size == min) {
                    newMap[x][y] += map[x][y];
                    continue;
                }
                int[] victim = null;
                int k = 1;
                while (victim == null && (x - k >= 0 || x + k < high || y - k >= 0 || y + k < width)) {
                    victim = selectVictim(x, y, k++);
                }
                if (victim == null) {
                    throw new IllegalStateException();
                }
                int moveX = victim[0] > x ? x + 1 : victim[0] < x ? x - 1 : x;
                int moveY = victim[1] > y ? y + 1 : victim[1] < y ? y - 1 : y;
                newMap[moveX][moveY] += map[x][y];
            }
            for (int i = 0; i < map.length; i++) {
                System.arraycopy(newMap[i], 0, map[i], 0, map[i].length);
            }
            updateBlobs();
            iterations--;
        }
    }

    public List<List<Integer>> printState() {
        return blobs;
    }

    private void updateBlobs() {
        blobs = IntStream.range(0, map.length)
                .mapToObj(i -> IntStream.range(0, map[i].length)
                        .filter(j -> map[i][j] > 0)
                        .mapToObj(j -> List.of(i, j, map[i][j])))
                .flatMap(Function.identity())
                .collect(Collectors.toList());
    }

    private int[] selectVictim(int x, int y, int k) {
        int[][] points = new int[8 * k][];
        int j = 0;
        for (int i = y; i <= y + k; i++) {
            points[j++] = new int[] { x - k, i, j - 1 };
        }
        for (int i = x - k + 1; i <= x + k; i++) {
            points[j++] = new int[] { i, y + k, j - 1 };
        }
        for (int i = y + k - 1; i >= y - k; i--) {
            points[j++] = new int[] { x + k, i, j - 1 };
        }
        for (int i = x + k - 1; i >= x - k; i--) {
            points[j++] = new int[] { i, y - k, j - 1 };
        }
        for (int i = y - k + 1; i < y; i++) {
            points[j++] = new int[] { x - k, i, j - 1 };
        }
        Comparator<int[]> sizeComparator = comparingInt((int[] b) -> b[2]);
        Comparator<int[]> clockwiseComparator = comparingInt((int[] b) -> b[3]).reversed();
        return Arrays.stream(points)
                .filter(i -> i[0] >= 0 && i[1] >= 0 && i[0] < high && i[1] < width)
                .map(i -> new int[] { i[0], i[1], map[i[0]][i[1]], i[2] })
                .filter(b -> b[2] > 0 && b[2] < map[x][y])
                .max(sizeComparator.thenComparing(clockwiseComparator))
                .orElse(null);
    }
}
