import numpy as np
import matplotlib.pyplot as plt

# Fonction pour lire les données à partir d'un fichier
def read_data(file_path):
    data = []
    with open(file_path, 'r') as file:
        for line in file:
            # Remplacer les virgules par des points et diviser la ligne en éléments
            cleaned_line = line.strip().replace(',', '.')
            elements = cleaned_line.split()

            # Vérifier que la ligne contient exactement 4 éléments
            if len(elements) == 4:
                # Convertir les éléments en nombres flottants
                error = float(elements[0])
                total_count = int(elements[1])
                num_workers = int(elements[2])
                execution_time = float(elements[3])

                # Ignorer les lignes avec un temps d'exécution nul ou négatif
                if execution_time > 0:
                    data.append([error, total_count, num_workers, execution_time])

    # Convertir les données en tableau NumPy
    return np.array(data)

# Fonction pour calculer le speedup de manière cumulative
def calculate_speedup(data, ntot):
    # Extraire les colonnes
    temps_execution = data[:, 3]  # temps_ms
    nombre_process = data[:, 2]    # nombre_process

    # Trier les données par nombre de processus (au cas où elles ne le seraient pas)
    sorted_indices = np.argsort(nombre_process)
    temps_execution = temps_execution[sorted_indices]
    nombre_process = nombre_process[sorted_indices]

    # Calculer la somme des temps pour chaque nombre de processus
    unique_processes = np.unique(nombre_process)
    sum_temps = []

    for p in unique_processes:
        sum_temps.append(np.sum(temps_execution[nombre_process == p]))

    # Calculer le speedup
    speedup = []
    for i in range(len(unique_processes)):
        if i == 0:
            # Premier point : speedup = 1
            speedup.append(sum_temps[i] / sum_temps[i])
        else:
            # Speedup = somme des temps pour 1 processus / somme des temps pour p processus
            speedup.append(sum_temps[0] / sum_temps[i])

    return speedup, unique_processes

# Fonction pour tracer le graphique
def plot_speedup(speedup_data, nombre_process_data, ntot_values):
    plt.figure(figsize=(10, 6))

    for speedup, nombre_process, ntot in zip(speedup_data, nombre_process_data, ntot_values):
        plt.plot(nombre_process, speedup, marker='o', label=f'ntot = {int(ntot)}')

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

    plt.savefig('./speedup.png')
    plt.show()

# Main
if __name__ == "__main__":
    file_path = './results.txt'  # Remplacez par le chemin de votre fichier

    # Lire les données
    data = read_data(file_path)
    ntot_values = np.unique(data[:, 1])  # Extraire les valeurs uniques de ntot

    speedup_data = []
    nombre_process_data = []

    for ntot in ntot_values:
        # Filtrer les données pour le nombre total de fléchettes correspondant
        filtered_data = data[data[:, 1] == ntot]

        # Calculer le speedup pour les données filtrées
        speedup, nombre_process = calculate_speedup(filtered_data, ntot)

        # Ajouter les résultats à la liste
        speedup_data.append(speedup)
        nombre_process_data.append(nombre_process)

    # Tracer les courbes pour chaque nombre total de fléchettes
    plot_speedup(speedup_data, nombre_process_data, ntot_values)