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

public class MainWindow extends JPanel {

    private Graph graph;
    private GraphPanel graphPanel;

    public MainWindow(){
        super.setLayout(new BorderLayout());
        setGraphPanel();
    }

    private void setGraphPanel(){
        graph = new Graph();
        graphPanel = new GraphPanel(graph);
        graphPanel.setPreferredSize(new Dimension(900, 400));

        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(graphPanel);
        scroll.setPreferredSize(new Dimension(750, 500));
        scroll.getViewport().setViewPosition(new Point(400, 0));
        add(scroll, BorderLayout.CENTER);
        setButtons();

        initializeMap();
    }

    private void initializeMap(){


        Node node1 = new Node(new Point(100,200));
        Node node2 = new Node(new Point(100,500));
        Node node3 = new Node(new Point(200,200));
        Node node4 = new Node(new Point(200,500));

        Node node5 = new Node(new Point(700,200));
        Node node6 = new Node(new Point(800,200));


        Node node7 = new Node(new Point(300,300));
        Node node8 = new Node(new Point(400,300));
        Node node9 = new Node(new Point(300,400));
        Node node10 = new Node(new Point(400,400));


        graph.addNode(node1);
        graph.addNode(node2);
        graph.addNode(node3);
        graph.addNode(node4);
        graph.addNode(node5);
        graph.addNode(node6);

        graph.addNode(node7);
        graph.addNode(node8);
        graph.addNode(node9);
        graph.addNode(node10);


        Edge street1 = new Edge(node1,node2);
        street1.setName("Coventry Rd");
        graph.addEdge(street1);

        Edge street2 = new Edge(node1,node3);
        street2.setName("Larchmere Blvd");
        graph.addEdge(street2);

        Edge street3 = new Edge(node3,node4);
        street3.setName("Edicott Rd");
        graph.addEdge(street3);


        Edge street4 = new Edge(node2,node4);
        street4.setName("Shaker Blvd");
        graph.addEdge(street4);




    }


    private void setButtons(){
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
                DijkstraAlgorithm dijkstraAlgorithm = new DijkstraAlgorithm(graph);
                try{
                    dijkstraAlgorithm.run();
                    graphPanel.setPath(dijkstraAlgorithm.getDestinationPath());
                } catch (IllegalStateException ise){
                    JOptionPane.showMessageDialog(null, ise.getMessage());
                }
            }
        });

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupIcon(JButton button, String img){
        try {
            Image icon = ImageIO.read(getClass().getResource(
                    "/resources/" + img + ".png"));
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
