import openai
openai.api_key = "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"

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
