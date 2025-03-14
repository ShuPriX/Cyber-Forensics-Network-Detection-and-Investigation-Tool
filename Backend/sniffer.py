from scapy.all import sniff, IP
from flask import Flask, jsonify
from threading import Thread

app = Flask(__name__)
captured_packets = []

def packet_callback(packet):
    try:
        if IP in packet:
            packet_info = {
                "src": packet[IP].src,
                "dst": packet[IP].dst,
                "proto": packet[IP].proto,
                "size": len(packet),
            }
            captured_packets.append(packet_info)
    except Exception as e:
        print(f"Error processing packet: {e}")

@app.route('/packets', methods=['GET'])
def get_packets():
    return jsonify(captured_packets[-10:])  # Send last 10 packets

def start_sniffing():
    sniff(prn=packet_callback, store=False, iface="wlan0")

if __name__ == "__main__":
    # Start the packet sniffing in a separate thread
    sniff_thread = Thread(target=start_sniffing)
    sniff_thread.start()

    # Run the Flask app
    app.run(port=5000)