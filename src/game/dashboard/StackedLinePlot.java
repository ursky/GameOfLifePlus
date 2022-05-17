package game.dashboard;

import game.Engine;

import java.awt.*;
import java.util.ArrayList;

public class StackedLinePlot extends LinePlot {
    int[][] xPolygonCoordinates;
    int[][] yPolygonCoordinates;
    ArrayList<Color> fixedColors = new ArrayList<>();
    int polygonSize;

    public void update(ArrayList<ArrayList<Float>> values, ArrayList<Color> colors) {
        if (values.get(0).size() < 2) {
            return;
        }
        // generate polygon coordinates
        this.anticipatedSize = 2 * Math.min(
                values.get(0).size(),
                (this.maxX - this.minX) / this.xIncrement);
        this.generatePolygonX(values);
        this.generatePolygonY(values);
        this.fixedColors = adjustAlphas(colors);
        this.polygonSize = xPolygonCoordinates[0].length;
    }

    @Override
    public void draw() {
        for (int set=0; set<this.fixedColors.size(); set++) {
            this.engine.g2D.setColor(this.fixedColors.get(set));
            this.engine.g2D.fillPolygon(this.xPolygonCoordinates[set], this.yPolygonCoordinates[set], this.polygonSize);
        }
        this.postProcess();
    }

    private void generatePolygonX(ArrayList<ArrayList<Float>> values) {
        // create x-coordinates (points go around clockwise starting from bottom right)
        this.xPolygonCoordinates = new int[values.size()][this.anticipatedSize];
        for (int i=0; i<values.size(); i++) {
            // go left
            int xPos = this.maxX;
            int xIndex = 0;
            for (int j=0; j<this.anticipatedSize / 2; j++) {
                this.xPolygonCoordinates[i][xIndex] = xPos;
                xPos -= this.xIncrement;
                xIndex ++;
            }
            // then go back right
            xPos += this.xIncrement;
            for (int j=0; j<this.anticipatedSize / 2; j++) {
                this.xPolygonCoordinates[i][xIndex] = xPos;
                xPos += this.xIncrement;
                xIndex ++;
            }
        }
    }

    private void generatePolygonY(ArrayList<ArrayList<Float>> values) {
        // create y-coordinates (points go around clockwise starting from bottom right)
        float[][] yFloatPolygonCoordinates = new float[values.size()][this.anticipatedSize];
        float currentMin, currentMax, yIncrement;
        int dataPosition, pointPosition;
        int pointsToPlot = this.xPolygonCoordinates[0].length / 2;
        for (int i=0; i<pointsToPlot; i++) {
            dataPosition = values.get(0).size() - i - 1;
            pointPosition = pointsToPlot - i - 1;
            currentMax = this.minY;
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

    private int[][] convertToInt(float[][] values) {
        int[][] valuesInt = new int[values.length][values[0].length];
        for (int i=0; i<values.length; i++) {
            for (int j=0; j<values[0].length; j++) {
                valuesInt[i][j] = (int) values[i][j];
            }
        }
        return valuesInt;
    }

    private ArrayList<Color> adjustAlphas(ArrayList<Color> colors) {
        for (int i=0; i<colors.size(); i++) {
            Color color = colors.get(i);
            Color newColor = new Color(
                    color.getRed(), color.getGreen(), color.getBlue(), 150);
            colors.set(i, newColor);
        }
        return colors;
    }

    public StackedLinePlot(Engine engine, int startX, int endX, int xIncrement, String label) {
        super(engine, startX, endX, xIncrement, label);
    }
}