package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.List;
import java.util.Random;

public class WorldCreator extends RectangleHelper {
    public static TETile[][] worldGenerator(Random RANDOM, TETile[][] world) {
        fillWithWalls(world);
        List<Room> rooms = Room.roomGenerator(RANDOM, world);
        Hallway.hallwayGenerator(RANDOM, world);
        for (Room room : rooms) {
            room.randomRemoveWalls(RANDOM, world);
        }
        removeDeadEnds(world);
        removeInnerWalls(world);
        return world;
    }

    /**
     * Start by filling world with walls like this:
     * 0 0 0 0 0
     * 0 1 0 1 0
     * 0 0 0 0 0
     * 0 1 0 1 0
     * 0 0 0 0 0
     */

    public static void fillWithWalls(TETile[][] world) {
        // Print rows with walls and none
        for (int i = 0; i < world.length; i += 2) {
            for (int j = 1; j < world[0].length; j += 2) {
                world[i][j] = Tileset.WALL;
            }
        }

        // Print rows full of walls
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j += 2) {
                world[i][j] = Tileset.WALL;
            }
        }
    }

    /**
     * Remove all the dead ends.
     */
    private static void removeDeadEnds(TETile[][] world) {
        boolean done = false;
        while (!done) {
            done = true;
            for (int i = 0; i < world[0].length; i++) {
                for (int j = 0; j < world.length; j++) {
                    if (world[j][i] != Tileset.FLOOR) {
                        continue;
                    }
                    if (!isInDeadEnd(new Position(j, i), world)) {
                        continue;
                    }
                    done = false;
                    world[j][i] = Tileset.WALL;
                }
            }
        }
    }

    /**
     * Remove all the inner walls.
     * When all four corner positions of a wall aren't floor
     * it's called a inner wall.
     */
    private static void removeInnerWalls(TETile[][] world) {
        for (int i = 1; i < world[0].length - 1; i++) {
            for (int j = 1; j < world.length - 1; j++) {
                if (world[j][i] != Tileset.WALL) {
                    continue;
                }
                if (!isInnerWall(new Position(j, i), world)) {
                    continue;
                }
                world[j][i] = Tileset.NOTHING;
            }
        }
    }
}