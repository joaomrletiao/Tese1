package dados;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class GraphPanel extends JPanel {

    private int width = 800;
    private int heigth = 400;
    private int padding = 25;
    private int labelPadding = 25;
    private Color lineColor = new Color(44, 102, 230, 180);
    private Color pointColor = new Color(100, 100, 100, 180);
    private Color gridColor = new Color(200, 200, 200, 200);
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    private int pointWidth = 4;
    private int numberYDivisions = 10;
    private List<Point2D.Double> scores;

    public GraphPanel(List<Point2D.Double> scores) {
        this.scores = scores;
    }
    public GraphPanel(){	
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double xScale = ((double) getWidth() - (2 * padding) - labelPadding) / ((scores.get(scores.size()-1).getX()));
        double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (getMaxScoreY() - getMinScoreY());

        List<Point> graphPoints = new ArrayList<Point>();
        for (int i = 0; i < scores.size(); i++) {
            int x1 = (int) (scores.get(i).getX() * xScale + padding + labelPadding);
            int y1 = (int) ((getMaxScoreY() - scores.get(i).getY()) * yScale + padding); 
            graphPoints.add(new Point(x1, y1));
          
        }

        // draw white background
        g2.setColor(Color.WHITE);
        g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding, getHeight() - 2 * padding - labelPadding);
        g2.setColor(Color.BLACK);

        // create hatch marks and grid lines for y axis.
        for (int i = 0; i < numberYDivisions + 1; i++) {
            int x0 = padding + labelPadding;
            int x1 = pointWidth + padding + labelPadding;
            int y0 = getHeight() - ((i * (getHeight() - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding);
            int y1 = y0;
            if (scores.size() > 0) {
                g2.setColor(gridColor);
                g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);
                g2.setColor(Color.BLACK);
                String yLabel = ((int) ((getMinScoreY() + (getMaxScoreY() - getMinScoreY()) * ((i * 1.0) / numberYDivisions)) * 100)) / 100.0 + "";
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
            }
            g2.drawLine(x0, y0, x1, y1);
        }

        // and for x axis
        //alterar para representar PPIS ou series temporais
        for (int i = 0; i <= /* numberYDivisions + 1*/scores.get(scores.size()-1).getX(); i++) {
            if (scores.size() > 1) {
            	/*int x0 = i * (getWidth() - padding * 2 - labelPadding) /  numberYDivisions + padding + labelPadding;
                int x1 = x0;
                int y0 = getHeight() - padding - labelPadding;
                int y1 = y0 -pointWidth;*/
                int x0 = i * (getWidth() - padding * 2 - labelPadding) / ((int)scores.get(scores.size()-1).getX()) + padding + labelPadding;
                int x1 = x0;
                int y0 = getHeight() - padding - labelPadding;
                int y1 = y0 - pointWidth;
                if ((i % ((int) ((scores.size() / 20.0)) + 1)) == 0) {
                    g2.setColor(gridColor);
                    g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x1, padding);
                    g2.setColor(Color.BLACK);
                    //String xLabel = ((int) ((getMinScoreX() + (getMaxScoreX()+1 - getMinScoreX()) * ((i * 1.0) / numberYDivisions)) * 100)) / 100.0 + "";
                    String xLabel = i + "";
                    FontMetrics metrics = g2.getFontMetrics();
                    int labelWidth = metrics.stringWidth(xLabel);
                    g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
                }
                g2.drawLine(x0, y0, x1, y1);
            }
        }

        // create x and y axes 
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);

        Stroke oldStroke = g2.getStroke();
        g2.setColor(lineColor);
        g2.setStroke(GRAPH_STROKE);
        for (int i = 0; i < graphPoints.size()-1; i++) {
            int x1 = graphPoints.get(i).x;
            int y1 = graphPoints.get(i).y;
            int x2 = graphPoints.get(i + 1).x;
            int y2 = graphPoints.get(i + 1).y;
            g2.drawLine(x1, y1, x2, y2);
        }

        g2.setStroke(oldStroke);
        g2.setColor(pointColor);
        for (int i = 0; i < graphPoints.size(); i++) {
            int x = graphPoints.get(i).x - pointWidth / 2;
            int y = graphPoints.get(i).y - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            g2.fillOval(x, y, ovalW, ovalH);
        }
    }

//    @Override
//    public Dimension getPreferredSize() {
//        return new Dimension(width, heigth);
//    }
    private double getMinScoreY() {
        double minScore = Double.MAX_VALUE;
        for (Point2D.Double score : scores) {
            minScore = Math.min(minScore, score.getY());
        }
        return minScore;
    }

    private double getMaxScoreY() {
        double maxScore = Double.MIN_VALUE;
        for (Point2D.Double score : scores) {
            maxScore = Math.max(maxScore, score.getY());
        }
        return maxScore;
    }
    
    private double getMinScoreX() {
        double minScore = Double.MAX_VALUE;
        for (Point2D.Double score : scores) {
            minScore = Math.min(minScore, score.getX());
        }
        return minScore;
    }

    private double getMaxScoreX() {
        double maxScore = Double.MIN_VALUE;
        for (Point2D.Double score : scores) {
            maxScore = Math.max(maxScore, score.getX());
        }
        return maxScore;
    }

    public void setScores(List<Point2D.Double> scores) {
        this.scores = scores;
        invalidate();
        this.repaint();
    }

    public List<Point2D.Double> getScores() {
        return scores;
    }

    static void createAndShowGui(ArrayList<Point2D.Double> ppis) {
        List<Point2D.Double> scores = new ArrayList<Point2D.Double>();
        //Random random = new Random();
        int maxDataPoints = ppis.size();
       // int maxScore = (int) ppis.get(ppis.size()-1).getX();
        for (int i = 0; i < maxDataPoints; i++) {
           // scores.add((double) random.nextDouble() * maxScore);
        	scores.add(ppis.get(i));
        }
        GraphPanel mainPanel = new GraphPanel(scores);
        mainPanel.setPreferredSize(new Dimension(800, 600));
        JFrame frame = new JFrame("DrawGraph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    static GraphPanel createGui(ArrayList<Point2D.Double> ppis) {
        List<Point2D.Double> scores = new ArrayList<Point2D.Double>();
        //Random random = new Random();
        int maxDataPoints = ppis.size();
       // int maxScore = (int) ppis.get(ppis.size()-1).getX();
        for (int i = 0; i < maxDataPoints; i++) {
           // scores.add((double) random.nextDouble() * maxScore);
        	scores.add(ppis.get(i));
        }
        GraphPanel mainPanel = new GraphPanel(scores);
        mainPanel.setPreferredSize(new Dimension(800, 600));
        JFrame frame = new JFrame("DrawGraph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(false);
        return mainPanel;
    }
    
	public static BufferedImage createImage(JPanel panel) {
	    int w = 800;
	    int h = 600;
	    BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = bi.createGraphics();
	    panel.paint(g);
	    return bi;
	}
 }
