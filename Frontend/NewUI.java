import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import org.json.JSONObject;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class NewUI2 {
    private static JTable packetTable;
    private static DefaultTableModel tableModel;
    private static Socket socket;
    private static BufferedReader in;
    private static boolean isConnected = false;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Cyber Forensics AI - Network Monitor");
        frame.setSize(1240, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the font
        Font font = new Font("Cascadia Code", Font.PLAIN, 18);
        UIManager.put("Button.font", font);
        UIManager.put("Table.font", font);

        // Custom titled border with centered title
        Border border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 5), "NetHawk2");
        TitledBorder titledBorder = (TitledBorder) border;
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        titledBorder.setTitlePosition(TitledBorder.TOP);
        titledBorder.setTitleFont(new Font("Cascadia Code", Font.PLAIN, 24));

        JPanel panel = new JPanel();
        panel.setBorder(border);
        panel.setLayout(new BorderLayout());
        frame.add(panel);

        // Create table model and table
        String[] columnNames = {"Source IP", "Destination IP", "Protocol", "Size", "Direction", "Details"};
        tableModel = new DefaultTableModel(columnNames, 0);
        packetTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(packetTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton connectButton = new JButton("Connect");
        styleButton(connectButton, Color.GREEN);
        connectButton.addActionListener(e -> {
            if (!isConnected) {
                connectToPythonServer();
            }
        });
        buttonPanel.add(connectButton);

        JButton disconnectButton = new JButton("Disconnect");
        styleButton(disconnectButton, Color.RED);
        disconnectButton.addActionListener(e -> {
            if (isConnected) {
                disconnectFromPythonServer();
            }
        });
        buttonPanel.add(disconnectButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private static void connectToPythonServer() {
        try {
            socket = new Socket("localhost", 5000);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            isConnected = true;
            new Thread(() -> {
                try {
                    String line;
                    while ((line = in.readLine()) != null) {
                        JSONObject packetData = new JSONObject(line);
                        String src = packetData.getString("src");
                        String dst = packetData.getString("dst");
                        String proto = packetData.getString("proto");
                        int size = packetData.getInt("size");
                        String direction = packetData.getString("direction");

                        // Extract extra details
                        String details = "";
                        if (packetData.has("tcp_flags")) {
                            details = "TCP Flags: " + packetData.getString("tcp_flags");
                        } else if (packetData.has("udp_sport") && packetData.has("udp_dport")) {
                            details = "UDP Ports: " + packetData.getInt("udp_sport") + " → " + packetData.getInt("udp_dport");
                        } else if (packetData.has("icmp_type") && packetData.has("icmp_code")) {
                            details = "ICMP Type: " + packetData.getInt("icmp_type") + ", Code: " + packetData.getInt("icmp_code");
                        }

                        // Add row to table
                        tableModel.addRow(new Object[]{src, dst, proto, size, direction, details});
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    isConnected = false;
                }
            }).start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void disconnectFromPythonServer() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            isConnected = false;
            tableModel.setRowCount(0); // Clear table on disconnect
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Cascadia Code", Font.PLAIN, 16));
        button.setBorder(new RoundedBorder(color, 20));
        button.setPreferredSize(new Dimension(150, 32));
    }

    // Custom Rounded Border Class
    static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color color;

        public RoundedBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(color);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.draw(new RoundRectangle2D.Float(x, y, width - 1, height - 1, radius, radius));
            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius, radius, radius, radius);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.top = insets.right = insets.bottom = radius;
            return insets;
        }

        @Override
        public boolean isBorderOpaque() {
            return true;
        }
    }
}
