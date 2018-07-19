/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;

import new_simvasos.not_decided.CS;
import new_simvasos.not_decided.FireFighter;
import new_simvasos.not_decided.SoS;
import new_simvasos.property.MCIProperty;
import new_simvasos.property.MCIPropertyChecker;
import new_simvasos.scenario.Event;
import new_simvasos.scenario.PatientOccurrence;
import new_simvasos.scenario.Scenario;
import new_simvasos.simulator.Simulator;
import new_simvasos.timebound.ConstantTimeBound;
import new_simvasos.verifier.SPRT;




/**
 *
 * @author TaeGeon
 */

public class NewJFrame extends javax.swing.JFrame {

    static AtomicInteger foo = new AtomicInteger(1);
    private JTextArea textArea;
    private PrintStream standardOut;

    /**
     * Creates new form NewJFrame
     */
    public NewJFrame() {
        initComponents();
        this.setLocationRelativeTo(null);
        graph_panel.setLayout(new java.awt.BorderLayout());
        scheduler = Executors.newScheduledThreadPool(2);
        dataReadingScheduler = Executors.newScheduledThreadPool(1);
        dataAddingScheduler = Executors.newScheduledThreadPool(1);
        dataTool = new DatasetUtility(new DefaultCategoryDataset());
        
        
        jTextArea1.setEditable(false);
        PrintStream printStream = new PrintStream(new CustomOutputStream(jTextArea1));
        
        //keep reference of standard output stream
        standardOut = System.out;
        
        //re-assign standard output stream and error output stream
        System.setOut(printStream);
        System.setErr(printStream);
        

    }

    public class CustomOutputStream extends OutputStream {
    private JTextArea textArea;
     
    public CustomOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }
     
    @Override
    public void write(int b) throws IOException {
        // redirects data to the text area
        textArea.append(String.valueOf((char)b));
        // scrolls the text area to the end of data
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
    
}

    public void drawGraph(DefaultCategoryDataset dataset) {
        JFreeChart lineChart = ChartFactory.createLineChart("Title", "x-axis", "y-axis", dataset, PlotOrientation.VERTICAL, true, true, false);
        //set color
        CategoryPlot plot = (CategoryPlot) lineChart.getPlot();
        plot.getRenderer().setSeriesPaint(0, Color.BLUE);
        //create chart panel the add it to swing panel in jframe
        ChartPanel chartpanel = new ChartPanel(lineChart);
        graph_panel.removeAll();
        graph_panel.add(chartpanel, BorderLayout.CENTER);
        graph_panel.revalidate();
    }
    
    public void drawBarGraph(DefaultCategoryDataset dataset){
        JFreeChart barChart = ChartFactory.createBarChart("Title", "x-axis", "y-axis", dataset, PlotOrientation.VERTICAL, true, true, false); // horizontal
        CategoryPlot plot = (CategoryPlot) barChart.getPlot();
        plot.getRenderer().setSeriesPaint(0, Color.BLUE);
        ChartPanel chartpanel = new ChartPanel(barChart);
        graph_panel.removeAll();
        graph_panel.add(chartpanel, BorderLayout.CENTER);
        graph_panel.revalidate();
    }

