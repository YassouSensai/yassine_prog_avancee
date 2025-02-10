import numpy as np
import matplotlib.pyplot as plt

# Fonction pour lire les données à partir d'un fichier
def read_data(file_path):
    with open(file_path, 'r') as file:
        data = [line.strip().replace(',', '.') for line in file if line.strip()]
    return np.array([list(map(float, line.split())) for line in data])

# Fonction pour calculer le speedup
def calculate_speedup(data, total_counts):
    speedups = {}
    for total_count in total_counts:
        filtered_data = data[data[:, 1] == total_count]  # Filtrer par taille de problème
        temps_execution = filtered_data[:, 3]  # Temps d'exécution
        nombre_process = filtered_data[:, 2]   # Nombre de processus

        unique_processes = np.unique(nombre_process)
        T1 = np.mean(temps_execution[nombre_process == 1])  # Temps pour 1 processus
        Tp = np.array([np.mean(temps_execution[nombre_process == p]) for p in unique_processes])
        Sp = T1 / Tp  # Speedup

        speedups[total_count] = (unique_processes, Sp)
    return speedups

# Fonction pour tracer le speedup
def plot_speedup(speedups):
    plt.figure(figsize=(10, 6))
    for total_count, (nombre_process, speedup) in speedups.items():
        plt.plot(nombre_process, speedup, marker='o', linestyle='-', label=f'Scalabilité forte (N={total_count})')

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
    file_path = "./results.txt"  # Assure-toi que le chemin est correct
    total_counts = [16_000_000, 160_000_000, 1_600_000_000]  # Tailles de problèmes
    data = read_data(file_path)
    speedups = calculate_speedup(data, total_counts)
    plot_speedup(speedups)
