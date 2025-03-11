import openai
openai.api_key = ""

def generate_report(threat_data):
    prompt = f"Analyze the following network threat:\n{threat_data}"
    response = openai.ChatCompletion.create(
        model="gpt-4",
        messages=[{"role": "system", "content": "You are a cybersecurity expert."},
                  {"role": "user", "content": prompt}]
    )
    return response["choices"][0]["message"]["content"]

# Example Usage
threat = "DDoS attack detected from 192.168.1.15"
print(generate_report(threat))
