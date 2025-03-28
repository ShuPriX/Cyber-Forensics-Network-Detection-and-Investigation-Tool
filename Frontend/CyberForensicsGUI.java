import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class CyberForensicsGUI {
    private static JTextArea textArea;
    private static Socket socket;
    private static BufferedReader in;
    private static boolean isConnected = false;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Cyber Forensics AI - Network Monitor");
        frame.setSize(1240, 720); // Set the resolution to 1080p
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the font to Cascadia Code
        Font font = new Font("Cascadia Code", Font.PLAIN, 18);
        UIManager.put("Button.font", font);
        UIManager.put("TextArea.font", font);

        // Create a custom titled border with centered title
        Border border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 5), "NetHawk2");
        TitledBorder titledBorder = (TitledBorder) border;
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        titledBorder.setTitlePosition(TitledBorder.TOP);

        // Set the title font size to 20
        Font titleFont = new Font("Cascadia Code", Font.PLAIN, 24);
        titledBorder.setTitleFont(titleFont);

        JPanel panel = new JPanel();
        panel.setBorder(border);
        panel.setLayout(new BorderLayout());
        frame.add(panel);

        textArea = new JTextArea();
        textArea.setEditable(false);
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // Add some spacing between buttons

        JButton connectButton = new JButton("Connect");
        connectButton.setBackground(Color.GREEN);
        connectButton.setForeground(Color.WHITE);
        connectButton.setFont(new Font("Cascadia Code", Font.PLAIN, 16)); // Increase font size
        connectButton.setBorder(new RoundedBorder(Color.GREEN, 20)); // Rounded border
        connectButton.setPreferredSize(new Dimension(150, 32)); // Increase button size
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isConnected) {
                    connectToPythonServer();
                }
            }
        });
        buttonPanel.add(connectButton);

        JButton disconnectButton = new JButton("Disconnect");
        disconnectButton.setBackground(Color.RED);
        disconnectButton.setForeground(Color.WHITE);
        disconnectButton.setFont(new Font("Cascadia Code", Font.PLAIN, 16)); // Increase font size
        disconnectButton.setBorder(new RoundedBorder(Color.RED, 20)); // Rounded border
        disconnectButton.setPreferredSize(new Dimension(150, 32)); // Increase button size
        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isConnected) {
                    disconnectFromPythonServer();
                }
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
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String line;
                        while ((line = in.readLine()) != null) {
                            textArea.append(line + "\n");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        isConnected = false;
                    }
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
            textArea.append("Disconnected from the server.\n");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Custom Rounded Border Class
    static class RoundedBorder extends AbstractBorder {
        private int radius;
        private Color color;

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