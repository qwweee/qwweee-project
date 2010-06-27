package Project.test;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.*;
import org.jfree.ui.*;

import Project.config.Config;
import Project.struct.DataStruct;
import Project.utils.FFT.Complex;

// Referenced classes of package demo:
//            DemoPanel

public class XYSpinLine extends ApplicationFrame {
    public class MyDemoPanel extends DemoPanel {

        private XYDataset createSampleData(String lineName, DataStruct[] data) {
            XYSeriesCollection xyseriescollection = new XYSeriesCollection();
            XYSeries xyseries = new XYSeries(lineName);
            for (int i = 0 ; i < data.length ; i ++) {
                xyseries.add(i, data[i].dataSize);
            }
            xyseriescollection.addSeries(xyseries);
            return xyseriescollection;
        }
        private XYDataset createSampleData(String lineName, Complex[] fft) {
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

        private JTabbedPane createContent(String xTitle) {
            JTabbedPane jtabbedpane = new JTabbedPane();
            chartPanel1 = createChartPanel1("Source Data",xTitle ,"Octets");
            jtabbedpane.add("Source Data", chartPanel1);
            chartPanel2 = createChartPanel2("FFT Spectrum",xTitle,"HZ");
            jtabbedpane.add("FFT Spectrum", chartPanel2);
            return jtabbedpane;
        }

        private ChartPanel createChartPanel1(String title, String xTitle, String yTitle) {
            NumberAxis numberaxis = new NumberAxis(xTitle);
            numberaxis.setAutoRangeIncludesZero(false);
            NumberAxis numberaxis1 = new NumberAxis(yTitle);
            numberaxis1.setAutoRangeIncludesZero(false);
            XYLineAndShapeRenderer xylineandshaperenderer = new XYLineAndShapeRenderer();
            XYPlot xyplot = new XYPlot(data1, numberaxis, numberaxis1,
                    xylineandshaperenderer);
            xyplot.setBackgroundPaint(Color.lightGray);
            xyplot.setDomainGridlinePaint(Color.white);
            xyplot.setRangeGridlinePaint(Color.white);
            xyplot.setAxisOffset(new RectangleInsets(4D, 4D, 4D, 4D));
            chart1 = new JFreeChart(title,
                    JFreeChart.DEFAULT_TITLE_FONT, xyplot, true);
            addChart(chart1);
            ChartUtilities.applyCurrentTheme(chart1);
            ChartPanel chartpanel = new ChartPanel(chart1);
            return chartpanel;
        }

        private ChartPanel createChartPanel2(String title, String xTitle, String yTitle) {
            NumberAxis numberaxis = new NumberAxis(xTitle);
            numberaxis.setAutoRangeIncludesZero(true);
            NumberAxis numberaxis1 = new NumberAxis(yTitle);
            numberaxis1.setAutoRangeIncludesZero(true);
            XYLineAndShapeRenderer xylineandshaperenderer = new XYLineAndShapeRenderer();
            XYPlot xyplot = new XYPlot(data2, numberaxis, numberaxis1,
                    xylineandshaperenderer);
            xyplot.setBackgroundPaint(Color.lightGray);
            xyplot.setDomainGridlinePaint(Color.white);
            xyplot.setRangeGridlinePaint(Color.white);
            xyplot.setAxisOffset(new RectangleInsets(4D, 4D, 4D, 4D));
            chart2 = new JFreeChart(title,
                    JFreeChart.DEFAULT_TITLE_FONT, xyplot, true);
            addChart(chart2);
            ChartUtilities.applyCurrentTheme(chart2);
            ChartPanel chartpanel = new ChartPanel(chart2);
            return chartpanel;
        }
        
        public void savePNG(String filepath) {
            try {
                ChartUtilities.saveChartAsPNG(new File(filepath+"_S.png"), chart1 ,Config.IMAGEWIDTH, Config.IMAGEHEIGHT);
                ChartUtilities.saveChartAsPNG(new File(filepath+"_F.png"), chart2 ,Config.IMAGEWIDTH, Config.IMAGEHEIGHT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        public ChartPanel chartPanel1;
        public ChartPanel chartPanel2;
        public JFreeChart chart1;
        public JFreeChart chart2;

        private XYDataset data1;
        private XYDataset data2;

        public MyDemoPanel() {
            super(new BorderLayout());
        }
        public void initPanel(String filepath, DataStruct[] data, Complex[] fft) {
            data1 = createSampleData("Source Data", data);
            data2 = createSampleData("FFT Spectrum", fft);
            add(createContent(String.format("Time(%ds)", data[0].gcd)));
        }
    }

    public XYSpinLine(String s) {
        super(s);
        JPanel jpanel = createDemoPanel();
        getContentPane().add(jpanel);
    }
    
    public MyDemoPanel demo;

    public JPanel createDemoPanel() {
        return (demo = new MyDemoPanel());
    }
}