from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import train_test_split
import pandas as pd
import pickle

# Load dataset
df = pd.read_csv("network_traffic.csv")
X = df[['src_ip', 'dst_ip', 'packet_size', 'protocol']]
y = df['attack_label']

# Train model
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)
model = RandomForestClassifier()
model.fit(X_train, y_train)

# Save model
pickle.dump(model, open("cyber_model.pkl", "wb"))
