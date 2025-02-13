import numpy as np
import matplotlib.pyplot as plt
from six import print_


def read_data(file_path):
    with open(file_path, 'r') as file:
        data = [line.strip().replace(',', '.') for line in file if line.strip()]

    return np.array([list(map(float, line.split())) for line in data])

def calculate_speedup(data):
    temps_execution = data[:, 3]
    nombre_process = data[:, 2]

    T1 = np.median(temps_execution[nombre_process == 1])        # Calcule de T1

    unique_processes = np.unique(nombre_process)        # Calcule des Tp
    Tp = []

    for p in unique_processes:
        Tp.append(np.median(temps_execution[nombre_process == p]))

    Sp = T1 / np.array(Tp)    # Calcule de Sp

    return Sp, unique_processes

def plot_speedup(speedup_data, nombre_process_data, ntot_values):
    plt.figure(figsize=(10, 8))

    for speedup, nombre_process, ntot in zip(speedup_data, nombre_process_data, ntot_values):
        plt.plot(nombre_process, speedup, marker='o', label=f'ntot = {ntot}')

    plt.title('Evaluation de la scalabilité forte')
    plt.xlabel('Nombre de processus')
    plt.ylabel('Speedup')
    plt.axhline(1, color='red', linestyle='--', label='Speedup = 1')

    max_process = max(max(nombre_process) for nombre_process in nombre_process_data)
    plt.plot([1, max_process], [1, max_process], color='blue', linestyle='--', label='Scalabilité idéale')

    plt.legend()
    plt.grid()

    plt.xlim(left=0)
    plt.ylim(bottom=0)
    plt.gca().set_aspect('equal', adjustable='box')

    x_ticks = np.arange(0, max_process + 4, 1)
    y_ticks = np.arange(0, max(max(speedup) for speedup in speedup_data) + 4, 1)
    plt.xticks(x_ticks)
    plt.yticks(y_ticks)

    plt.show()


if __name__ == "__main__":
    file_path = './results_fort_M3.txt'  # Chemin du fichier

    data = read_data(file_path)

    filtered_data_1_core = data[data[:, 2] == 1]
    ntot_values = np.unique(filtered_data_1_core[:, 1])

    speedup_data = []
    nombre_process_data = []

    for ntot in ntot_values:
        filtered_data = data[data[:, 1] == ntot]

        speedup, nombre_process = calculate_speedup(filtered_data)

        speedup_data.append(speedup)
        nombre_process_data.append(nombre_process)

    plot_speedup(speedup_data, nombre_process_data, ntot_values)
