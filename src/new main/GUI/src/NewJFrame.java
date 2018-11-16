/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.util.Pair;
import new_simvasos.log.Snapshot;
import new_simvasos.property.*;
import new_simvasos.simulation.Simulation_Firefighters;
import new_simvasos.verifier.SPRT;
import org.jdesktop.swingx.JXTreeTable;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author TaeGeon, SuMin, YongJun, SangWon
 */

public class NewJFrame extends javax.swing.JFrame {
    private PrintStream printStreamSimulation;
    private PrintStream printStreamSingle;
    private JFreeChart lineChart;
    private JFreeChart barChart;
    
    private final ScheduledExecutorService scheduler;
    private final ScheduledExecutorService dataReadingScheduler;
    private ScheduledExecutorService dataAddingScheduler;
    private ScheduledFuture<?> dataAddingThread;
    private DatasetUtility dataTool1;
    private DatasetUtility dataTool2;
    private ArrayList<String> fileBufferVerification;
    private ArrayList<String> fileBufferSingle;
    
    /**
     * Creates new form NewJFrame
     */
    public NewJFrame() {
        setTitle("[SIMVA-SoS] Main Window");
        initComponents();
        this.setLocationRelativeTo(null);
        graph_panel.setLayout(new java.awt.BorderLayout());
        graph_panel1.setLayout(new java.awt.BorderLayout());
        
        scheduler = Executors.newScheduledThreadPool(2);
        dataReadingScheduler = Executors.newScheduledThreadPool(1);
        dataAddingScheduler = Executors.newScheduledThreadPool(1);
        dataTool1 = new DatasetUtility(new DefaultCategoryDataset());
        dataTool2 = new DatasetUtility(new DefaultCategoryDataset());
        
        lineChart = ChartFactory.createLineChart("Single Simulation", "Tick", "Patients", dataTool1.getDataset(), PlotOrientation.VERTICAL, true, true, false);
        barChart = ChartFactory.createBarChart("Statistical Verification", "Theta", "NumSamples", dataTool2.getDataset(), PlotOrientation.VERTICAL, true, true, false); // horizontal
        
        // Single
        drawGraph(null);
        // Verification
        drawBarGraph(null);
        
        jTextArea1.setEditable(false); // log for the simulation-based Analysis
        jTextArea2.setEditable(false); // log for the single simulation
        
        printStreamSimulation = new PrintStream(new CustomOutputStream(jTextArea1));
        printStreamSingle = new PrintStream(new CustomOutputStream(jTextArea2));
        
        fileBufferVerification = new ArrayList<>();
        fileBufferSingle = new ArrayList<>();
        
        // 시험 검증 평가
        jTextPane24.setText("300");
        jTextField_VC.setText("Existence Property");
        jTextField_SS.setText("MCI Firefighter Scenario1");
        
        // Slicing Part Disable
        jLabel24.setVisible(false);
        enable_button1.setVisible(false);
        disable_button1.setVisible(false);
        jComboBox2.setVisible(false);
        
        // Verification Property Information (Alpha, Beta, Gamma)
        jTextPane20.setText("0.05");
        jTextPane22.setText("0.05");
        jTextPane21.setText("0.01");
        // TODO Minimum Sample number 2?
        jTextPane23.setText("2");
        jTextPane25.setText("1500");
        
        // Single Simulation Information
        jTextField_SS2.setText("MCI Firefighter Scenario 1");
        jTextField_SS3.setBackground(Color.LIGHT_GRAY);
        jTextField_SS3.setText("Not supported");
        jTextField_SS3.setEditable(false);
        jTextField_VC2.setBackground(Color.LIGHT_GRAY);
        jTextField_VC2.setText("Not supported");
        jTextField_VC2.setEditable(false);
        
        // Single Simulation Progress & Result
        jTextField_SS5.setVisible(false);
        jButton11.setVisible(false);
        jLabel47.setVisible(false);
        jButton9.setVisible(false);
        jButton10.setVisible(false);
    }
    
    /**
     * Transfers output console to simulation log
     */
    public class CustomOutputStream extends OutputStream {
        private JTextArea textArea;
        
        public CustomOutputStream(JTextArea textArea) {
            this.textArea = textArea;
        }
        
        @Override
        public void write(int b) throws IOException {
            // redirects data to the text area
            textArea.append(String.valueOf((char) b));
            // scrolls the text area to the end of data
            textArea.setCaretPosition(textArea.getDocument().getLength());
        }
    }
    
