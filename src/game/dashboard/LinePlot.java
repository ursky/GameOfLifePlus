package game.dashboard;

import game.constants.DashboardConstants;
import game.constants.UiConstants;
import game.Engine;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

/**
 * This class handles a single plot in the dashboard
 */
public class LinePlot {
    Engine engine;
    // bounds of the plot
    int minX, minY, maxX, maxY, midPointY, height;
    // how many numerical values does the plot contain right now?
    int dataSize;
    // x and y values of the data
    int[] xValues;
    int[] yValues;
    int xIncrement;
    String label;
    Color color = DashboardConstants.mainColor;
    // pre-store rotated fonts
    AffineTransform affineTransform;
    Font font, font90;
    int maxValue = 1;
    int yStart, yEnd;

    /**
     * Update the data in the plot
     * @param values: list of values to update to (if longer than the x-axis - only the last values will be included)
     */
    public void update(ArrayList<Integer> values) {
        // how many points to include in the plot
        this.dataSize = Math.min(values.size(), (this.maxX - this.minX) / this.xIncrement);
        this.xValues = new int[this.dataSize];
        this.yValues = new int[this.dataSize];
        int xPosition = this.maxX;
        int index;
        for (int i = 0; i<Math.min(this.dataSize, values.size()); i++) {
            index = values.size() - i - 1;
            this.xValues[i] = xPosition;
            this.yValues[i] = values.get(index);
            xPosition -= this.xIncrement;
        }
    }

    /**
     * Display the plot on graphics panel
     */
    public void draw() {
        if (this.yValues.length < 2) {
            return;
        }
        this.maxValue = Math.max(1, this.getMax(this.yValues));
        this.engine.g2D.setStroke(new BasicStroke(1));
        for (int i=0; i<this.xValues.length-1; i++) {
            this.engine.g2D.setColor(this.color);
            this.yStart = this.maxY - (this.maxY - this.minY) * this.yValues[i] / this.maxValue;
            this.yEnd = this.maxY - (this.maxY - this.minY) * this.yValues[i+1] / this.maxValue;
            // note im using lines, not points, so the x increment can be large and still look OK
            this.engine.g2D.drawLine(this.xValues[i], this.yStart, this.xValues[i+1], this.yEnd);
        }
        this.postProcess();
    }

    /**
     * this adjusts values such as line thicknesses when the user selects smaller/larger window sizes
     */
    public int resize(int value) {
        return (int) (value * DashboardConstants.resizeRatio);
    }

    /**
     * Return maximum value of data for setting y-axis upper bound
     * @param values: values to plot
     * @return: the highest value
     */
    private int getMax(int[] values) {
        int maxValue = values[0];
        for (int value : values) {
            if (value > maxValue) {
                maxValue = value;
            }
        }
        return maxValue;
    }

    /**
     * Finish up the plot by adding final touches (lines and labels)
     */
    public void postProcess() {
        this.paintSeparationLines();
        this.paintLabels();
    }

    /**
     * Paint pretty lines on either side of the plot
     */
    private void paintSeparationLines() {
        // draw division lines
        this.engine.g2D.setColor(Color.black);
        this.paintVerticalLine(this.minX - resize(3), resize(13));
        this.paintVerticalLine(this.maxX, resize(3));
    }

    /**
     * Add axis and title labels to plot
     */
    private void paintLabels() {
        this.drawString(this.label, this.minX + resize(10), this.midPointY, this.font90);
        this.drawString("0", this.maxX + resize(8), this.maxY - resize(6), this.font);
        String upperValue;
        if (this.maxValue > 1000) {
            // some wacky math to turn the value into a pretty and concise decimal
            upperValue = this.maxValue / 1000 + "." + ((this.maxValue - 1000 * (this.maxValue / 1000)) / 100) + "K";
        }
        else {
            upperValue = this.asString(this.maxValue);
        }
        this.drawString(upperValue, this.maxX + this.getStringWidth(upperValue) / 2 + resize(4),
                this.minY + resize(7), this.font);
    }

    /**
     * Convert a integer to a string, but add ',' between evert 3 digits for cleaner look
     * @param value: value to convert
     * @return: string representing the number
     */
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

    /**
     * Draw a vertical line bound inside the dashboard
     * @param xPosition: horizontal position to add line to
     * @param width: width of line
     */
    public void paintVerticalLine(int xPosition, int width) {
        this.engine.g2D.setStroke(new BasicStroke(width));
        this.engine.g2D.drawLine(
                xPosition + width / 2,
                this.minY + width / 2,
                xPosition + width / 2,
                this.maxY);
    }

    /**
     * Draw and center a string onto the graphics panel. Features some extra math to center it correctly
     * @param text: string to paint
     * @param xPos: position of label
     * @param yPos: position of label
     * @param font: font to use - supports font and font90 (for vertical text)
     */
    public void drawString(String text, int xPos, int yPos, Font font) {
        int width = getStringWidth(text);
        this.engine.g2D.setColor(this.color);
        this.engine.g2D.setFont(font);
        // adjust positions to account for string length
        if (font == this.font) {
            xPos -= width / 2;
            yPos += resize(3);
        }
        if (font == this.font90) {
            yPos += width / 2;
            xPos -= resize(3);
        }
        this.engine.g2D.drawString(text, xPos, yPos);
    }

    /**
     * Get pixel width of a string as it will show on the plot. Used for centering the text position
     * @param text: string to plot
     * @return: pixel width
     */
    private int getStringWidth(String text) {
        this.engine.g2D.setFont(this.font);
        return this.engine.g2D.getFontMetrics().stringWidth(text);
    }

    /**
     * Initialize a line plot to add to the dashboard
     * @param engine: game engine
     * @param startX: left bound of this plot
     * @param endX: right bound of this plot
     * @param xIncrement: how many pixels to move over for every point (higher means plot moves faster)
     * @param label: text label to add as heading
     */
    public LinePlot(Engine engine, int startX, int endX, int xIncrement, String label) {
        this.engine = engine;
        this.label = label;
        this.minX = startX + resize(4);
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
        this.font = new Font(null, Font.PLAIN, DashboardConstants.fontSize);
        this.affineTransform.rotate(Math.toRadians(-90), 0, 0);
        this.font90 = this.font.deriveFont(this.affineTransform);
    }
}