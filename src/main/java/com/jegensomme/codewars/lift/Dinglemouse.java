package com.jegensomme.codewars.lift;

import java.util.*;
import java.util.stream.Collectors;

/**
 * https://www.codewars.com/kata/58905bfa1decb981da00009e
 */
public class Dinglemouse {
    public static int[] theLift(final int[][] queues, final int capacity) {
        Floor[] floors = new Floor[queues.length];
        Lift lift = new Lift(capacity, floors);
        for (int i = 0; i < queues.length; i++) {
            floors[i] = new Floor(lift, i, queues[i]);
            floors[i].start();
        }
        return lift.run(floors);
    }

    private static class Lift {
        final int capacity;
        final Floor[] floors;

        int level;
        List<Floor> calls = new ArrayList<>();
        List<Integer> inside = new ArrayList<>();
        Direction dir;
        Direction call;

        Lift(int capacity, Floor[] floors) {
            this.capacity = capacity;
            this.floors = floors;
        }

        void call(Floor floor) {
            if (!calls.contains(floor)) {
                calls.add(floor);
            }
        }

        int[] run(Floor[] floors) {
            level = 0;
            dir = Direction.UP;
            call = calls.stream().anyMatch(f -> !f.getUp().isEmpty())
                    ? Direction.UP : Direction.DOWN;
            List<Integer> history = new ArrayList<>() {{
                add(0);
            }};
            while (!inside.isEmpty() || !calls.isEmpty()) {
                if (needToRelease() || needToEnter()) {
                    inside.removeIf(d -> d == level);
                    fixDirection();
                    calls.remove(floors[level]);
                    floors[level].onArriving();
                    if (!(history.size() == 1 && level == 0))  {
                        history.add(level);
                    }
                }
                level = dir == Direction.UP ? level + 1 : level - 1;
                fixDirection();
            }
            if (history.get(history.size() - 1) != 0)  {
                history.add(0);
            }
            return history.stream().mapToInt(Integer::intValue).toArray();
        }

        boolean needToRelease() {
            return inside.contains(level);
        }

        boolean needToEnter() {
            return calls.contains(floors[level]) &&
                    (!floors[level].getUp().isEmpty() && dir == Direction.UP ||
                            !floors[level].getDown().isEmpty() && dir == Direction.DOWN);
        }

        void fixDirection() {
            if (!inside.isEmpty() || needToEnter()) {
                return;
            }
            if (dir == Direction.UP) {
                List<Floor> upperCalls = calls.stream().filter(f -> f.level > level).collect(Collectors.toList());
                if (call == Direction.UP) {
                    if (upperCalls.isEmpty()) {
                        dir = call = Direction.DOWN;
                    } else if (upperCalls.stream().allMatch(f -> f.getUp().isEmpty())) {
                        call = Direction.DOWN;
                    }
                } else if (upperCalls.isEmpty()) {
                    dir = Direction.DOWN;
                }
            } else {
                List<Floor> lowerCalls = calls.stream().filter(f -> f.level < level).collect(Collectors.toList());
                if (call == Direction.DOWN) {
                    if (lowerCalls.isEmpty()) {
                        dir = call = Direction.UP;
                    } else if (lowerCalls.stream().allMatch(f -> f.getDown().isEmpty())) {
                        call = Direction.UP;
                    }
                } else if (lowerCalls.isEmpty()) {
                    dir = Direction.UP;
                }
            }
        }

        void accept(int direction) {
            inside.add(direction);
        }

        boolean isFull() {
            return inside.size() == capacity;
        }
    }


    private static class Floor {
        final Lift lift;
        final int level;
        final List<Integer> queue;

        Floor(Lift lift, int level, int[] queue) {
            this.lift = lift;
            this.level = level;
            this.queue = Arrays.stream(queue).boxed().collect(Collectors.toList());
        }

        List<Integer> getUp() {
            return queue.stream().filter(i -> i > level).collect(Collectors.toList());
        }

        List<Integer> getDown() {
            return queue.stream().filter(i -> i < level).collect(Collectors.toList());
        }

        void start() {
            if (!queue.isEmpty()) {
                lift.call(this);
            }
        }

        void onArriving() {
            List<Integer> incoming = lift.dir == Direction.UP ? getUp() : getDown();
            for (int i = 0; i < incoming.size() && !lift.isFull(); i++) {
                Integer next = incoming.get(i);
                lift.accept(next);
                queue.remove(next);
            }
            if (!queue.isEmpty()) {
                lift.call(this);
            }
        }
    }

    private enum Direction {
        UP, DOWN
    }
}
