import numpy as np
import matplotlib.pyplot as plt

# Fonction pour lire les données à partir d'un fichier
def read_data(file_path):
    with open(file_path, 'r') as file:
        # Lire les lignes et remplacer les virgules par des points
        data = [line.strip().replace(',', '.') for line in file if line.strip()]
    # Convertir les données en tableau NumPy
    return np.array([list(map(float, line.split())) for line in data])

# Fonction pour calculer le speedup
def calculate_speedup(data, ntot):
    # Extraire les colonnes
    temps_execution = data[:, 3]  # temps_ms
    nombre_process = data[:, 2]    # nombre_process

    # Calculer T1 (moyenne des temps d'exécution pour 1 processus)
    T1 = np.median(temps_execution[nombre_process == 1])

    # Calculer Tp (moyenne des temps d'exécution pour chaque nombre de processus)
    unique_processes = np.unique(nombre_process)
    Tp = []

    for p in unique_processes:
        Tp.append(np.median(temps_execution[nombre_process == p]))

    # Calculer Sp (speedup pour chaque nombre de processus)
    Sp = T1 / np.array(Tp)

    return Sp, unique_processes

# Fonction pour tracer le graphique
def plot_speedup(speedup_data, nombre_process_data, ntot_values):
    plt.figure(figsize=(10, 6))

    for speedup, nombre_process, ntot in zip(speedup_data, nombre_process_data, ntot_values):
        plt.plot(nombre_process, speedup, marker='o', label=f'ntot = {ntot}')

    plt.title('Speedup en fonction du nombre de processus')
    plt.xlabel('Nombre de processus')
    plt.ylabel('Speedup')
    plt.axhline(1, color='red', linestyle='--', label='Speedup = 1')

    # Ligne diagonale pour représenter une scalabilité idéale
    max_process = max(max(nombre_process) for nombre_process in nombre_process_data)
    plt.plot([1, max_process], [1, max_process], color='blue', linestyle='--', label='Scalabilité idéale')

    plt.legend()
    plt.grid()

    plt.xlim(left=0)
    plt.ylim(bottom=0)  # Pour ne pas avoir de valeurs négatives sur l'axe des ordonnées
    plt.gca().set_aspect('equal', adjustable='box')

    # Afficher toutes les unités des axes
    x_ticks = np.arange(0, max_process + 1, 1)  # Ajustez l'intervalle selon vos besoins
    y_ticks = np.arange(0, max(max(speedup) for speedup in speedup_data) + 1, 1)  # Ajustez l'intervalle selon vos besoins
    plt.xticks(x_ticks)
    plt.yticks(y_ticks)

    plt.show()

# Main
if __name__ == "__main__":
    file_path = './results.txt'  # Remplacez par le chemin de votre fichier
    # ntot_values = [12000, 12000000, 120000000]  # Nombre total de fléchettes

    # Lire toutes les données
    data = read_data(file_path)
#     print(data)
    ntot_values = np.unique(data[:, 1])

    # Initialiser des listes pour stocker les résultats
    speedup_data = []
    nombre_process_data = []

    # Calculer le speedup pour chaque ensemble de données
    for ntot in ntot_values:
        # Filtrer les données pour le nombre total de fléchettes correspondant
        filtered_data = data[data[:, 1] == ntot]
        # print(filtered_data)

        # Calculer le speedup pour les données filtrées
        speedup, nombre_process = calculate_speedup(filtered_data, ntot)
        # print(speedup, nombre_process)

        # Ajouter les résultats à la liste
        speedup_data.append(speedup)
        nombre_process_data.append(nombre_process)

    # Tracer les courbes pour chaque nombre total de fléchettes
    plot_speedup(speedup_data, nombre_process_data, ntot_values)