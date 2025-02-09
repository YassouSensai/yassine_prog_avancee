import numpy as np
import matplotlib.pyplot as plt

# Fonction pour lire les données à partir d'un fichier
def read_data(file_path):
    with open(file_path, 'r') as file:
        data = [line.strip().replace(',', '.') for line in file if line.strip()]
    return np.array([list(map(float, line.split())) for line in data])

# Fonction pour calculer le speedup
def calculate_speedup(data):
    temps_execution = data[:, 3]  # Temps d'exécution
    nombre_process = data[:, 2]   # Nombre de processus

    unique_processes = np.unique(nombre_process)
    T1 = np.mean(temps_execution[nombre_process == 1])  # Temps pour 1 processus
    Tp = np.array([np.mean(temps_execution[nombre_process == p]) for p in unique_processes])
    Sp = T1 / Tp  # Speedup

    return unique_processes, Sp

# Fonction pour tracer le graphique
def plot_speedup(nombre_process, speedup):
    plt.figure(figsize=(10, 6))
    plt.plot(nombre_process, speedup, marker='o', linestyle='-', label='Speedup mesuré')
    plt.plot(nombre_process, nombre_process, linestyle='--', color='blue', label='Speedup idéal')
    plt.axhline(1, color='red', linestyle='--', label='Speedup = 1')

    plt.title('Scalabilité Forte - Speedup en fonction du nombre de processus')
    plt.xlabel('Nombre de Processus')
    plt.ylabel('Speedup')
    plt.legend()
    plt.grid()
    plt.xlim(left=0)
    plt.ylim(bottom=0)
    plt.show()

# Exécution principale
if __name__ == "__main__":
    file_path = './results.txt'  # Remplace par ton fichier de résultats
    data = read_data(file_path)
    nombre_process, speedup = calculate_speedup(data)
    plot_speedup(nombre_process, speedup)
