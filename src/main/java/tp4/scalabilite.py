import pandas as pd
import matplotlib.pyplot as plt

# Charger les fichiers CSV
results = pd.read_csv("./results.csv")
processus = pd.read_csv("./processus.csv")

# Calculer T1 : Moyenne des temps pour NbProc = 1
T1 = results[results["NbProc"] == 1]["Time"].mean()

# Calculer Tp : Somme des temps pour chaque NbProc dans processus.csv
Tp = processus.groupby("NumWorkers")["Time"].sum()

# Calculer le speedup S = T1 / Tp
speedup = T1 / Tp

# Tracer le graphique
plt.figure(figsize=(8, 5))
plt.plot(speedup.index, speedup.values, marker='o', linestyle='-', color='b', label="Speedup")

print(speedup.index, speedup.values)

# Labels et titre
plt.xlabel("Nombre de Processus (P)")
plt.ylabel("Accélération (Speedup)")
plt.title("Évaluation de la Scalabilité")
plt.legend()
plt.grid()

# Afficher le graphique
plt.show()