    private final ScheduledExecutorService scheduler;
    private final ScheduledExecutorService dataReadingScheduler;
    private ScheduledExecutorService dataAddingScheduler;
    private ScheduledFuture<?> dataAddingThread;
    private boolean isGraphInitialized = false;
    private DatasetUtility dataTool;
    private DefaultCategoryDataset createDataset() throws InterruptedException {
        //if (!isGraphInitialized) {
            Runnable timerTask = new Runnable() {
                @Override
                public void run() {
                    System.out.println("running");
                    DefaultCategoryDataset data = dataTool.getDataset();
                    drawGraph (data);
                    try {
                        Thread.sleep(500);
                    }
                    catch (InterruptedException ex) {}
                }
            };
            ScheduledFuture<?> timerHandle =
                    dataReadingScheduler.scheduleAtFixedRate(timerTask, 0, 1, TimeUnit.SECONDS);

            dataAddingThread = dataAddingScheduler.schedule(new Runnable() {
                @Override
                public void run() {
//                    dataTool.addValueIntoDataset(15, "line", "23");
//                    dataTool.addValueIntoDataset(24, "line", "234");
//                    dataTool.addValueIntoDataset(26, "line", "23456");
//                    dataTool.addValueIntoDataset(30, "line", "234567");
//                    dataTool.addValueIntoDataset(35, "line", "2345678");
//                    dataTool.addValueIntoDataset(40, "line", "23456789");
                    dataTool.reset();
                    ConstantTimeBound constantTimeBound;
                    PatientOccurrence patientOccurrence;
                    Event event;
                    Scenario MCIScenario;
                    Simulator MCISim;
                    MCIProperty rescuedProperty;
                    MCIPropertyChecker rescuedChecker;
                    SPRT verifier;
                    double fireFighterPr = 0.8;
                    int numFireFighter = 3;
                    ArrayList<CS> CSs = new ArrayList();

                    for (int i = 0; i < numFireFighter; i++) {      // start from zero or one?
                        FireFighter fireFighter = new FireFighter(Integer.toString(i), fireFighterPr);
                        CSs.add(fireFighter);
                    }

                    int mapSize = 20;
                    ArrayList<Integer> MCIMap = new ArrayList<>();

                    for (int i = 0; i < mapSize; i++) {
                        MCIMap.add(0);
                    }

                    SoS MCISoS = new SoS(CSs, MCIMap);
                    ArrayList MCIEvents = new ArrayList();
                    int numPatients = 20;

                    for(int j = 0; j < numPatients; j++) {
                        constantTimeBound = new ConstantTimeBound(0);
                        patientOccurrence = new PatientOccurrence("patient + 1", MCIMap);
                        event = new Event(patientOccurrence, constantTimeBound);
                        MCIEvents.add(event);
                    }
                    MCIScenario = new Scenario(MCIEvents);

                    // Simulation
                    int repeatSim = 2000;
                    int simulationTime = 15;
                    boolean ret = true;
                    int numSamples = 0;
                    double theta;
                    int flag=0;
                    MCISim = new Simulator(simulationTime, MCISoS, MCIScenario);
                    long start = System.currentTimeMillis();
                    rescuedProperty = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIPropertyType", 0.8);
                    rescuedChecker = new MCIPropertyChecker();
                    verifier = new SPRT(rescuedChecker);
                    System.out.println("Verify with simulator");
                    for (int i =1; i<= 100; i++) {
                        theta = i * 0.01;
                        numSamples = verifier.verifyWithSimulator(MCISim, rescuedProperty, repeatSim, theta);

                    /*
                    if(ret == true)
                        flag = 1;
                    if(ret == false)
                        flag = 0;
                    */

                        //dataTool.addValueIntoDataset(ret, "line", String.valueOf(theta));
                        dataTool.addValueIntoDataset(numSamples, "line", String.valueOf(theta));
                        try {
                            Thread.sleep(40);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }, 1, TimeUnit.SECONDS);
            //isGraphInitialized = true;
        //}
//        else {
//            try {
//                dataAddingThread.get();
//            }
//            catch (Exception ex) { }
//            dataAddingThread = dataAddingScheduler.schedule(new Runnable() {
//                @Override
//                public void run() {
//                    dataTool.reset();
//                    System.out.println("-------Readding data");
//                    ConstantTimeBound constantTimeBound;
//                    PatientOccurrence patientOccurrence;
//                    Event event;
//                    Scenario MCIScenario;
//                    Simulator MCISim;
//                    MCIProperty rescuedProperty;
//                    MCIPropertyChecker rescuedChecker;
//                    SPRT verifier;
//                    double fireFighterPr = 0.8;
//                    int numFireFighter = 3;
//                    ArrayList<CS> CSs = new ArrayList();
//
//                    for (int i = 0; i < numFireFighter; i++) {      // start from zero or one?
//                        FireFighter fireFighter = new FireFighter(Integer.toString(i), fireFighterPr);
//                        CSs.add(fireFighter);
//                    }
//
//                    int mapSize = 20;
//                    ArrayList<Integer> MCIMap = new ArrayList<>();
//
//                    for (int i = 0; i < mapSize; i++) {
//                        MCIMap.add(0);
//                    }
//
//                    SoS MCISoS = new SoS(CSs, MCIMap);
//                    ArrayList MCIEvents = new ArrayList();
//                    int numPatients = 20;
//
//                    for(int j = 0; j < numPatients; j++) {
//                        constantTimeBound = new ConstantTimeBound(0);
//                        patientOccurrence = new PatientOccurrence("patient + 1", MCIMap);
//                        event = new Event(patientOccurrence, constantTimeBound);
//                        MCIEvents.add(event);
//                    }
//                    MCIScenario = new Scenario(MCIEvents);
//
//                    // Simulation
//                    int repeatSim = 2000;
//                    int simulationTime = 15;
//                    boolean ret = true;
//                    int numSamples = 0;
//                    double theta;
//                    int flag=0;
//                    MCISim = new Simulator(simulationTime, MCISoS, MCIScenario);
//                    long start = System.currentTimeMillis();
//                    rescuedProperty = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIPropertyType", 0.8);
//                    rescuedChecker = new MCIPropertyChecker();
//                    verifier = new SPRT(rescuedChecker);
//                    System.out.println("Verify with simulator");
//                    for (int i =1; i<= 100; i++) {
//                        theta = i * 0.01;
//                        numSamples = verifier.verifyWithSimulator(MCISim, rescuedProperty, repeatSim, theta);
//
//                    /*
//                    if(ret == true)
//                        flag = 1;
//                    if(ret == false)
//                        flag = 0;
//                    */
//
//                        //dataTool.addValueIntoDataset(ret, "line", String.valueOf(theta));
//                        dataTool.addValueIntoDataset(numSamples, "line", String.valueOf(theta));
//                        try {
//                            Thread.sleep(40);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                }
//            }, 0, TimeUnit.SECONDS);
//        }

        return dataTool.getDataset();
    }
    

  //This is for bar graph
private  DefaultCategoryDataset createDataset2() throws InterruptedException {
        DatasetUtility dataTool = new DatasetUtility(new DefaultCategoryDataset());
        Runnable timerTask = new Runnable() {
            @Override
            public void run() {
                DefaultCategoryDataset data = dataTool.getDataset();
                drawBarGraph (data);
            }
        };
        ScheduledFuture<?> timerHandle =
        scheduler.scheduleAtFixedRate(timerTask, 0, 1, TimeUnit.SECONDS);
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                try{

                    for(int i = 0; i<10; i++){
                        dataTool.addValueIntoDataset(new Random().nextInt(50), "line", "0.02" + String.valueOf(i));
                        Thread.sleep(50);

                    }


                    // something
                }
                catch (InterruptedException ex) {}

            }
        }, 1, TimeUnit.SECONDS);

        return dataTool.getDataset();
    }



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem12 = new javax.swing.JMenuItem();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        Simulation_based_Analysis = new javax.swing.JPanel();
        VP_Panel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        Start_Button = new javax.swing.JButton();
        Stop_Button = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();
        VPR_Grey = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        VPR_White = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTextPane12 = new javax.swing.JTextPane();
        jScrollPane18 = new javax.swing.JScrollPane();
        jTextPane16 = new javax.swing.JTextPane();
        jScrollPane19 = new javax.swing.JScrollPane();
        VP_TextPanel = new javax.swing.JTextPane();
        jScrollPane20 = new javax.swing.JScrollPane();
        jTextPane18 = new javax.swing.JTextPane();
        SVR_Button = new javax.swing.JButton();
        VI_Grey = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        VI_White = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jScrollPane22 = new javax.swing.JScrollPane();
        jTextPane20 = new javax.swing.JTextPane();
        jScrollPane23 = new javax.swing.JScrollPane();
        jTextPane21 = new javax.swing.JTextPane();
        jScrollPane24 = new javax.swing.JScrollPane();
        jTextPane22 = new javax.swing.JTextPane();
        jScrollPane25 = new javax.swing.JScrollPane();
        jTextPane23 = new javax.swing.JTextPane();
        jScrollPane26 = new javax.swing.JScrollPane();
        jTextPane24 = new javax.swing.JTextPane();
        jScrollPane27 = new javax.swing.JScrollPane();
        jTextPane25 = new javax.swing.JTextPane();
        jLabel24 = new javax.swing.JLabel();
        enable_button1 = new javax.swing.JRadioButton();
        disable_button1 = new javax.swing.JRadioButton();
        jComboBox2 = new javax.swing.JComboBox<>();
        jTextField_SS = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jTextField_VC = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        SAR_Button = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        graph_panel = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        VI_White1 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jScrollPane28 = new javax.swing.JScrollPane();
        jTextPane26 = new javax.swing.JTextPane();
        jScrollPane29 = new javax.swing.JScrollPane();
        jTextPane27 = new javax.swing.JTextPane();
        jScrollPane30 = new javax.swing.JScrollPane();
        jTextPane28 = new javax.swing.JTextPane();
        jScrollPane31 = new javax.swing.JScrollPane();
        jTextPane29 = new javax.swing.JTextPane();
        jScrollPane32 = new javax.swing.JScrollPane();
        jTextPane30 = new javax.swing.JTextPane();
        jScrollPane33 = new javax.swing.JScrollPane();
        jTextPane31 = new javax.swing.JTextPane();
        jLabel33 = new javax.swing.JLabel();
        enable_button2 = new javax.swing.JRadioButton();
        disable_button2 = new javax.swing.JRadioButton();
        jComboBox3 = new javax.swing.JComboBox<>();
        jTextField_SS1 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jTextField_VC1 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        VPR_White1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane2 = new javax.swing.JTextPane();
        jScrollPane15 = new javax.swing.JScrollPane();
        jTextPane13 = new javax.swing.JTextPane();
        jScrollPane21 = new javax.swing.JScrollPane();
        jTextPane17 = new javax.swing.JTextPane();
        jScrollPane34 = new javax.swing.JScrollPane();
        VP_TextPanel1 = new javax.swing.JTextPane();
        jScrollPane35 = new javax.swing.JScrollPane();
        jTextPane19 = new javax.swing.JTextPane();
        jTextArea2 = new javax.swing.JTextArea();
        graph_panel1 = new javax.swing.JPanel();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();

