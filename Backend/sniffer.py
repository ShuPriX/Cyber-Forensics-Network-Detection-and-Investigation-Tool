from scapy.all import sniff, IP
from flask import Flask, jsonify

app = Flask(__name__)
captured_packets = []

def packet_callback(packet):
    if IP in packet:
        packet_info = {
            "src": packet[IP].src,
            "dst": packet[IP].dst,
            "proto": packet[IP].proto,
            "size": len(packet),
        }
        captured_packets.append(packet_info)

@app.route('/packets', methods=['GET'])
def get_packets():
    return jsonify(captured_packets[-10:])  # Send last 10 packets

if __name__ == "__main__":
    sniff(prn=packet_callback, store=False)
    app.run(port=5000)
