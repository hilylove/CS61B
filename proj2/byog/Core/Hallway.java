package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Hallway extends RectangleHelper {
    // Save positions of path
    private static Deque<Position> positionList = new LinkedList<>();

    /**
     * Generate hallway!
     */
    public static void hallwayGenerator(Random RANDOM, TETile[][] world) {
        Position start = randomStart(RANDOM, world);
        world[start.x][start.y] = Tileset.FLOOR;
        positionList.add(start);
        while (!positionList.isEmpty()) {
            Position curPosition = positionList.getLast();
            List<Position> availablePositions = checkPath(curPosition, world);
            if (!availablePositions.isEmpty()) {
                connectPath(availablePositions, curPosition, RANDOM, world);
            } else {
                positionList.removeLast();
            }
        }
    }

    private static Position randomStart(Random RANDOM, TETile[][] world) {
        boolean needToContinue = true;
        while (needToContinue) {
            int i = RANDOM.nextInt(world.length - 1) + 1;
            if (!(world[i + 1][1].equals(Tileset.NOTHING) || world[i - 1][1].equals(Tileset.NOTHING))) {
                needToContinue = false;
                return new Position(i, 1);
            }
        }
        throw new RuntimeException("You are lucky! There is no start position!");
    }

    /**
     * Find all available paths around a position. If not exist, return null.
     */
    private static List<Position> checkPath(Position curPosition, TETile[][] world) {
        List<Position> availablePositions = new LinkedList<>();
        Position[] around = aroundPositions(curPosition);
        Position[] connect = aroundPositions(curPosition, 2);
        for (int i = 0; i < 4; i++) {
            if (!isOnEdge(around[i], world) && !world[around[i].x][around[i].y].equals(Tileset.FLOOR)
                    && world[connect[i].x][connect[i].y].equals(Tileset.NOTHING)) {
                availablePositions.add(connect[i]);
            }
        }
        return availablePositions;
    }

    /**
     * Connect a path randomly(three directions).
     */
    private static void connectPath(List<Position> availablePositions, Position curPosition, Random RANDOM, TETile[][] world) {
        int whichWay = RANDOM.nextInt(availablePositions.size());
        Position middle = availablePositions.get(whichWay).middlePosition(curPosition);
        Position des = availablePositions.get(whichWay);
        world[middle.x][middle.y] = Tileset.FLOOR;
        world[des.x][des.y] = Tileset.FLOOR;
        positionList.addLast(des);
    }
}