        jMenuItem12.setText("Policy Details");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(244, 244, 244));

        jTabbedPane2.setBackground(new java.awt.Color(244, 244, 244));

        Simulation_based_Analysis.setBackground(new java.awt.Color(244, 244, 244));

        VP_Panel.setBackground(new java.awt.Color(244, 244, 244));

        jLabel1.setText("Verification Progress");

        jPanel2.setBackground(new java.awt.Color(244, 244, 244));

        Start_Button.setText("▶");
        Start_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Start_ButtonActionPerformed(evt);
            }
        });

        Stop_Button.setFont(new java.awt.Font("Lucida Grande", 0, 25)); // NOI18N
        Stop_Button.setText("■");
        Stop_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Stop_ButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Start_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Stop_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(8, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Stop_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Start_Button)))
        );

        jProgressBar1.setString("0% Done");
        jProgressBar1.setStringPainted(true);

        javax.swing.GroupLayout VP_PanelLayout = new javax.swing.GroupLayout(VP_Panel);
        VP_Panel.setLayout(VP_PanelLayout);
        VP_PanelLayout.setHorizontalGroup(
            VP_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(VP_PanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(VP_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(VP_PanelLayout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(29, 29, 29))
        );
        VP_PanelLayout.setVerticalGroup(
            VP_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(VP_PanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(VP_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        VPR_Grey.setBackground(new java.awt.Color(244, 244, 244));

        jLabel2.setText("Verification Progress & Results ");

        VPR_White.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setText("Total # of Simulation");

        jLabel11.setText("Time Consumed");

        jLabel12.setText("Verification Progress");

        jLabel13.setText("Property Checking");

        jLabel14.setText("Target Model");

        jScrollPane2.setViewportView(jTextPane1);

        jScrollPane14.setViewportView(jTextPane12);

        jScrollPane18.setViewportView(jTextPane16);

        jScrollPane19.setViewportView(VP_TextPanel);

        jScrollPane20.setViewportView(jTextPane18);

        javax.swing.GroupLayout VPR_WhiteLayout = new javax.swing.GroupLayout(VPR_White);
        VPR_White.setLayout(VPR_WhiteLayout);
        VPR_WhiteLayout.setHorizontalGroup(
            VPR_WhiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(VPR_WhiteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(VPR_WhiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(VPR_WhiteLayout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(93, 93, 93)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(VPR_WhiteLayout.createSequentialGroup()
                        .addGroup(VPR_WhiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(VPR_WhiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                            .addComponent(jScrollPane18)
                            .addComponent(jScrollPane19)
                            .addComponent(jScrollPane20))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        VPR_WhiteLayout.setVerticalGroup(
            VPR_WhiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(VPR_WhiteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(VPR_WhiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(VPR_WhiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(VPR_WhiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(VPR_WhiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(VPR_WhiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jScrollPane20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        SVR_Button.setText("Save verification results");
        SVR_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SVR_ButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout VPR_GreyLayout = new javax.swing.GroupLayout(VPR_Grey);
        VPR_Grey.setLayout(VPR_GreyLayout);
        VPR_GreyLayout.setHorizontalGroup(
            VPR_GreyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(VPR_GreyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(VPR_GreyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(VPR_GreyLayout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(SVR_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(VPR_White, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(9, Short.MAX_VALUE))
        );
        VPR_GreyLayout.setVerticalGroup(
            VPR_GreyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(VPR_GreyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(VPR_GreyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(SVR_Button))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(VPR_White, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        VI_Grey.setBackground(new java.awt.Color(244, 244, 244));

        jLabel5.setText("Verification Information ");

        VI_White.setBackground(new java.awt.Color(255, 255, 255));

        jLabel16.setText("Verification Configuration");

        jLabel17.setText("alpha (α)");

        jLabel18.setText("beta (β)");

        jLabel19.setText("delta (δ)");

        jLabel20.setText("Number of minimum samples");

        jLabel21.setText("Simulation time");

        jLabel22.setText("Number of simulations");

        jLabel23.setText("Simulation Scenario");

        jScrollPane22.setViewportView(jTextPane20);

        jScrollPane23.setViewportView(jTextPane21);

        jScrollPane24.setViewportView(jTextPane22);

        jScrollPane25.setViewportView(jTextPane23);

        jScrollPane26.setViewportView(jTextPane24);

        jScrollPane27.setViewportView(jTextPane25);

        jLabel24.setText("Sliced");

        enable_button1.setText("Yes");
        enable_button1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                enable_button1MouseClicked(evt);
            }
        });
        enable_button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enable_button1ActionPerformed(evt);
            }
        });

        disable_button1.setText("No");
        disable_button1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                disable_button1MouseClicked(evt);
            }
        });
        disable_button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disable_button1ActionPerformed(evt);
            }
        });

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jTextField_SS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_SSActionPerformed(evt);
            }
        });

        jButton2.setText("File..");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("File..");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout VI_WhiteLayout = new javax.swing.GroupLayout(VI_White);
        VI_White.setLayout(VI_WhiteLayout);
        VI_WhiteLayout.setHorizontalGroup(
            VI_WhiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(VI_WhiteLayout.createSequentialGroup()
                .addGroup(VI_WhiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, VI_WhiteLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(VI_WhiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel24)
                            .addComponent(jLabel23))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(VI_WhiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(VI_WhiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, VI_WhiteLayout.createSequentialGroup()
                                    .addComponent(jTextField_SS, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(VI_WhiteLayout.createSequentialGroup()
                                    .addComponent(enable_button1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(disable_button1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(VI_WhiteLayout.createSequentialGroup()
                                .addComponent(jTextField_VC, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 8, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, VI_WhiteLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(VI_WhiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(VI_WhiteLayout.createSequentialGroup()
                                .addComponent(jLabel22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane27, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(VI_WhiteLayout.createSequentialGroup()
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane25, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(VI_WhiteLayout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane22, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel18)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane24, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel19)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane23, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(VI_WhiteLayout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane26, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        VI_WhiteLayout.setVerticalGroup(
            VI_WhiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(VI_WhiteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(VI_WhiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jTextField_SS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addGap(2, 2, 2)
                .addGroup(VI_WhiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(enable_button1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(disable_button1)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24))
                .addGap(2, 2, 2)
                .addGroup(VI_WhiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jTextField_VC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addGap(8, 8, 8)
                .addGroup(VI_WhiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(jScrollPane22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(jScrollPane24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(jScrollPane23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(VI_WhiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20)
                    .addComponent(jScrollPane25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(VI_WhiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21)
                    .addComponent(jScrollPane26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(VI_WhiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22)
                    .addComponent(jScrollPane27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout VI_GreyLayout = new javax.swing.GroupLayout(VI_Grey);
        VI_Grey.setLayout(VI_GreyLayout);
        VI_GreyLayout.setHorizontalGroup(
            VI_GreyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(VI_GreyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(VI_GreyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(VI_White, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        VI_GreyLayout.setVerticalGroup(
            VI_GreyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(VI_GreyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(VI_White, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(244, 244, 244));

        jLabel4.setText("Analysis ");

        SAR_Button.setText("Save analysis results");
        SAR_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SAR_ButtonActionPerformed(evt);
            }
        });

        jScrollPane1.setBackground(new java.awt.Color(244, 244, 244));
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        graph_panel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout graph_panelLayout = new javax.swing.GroupLayout(graph_panel);
        graph_panel.setLayout(graph_panelLayout);
        graph_panelLayout.setHorizontalGroup(
            graph_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        graph_panelLayout.setVerticalGroup(
            graph_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(graph_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(SAR_Button))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SAR_Button)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(graph_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout Simulation_based_AnalysisLayout = new javax.swing.GroupLayout(Simulation_based_Analysis);
        Simulation_based_Analysis.setLayout(Simulation_based_AnalysisLayout);
        Simulation_based_AnalysisLayout.setHorizontalGroup(
            Simulation_based_AnalysisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Simulation_based_AnalysisLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Simulation_based_AnalysisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Simulation_based_AnalysisLayout.createSequentialGroup()
                        .addGroup(Simulation_based_AnalysisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(VPR_Grey, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(VI_Grey, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(VP_Panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        Simulation_based_AnalysisLayout.setVerticalGroup(
            Simulation_based_AnalysisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Simulation_based_AnalysisLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(VP_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Simulation_based_AnalysisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Simulation_based_AnalysisLayout.createSequentialGroup()
                        .addComponent(VI_Grey, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(VPR_Grey, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Simulation-based Analysis", Simulation_based_Analysis);

        VI_White1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel25.setText("Verification Configuration");

        jLabel26.setText("alpha (α)");

        jLabel27.setText("beta (β)");

        jLabel28.setText("delta (δ)");

        jLabel29.setText("Number of minimum samples");

        jLabel30.setText("Simulation time");

        jLabel31.setText("Number of simulations");

        jLabel32.setText("Simulation Scenario");

        jScrollPane28.setViewportView(jTextPane26);

        jScrollPane29.setViewportView(jTextPane27);

        jScrollPane30.setViewportView(jTextPane28);

        jScrollPane31.setViewportView(jTextPane29);

        jScrollPane32.setViewportView(jTextPane30);

        jScrollPane33.setViewportView(jTextPane31);

        jLabel33.setText("Sliced");

        enable_button2.setText("Yes");
        enable_button2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                enable_button2MouseClicked(evt);
            }
        });
        enable_button2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enable_button2ActionPerformed(evt);
            }
        });

        disable_button2.setText("No");
        disable_button2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                disable_button2MouseClicked(evt);
            }
        });
        disable_button2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disable_button2ActionPerformed(evt);
            }
        });

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox3ItemStateChanged(evt);
            }
        });
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        jTextField_SS1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_SS1ActionPerformed(evt);
            }
        });

        jButton4.setText("File..");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("File..");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout VI_White1Layout = new javax.swing.GroupLayout(VI_White1);
        VI_White1.setLayout(VI_White1Layout);
        VI_White1Layout.setHorizontalGroup(
            VI_White1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(VI_White1Layout.createSequentialGroup()
                .addGroup(VI_White1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, VI_White1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(VI_White1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel25)
                            .addComponent(jLabel33)
                            .addComponent(jLabel32))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(VI_White1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(VI_White1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, VI_White1Layout.createSequentialGroup()
                                    .addComponent(jTextField_SS1, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(VI_White1Layout.createSequentialGroup()
                                    .addComponent(enable_button2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(disable_button2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(VI_White1Layout.createSequentialGroup()
                                .addComponent(jTextField_VC1, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 8, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, VI_White1Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(VI_White1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(VI_White1Layout.createSequentialGroup()
                                .addComponent(jLabel31)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane33, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(VI_White1Layout.createSequentialGroup()
                                .addComponent(jLabel29)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane31, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(VI_White1Layout.createSequentialGroup()
                                .addComponent(jLabel26)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane28, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel27)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane30, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel28)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane29, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 33, Short.MAX_VALUE))
                            .addGroup(VI_White1Layout.createSequentialGroup()
                                .addComponent(jLabel30)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane32, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        VI_White1Layout.setVerticalGroup(
            VI_White1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(VI_White1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(VI_White1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(jTextField_SS1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4))
                .addGap(2, 2, 2)
                .addGroup(VI_White1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(enable_button2, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(disable_button2)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33))
                .addGap(2, 2, 2)
                .addGroup(VI_White1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(jTextField_VC1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5))
                .addGap(8, 8, 8)
                .addGroup(VI_White1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel26)
                    .addComponent(jScrollPane28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27)
                    .addComponent(jScrollPane30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28)
                    .addComponent(jScrollPane29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(VI_White1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29)
                    .addComponent(jScrollPane31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(VI_White1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel30)
                    .addComponent(jScrollPane32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(VI_White1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel31)
                    .addComponent(jScrollPane33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        VPR_White1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setText("Total # of Simulation");

        jLabel15.setText("Time Consumed");

        jLabel34.setText("Verification Progress");

        jLabel35.setText("Property Checking");

        jLabel36.setText("Target Model");

        jScrollPane3.setViewportView(jTextPane2);

        jScrollPane15.setViewportView(jTextPane13);

        jScrollPane21.setViewportView(jTextPane17);

        jScrollPane34.setViewportView(VP_TextPanel1);

        jScrollPane35.setViewportView(jTextPane19);

        javax.swing.GroupLayout VPR_White1Layout = new javax.swing.GroupLayout(VPR_White1);
        VPR_White1.setLayout(VPR_White1Layout);
        VPR_White1Layout.setHorizontalGroup(
            VPR_White1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(VPR_White1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(VPR_White1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(VPR_White1Layout.createSequentialGroup()
                        .addComponent(jLabel36)
                        .addGap(93, 93, 93)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(VPR_White1Layout.createSequentialGroup()
                        .addGroup(VPR_White1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel15)
                            .addComponent(jLabel34)
                            .addComponent(jLabel35))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(VPR_White1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                            .addComponent(jScrollPane21)
                            .addComponent(jScrollPane34)
                            .addComponent(jScrollPane35))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        VPR_White1Layout.setVerticalGroup(
            VPR_White1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(VPR_White1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(VPR_White1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(VPR_White1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(VPR_White1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jScrollPane21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(VPR_White1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel34)
                    .addComponent(jScrollPane34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(VPR_White1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel35)
                    .addComponent(jScrollPane35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);

        graph_panel1.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout graph_panel1Layout = new javax.swing.GroupLayout(graph_panel1);
        graph_panel1.setLayout(graph_panel1Layout);
        graph_panel1Layout.setHorizontalGroup(
            graph_panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 420, Short.MAX_VALUE)
        );
        graph_panel1Layout.setVerticalGroup(
            graph_panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(VI_White1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextArea2, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(VPR_White1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(graph_panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(32, 32, 32))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(105, 105, 105)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(VI_White1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextArea2, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(VPR_White1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 51, Short.MAX_VALUE))
                    .addComponent(graph_panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane2.addTab("Single Simulation", jPanel6);

        jMenu3.setText("Model");

        jMenuItem2.setText("Model Details");
        jMenu3.add(jMenuItem2);

        jMenuItem1.setText("Policy Details");
        jMenu3.add(jMenuItem1);

        jMenuBar2.add(jMenu3);

        jMenu4.setText("Simulation");

        jMenuItem3.setText("Start Simulation");
        jMenu4.add(jMenuItem3);

        jMenuItem4.setText("Stop Simulation (disabled)");
        jMenu4.add(jMenuItem4);

        jMenuItem5.setText("Simulation Configuration Details");
        jMenu4.add(jMenuItem5);

        jMenuItem6.setText("Save Simulation Logs");
        jMenu4.add(jMenuItem6);

        jMenuBar2.add(jMenu4);

        jMenu5.setText("Verification");

        jMenuItem7.setText("Verification Properties Details");
        jMenu5.add(jMenuItem7);

        jMenuItem8.setText("Save Analysis Results");
        jMenu5.add(jMenuItem8);

        jMenuBar2.add(jMenu5);

        jMenu6.setText("Help");

        jMenuItem9.setText("Help");
        jMenu6.add(jMenuItem9);

        jMenuItem10.setText("About SIMVA-SoS");
        jMenu6.add(jMenuItem10);

        jMenuItem11.setText("Documentation");
        jMenu6.add(jMenuItem11);

        jMenuBar2.add(jMenu6);

        setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SAR_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SAR_ButtonActionPerformed
        // TODO add your handling code here:
        int yes_or_no = JOptionPane.showConfirmDialog(null, "Are you sure?", "Warning", JOptionPane.YES_NO_OPTION);
        if (yes_or_no == 0) {
            VP_TextPanel.setText("Yes2");
        } else {
            VP_TextPanel.setText("No2");
        }
    }//GEN-LAST:event_SAR_ButtonActionPerformed

    private void Start_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Start_ButtonActionPerformed

        JFrame frame = new JFrame();
        String[] options = new String[2];
        options[0] = new String("Line Graph");
        options[1] = new String("Bar Graph");
        int oPtions = JOptionPane.showOptionDialog(frame.getContentPane(),"What type of graph would you like to draw?","Options", 0,JOptionPane.INFORMATION_MESSAGE,null,options,null);
        
//        JTextArea textArea = new JTextArea("yu");
//        JScrollPane scroll = new JScrollPane(textArea);
//        scroll. setVerticalScrollBarPolicy( JScrollPane. VERTICAL_SCROLLBAR_ALWAYS );
//        
        
        
        if (oPtions == 0){ // line graph
       try{
                createDataset();
                System.out.println ("returned");
                
        
       }
       catch (Exception ex) {} // bar graph
            
        }else{
            try{
                createDataset2();
                
                    //printLog();
//                    String output = "";
//                    
//                    for(int i =0; i<30;i++){
//                        output = output +i +"\n";
//                        
//                    }
//                    jTextArea1.setText(output);
//                    jTextArea1.setEditable(false);
                              
                
            }catch (Exception wow){}
        }

     
      
      
    }//GEN-LAST:event_Start_ButtonActionPerformed

 
    private void Stop_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Stop_ButtonActionPerformed
        // TODO add your handling code here:
        //Start_ButtonActionPerformed(evt);
        //createDataset();
        Start_ButtonActionPerformed(evt);
       
     
    }//GEN-LAST:event_Stop_ButtonActionPerformed

    private void enable_button1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_enable_button1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_enable_button1MouseClicked

    private void enable_button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enable_button1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_enable_button1ActionPerformed

    private void disable_button1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_disable_button1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_disable_button1MouseClicked

    private void disable_button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disable_button1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_disable_button1ActionPerformed

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Simulation Scenario Button
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        String filename = f.getAbsolutePath();
        jTextField_SS.setText(filename);

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField_SSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_SSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_SSActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Verification Configuration Button
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f1 = chooser.getSelectedFile();
        String filename1 = f1.getAbsolutePath();
        jTextField_VC.setText(filename1);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void SVR_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SVR_ButtonActionPerformed
        // TODO add your handling code here:
        int yes_or_no = JOptionPane.showConfirmDialog(null, "Are you sure?", "Warning", JOptionPane.YES_NO_OPTION);
        if (yes_or_no == 0) {
            VP_TextPanel.setText("Yes");
        } else {
            VP_TextPanel.setText("No");
        }
    }//GEN-LAST:event_SVR_ButtonActionPerformed

    private void enable_button2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_enable_button2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_enable_button2MouseClicked

    private void enable_button2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enable_button2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_enable_button2ActionPerformed

    private void disable_button2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_disable_button2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_disable_button2MouseClicked

    private void disable_button2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disable_button2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_disable_button2ActionPerformed

    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox3ItemStateChanged

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jTextField_SS1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_SS1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_SS1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewJFrame().setVisible(true);


            }
        });
    }
    //create the line graph


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton SAR_Button;
    private javax.swing.JButton SVR_Button;
    private javax.swing.JPanel Simulation_based_Analysis;
    private javax.swing.JButton Start_Button;
    private javax.swing.JButton Stop_Button;
    private javax.swing.JPanel VI_Grey;
    private javax.swing.JPanel VI_White;
    private javax.swing.JPanel VI_White1;
    private javax.swing.JPanel VPR_Grey;
    private javax.swing.JPanel VPR_White;
    private javax.swing.JPanel VPR_White1;
    private javax.swing.JPanel VP_Panel;
    private javax.swing.JTextPane VP_TextPanel;
    private javax.swing.JTextPane VP_TextPanel1;
    private javax.swing.JRadioButton disable_button1;
    private javax.swing.JRadioButton disable_button2;
    private javax.swing.JRadioButton enable_button1;
    private javax.swing.JRadioButton enable_button2;
    private javax.swing.JPanel graph_panel;
    private javax.swing.JPanel graph_panel1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane21;
    private javax.swing.JScrollPane jScrollPane22;
    private javax.swing.JScrollPane jScrollPane23;
    private javax.swing.JScrollPane jScrollPane24;
    private javax.swing.JScrollPane jScrollPane25;
    private javax.swing.JScrollPane jScrollPane26;
    private javax.swing.JScrollPane jScrollPane27;
    private javax.swing.JScrollPane jScrollPane28;
    private javax.swing.JScrollPane jScrollPane29;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane30;
    private javax.swing.JScrollPane jScrollPane31;
    private javax.swing.JScrollPane jScrollPane32;
    private javax.swing.JScrollPane jScrollPane33;
    private javax.swing.JScrollPane jScrollPane34;
    private javax.swing.JScrollPane jScrollPane35;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField_SS;
    private javax.swing.JTextField jTextField_SS1;
    private javax.swing.JTextField jTextField_VC;
    private javax.swing.JTextField jTextField_VC1;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextPane jTextPane12;
    private javax.swing.JTextPane jTextPane13;
    private javax.swing.JTextPane jTextPane16;
    private javax.swing.JTextPane jTextPane17;
    private javax.swing.JTextPane jTextPane18;
    private javax.swing.JTextPane jTextPane19;
    private javax.swing.JTextPane jTextPane2;
    private javax.swing.JTextPane jTextPane20;
    private javax.swing.JTextPane jTextPane21;
    private javax.swing.JTextPane jTextPane22;
    private javax.swing.JTextPane jTextPane23;
    private javax.swing.JTextPane jTextPane24;
    private javax.swing.JTextPane jTextPane25;
    private javax.swing.JTextPane jTextPane26;
    private javax.swing.JTextPane jTextPane27;
    private javax.swing.JTextPane jTextPane28;
    private javax.swing.JTextPane jTextPane29;
    private javax.swing.JTextPane jTextPane30;
    private javax.swing.JTextPane jTextPane31;
    // End of variables declaration//GEN-END:variables
}
