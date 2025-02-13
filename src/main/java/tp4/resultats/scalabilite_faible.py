import numpy as np
import matplotlib.pyplot as plt
from six import print_

def read_data(file_path):
    with open(file_path, 'r') as file:
        data = [line.strip().replace(',', '.') for line in file if line.strip()]

    return np.array([list(map(float, line.split())) for line in data])

def calculate_speedup(data):
    temps_execution = data[:, 3]        # temps_ms
    nombre_process = data[:, 2]         # nombre_process

    T1 = np.median(temps_execution[nombre_process == 1])        # Calcule de T1


    unique_processes = np.unique(nombre_process)        # Calcule des Tp
    Tp = []

    for p in unique_processes:
        Tp.append(np.median(temps_execution[nombre_process == p]))

    Sp = T1 / np.array(Tp)      # Calcule de Sp

    return Sp, unique_processes

def plot_speedup(speedup_data, nombre_process_data, ntot_values):
    plt.figure(figsize=(10, 8))

    for speedup, nombre_process, ntot in zip(speedup_data, nombre_process_data, ntot_values):
        plt.plot(nombre_process, speedup, marker='o', label=f'ntot = {ntot}')

    plt.title('Evaluation de la scalabilité faible')
    plt.xlabel('Nombre de processus')
    plt.ylabel('Speedup')
    plt.axhline(1, color='red', linestyle='--', label='Speedup = 1')

    plt.legend()
    plt.grid()

    plt.xlim(left=0)
    plt.ylim(bottom=0, top=2)
    plt.gca().set_aspect('equal', adjustable='box')

    plt.show()


if __name__ == "__main__":
    file_paths = [
        './results_faible_1200000.txt',
        './results_faible_12000000.txt',
        './results_faible_120000000.txt'
    ]

    speedup_data = []
    nombre_process_data = []
    ntot_values = []

    for file_path in file_paths:
        data = read_data(file_path)

        filtered_data_1_core = data[data[:, 2] == 1]
        ntot_value = np.unique(filtered_data_1_core[:, 1])[0]
        speedup, nombre_process = calculate_speedup(data)

        speedup_data.append(speedup)
        nombre_process_data.append(nombre_process)
        ntot_values.append(ntot_value)

    plot_speedup(speedup_data, nombre_process_data, ntot_values)