    /**
     * @param dataset Creates line graph
     */
    public void drawGraph(DefaultCategoryDataset dataset) {
        
        //set color
        CategoryPlot plot = (CategoryPlot) lineChart.getPlot();
        plot.getRenderer().setSeriesPaint(0, Color.BLUE);
        
        //create chart panel the add it to swing panel in jframe
        ChartPanel chartpanel1 = new ChartPanel(lineChart);
        graph_panel1.removeAll();
        graph_panel1.add(chartpanel1, BorderLayout.CENTER);
        graph_panel1.revalidate();
    }
    
    /**
     * @param dataset Creates bar graph
     */
    public void drawBarGraph(DefaultCategoryDataset dataset) {
        CategoryPlot plot = (CategoryPlot) barChart.getPlot();
        plot.getRenderer().setSeriesPaint(0, Color.BLUE);
        plot.getRenderer().setSeriesPaint(1, Color.RED);
        ChartPanel chartpanel = new ChartPanel(barChart);
        
        graph_panel.removeAll();
        graph_panel.add(chartpanel, BorderLayout.CENTER);
        graph_panel.revalidate();
    }
    
    /**
     * @return Adding line graph data to data tool (DataUtility class)
     * @throws InterruptedException
     */
    private DefaultCategoryDataset createDataset() throws InterruptedException {
        Runnable timerTask = new Runnable() {
            @Override
            public void run() {
                
                DefaultCategoryDataset data = dataTool1.getDataset();
                drawGraph(data);
                
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                }
            }
        };
        ScheduledFuture<?> timerHandle =
            dataReadingScheduler.scheduleAtFixedRate(timerTask, 0, 1, TimeUnit.SECONDS);
        
        dataAddingThread = dataAddingScheduler.schedule(new Runnable() {
            @Override
            public void run() {   //single simulation tab
                
                dataTool1.reset();
                double count = 0;
                
                // Simulation
                int simulationTime = 300;
                
                jTextField_SS4.setText("Simulation in Progress");
                System.out.println("Run Single Simulation");
    
                Simulation_Firefighters sim1 = new Simulation_Firefighters(simulationTime);
                
                long start = System.currentTimeMillis();
                HashMap<Integer, Snapshot> snapshotMap = sim1.runSimulation().getSnapshotMap();
                long end = System.currentTimeMillis();
                
                //writer.println("Run Single Simulation");
                
                for (int i = 0; i < snapshotMap.size(); i++) {
                    
                    // Calculate a progress rate
                    count += (double)100 / (double)snapshotMap.size();
                    if (i == snapshotMap.size()-1) count = 100;
                    jProgressBar2.setString((int)count + "% Done");
                    jProgressBar2.setValue((int)count);
                    
                    System.out.println("tick: " + (i + 1) + " " + snapshotMap.get(i).getSnapshotString());
                    //writer.println(snapshotMap.get(i).getSnapshotString());
                    fileBufferSingle.add("tick: " + (i + 1) + " " + snapshotMap.get(i).getSnapshotString());
                    
                    StringTokenizer st = new StringTokenizer(snapshotMap.get(i).getSnapshotString(), " ");
                    while (st.hasMoreTokens()) {
                        if (st.nextToken().equals("NotRescuedPatients:"))
                            break;
                    }
                    
                    int notRescuedPatients = Integer.parseInt(st.nextToken());
                    // number of samples for this theta iteration
                    dataTool1.addValueIntoDataset(notRescuedPatients, "line", String.valueOf(i + 1));
                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                
                jTextField_SS4.setText("Simulation Finished");
                jTextField_VC3.setText("Total runtime: " + ( end - start )/1000.0 + " sec");
                jTextField_VC3.setEditable(false);
            }
            
        }, 1, TimeUnit.SECONDS);
        
        return dataTool1.getDataset();
    }
    
    
    /**
     * @return Adding bar graph data to data tool (DataUtility class)
     * @throws InterruptedException
     */
    //This is for bar graph
    private DefaultCategoryDataset createDataset2() throws InterruptedException {
        Runnable timerTask = new Runnable() {
            @Override
            public void run() {
                DefaultCategoryDataset data = dataTool2.getDataset();
                
                drawBarGraph(data);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                }
            }
        };
        ScheduledFuture<?> timerHandle =
            dataReadingScheduler.scheduleAtFixedRate(timerTask, 0, 1, TimeUnit.SECONDS);
        
