package com.example.rksp_coursework.services;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.example.rksp_coursework.models.Securities;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
//import org.jfree.ui.RectangleInsets;
//import org.jfree.ui.ApplicationFrame;
//import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.imageio.ImageIO;
import java.util.Random;

public class GraphCreation {

    List<Securities> currSecList;
    String currStrId;
    String currName;
    int currSecCount;


    public GraphCreation(List<Securities> currSecList)
    {
        this.currSecList = currSecList;
        currStrId = currSecList.get(0).getStrId();
        currSecCount = currSecList.size();
        currName = currSecList.get(0).getName();
        XYDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);
        BufferedImage image = chart.createBufferedImage( 1920, 1080);
        try {
            saveToFile(image);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void saveToFile(BufferedImage img) throws FileNotFoundException, IOException
    {


        Random rnd = new Random(System.currentTimeMillis());
        int number = rnd.nextInt();
        File outputfile = new File("src\\main\\resources\\static\\imgs\\" + currStrId + ".png");
        ImageIO.write(img, "png", outputfile);
    }


    private XYDataset createDataset()
    {
        XYSeries series = new XYSeries("Series");
        for(int i = 0; i < currSecCount; i++){
            series.add( (-currSecCount + i + 1) / 4.0, currSecList.get(i).getPrice());
        }


        XYSeriesCollection dataset = new XYSeriesCollection();

        dataset.addSeries(series);

        return dataset;
    }

    private JFreeChart createChart(XYDataset dataset)
    {
        final JFreeChart chart = ChartFactory.createXYLineChart(
                currName, "Часы до текущего момента", "Цена", dataset,
                PlotOrientation.VERTICAL, false, false, false);

        chart.setBackgroundPaint(Color.white);

        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.white);

        plot.setDomainGridlinePaint(Color.gray);
        plot.setRangeGridlinePaint (Color.gray);

        ValueAxis domainAxis = plot.getDomainAxis();
        domainAxis.setLowerMargin(0);
        domainAxis.setUpperMargin(0);


        // Определение отступа меток делений
        //plot.setAxisOffset(new RectangleInsets (1.0, 1.0, 1.0, 1.0));

        // Скрытие осевых линий
        ValueAxis axis = plot.getDomainAxis();
        axis.setAxisLineVisible (false);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        // Удаление меток Series
        renderer.setSeriesShapesVisible(0, false);

        // Настройка графика (цвет, ширина линии) Series
        renderer.setSeriesStroke (0, new BasicStroke(5f));
        plot.setRenderer(renderer);

        // Настройка NumberAxis
        ValueAxis rangeAxis = plot.getRangeAxis();
        Securities rootEl = currSecList.get(currSecList.size() - 1);
        double currPrice = rootEl.getPrice();
        if(currPrice > 0)
            rangeAxis.setRange(currPrice - currPrice / 10,currPrice + currPrice / 10);

        rangeAxis.setAxisLineVisible (false);
        //rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        return chart;
    }



}
