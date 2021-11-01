package com.jegensomme.codewars.closestpairs;

import java.util.Arrays;
import java.util.List;

import static java.lang.Math.*;
import static java.util.Comparator.comparingDouble;

/**
 * Given a number of points on a plane,
 * your task is to find two points with the smallest distance
 * between them in linearithmic O(n log n) time.
 */
class Kata {
    public static List<Point> closestPair(List<Point> points) {
        if (points.size() == 2) return points;
        Distance closest = getClosest(points.stream()
                .sorted(comparingDouble((Point p) -> p.x))
                .toArray(Point[]::new));
        return Arrays.asList(closest.first, closest.second);
    }

    private static Distance getClosest(Point[] points) {
        if (points.length <= 3) {
            return getClosestForce(points);
        }
        int mid = points.length / 2;
        Distance leftClosest = getClosest(Arrays.copyOfRange(points, 0, mid));
        Distance rightClosest = getClosest(Arrays.copyOfRange(points, mid, points.length));
        Distance lrClosest = leftClosest.compareTo(rightClosest) < 0 ? leftClosest : rightClosest;
        Point[] strip = Arrays.stream(points)
                .filter(p -> abs(p.x - points[mid].x) < lrClosest.distance)
                .sorted(comparingDouble(p -> p.y))
                .toArray(Point[]::new);
        Distance mClosest = middleClosest(strip, lrClosest);
        return lrClosest.compareTo(mClosest) < 0 ? lrClosest : mClosest;
    }

    private static Distance middleClosest(Point[] points, Distance d) {
        var min = d;
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length && abs(points[j].y - points[i].y) < min.distance; j++) {
                var distance = getDistance(points[i], points[j]);
                if (getDistance(points[i], points[j]).compareTo(min) < 0) {
                    min = distance;
                }
            }
        }
        return min;
    }

    private static Distance getClosestForce(Point[] points) {
        if (points.length < 2) {
            throw new IllegalArgumentException();
        }
        var min = getDistance(points[0], points[1]);
        if (points.length == 2) return min;
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                var distance = getDistance(points[i], points[j]);
                if (getDistance(points[i], points[j]).compareTo(min) < 0) {
                    min = distance;
                }
            }
        }
        return min;
    }

    private static final class Distance implements Comparable<Distance> {
        final Point first;
        final Point second;
        final double distance;

        public Distance(Point first, Point second, double distance) {
            this.first = first;
            this.second = second;
            this.distance = distance;
        }

        @Override
        public int compareTo(Distance o) {
            return Double.compare(distance, o.distance);
        }
    }

    private static Distance getDistance(Point first, Point second) {
        return new Distance(first, second, sqrt(pow(first.x - second.x, 2) + pow(first.y - second.y, 2)));
    }
}