        dataAddingThread = dataAddingScheduler.schedule(new Runnable() {
            @Override
            public void run() {
                
                dataTool2.reset();
                MCIProperty rescuedProperty;
                SPRT verifier;
                
                //시험 검증 평가
                System.out.println("**********SIMVA_SoS Existence Property Evaluation START**********");
                
                MCIPropertyChecker existenceChecker = new MCIPropertyChecker();
                MCIAbsenceChecker absenceChecker = new MCIAbsenceChecker();
                MCISteadyStateProbabilityChecker steadyChecker = new MCISteadyStateProbabilityChecker();
                MCITransientStateProbabilityChecker transientChecker = new MCITransientStateProbabilityChecker();
                MCIUniversalityChecker universalityChecker = new MCIUniversalityChecker();
                MCIMinimumDurationChecker minimumDurationChecker = new MCIMinimumDurationChecker();
    
                verifier = new SPRT(existenceChecker);
                //verifier = new SPRT(absenceChecker);
                //verifier = new SPRT(steadyChecker);
                //verifier = new SPRT(transientChecker);
                //verifier = new SPRT(universalityChecker);
                //verifier = new SPRT(minimumDurationChecker);
                
                // Simulation
                int repeatSim = 1500;
                int simulationTime = 300;
                Pair<Pair<Integer, Boolean>, String> verificationResult;
                double theta;
                int count = 0;
                Boolean totalRet = true;
                double probability = 0;
                int accumulatedSimulation = 0;
                
                Simulation_Firefighters sim1 = new Simulation_Firefighters(simulationTime);
                
                rescuedProperty = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIPropertyType", 0.90);
                
                // SteadyStateProbability
               //rescuedProperty.setThresholdPatient(0.5);
                
                // TransientStateProbability
                //rescuedProperty.setThresholdPatient(0.9);
                
                // Universality, MinimumDuration
                //rescuedProperty.setThresholdPatient(1.0);
                
                
                System.out.println("Simulation based Analysis");
                fileBufferVerification.add("Simulation based Analysis");
    
                // Verification Progress & Result Modification
                jTextPane1.setText("MCI Response SoS Model (Simulation_Firefighters.java)");
                VP_TextPanel.setText("Verification in progress");
    
                long start = System.currentTimeMillis();
                for (int i = 1; i < 100; i++) {
                    
                    theta = i * 0.01;
                    verificationResult = verifier.verifyWithSimulationGUI(sim1, rescuedProperty, repeatSim, theta);
                    
                    // True or False for this theta iteration
                    int myInt = verificationResult.getKey().getValue() ? 1 : 0;
                    
                    // number of samples for this theta iteration
                    dataTool2.addValueIntoDataset(verificationResult.getKey().getKey(), "line", String.valueOf(theta));
                    accumulatedSimulation += verificationResult.getKey().getKey();
                    
                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    
                    if (totalRet) {
                        if (!verificationResult.getKey().getValue()) {
                            totalRet = false;
                            probability = theta;
                        }
                    }
                    System.out.println(verificationResult.getValue());
                    fileBufferVerification.add(verificationResult.getValue());
    
                    jTextPane12.setText(Integer.toString(accumulatedSimulation));
    
                    count++;
                    jProgressBar1.setString(count + "% Done");
                    jProgressBar1.setValue(count);
                }
                long end = System.currentTimeMillis();
    
                // Verification Progress & Result Modification
                VP_TextPanel.setText("Verification in progress");
                
                System.out.println("Probability: about " + probability * 100 + "%");
                fileBufferVerification.add("Probability: about " + probability * 100 + "%");
                System.out.println("---------------------------------------------------------------------");
                fileBufferVerification.add("---------------------------------------------------------------------");
                System.out.print("Total runtime: " + (end - start) / 1000.0 + " sec");
                fileBufferVerification.add("Total runtime: " + (end - start) / 1000.0 + " sec");
                
                //writer.close();
                jTextPane16.setText("Total runtime: " + (end - start) / 1000.0 + " sec");
                jTextPane16.setEditable(false);
                
                // Verification Progress & Result Modification
                jTextPane18.setText("Transient Universality Checking Result: " + probability*100 + "%");
            }
            
        }, 1, TimeUnit.SECONDS);
        
