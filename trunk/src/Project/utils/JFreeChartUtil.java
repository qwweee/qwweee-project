package Project.utils;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

import Project.config.Config;
import Project.struct.DataStruct;
import Project.utils.FFT.Complex;

public class JFreeChartUtil {
    public static void createFFTImage(String ip, String dstip, int port, boolean isScan, String filepath, DataStruct[] data, Complex[] fft) {
        XYDataset data1 = createSampleData("Source Data", data);
        XYDataset data2 = createSampleData("FFT Spectrum", fft);
        String xTitle = String.format("Time(%ds)", data[0].gcd);
        JFreeChart chart1 = createChartSource("Source Data",xTitle ,"Octets", data1);
        JFreeChart chart2 = createChartFFT("FFT Spectrum",xTitle,"HZ", data2);
        savePNG(filepath, chart1, chart2);
    }
    private static XYDataset createSampleData(String lineName, DataStruct[] data) {
        XYSeriesCollection xyseriescollection = new XYSeriesCollection();
        XYSeries xyseries = new XYSeries(lineName);
        for (int i = 0 ; i < data.length ; i ++) {
            xyseries.add(i, data[i].dataSize);
        }
        xyseriescollection.addSeries(xyseries);
        return xyseriescollection;
    }
    private static XYDataset createSampleData(String lineName, Complex[] fft) {
        XYSeriesCollection xyseriescollection = new XYSeriesCollection();
        XYSeries xyseries = new XYSeries(lineName);
        if (fft != null) {
            for (int i = 0 ; i < fft.length ; i ++) {
                xyseries.add(i, Complex.abs(fft[i]));
            }
        }
        xyseriescollection.addSeries(xyseries);
        return xyseriescollection;
    }
    private static JFreeChart createChartSource(String title, String xTitle, String yTitle, XYDataset data) {
        NumberAxis numberaxis = new NumberAxis(xTitle);
        numberaxis.setAutoRangeIncludesZero(false);
        NumberAxis numberaxis1 = new NumberAxis(yTitle);
        numberaxis1.setAutoRangeIncludesZero(false);
        XYLineAndShapeRenderer xylineandshaperenderer = new XYLineAndShapeRenderer();
        XYPlot xyplot = new XYPlot(data, numberaxis, numberaxis1,
                xylineandshaperenderer);
        xyplot.setBackgroundPaint(Color.lightGray);
        xyplot.setDomainGridlinePaint(Color.white);
        xyplot.setRangeGridlinePaint(Color.white);
        xyplot.setAxisOffset(new RectangleInsets(4D, 4D, 4D, 4D));
        JFreeChart chart = new JFreeChart(title,
                JFreeChart.DEFAULT_TITLE_FONT, xyplot, true);
        return chart;
    }

    private static JFreeChart createChartFFT(String title, String xTitle, String yTitle, XYDataset data) {
        NumberAxis numberaxis = new NumberAxis(xTitle);
        numberaxis.setAutoRangeIncludesZero(true);
        NumberAxis numberaxis1 = new NumberAxis(yTitle);
        numberaxis1.setAutoRangeIncludesZero(true);
        XYLineAndShapeRenderer xylineandshaperenderer = new XYLineAndShapeRenderer();
        XYPlot xyplot = new XYPlot(data, numberaxis, numberaxis1,
                xylineandshaperenderer);
        xyplot.setBackgroundPaint(Color.lightGray);
        xyplot.setDomainGridlinePaint(Color.white);
        xyplot.setRangeGridlinePaint(Color.white);
        xyplot.setAxisOffset(new RectangleInsets(4D, 4D, 4D, 4D));
        JFreeChart chart = new JFreeChart(title,
                JFreeChart.DEFAULT_TITLE_FONT, xyplot, true);
        return chart;
    }
    private static void savePNG(String filepath, JFreeChart chart1, JFreeChart chart2) {
        try {
            ChartUtilities.saveChartAsPNG(new File(filepath+"_S.png"), chart1 ,Config.IMAGEWIDTH, Config.IMAGEHEIGHT);
            ChartUtilities.saveChartAsPNG(new File(filepath+"_F.png"), chart2 ,Config.IMAGEWIDTH, Config.IMAGEHEIGHT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
