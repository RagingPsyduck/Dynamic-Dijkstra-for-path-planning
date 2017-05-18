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
import java.util.HashMap;

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
        graphPanel.setPreferredSize(new Dimension(1200, 2000));

        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(graphPanel);
        scroll.setPreferredSize(new Dimension(1200, 2000));
        scroll.getViewport().setViewPosition(new Point(400, 0));
        add(scroll, BorderLayout.CENTER);
        setButtons();

        initializeMap();
    }

    private void initializeMap(){
        HashMap<String,Node> map = new HashMap<>();
        try{
            BufferedReader nodeReader = new BufferedReader(new FileReader("node.csv"));
            nodeReader.readLine();
            String line;
            while((line=nodeReader.readLine())!=null){
                String item[] = line.split(",");
                String nodeName = item[0];
                int xValue = Integer.parseInt(item[1]);
                int yValue = Integer.parseInt(item[2]);
                map.put(nodeName,new Node(new Point(xValue,yValue)));
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }

        int size = map.size();
        for( int i = 1 ; i <= size ; i++){
            StringBuilder sb = new StringBuilder();
            sb.append("node").append(i);
            graph.addNode(map.get(sb.toString()));
        }

        try{
            BufferedReader roadReader = new BufferedReader(new FileReader("road.csv"));
            roadReader.readLine();
            String line;
            while((line=roadReader.readLine())!=null){
                String roadInfo[] = line.split(",");
                String nodeA = roadInfo[0];
                String nodeB = roadInfo[1];
                Node temp1 = map.get(nodeA);
                Node temp2 = map.get(nodeB);
                Edge street = new Edge(temp1,temp2);
                street.setName(roadInfo[2]);
                graph.addEdge(street);
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }



        /*
        Node node1 = new Node(new Point(100,200));
        Node node2 = new Node(new Point(100,500));
        Node node3 = new Node(new Point(200,200));
        Node node4 = new Node(new Point(200,500));
        Node node5 = new Node(new Point(300,200));
        Node node6 = new Node(new Point(300,500));


        Node node7 = new Node(new Point(450,200));
        Node node8 = new Node(new Point(450,500));

        Node node9 = new Node(new Point(575,350));
        Node node10 = new Node(new Point(700,500));


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

        Edge street5 = new Edge(node3,node5);
        graph.addEdge(street5);

        Edge street6 = new Edge(node4,node6);
        graph.addEdge(street6);


        Edge street7 = new Edge(node5,node6);
        street7.setName("Wicklow Rd");
        graph.addEdge(street7);


        Edge street8 = new Edge(node5,node7);
        graph.addEdge(street8);

        Edge street9 = new Edge(node6,node8);
        graph.addEdge(street9);

        Edge street10 = new Edge(node7,node8);
        street10.setName("Leighton Rd");
        graph.addEdge(street10);

        Edge street11 = new Edge(node7,node9);
        street11.setName("W park Rd");
        graph.addEdge(street11);

        Edge street12 = new Edge(node8,node9);
        street12.setName("Southington Rd");
        graph.addEdge(street12);

        Edge street13 = new Edge(node8,node10);
        graph.addEdge(street13);

        Edge street14 = new Edge(node9,node10);
        graph.addEdge(street14);
        */
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