        return dataTool2.getDataset();
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenuItem19 = new javax.swing.JMenuItem();
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
        jLabel3 = new javax.swing.JLabel();
        VI_Grey1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        VI_White2 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jTextField_SS2 = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jTextField_VC2 = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jLabel45 = new javax.swing.JLabel();
        jTextField_SS3 = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        VI_Grey3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        VI_Grey2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        VI_White3 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jTextField_SS4 = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        jTextField_VC3 = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        jLabel47 = new javax.swing.JLabel();
        jTextField_SS5 = new javax.swing.JTextField();
        jButton11 = new javax.swing.JButton();
        SVR_Button1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        Start_Button1 = new javax.swing.JButton();
        Stop_Button1 = new javax.swing.JButton();
        jProgressBar2 = new javax.swing.JProgressBar();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        SAR_Button1 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
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
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        
        
        //imhere
        
        
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
        jProgressBar1.setBorderPainted(true);
        
        
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
        
        jLabel22.setText("Maximum Number of simulations");
        
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
        
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
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
        
        jPanel6.setBackground(new java.awt.Color(244, 244, 244));
        
        jLabel3.setText("Simulation Progress");
        
        VI_Grey1.setBackground(new java.awt.Color(244, 244, 244));
        
        jLabel6.setText("Simulation Information ");
        
        VI_White2.setBackground(new java.awt.Color(255, 255, 255));
        
        jLabel37.setText("Simulation Configuration");
        
        jLabel44.setText("Simulation Scenario");
        
