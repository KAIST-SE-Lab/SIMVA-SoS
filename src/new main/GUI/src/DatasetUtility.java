
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class DatasetUtility{
    public DefaultCategoryDataset dataset;
    public boolean hasUnprocessedData;

    DatasetUtility (DefaultCategoryDataset newDataset) {
        this.dataset = newDataset;
        this.hasUnprocessedData = true;
    }

    public synchronized void addValueIntoDataset (int num, String shape, String length) {
        System.out.println("Adding ");

        dataset.addValue(num, shape, length);
        //hasUnprocessedData = true;
        notifyAll();
        //System.out.println("Adding Done");
    }

    public synchronized DefaultCategoryDataset getDataset() {
        //  System.out.println("get dataset");
        while (!hasUnprocessedData) {
            // If there is no new data, wait for new data
            try {
            System.out.println("get dataset Wait");
            wait();
            }
            catch (InterruptedException e) { }
            }
        hasUnprocessedData = false;
        notifyAll();
        System.out.println("aaa");
        return dataset;
    }
}