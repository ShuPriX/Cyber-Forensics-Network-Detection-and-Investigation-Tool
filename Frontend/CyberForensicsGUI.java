import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class CyberForensicsGUI {
    private static JTextArea textArea;
    private static Socket socket;
    private static BufferedReader in;
    private static boolean isConnected = false;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Cyber Forensics AI - Network Monitor");
        frame.setSize(1080, 920); // Set the resolution to 1080p
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the font to Cascadia Code
        Font font = new Font("Cascadia Code", Font.PLAIN, 12);
        UIManager.put("Button.font", font);
        UIManager.put("TextArea.font", font);

        // Create a custom titled border with centered title
        Border border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE, 2), "NetHawk2");
        TitledBorder titledBorder = (TitledBorder) border;
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        titledBorder.setTitlePosition(TitledBorder.TOP);

        JPanel panel = new JPanel();
        panel.setBorder(border);
        panel.setLayout(new BorderLayout());
        frame.add(panel);

        textArea = new JTextArea();
        textArea.setEditable(false);
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton connectButton = new JButton("Connect");
        connectButton.setBackground(Color.GREEN);
        connectButton.setForeground(Color.WHITE);
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
        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isConnected) {
                    disconnectFromPythonServer();
                }
            }
        });
        buttonPanel.add(disconnectButton);

        JButton analyzeButton = new JButton("Analyze Threat");
        analyzeButton.setBackground(Color.ORANGE);
        analyzeButton.setForeground(Color.WHITE);
        analyzeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                analyzeThreat();
            }
        });
        buttonPanel.add(analyzeButton);

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

    private static void analyzeThreat() {
        String threatData = "Example threat data"; // Replace with actual threat data
        try {
            URL url = new URL("http://localhost:5001/analyze_threat");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            String jsonInputString = "{\"threat\": \"" + threatData + "\"}";
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int code = connection.getResponseCode();
            if (code == 200) { // Success
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Display the analysis report in the text area
                textArea.append("Threat Analysis Report:\n" + response.toString() + "\n");
            } else {
                textArea.append("Failed to analyze threat. HTTP response code: " + code + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            textArea.append("Error analyzing threat: " + e.getMessage() + "\n");
        }
    }
}