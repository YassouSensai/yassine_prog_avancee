import numpy as np
import pandas as pd
import matplotlib.pyplot as plt

def read_data(file_path):
    """Lecture du fichier CSV contenant les résultats."""
    try:
        data = pd.read_csv(file_path)
        return data
    except Exception as e:
        print(f"Erreur lors de la lecture du fichier : {e}")
        return None

def calculate_speedup(data, use_median=True):
    """Calcul du speedup à partir des temps d'exécution et du nombre de processus."""
    if 'Time' not in data.columns or 'NbProc' not in data.columns:
        print("Colonnes 'Time' et 'NbProc' requises dans le fichier CSV.")
        return None, None

    unique_processes = np.sort(data['NbProc'].unique())

    if 1 not in unique_processes:
        print("Aucune donnée pour un seul processus. Impossible de calculer le speedup.")
        return None, None

    # Fonction d'agrégation (moyenne ou médiane)
    agg_func = np.median if use_median else np.mean

    # Calcul du temps de référence T1 (avec 1 processus)
    T1_values = data[data['NbProc'] == 1]['Time']
    if len(T1_values) == 0:
        print("Pas de temps d'exécution pour 1 processus.")
        return None, None

    T1 = agg_func(T1_values)

    # Calcul du temps pour chaque nombre de processus et des speedups associés
    Tp = np.array([agg_func(data[data['NbProc'] == p]['Time']) for p in unique_processes])
    Sp = T1 / Tp  # Speedup = T1 / Tp

    print(f"T1 ({'médian' if use_median else 'moyen'} avec 1 processus) : {T1:.5f} sec")
    print(f"Nombre de processus uniques : {unique_processes}")
    print(f"Temps {'médians' if use_median else 'moyens'} correspondants : {Tp}")
    print(f"Speedup calculé : {Sp}")

    return Sp, unique_processes

def plot_speedup(speedup_data, nombre_process_data):
    """Trace la courbe du speedup en fonction du nombre de processus."""
    if speedup_data is None or nombre_process_data is None:
        print("Données invalides, impossible de tracer le graphique.")
        return

    plt.figure(figsize=(10, 6))

    # Tracer le speedup mesuré
    plt.plot(nombre_process_data, speedup_data, marker='o', linestyle='-', color='green', label='Speedup mesuré')

    # Courbe de speedup idéal (Scalabilité parfaite)
    max_process = max(nombre_process_data)
    plt.plot([1, max_process], [1, max_process], linestyle='--', color='blue', label='Scalabilité idéale')

    # Ligne horizontale Speedup = 1
    plt.axhline(1, color='red', linestyle='--', label='Speedup = 1')

    plt.title('Speedup en fonction du nombre de processus')
    plt.xlabel('Nombre de processus')
    plt.ylabel('Speedup')

    plt.legend()
    plt.grid(True)

    plt.xlim(left=0)
    plt.ylim(bottom=0)

    x_ticks = np.arange(0, max_process + 1, 1)
    y_ticks = np.arange(0, max(speedup_data) + 1, 1)
    plt.xticks(x_ticks)
    plt.yticks(y_ticks)

    # Sauvegarde et affichage du graphique
    plt.savefig('./speedup.png', dpi=300)
    plt.show()

if __name__ == "__main__":
    file_path = './results.csv'

    data = read_data(file_path)
    if data is not None:
        speedup_data, nombre_process_data = calculate_speedup(data, use_median=False)  # False = utilisation de la moyenne
        plot_speedup(speedup_data, nombre_process_data)
