package engine.dashboard;

import engine.userIO.UiConstants;
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
    Color color = DashboardConstants.mainColor;
    AffineTransform affineTransform;
    Font font, font90;
    int maxValue = 1;
    int yStart, yEnd;

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
        this.engine.g2D.setStroke(new BasicStroke(1));
        for (int i=0; i<this.xValues.length-1; i++) {
            this.engine.g2D.setColor(this.color);
            this.yStart = this.maxY - (this.maxY - this.minY) * this.yValues[i] / this.maxValue;
            this.yEnd = this.maxY - (this.maxY - this.minY) * this.yValues[i+1] / this.maxValue;
            this.engine.g2D.drawLine(this.xValues[i], this.yStart, this.xValues[i+1], this.yEnd);
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

    public void postProcess() {
        this.paintSeparationLines();
        this.paintLabels();
    }

    private void paintSeparationLines() {
        // draw division lines
        this.engine.g2D.setColor(Color.black);
        this.paintVerticalLine(this.minX - 3, 13);
        this.paintVerticalLine(this.maxX, 3);
    }

    private void paintLabels() {
        this.drawString(this.label, this.minX + 9, this.midPointY, this.font90);
        this.drawString("0", this.maxX + 8, this.maxY - 6, this.font);
        String upperValue = this.asString(this.maxValue);
        this.drawString(upperValue, this.maxX + this.getStringWidth(upperValue) / 2 + 4,
                this.minY + 7, this.font);
    }

    public String asString(int value) {
        // add comma to large numbers for cleaner presentation
        String original = String.valueOf(value);
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < original.length(); i++) {
            char c = original.charAt(i);
            output.append(c);
            if (original.length()-i-1 == 3 || original.length()-i-1 == 6) {
                output.append(',');
            }
        }
        return output.toString();
    }

    public void paintVerticalLine(int xPosition, int width) {
        this.engine.g2D.setStroke(new BasicStroke(width));
        this.engine.g2D.drawLine(
                xPosition + width / 2,
                this.minY + width / 2,
                xPosition + width / 2,
                this.maxY);
    }

    public void drawString(String text, int xPos, int yPos, Font font) {
        int width = getStringWidth(text);
        this.engine.g2D.setColor(this.color);
        this.engine.g2D.setFont(font);
        // adjust positions to account for string length
        if (font == this.font) {
            xPos -= width / 2;
            yPos += 3;
        }
        if (font == this.font90) {
            yPos += width / 2;
            xPos -= 3;
        }
        this.engine.g2D.drawString(text, xPos, yPos);
    }

    private int getStringWidth(String text) {
        this.engine.g2D.setFont(this.font);
        return this.engine.g2D.getFontMetrics().stringWidth(text);
    }

    public LinePlot(Engine engine, int startX, int endX, int xIncrement, String label) {
        this.engine = engine;
        this.label = label;
        this.minX = startX + 4;
        this.maxX = endX;
        this.minY = UiConstants.panelHeight - DashboardConstants.dashboardHeight;
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