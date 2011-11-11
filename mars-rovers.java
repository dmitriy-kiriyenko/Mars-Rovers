package dim.mars.rovers;

import java.io.BufferedReader;
import java.io.FileReader;

public class MarsRovers {
    static final String dirs = "NESW";
    static final int[][] dirShifts = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };
    static final int dirCount = dirs.length();
    static int xMin = 0, yMin = 0, xMax, yMax;
    static final int X = 0, Y = 1, DIR = 2;

    static String move(String position, String instructions) {
        int[] rover = createRover(position.split(" "));
        for (final char instruction : instructions.toCharArray()) {
            rover = singleMove(rover, instruction);
        }

        return rover[X] + " " + rover[Y] + " " + dirs.charAt(rover[DIR]);
    }

    static private int[] singleMove(int[] rover, char instruction) {
        switch (instruction) {
        case 'R':
            return new int[] { rover[X], rover[Y], (rover[DIR] + 1) % dirCount };
        case 'L':
            return new int[] { rover[X], rover[Y], (rover[DIR] - 1 + dirCount) % dirCount };
        case 'M':
            return createRover(rover[X] + dirShifts[rover[DIR]][X], rover[Y] + dirShifts[rover[DIR]][Y],
                               rover[DIR]);
        default:
            return rover;
        }
    }

    static private int[] createRover(String[] positionParts) {
        return createRover(Integer.parseInt(positionParts[X]), Integer.parseInt(positionParts[Y]),
                           dirs.indexOf(positionParts[DIR]));
    }

    private static int[] createRover(int x, int y, int dir) {
        return new int[] { ensureBetween(x, xMin, xMax), ensureBetween(y, xMin, yMax), dir };
    }

    static private int ensureBetween(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public static void main(String[] args) throws Exception {
        final BufferedReader in = new BufferedReader(new FileReader("input.txt"));

        final String plateau = in.readLine();
        final String[] dimensions = plateau == null ? new String[] { "0", "0" } : plateau.split(" ");
        xMax = Integer.parseInt(dimensions[X]);
        yMax = Integer.parseInt(dimensions[Y]);

        String position, instructions;
        while ((position = in.readLine()) != null && (instructions = in.readLine()) != null) {
            System.out.println(move(position.toUpperCase(), instructions.toUpperCase()));
        }
    }
}
