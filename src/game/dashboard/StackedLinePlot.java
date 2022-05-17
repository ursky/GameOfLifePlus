package game.dashboard;

import game.Game;

import java.awt.*;
import java.util.ArrayList;

/**
 * A class inheriting the LinePlot that allows for plotting a stacked line graph (for relative abundance plot)
 */
public class StackedLinePlot extends LinePlot {
    // these values store the polygon values of each of the lines so they are not re-calculated every frame
    int[][] xPolygonCoordinates;
    int[][] yPolygonCoordinates;
    // colors of each line
    ArrayList<Color> fixedColors = new ArrayList<>();
    int polygonSize;

    /**
     * Update the creature relative abundances and then generate new polygon shapes to represent them on the plot
     * @param values: relative abundances of each creature
     * @param colors: colors to paint each creature by
     */
    public void update(ArrayList<ArrayList<Float>> values, ArrayList<Color> colors) {
        if (values.get(0).size() < 2) {
            return;
        }
        // generate polygon coordinates
        // note that each polygon has to go around the data twice (imagine tracing the shape clockwise)
        this.dataSize = 2 * Math.min(
                values.get(0).size(),
                (this.maxX - this.minX) / this.xIncrement);
        this.generatePolygonX(values);
        this.generatePolygonY(values);
        // reduce alpha of colors
        this.fixedColors = adjustAlphas(colors);
        this.polygonSize = xPolygonCoordinates[0].length;
    }

    /**
     * Draw the stacked plot onto the dashboard
     */
    @Override
    public void draw() {
        for (int set=0; set<this.fixedColors.size(); set++) {
            this.game.g2D.setColor(this.fixedColors.get(set));
            this.game.g2D.fillPolygon(this.xPolygonCoordinates[set], this.yPolygonCoordinates[set], this.polygonSize);
        }
        this.postProcess();
    }

    /**
     * Generate x coordinates of each polygon. Simply move to the left and then to the right, storing the x-values
     * @param values: values to plot
     */
    private void generatePolygonX(ArrayList<ArrayList<Float>> values) {
        // create x-coordinates (points go around clockwise starting from bottom right)
        this.xPolygonCoordinates = new int[values.size()][this.dataSize];
        for (int i=0; i<values.size(); i++) {
            // go left
            int xPos = this.maxX;
            int xIndex = 0;
            for (int j = 0; j<this.dataSize / 2; j++) {
                this.xPolygonCoordinates[i][xIndex] = xPos;
                xPos -= this.xIncrement;
                xIndex ++;
            }
            // then go back right
            xPos += this.xIncrement;
            for (int j = 0; j<this.dataSize / 2; j++) {
                this.xPolygonCoordinates[i][xIndex] = xPos;
                xPos += this.xIncrement;
                xIndex ++;
            }
        }
    }

    /**
     * Store the y-coordinates of polygons from the data. Note the lower bounds of each polygon is the upper
     * bound of the polygon below it.
     * @param values: values to plot (relative abuundance)
     */
    private void generatePolygonY(ArrayList<ArrayList<Float>> values) {
        // create y-coordinates (points go around clockwise starting from bottom right)
        float[][] yFloatPolygonCoordinates = new float[values.size()][this.dataSize];
        float currentMin, currentMax, yIncrement;
        int dataPosition, pointPosition;
        int pointsToPlot = this.xPolygonCoordinates[0].length / 2;
        // loop over the data and calculate the y values for each polygon at that data position
        for (int i=0; i<pointsToPlot; i++) {
            dataPosition = values.get(0).size() - i - 1;
            pointPosition = pointsToPlot - i - 1;
            currentMax = this.minY;
            // note that we keep track of the previous upper bounds as we loop over the polygons to add to
            for (int set=0; set<values.size(); set++) {
                currentMin = currentMax;
                yIncrement = values.get(set).get(dataPosition) * this.height;
                if (yIncrement != yIncrement) {
                    yIncrement = 0;
                }
                currentMax = currentMin + yIncrement;
                // save min and max y values
                yFloatPolygonCoordinates[set][pointsToPlot - pointPosition - 1] = currentMin;
                yFloatPolygonCoordinates[set][pointsToPlot + pointPosition] = currentMax;
            }
        }

        // I have to convert float y-coordinated to int AFTER computing all of them to prevent rounding accumulation
        this.yPolygonCoordinates = this.convertToInt(yFloatPolygonCoordinates);
    }

    /**
     * Convert 2d list from clot to int. Note this is done to the y positions after the values are generated to
     * prevent rounding accumulation.
     * @param values: list with values to convert
     * @return: list with converted int values
     */
    private int[][] convertToInt(float[][] values) {
        int[][] valuesInt = new int[values.length][values[0].length];
        for (int i=0; i<values.length; i++) {
            for (int j=0; j<values[0].length; j++) {
                valuesInt[i][j] = (int) values[i][j];
            }
        }
        return valuesInt;
    }

    /**
     * Reduce alphas of the plot colors to make them a bit less "clown vomit"
     * @param colors: colors to scale down
     * @return: colors with reduced alphas
     */
    private ArrayList<Color> adjustAlphas(ArrayList<Color> colors) {
        for (int i=0; i<colors.size(); i++) {
            Color color = colors.get(i);
            Color newColor = new Color(
                    color.getRed(), color.getGreen(), color.getBlue(), 150);
            colors.set(i, newColor);
        }
        return colors;
    }

    /**
     * Initialize stacked line plot object to add to the dashboard
     * @param game: game engine
     * @param startX: left bound of this plot
     * @param endX: right bound of this plot
     * @param xIncrement: how many pixels to move over for every point (higher means plot moves faster)
     * @param label: text label to add as heading
     */
    public StackedLinePlot(Game game, int startX, int endX, int xIncrement, String label) {
        super(game, startX, endX, xIncrement, label);
    }
}