        jTextField_SS2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_SS2ActionPerformed(evt);
            }
        });
        
        jButton6.setText("File..");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        
        jButton7.setText("File..");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        
        jLabel45.setText("Imported Policy");
        
        jTextField_SS3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_SS3ActionPerformed(evt);
            }
        });
        
        jButton8.setText("File..");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        
        javax.swing.GroupLayout VI_White2Layout = new javax.swing.GroupLayout(VI_White2);
        VI_White2.setLayout(VI_White2Layout);
        VI_White2Layout.setHorizontalGroup(
            VI_White2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(VI_White2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(VI_White2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel37)
                        .addComponent(jLabel44)
                        .addComponent(jLabel45))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(VI_White2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(VI_White2Layout.createSequentialGroup()
                            .addComponent(jTextField_SS3, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(VI_White2Layout.createSequentialGroup()
                            .addComponent(jTextField_SS2, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(VI_White2Layout.createSequentialGroup()
                            .addComponent(jTextField_VC2, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(27, Short.MAX_VALUE))
        );
        VI_White2Layout.setVerticalGroup(
            VI_White2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(VI_White2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(VI_White2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel44)
                        .addComponent(jTextField_SS2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton6))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(VI_White2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField_SS3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel45)
                        .addComponent(jButton8))
                    .addGap(6, 6, 6)
                    .addGroup(VI_White2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel37)
                        .addComponent(jTextField_VC2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton7))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        
        VI_Grey3.setBackground(new java.awt.Color(244, 244, 244));
        
        jLabel9.setText("Model Information ");
        
        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("Organization1");
        javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("CS_Firefighter");
        javax.swing.tree.DefaultMutableTreeNode treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("CS_Action_Rescue");
        javax.swing.tree.DefaultMutableTreeNode treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Behaviour_First_Aid");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Behaviour_Pick_up");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("CS_Action_Move");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("hi");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("CS_Action_Extinguish");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("hello");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        jTree1.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        //jScrollPane3.setViewportView(jTree1);
        
        
        javax.swing.GroupLayout VI_Grey3Layout = new javax.swing.GroupLayout(VI_Grey3);
        VI_Grey3.setLayout(VI_Grey3Layout);
        VI_Grey3Layout.setHorizontalGroup(
            VI_Grey3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(VI_Grey3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(VI_Grey3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(27, Short.MAX_VALUE))
        );
        VI_Grey3Layout.setVerticalGroup(
            VI_Grey3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(VI_Grey3Layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(jLabel9)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGap(11, 11, 11))
        );
        
        VI_Grey2.setBackground(new java.awt.Color(244, 244, 244));
        
        jLabel8.setText("Simulation Progress & Results");
        
        VI_White3.setBackground(new java.awt.Color(255, 255, 255));
        
        jLabel38.setText("Time Consumed");
        
        jLabel46.setText("Simulation Progress");
        
        jTextField_SS4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_SS4ActionPerformed(evt);
            }
        });
        
        jButton9.setText("File..");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        
        jButton10.setText("File..");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        
        jLabel47.setText("Imported Policy");
        
        jTextField_SS5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_SS5ActionPerformed(evt);
            }
        });
        
        jButton11.setText("File..");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        
        javax.swing.GroupLayout VI_White3Layout = new javax.swing.GroupLayout(VI_White3);
        VI_White3.setLayout(VI_White3Layout);
        VI_White3Layout.setHorizontalGroup(
            VI_White3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(VI_White3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(VI_White3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel38)
                        .addComponent(jLabel46)
                        .addComponent(jLabel47))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(VI_White3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(VI_White3Layout.createSequentialGroup()
                            .addComponent(jTextField_SS5, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(VI_White3Layout.createSequentialGroup()
                            .addComponent(jTextField_SS4, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                            //.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(VI_White3Layout.createSequentialGroup()
                            .addComponent(jTextField_VC3, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                            //.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(29, Short.MAX_VALUE))
        );
        VI_White3Layout.setVerticalGroup(
            VI_White3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(VI_White3Layout.createSequentialGroup()
                    // Simualtion Progress & Results Gap
                    .addGap(10)
                    .addContainerGap()
                    .addGroup(VI_White3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel46)
                        .addComponent(jTextField_SS4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton9))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGap(15)
                    .addGroup(VI_White3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField_SS5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel47)
                        .addComponent(jButton11))
                    .addGap(6, 6, 6)
                    .addGroup(VI_White3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel38)
                        .addComponent(jTextField_VC3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton10))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        
        SVR_Button1.setText("Save verification results");
        SVR_Button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SVR_Button1ActionPerformed(evt);
            }
        });
        
        javax.swing.GroupLayout VI_Grey2Layout = new javax.swing.GroupLayout(VI_Grey2);
        VI_Grey2.setLayout(VI_Grey2Layout);
        VI_Grey2Layout.setHorizontalGroup(
            VI_Grey2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(VI_Grey2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(VI_Grey2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(VI_White3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(VI_Grey2Layout.createSequentialGroup()
                            .addComponent(jLabel8)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(SVR_Button1, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap())
        );
        VI_Grey2Layout.setVerticalGroup(
            VI_Grey2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(VI_Grey2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(VI_Grey2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(SVR_Button1))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(VI_White3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        
        javax.swing.GroupLayout VI_Grey1Layout = new javax.swing.GroupLayout(VI_Grey1);
        VI_Grey1.setLayout(VI_Grey1Layout);
        VI_Grey1Layout.setHorizontalGroup(
            VI_Grey1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(VI_Grey1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(VI_Grey1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(VI_Grey3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(VI_Grey2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(VI_Grey1Layout.createSequentialGroup()
                            .addGap(6, 6, 6)
                            .addGroup(VI_Grey1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(VI_White2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        VI_Grey1Layout.setVerticalGroup(
            VI_Grey1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(VI_Grey1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel6)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(VI_White2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(VI_Grey3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(VI_Grey2, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
        );
        
        jPanel3.setBackground(new java.awt.Color(244, 244, 244));
        
        Start_Button1.setText("▶");
        Start_Button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Start_Button1ActionPerformed(evt);
            }
        });
        
        Stop_Button1.setFont(new java.awt.Font("Lucida Grande", 0, 25)); // NOI18N
        Stop_Button1.setText("■");
        Stop_Button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Stop_Button1ActionPerformed(evt);
            }
        });
        
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(Start_Button1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(Stop_Button1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(8, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(Stop_Button1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Start_Button1)))
        );
        
        jProgressBar2.setString("0% Done");
        jProgressBar2.setStringPainted(true);
        
        
        jPanel4.setBackground(new java.awt.Color(244, 244, 244));
        
        jLabel10.setText("Simulation Log");
        
        SAR_Button1.setText("Save simulation log");
        SAR_Button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SAR_Button1ActionPerformed(evt);
            }
        });
        
        jScrollPane4.setBackground(new java.awt.Color(244, 244, 244));
        jScrollPane4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        
        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane4.setViewportView(jTextArea2);
        
        graph_panel1.setBackground(new java.awt.Color(255, 255, 255));
        
        javax.swing.GroupLayout graph_panel1Layout = new javax.swing.GroupLayout(graph_panel1);
        graph_panel1.setLayout(graph_panel1Layout);
        graph_panel1Layout.setHorizontalGroup(
            graph_panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE)
        );
        graph_panel1Layout.setVerticalGroup(
            graph_panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 222, Short.MAX_VALUE)
        );
        
        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(graph_panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 272, Short.MAX_VALUE)
                            .addComponent(SAR_Button1))
                        .addComponent(jScrollPane4))
                    .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel10))
                        .addComponent(SAR_Button1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(graph_panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
        );
        
        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel6Layout.createSequentialGroup()
                                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jProgressBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addComponent(VI_Grey1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel3)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jProgressBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(20, 20, 20)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addComponent(VI_Grey1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGap(25, 25, 25))
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addContainerGap())))
        );
        
        jTabbedPane2.addTab("Single Simulation", jPanel6);
        
        jMenu3.setText("Model");
        
        jMenuItem2.setText("Model Details");
        jMenu3.add(jMenuItem2);
        
        jMenuItem1.setText("Policy Details");
        jMenu3.add(jMenuItem1);
        
        jMenuBar2.add(jMenu3);
        
        jMenu4.setText("Simulation-based Analysis"); // simulation-based analysis menu
        
        jMenuItem3.setText("Initialize analysis");
        jMenu4.add(jMenuItem3);
        
        jMenu4.add(jSeparator1);
        
        jMenu1.setText("Import");
        jMenuItem13.setText("Simulation Scenario");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileformenu(evt);
            }
        });
        
        jMenuItem14.setText("Verification Configuration");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileformenu(evt);
            }
        });
        
        
        jMenu1.add(jMenuItem13);
        jMenu1.add(jMenuItem14);
        
        
        jMenu4.add(jMenu1); // jmenu4 = simulation-based analysis
        jMenu4.add(jSeparator2);
        jMenuItem4.setText("Start analysis");
        
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        
        jMenu4.add(jMenuItem4);
        
        jMenuItem5.setText("Stop analysis");
        jMenu4.add(jMenuItem5);
        
        jMenu4.add(jSeparator3);
        jMenuItem6.setText("Save analysis results");
        jMenu4.add(jMenuItem6);
        
        jMenuBar2.add(jMenu4);
        
        jMenu5.setText("Single Simulation");
        
        jMenuItem7.setText("Initialize single simulation");
        jMenu5.add(jMenuItem7);
        jMenu5.add(jSeparator4);
        
        jMenu2.setText("Import");
        jMenu5.add(jMenu2);
        jMenuItem15.setText("Simulation Scenario");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileformenu(evt);
            }
        });
        
        jMenuItem16.setText("Policy Specification");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileformenu(evt);
            }
        });
        
        jMenuItem17.setText("Simulation Configuration");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileformenu(evt);
            }
        });
        
        
        jMenu2.add(jMenuItem15);
        jMenu2.add(jMenuItem16);
        jMenu2.add(jMenuItem17);
        
        jMenu5.add(jSeparator5);
        
        jMenuItem18.setText("Start simulation");
        
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        
        jMenuItem19.setText("Stop simulation");
        jMenu5.add(jMenuItem18);
        jMenu5.add(jMenuItem19);
        jMenu5.add(jSeparator6);
        
        jMenuItem8.setText("Save simulation results");
        jMenu5.add(jMenuItem8);
        
        jMenuBar2.add(jMenu5);
        
        jMenu6.setText("Help");
        
        jMenuItem9.setText("Help");
        jMenu6.add(jMenuItem9);
        
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        
        
        jMenuItem10.setText("About SIMVA-SoS");
        jMenu6.add(jMenuItem10);
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        
        
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
    }// </editor-fold>
    
    private void jMenuItem10ActionPerformed(ActionEvent evt) {
        Desktop d = Desktop.getDesktop();
        try {
            d.browse(new URI("http://se.kaist.ac.kr/"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    
    private void jMenuItem9ActionPerformed(ActionEvent evt) {
        Desktop d = Desktop.getDesktop();
        try {
            d.browse(new URI("http://se.kaist.ac.kr/"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * @param evt Enables start button for bar graph
     */
    private void Start_ButtonActionPerformed(ActionEvent evt) {
        fileBufferVerification.clear();
        System.setOut(this.printStreamSimulation);
        System.setErr(this.printStreamSimulation);
        try {// bar graph
            createDataset2();
        } catch (Exception ex) {
        }
    }
    
    
    private void Stop_ButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        
    }
    
    private void enable_button1MouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }
    
    private void enable_button1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }
    
    private void disable_button1MouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }
    
    private void disable_button1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }
    
    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {
        // TODO add your handling code here:
    }
    
    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }
    
    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        Start_ButtonActionPerformed(evt);
    }
    
    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        Start_Button1ActionPerformed(evt);
    }
    
    /**
     * @param evt Activates file explorer for Simulation Scenario on Simulation-based Analysis tab
     */
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        // Simulation Scenario Button
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        String filename = f.getAbsolutePath();
        jTextField_SS.setText(filename);
    }
    
    private void fileformenu(java.awt.event.ActionEvent evt) {
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        try {
            f.getCanonicalFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void jTextField_SSActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }
    
    /**
     * @param evt Activates file explorer for Verification Configuration on Simulation-based Analysis tab
     */
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        // Verification Configuration Button
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f1 = chooser.getSelectedFile();
        String filename1 = f1.getAbsolutePath();
        jTextField_VC.setText(filename1);
    }
    
    /**
     * @param evt Activates Save Verification Results on Simulation-based Analysis tab, which saves time consumed in .txt file
     */
    private void SVR_ButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        int yes_or_no = JOptionPane.showConfirmDialog(null, "Are you sure?", "Location", JOptionPane.YES_NO_OPTION);
        
    }
    
    /**
     * @param evt Activates Save Verification Results on Single Simulation tab
     */
    private void SAR_Button1ActionPerformed(java.awt.event.ActionEvent evt) {
        // Save analysis results on single simulation tab
        
        JOptionPane.showConfirmDialog(null, "Location: ./src/new main/GUI/testing/SingleSimulationLog.txt", "Simulation Results Saved", JOptionPane.CLOSED_OPTION);
        
            String filetowrite = "./src/new main/GUI/testing/SingleSimulationLog.txt";
            FileWriter fw = null;
            try {
                fw = new FileWriter(filetowrite);
                
                for (int i = 0; i < fileBufferSingle.size(); i++) {
                    fw.write(fileBufferSingle.get(i));
                    fw.write(System.getProperty("line.separator"));
                }
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            jTextField_SS4.setText("Simulation Saved");
    }
    
    /**
     * @param evt Saves anyalysis results
     */
    private void SAR_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SAR_ButtonActionPerformed
        // TODO add your handling code here:
    
        JOptionPane.showConfirmDialog(null, "Location: ./src/new main/GUI/testing/VerificationLog.txt", "Verification Results Saved", JOptionPane.CLOSED_OPTION);
        
            String filetowrite = "./src/new main/GUI/testing/VerificationLog.txt";
            FileWriter fw = null;
            try {
                fw = new FileWriter(filetowrite);
                for (int i = 0; i < fileBufferVerification.size(); i++) {
                    fw.write(fileBufferVerification.get(i));
                    fw.write(System.getProperty("line.separator"));
                }
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            VP_TextPanel.setText("Verification Saved");
    }
    
    private void Stop_Button1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }
    
    /**
     * @param evt Activates start button for single simulation
     */
    private void Start_Button1ActionPerformed(java.awt.event.ActionEvent evt) {
        // start button for single simulation
        
        fileBufferSingle.clear();
        
        System.setOut(this.printStreamSingle);
        System.setErr(this.printStreamSingle);
        
        //This is for tree table on single simulation
        MyTreeTableModel treeTableModel = new MyTreeTableModel();
        JXTreeTable jXTreeTable = new JXTreeTable(treeTableModel);
        jScrollPane3.getViewport().add(jXTreeTable);
        
        //drawign line graph
        try {
            createDataset();
            
        } catch (Exception wow) {
        
        }
    }
    
    private void SVR_Button1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }
    
    /**
     * @param evt Activates file explorer for Imported Policy (Verification Progress & Results) on Single Simulation
     */
    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {
        // File chooser for Imported Policy on VP&R
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f11 = chooser.getSelectedFile();
        String filename = f11.getAbsolutePath();
        jTextField_SS5.setText(filename);
    }
    
    private void jTextField_SS5ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }
    
    /**
     * @param evt Activates file explorer for Simulation Configuration (Verification Progress & Results) on Single Simulation
     */
    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {
        // File chooser for Simulation Configuration on VP&R
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f10 = chooser.getSelectedFile();
        String filename = f10.getAbsolutePath();
        jTextField_VC3.setText(filename);
        
    }
    
    /**
     * @param evt Activates file explorer for Simulation Scenario (Verification Progress & Results) on Single Simulation
     */
    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {
        // File chooser for Simulation Scenario on VP&R
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f9 = chooser.getSelectedFile();
        String filename = f9.getAbsolutePath();
        jTextField_SS4.setText(filename);
    }
    
    private void jTextField_SS4ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }
    
    
    /**
     * @param evt Activates file explorer for Imported Policy (Simulation Information) on Single Simulation
     */
    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {
        //File chooser for imported policy in single simulation tab
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        String filename = f.getAbsolutePath();
        jTextField_SS3.setText(filename);
        
    }
    
    private void jTextField_SS3ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }
    
    /**
     * @param evt Activates file explorer for Simulation Configuration (Simulation Information) on Single Simulation
     */
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {
        // file chooser for Simulation Configuration in single simulation tab
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f7 = chooser.getSelectedFile();
        String filename = f7.getAbsolutePath();
        jTextField_VC2.setText(filename);
    }
    
    /**
     * @param evt Activates file explorer for Simulation Scenario (Simulation Information) on Single Simulation
     */
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {
        // File chooser for Simulation Scenario in single simulation tab
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f6 = chooser.getSelectedFile();
        String filename6 = f6.getAbsolutePath();
        jTextField_SS2.setText(filename6);
    }
    
    private void jTextField_SS2ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }
    
    
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
    
    
    // Variables declaration - do not modify
    private javax.swing.JButton SAR_Button; // Stastical Verification File Buffer Save Button
    private javax.swing.JButton SAR_Button1; // Single Simulation File Buffer Save Button
    private javax.swing.JButton SVR_Button;
    private javax.swing.JButton SVR_Button1;
    private javax.swing.JPanel Simulation_based_Analysis;
    private javax.swing.JButton Start_Button;
    private javax.swing.JButton Start_Button1;
    private javax.swing.JButton Stop_Button;
    private javax.swing.JButton Stop_Button1;
    private javax.swing.JPanel VI_Grey;
    private javax.swing.JPanel VI_Grey1;
    private javax.swing.JPanel VI_Grey2;
    private javax.swing.JPanel VI_Grey3;
    private javax.swing.JPanel VI_White;
    private javax.swing.JPanel VI_White2;
    private javax.swing.JPanel VI_White3;
    private javax.swing.JPanel VPR_Grey;
    private javax.swing.JPanel VPR_White;
    private javax.swing.JPanel VP_Panel;
    private javax.swing.JTextPane VP_TextPanel;
    private javax.swing.JRadioButton disable_button1;
    private javax.swing.JRadioButton enable_button1;
    private javax.swing.JPanel graph_panel;
    private javax.swing.JPanel graph_panel1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1; //import button on Simulation-based Analysis menu
    private javax.swing.JMenu jMenu2; //import button on Single simulation menu
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13; // Simulation Scenario on simulation-based Analysis menu
    private javax.swing.JMenuItem jMenuItem14; // Verification Configuration
    private javax.swing.JMenuItem jMenuItem15; // Simulation Scenario on Single Simulation menu
    private javax.swing.JMenuItem jMenuItem16; //policy Specification
    private javax.swing.JMenuItem jMenuItem17; // Simulation configuration
    private javax.swing.JMenuItem jMenuItem18; // start simulation
    private javax.swing.JMenuItem jMenuItem19; // stop simulation
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JProgressBar jProgressBar2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane22;
    private javax.swing.JScrollPane jScrollPane23;
    private javax.swing.JScrollPane jScrollPane24;
    private javax.swing.JScrollPane jScrollPane25;
    private javax.swing.JScrollPane jScrollPane26;
    private javax.swing.JScrollPane jScrollPane27;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField_SS;
    private javax.swing.JTextField jTextField_SS2;
    private javax.swing.JTextField jTextField_SS3;
    private javax.swing.JTextField jTextField_SS4;
    private javax.swing.JTextField jTextField_SS5;
    private javax.swing.JTextField jTextField_VC;
    private javax.swing.JTextField jTextField_VC2;
    private javax.swing.JTextField jTextField_VC3;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextPane jTextPane12;
    private javax.swing.JTextPane jTextPane16; //time consumed
    private javax.swing.JTextPane jTextPane18;
    private javax.swing.JTextPane jTextPane20;
    private javax.swing.JTextPane jTextPane21;
    private javax.swing.JTextPane jTextPane22;
    private javax.swing.JTextPane jTextPane23;
    private javax.swing.JTextPane jTextPane24;
    private javax.swing.JTextPane jTextPane25;
    private javax.swing.JTree jTree1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    
    
    // End of variables declaration
    
}
