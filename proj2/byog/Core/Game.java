package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.*;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 81;
    public static final int HEIGHT = 31;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */


    public TETile[][] playWithInputString(String input) {
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        input = input.toLowerCase();
        TETile[][] finalWorldFrame = null;
        finalWorldFrame = newGame(input);
        return finalWorldFrame;
    }


    /**
     * public void playWithInputString(String input) {
     * TERenderer ter = new TERenderer();
     * ter.initialize(WIDTH, HEIGHT);
     * <p>
     * input = input.toLowerCase();
     * TETile[][] finalWorldFrame = null;
     * finalWorldFrame = newGame(input);
     * <p>
     * // draws the world to the screen
     * ter.renderFrame(finalWorldFrame);
     * }
     */

    private TETile[][] newGame(String input) {
        TETile[][] finalWorldFrame;
        int indexS = input.indexOf('s');
        long seed = convertSeed(input.substring(1, indexS));
        finalWorldFrame = generateWorld(seed);

        return finalWorldFrame;
    }

    private long convertSeed(String seedString) {
        return Long.valueOf(seedString.toString());
    }

    private TETile[][] generateWorld(long seed) {
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        initializeWorld(world);

        Random r = new Random(seed);

        List<Room> rooms = generateRooms(world, r, 10);
        generateHalls(world, r);
        generateConnectors(world, r, rooms);
        if (!rooms.isEmpty()) {
            carveDeadEnds(world);
        }
        carveExtraWalls(world);
        addADoor(world, r);
        return world;
    }

    private void initializeWorld(TETile[][] world) {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                world[x][y] = Tileset.WALL;
            }
        }

        for (int x = 1; x < WIDTH; x += 2) {
            for (int y = 1; y < HEIGHT; y += 2) {
                world[x][y] = Tileset.UNDEVFLOOR;
            }
        }
    }

    private List<Room> generateRooms(TETile[][] world, Random r, int roomNum) {
        Room.setRoomMaxNum(roomNum);
        List<Room> rooms = new ArrayList<>();
        for (int i = 0; i < Room.getRoomMaxNum(); ) {
            Room newRoom;
            do {
                Position p1 = new Position(decideXOrY(r, 1, WIDTH - 3), decideXOrY(r, 1, HEIGHT - 3));
                Position p2 = new Position(decideXOrY(r, p1.getX() + 1, WIDTH - 1),
                        decideXOrY(r, p1.getY() + 1, HEIGHT - 1));
                newRoom = new Room(p1, p2);
            } while (!Room.isLegal(newRoom));
            if (!newRoom.isOverLapped(rooms)) {
                rooms.add(newRoom);
                i++;
                newRoom.drawRoom(world, Tileset.ROOMFLOOR);
            }
        }
        return rooms;
    }

    private int decideXOrY(Random r, int start, int end) {
        int x = RandomUtils.uniform(r, start, end);
        if (x % 2 == 0) {
            if (RandomUtils.bernoulli(r)) {
                x++;
            } else {
                x--;
            }
        }
        return x;
    }

    private void generateHalls(TETile[][] world, Random r) {
        // dfs
        Stack<Position> stack = new Stack<>();
        Position startPoint = decideStartPoint(r, world);
        startPoint.drawTile(world, Tileset.FLOOR);
        stack.push(startPoint);
        while (!stack.isEmpty()) {
            Position existed = stack.peek();
            Connector conn = nextConnector(r, existed, world);
            if (conn == null) {
                stack.pop();
                continue;
            }
            conn.getGoalPos().drawTile(world, Tileset.FLOOR);
            conn.connect(world, Tileset.FLOOR);
            stack.push(conn.getGoalPos());
        }
    }

    private Position decideStartPoint(Random r, TETile[][] world) {
        Position p = new Position();
        int selector = RandomUtils.uniform(r, 0, 4);
        switch (selector) {
            case 0:
                p.setX(1);
                do {
                    p.setY(decideXOrY(r, 1, HEIGHT - 1));
                } while (p.isTile(world, Tileset.ROOMFLOOR));
                break;
            case 1:
                p.setY(1);
                do {
                    p.setX(decideXOrY(r, 1, WIDTH - 1));
                } while (p.isTile(world, Tileset.ROOMFLOOR));
                break;
            case 2:
                p.setX(WIDTH - 2);
                do {
                    p.setY(decideXOrY(r, 1, HEIGHT - 1));
                } while (p.isTile(world, Tileset.ROOMFLOOR));
                break;
            default:
                p.setY(HEIGHT - 2);
                do {
                    p.setX(decideXOrY(r, 1, WIDTH - 1));
                } while (p.isTile(world, Tileset.ROOMFLOOR));
                break;
        }
        return p;
    }

    private Connector nextConnector(Random r, Position p, TETile[][] world) {
        List<Connector> possibleConnectors = new ArrayList<>();
        for (Direction d : Direction.values()) {
            Connector.addConnectableDirection(possibleConnectors, world, Tileset.UNDEVFLOOR, d, p, WIDTH, HEIGHT);
        }
        if (possibleConnectors.isEmpty()) {
            return null;
        }

        int selector = RandomUtils.uniform(r, 0, possibleConnectors.size());
        return possibleConnectors.get(selector);
    }

    private void generateConnectors(TETile[][] world, Random r, List<Room> rooms) {
        for (Room room : rooms) {
            List<Connector> connectors = room.findConnectors(world, WIDTH, HEIGHT);
            if (connectors.isEmpty()) {
                continue;
            }
            int numOfSelection = RandomUtils.uniform(r, 1, 4);
            for (int i = 0; i < numOfSelection; i++) {
                int selector = RandomUtils.uniform(r, 0, connectors.size());
                connectors.get(selector).connect(world, Tileset.FLOOR);
            }
        }

        for (Room room : rooms) {
            room.drawRoom(world, Tileset.FLOOR);
        }
    }

    private void carveDeadEnds(TETile[][] world) {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                Position p = new Position(i, j);
                if (p.isDeadEnd(world, WIDTH, HEIGHT)) {
                    p.carveDeadEnd(world, WIDTH, HEIGHT);
                }
            }
        }
    }

    private void carveExtraWalls(TETile[][] world) {
        List<Position> solidWalls = new ArrayList<>();
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                Position p = new Position(i, j);
                if (p.isSolidWall(world, WIDTH, HEIGHT)) {
                    solidWalls.add(p);
                }
            }
        }

        for (Position p : solidWalls) {
            p.drawTile(world, Tileset.NOTHING);
        }
    }

    private void addADoor(TETile[][] world, Random r) {
        List<Position> edges = new ArrayList<>();
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                Position p = new Position(i, j);
                if (p.isEdge(world, WIDTH, HEIGHT)) {
                    edges.add(p);
                }
            }
        }
        int selector = RandomUtils.uniform(r, 0, edges.size());
        edges.get(selector).drawTile(world, Tileset.LOCKED_DOOR);
    }

}
