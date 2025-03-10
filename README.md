# **Cyber Forensics: Network Detection & Investigation Tool**

## 🔍 **Introduction**
Cybersecurity threats are on the rise, and network monitoring is crucial for detecting and preventing malicious activity. This project is a **Cyber-Forensics AI-powered tool** that monitors live network traffic, detects anomalies using **Machine Learning**, and provides **AI-driven forensic analysis** of potential threats.

This tool is designed to help cybersecurity professionals, network administrators, and security enthusiasts analyze network activity in real-time. It features a **Java-based GUI** for visualization and a **Python-powered backend** for packet sniffing and threat analysis.

---
## 🛠️ **What Happens in the Project?**
1. **Live Packet Sniffing**: Captures network packets using **Scapy**.
2. **Machine Learning Classification**: Analyzes packets and classifies them as **benign or malicious**.
3. **Threat Investigation with AI**: Uses **OpenAI GPT-4** to generate an in-depth forensic report.
4. **Real-Time Java GUI**: Displays live network activity and allows users to analyze threats with a single click.

This seamless integration of **packet analysis, AI-based threat detection, and an interactive GUI** makes it a powerful network forensics tool.

---
## 🚀 **Tech Stack Used**
- **Python** (Backend & Machine Learning)
  - Flask (API for communication with Java GUI)
  - Scapy (Network packet sniffing)
  - Scikit-learn (Machine Learning for anomaly detection)
  - OpenAI API (For AI-driven threat analysis)
- **Java** (Frontend GUI)
  - Swing (UI components for packet display & user interaction)
  - HTTP Requests (For fetching data from Python backend)
- **Other Tools**
  - Pickle (For storing ML models)
  - SQLite (For future data storage & logging capabilities)

---
## 🏗️ **How to Run the Project**

### **1️⃣ Install Dependencies**
Make sure you have **Python 3.8+** and **Java 8+** installed.

#### **📌 Install Python Dependencies**
Run the following command inside the project directory:
```sh
pip install -r requirements.txt
```

#### **📌 Install Java Dependencies**
No additional dependencies are required for Java, as we use built-in **Swing** components.

---
### **2️⃣ Run the Python Backend**
Open a terminal, navigate to the **backend** folder, and start the packet sniffer:
```sh
python backend/sniffer.py
```
This will start the **Flask API** and begin capturing network packets.

Then, start the AI-based threat analysis module:
```sh
python backend/threat_analysis.py
```
This enables **GPT-4-powered** forensic investigation.

---
### **3️⃣ Run the Java GUI**
Open a terminal, navigate to the **frontend** folder, and compile the Java program:
```sh
javac frontend/CyberForensicsGUI.java
```
Then, run the GUI:
```sh
java frontend.CyberForensicsGUI
```
The GUI will now display **real-time packet monitoring** and allow you to analyze threats.

---
## 📌 **Conclusion**
This project provides a **powerful, AI-enhanced cybersecurity tool** for **network traffic analysis and forensics**. Whether you're a cybersecurity student, network engineer, or security enthusiast, this tool will help you understand **how to detect, analyze, and mitigate network threats** in real-time.

🔹 **Future Improvements:**
- **WebSocket integration** for instant updates 🔄
- **Dark mode & better UI enhancements** 🎨
- **Threat logging & reporting system** 📝

Feel free to contribute, improve, or customize this project! 🚀

