package goalModelSlicer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableUndirectedGraph;

public class Main extends JApplet {

    private static final long serialVersionUID = 3256444702936019250L;
    private static final Color DEFAULT_BG_COLOR = Color.decode("#FAFBFF");
    private static final Dimension DEFAULT_SIZE = new Dimension(530, 320);

    private static JGraphModelAdapter<String, DefaultEdge> jgAdapter;
    // create a JGraphT graph
    private static ListenableUndirectedGraph<String, DefaultEdge> g = new ListenableUndirectedGraph<>(
            DefaultEdge.class);

    static Set<Goal> criterion = null;
    static Set<Goal> goals = null;

    public static void main(String[] args) {
        Main applet = new Main();
        applet.init();

        JFrame frame = new JFrame();
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());

        JPanel consolePanel = new JPanel();
        JTextArea console = new JTextArea(10, 97);
        console.setFont(new Font("Courier New", Font.PLAIN, 13));
        JScrollPane consoleJsp = new JScrollPane(console);
        consolePanel.add(consoleJsp);

        JTextField csvFile = new JTextField(9);
        JButton openCSV = new JButton("Open");

        // when button (@param: openCSV) clicked,
        // goal model file is parsed and shown up at the applet.
        openCSV.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    goals = openWindow(1, frame, console, csvFile);
                    drawGoalModel(goals);
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });

        JTextField sliceCriterion = new JTextField(7);
        JButton openCriterion = new JButton("Open");

        // when button (@param: openCriterion) clicked,
        // slice criterion file is parsed
        openCriterion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    criterion = openWindow(2, frame, console, sliceCriterion);
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });

        JButton detectChange = new JButton("Detect Change-related goals");

        // when button (@param: detectChange) clicked,
        // goal model is sliced, the old model is removed, and new sliced goal model is shown up.
        detectChange.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GoalModelSlicer gms = new GoalModelSlicer();
                gms.sliceGoalModel(goals, criterion);
                goals = gms.getSlicedGoals();
                removeGoalModel(goals);
                drawGoalModel(goals);
            }
        });


        // menu, console and applet is constructed.
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        menuPanel.setPreferredSize(new Dimension(260, 600));
        menuPanel.add(new JLabel("         ===== Setting Analyzer =====     "));
        menuPanel.add(new JLabel("Goal model: "));

        menuPanel.add(csvFile);
        menuPanel.add(openCSV);

        menuPanel.add(new JLabel("Slicing criterion:"));
        menuPanel.add(sliceCriterion);
        menuPanel.add(openCriterion);

        JPanel startPanel = new JPanel();
        startPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        startPanel.add(detectChange);

        menuPanel.add(startPanel);

        content.add("South", consolePanel);
        content.add("West", menuPanel);
        content.add("East", applet);

        frame.setTitle("SoS Goal Model Slicer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    // find root goal among goal set (@param: goals)
    // @return root Goal
    public static Goal findRootGoal(Set<Goal> goals) {
        for (Goal goal : goals) {
            for (String higherGoal : goal.higherGoals) {
                if (higherGoal.equals("-"))
                    return goal;
            }
        }
        return null;
    }

    // @return Goal which has name (@param: goalName)
    public static Goal findGoalByName(String goalName) {
        for (Goal goal : goals) {
            if (goal.name.equals(goalName))
                return goal;
        }
        return null;
    }

    // draw goal model according to goal set (@param: goals)
    public static void drawGoalModel(Set<Goal> goals) {

        // add vertices
        for (Goal goal : goals) {
            g.addVertex(goal.name);
        }

        // add edges
        for (Goal goal : goals) {
            for (String subgoal : goal.subGoals) {
                if (!subgoal.equals("-"))
                    g.addEdge(goal.name, subgoal);
            }
        }

        // locate vertices
        Goal root = findRootGoal(goals);
        recursiveLocate(root, 60, 20, 1);

    }

    // recursively locate rectangle with @param: goal, posX, posY, depth
    public static void recursiveLocate(Goal goal, int posX, int posY, int depth) {
        positionVertexAt(goal.name, posX, posY);
        int i = 0;
        for (String subGoalName : goal.subGoals) {
            if (subGoalName.equals("-"))
                return;
            recursiveLocate(findGoalByName(subGoalName), posX + (450 / ((depth + 1) * 2) * i), posY + 150, depth + 1);
            i++;
        }
    }

    // remove old goal model (@param: goals) before slicing is done.
    public static void removeGoalModel(Set<Goal> goals) {
        for (Goal goal : goals) {
            g.removeVertex(goal.name);
            for (String subGoal : goal.subGoals) {
                if (subGoal.equals("-"))
                    continue;
                g.removeEdge(goal.name, subGoal);
            }
        }
    }

    // open file
    // type 1: open CSV file - goal model
    // type 2: open txt file - slicing criterion
    public static Set<Goal> openWindow(int type, JFrame frame, JTextArea console, JTextField textField)
            throws IOException {
        FileDialog fileDialog = null;
        if (type == 1) {
            fileDialog = new FileDialog(frame, "Open Goal Model", FileDialog.LOAD);
            fileDialog.setFile("*.csv");
            fileDialog.setVisible(true);
            File goalModel[] = fileDialog.getFiles();
            if (goalModel.length != 0) {
                GoalModelParser gmp = new GoalModelParser();
                gmp.parseGoalModel(goalModel[0]);
                consolePrint(console, gmp.getGoalLog());
                textField.setText(goalModel[0].getAbsolutePath());
                return gmp.getGoals();
            }

        } else if (type == 2) {
            fileDialog = new FileDialog(frame, "Open Slicing Criterion", FileDialog.LOAD);
            fileDialog.setFile("*.txt");
            fileDialog.setVisible(true);

            File criterion[] = fileDialog.getFiles();
            if (criterion.length != 0) {
                CriterionParser cp = new CriterionParser();
                textField.setText(criterion[0].getAbsolutePath());
                return cp.parseCriterion(criterion[0]);
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        // create a visualization using JGraph, via an adapter
        jgAdapter = new JGraphModelAdapter<>(g);

        JGraph jgraph = new JGraph(jgAdapter);

        adjustDisplaySettings(jgraph);
        getContentPane().add(jgraph);
        resize(DEFAULT_SIZE);
    }

    private void adjustDisplaySettings(JGraph jg) {
        jg.setPreferredSize(DEFAULT_SIZE);

        Color c = DEFAULT_BG_COLOR;
        String colorStr = null;

        try {
            colorStr = getParameter("bgcolor");
        } catch (Exception e) {
        }

        if (colorStr != null) {
            c = Color.decode(colorStr);
        }

        jg.setBackground(c);
    }

    @SuppressWarnings("unchecked")
    private static void positionVertexAt(Object vertex, int x, int y) {
        DefaultGraphCell cell = jgAdapter.getVertexCell(vertex);
        AttributeMap attr = cell.getAttributes();
        AttributeMap map = new AttributeMap();

        GraphConstants.setBounds(map, new Rectangle2D.Double(50, 50, 90, 30));
        GraphConstants.setBorder(map, BorderFactory.createRaisedBevelBorder());
        GraphConstants.setBackground(map, Color.BLUE);
        GraphConstants.setForeground(map, Color.white);
        GraphConstants.setFont(map, GraphConstants.DEFAULTFONT.deriveFont(Font.BOLD, 12));
        GraphConstants.setOpaque(map, true);

        Rectangle2D bounds = GraphConstants.getBounds(map);

        Rectangle2D newBounds = new Rectangle2D.Double(x, y, bounds.getWidth(), bounds.getHeight());

        GraphConstants.setBounds(map, newBounds);
        GraphConstants.setBounds(attr, newBounds);

        AttributeMap cellAttr = new AttributeMap();

        if (findGoalByName((String) vertex).isSliced)
            cellAttr.put(cell, map);
        else
            cellAttr.put(cell, attr);

        jgAdapter.edit(cellAttr, null, null, null);
    }

    // print text (@param: text) at console (@param: console)
    public static void consolePrint(JTextArea console, String text) {
        console.append(text);
        console.setCaretPosition(console.getDocument().getLength());
        console.requestFocus();
    }
}
