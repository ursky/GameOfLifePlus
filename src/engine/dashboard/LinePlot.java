package engine.dashboard;

import constants.PlotConstants;
import constants.UiConstants;
import engine.Engine;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class LinePlot {
    Engine engine;
    int minX, minY, maxX, maxY, midPointY, height;
    int anticipatedSize;
    int[] xValues;
    int[] yValues;
    int xIncrement;
    String label;
    Color color = Color.white;
    AffineTransform affineTransform;
    Font font, font90;
    int maxValue = 1;
    int minValue = 0;

    public void update(ArrayList<Integer> values) {
        this.anticipatedSize = Math.min(
                values.size(), (this.maxX - this.minX) / this.xIncrement);
        this.xValues = new int[this.anticipatedSize];
        this.yValues = new int[this.anticipatedSize];
        int xPosition = this.maxX;
        int index;
        for (int i=0; i<Math.min(this.anticipatedSize, values.size()); i++) {
            index = values.size() - i - 1;
            this.xValues[i] = xPosition;
            this.yValues[i] = values.get(index);
            xPosition -= this.xIncrement;
        }
    }

    public void draw() {
        if (this.yValues.length < 2) {
            return;
        }
        this.maxValue = this.getMax(this.yValues);
        this.minValue = this.getMin(this.yValues);
        int yStart, yEnd;
        this.engine.g2D.setStroke(new BasicStroke(1));
        for (int i=0; i<this.xValues.length-1; i++) {
            this.engine.g2D.setColor(this.color);
            yStart = this.maxY - (this.maxY - this.minY) * this.yValues[i] / maxValue;
            yEnd = this.maxY - (this.maxY - this.minY) * this.yValues[i+1] / maxValue;
            this.engine.g2D.drawLine(this.xValues[i], yStart, this.xValues[i+1], yEnd);
        }
        this.postProcess();
    }

    private int getMax(int[] values) {
        int maxValue = values[0];
        for (int value : values) {
            if (value > maxValue) {
                maxValue = value;
            }
        }
        return maxValue;
    }

    private int getMin(int[] values) {
        int minValue = values[0];
        for (int value : values) {
            if (value < minValue) {
                minValue = value;
            }
        }
        return minValue;
    }

    public void postProcess() {
        this.paintSeparationLines();
        this.paintLabels();
    }

    private void paintSeparationLines() {
        // draw division lines
        this.engine.g2D.setColor(Color.black);
        this.paintVerticalLine(this.minX - 3, 12);
        this.paintVerticalLine(this.maxX, 3);
    }

    private void paintLabels() {
        this.drawString(this.label, this.minX + 5, this.midPointY, this.font90);
        this.drawString(String.valueOf(this.minValue), this.maxX + 3, this.maxY - 5, this.font);
        this.drawString(String.valueOf(this.maxValue), this.maxX + 3, this.minY + 5, this.font);
    }

    private void paintVerticalLine(int xPosition, int width) {
        this.engine.g2D.setStroke(new BasicStroke(width));
        this.engine.g2D.drawLine(
                xPosition + width / 2,
                this.minY + width / 2,
                xPosition + width / 2,
                this.maxY);
    }

    public void drawString(String text, int xPos, int yPos, Font font) {
        this.engine.g2D.setColor(Color.white);
        this.engine.g2D.setFont(font);
        this.engine.g2D.drawString(text, xPos, yPos + text.length() * 2.5f);
    }

    public LinePlot(Engine engine, int startX, int endX, int xIncrement, String label) {
        this.engine = engine;
        this.label = label;
        this.minX = startX;
        this.maxX = endX;
        this.minY = UiConstants.panelHeight - PlotConstants.dashboardHeight;
        this.maxY = UiConstants.panelHeight;
        this.midPointY = (this.minY + this.maxY) / 2;
        this.height = this.maxY - this.minY;
        this.xIncrement = xIncrement;
        this.xValues = new int[0];
        this.yValues = new int[0];

        // pre-make rotated text fonts for labels
        this.affineTransform = new AffineTransform();
        this.font = new Font(null, Font.PLAIN, 10);
        this.affineTransform.rotate(Math.toRadians(-90), 0, 0);
        this.font90 = this.font.deriveFont(this.affineTransform);
    }
}