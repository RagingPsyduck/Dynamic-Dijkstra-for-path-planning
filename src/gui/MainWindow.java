package gui;

import algo.DijkstraAlgorithm;
import models.Edge;
import models.Graph;
import models.Node;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainWindow extends JPanel {

    private Graph graph;
    private GraphPanel graphPanel;

    public MainWindow() {
        super.setLayout(new BorderLayout());
        setGraphPanel();
    }

    private void setGraphPanel() {
        graph = new Graph();
        graphPanel = new GraphPanel(graph);
        graphPanel.setPreferredSize(new Dimension(1200, 2000));

        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(graphPanel);
        scroll.setPreferredSize(new Dimension(1200, 2000));
        scroll.getViewport().setViewPosition(new Point(400, 0));
        add(scroll, BorderLayout.CENTER);
        setButtons();

        initializeMap();
    }

    private void initializeMap() {
        HashMap<String, Node> map = new HashMap<>();
        try {
            BufferedReader nodeReader = new BufferedReader(new FileReader("node.csv"));
            nodeReader.readLine();
            String line;
            while ((line = nodeReader.readLine()) != null) {
                String item[] = line.split(",");
                String nodeName = item[0];
                int xValue = Integer.parseInt(item[1]);
                int yValue = Integer.parseInt(item[2]);
                map.put(nodeName, new Node(new Point(xValue, yValue)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int size = map.size();
        for (int i = 1; i <= size; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append("node").append(i);
            graph.addNode(map.get(sb.toString()));
        }

        try {
            BufferedReader roadReader = new BufferedReader(new FileReader("road.csv"));
            roadReader.readLine();
            String line;
            while ((line = roadReader.readLine()) != null) {
                String roadInfo[] = line.split(",");
                String nodeA = roadInfo[0];
                String nodeB = roadInfo[1];
                Node temp1 = map.get(nodeA);
                Node temp2 = map.get(nodeB);
                Edge street = new Edge(temp1, temp2);
                street.setName(roadInfo[2]);
                int cost = Integer.parseInt(roadInfo[3]);
                street.setWeight(cost);
                graph.addEdge(street);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setButtons() {
        JButton run = new JButton();
        setupIcon(run, "run");
        JButton reset = new JButton();
        setupIcon(reset, "reset");
        final JButton info = new JButton();
        setupIcon(info, "info");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(DrawUtils.parseColor("#DDDDDD"));
        buttonPanel.add(reset);
        buttonPanel.add(run);
        buttonPanel.add(info);

        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                graphPanel.reset();
            }
        });

        info.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "Click on empty space to create new node\n" +
                                "Drag from node to node to create an edge\n" +
                                "Click on edges to set the weight\n\n" +
                                "Combinations:\n" +
                                "Shift + Left Click       :    Set node as source\n" +
                                "Shift + Right Click     :    Set node as destination\n" +
                                "Ctrl  + Drag               :    Reposition Node\n" +
                                "Ctrl  + Click                :    Get Path of Node\n" +
                                "Ctrl  + Shift + Click   :    Delete Node/Edge\n");
            }
        });

        run.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Node> path;
                try {
                    DijkstraAlgorithm dijkstraAlgorithm = new DijkstraAlgorithm(graph);
                    dijkstraAlgorithm.run();
                    path = dijkstraAlgorithm.getDestinationPath();
                    graphPanel.setPath(path);
                } catch (IllegalStateException ise) {
                    JOptionPane.showMessageDialog(null, ise.getMessage());
                }
            }
        });

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupIcon(JButton button, String img) {
        try {
            Image icon = ImageIO.read(getClass().getResource("/resources/" + img + ".png"));
            ImageIcon imageIcon = new ImageIcon(icon);
            button.setIcon(imageIcon);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setContentAreaFilled(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
