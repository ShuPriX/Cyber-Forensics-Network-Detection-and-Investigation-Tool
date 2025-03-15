import openai

# Initialize the OpenAI client
client = openai.Client(api_key="secret")  # Replace "secret" with your actual API key

def generate_report(threat_data):
    prompt = f"Analyze the following network threat:\n{threat_data}"
    
    response = client.chat.completions.create(  # Correct API usage
        model="o1-mini",
        messages=[
            {"role": "system", "content": "You are a cybersecurity expert."},
            {"role": "user", "content": prompt}
        ]
    )
    return response.choices[0].message.content

def analyze_threat(threat_data):
    report = generate_report(threat_data)
    return report

if __name__ == "__main__":
    # Example usage
    threat_data = "Example threat data"
    report = analyze_threat(threat_data)
    print(report)
