import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkMonitor {
    private static JTextArea textArea;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Cyber Forensics AI - Network Monitor");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = new JTextArea();
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);

        Timer timer = new Timer(3000, e -> fetchPackets());
        timer.start();

        frame.setVisible(true);
    }

    private static void fetchPackets() {
        try {
            URL url = new URL("http://127.0.0.1:5000/packets");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine();
            in.close();

            textArea.setText(response);
        } catch (Exception e) {
            textArea.setText("Error fetching data.");
        }
    }
}
