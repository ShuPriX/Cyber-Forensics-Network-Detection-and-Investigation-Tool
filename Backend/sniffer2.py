from scapy.all import sniff, IP, TCP, UDP, ICMP
import socket
import json
import threading

# Protocol mapping
protocols = {
    1: "ICMP",
    6: "TCP",
    17: "UDP",
    89: "OSPF",
    132: "SCTP"
}

# Get the local machine's IP address
local_ip = socket.gethostbyname(socket.gethostname())

# Create a socket to send data to the Java application
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.bind(('localhost', 5000))
server_socket.listen(1)
print("Waiting for Java application to connect...")

def packet_callback(packet, conn):
    try:
        if IP in packet:
            proto_number = packet[IP].proto
            proto_name = protocols.get(proto_number, f"Unknown Protocol ({proto_number})")
            direction = "Incoming" if packet[IP].dst == local_ip else "Outgoing"
            packet_info = {
                "src": packet[IP].src,
                "dst": packet[IP].dst,
                "proto": proto_name,
                "size": len(packet),
                "direction": direction
            }

            # Additional detailed analysis
            if TCP in packet:
                packet_info["tcp_flags"] = str(packet[TCP].flags)
            elif UDP in packet:
                packet_info["udp_sport"] = packet[UDP].sport
                packet_info["udp_dport"] = packet[UDP].dport
            elif ICMP in packet:
                packet_info["icmp_type"] = packet[ICMP].type
                packet_info["icmp_code"] = packet[ICMP].code

            # Display packet info in CLI
            print("\n===== Packet Captured =====")
            for key, value in packet_info.items():
                print(f"{key}: {value}")
            print("===========================")

            # Send the packet info to the Java application
            conn.sendall((json.dumps(packet_info) + "\n").encode('utf-8'))
    except Exception as e:
        print(f"Error processing packet: {e}")

def start_sniffing(conn):
    sniff(prn=lambda packet: packet_callback(packet, conn), store=False, iface="wlan0")

def handle_client(conn, addr):
    print(f"Connected by {addr}")
    start_sniffing(conn)
    conn.close()
    print(f"Disconnected by {addr}")

def accept_connections():
    while True:
        conn, addr = server_socket.accept()
        client_thread = threading.Thread(target=handle_client, args=(conn, addr))
        client_thread.start()

if __name__ == "__main__":
    accept_connections()