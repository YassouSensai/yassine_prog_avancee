import numpy as np
import pandas as pd
import matplotlib.pyplot as plt

def read_data(file_path):
    data = pd.read_csv(file_path)
    return data

def calculate_speedup(data):
    temps_execution = data['Time'].values
    nombre_process = data['NbProc'].values

    T1 = np.median(temps_execution[nombre_process == 1])

    unique_processes = np.unique(nombre_process)
    Tp = [np.median(temps_execution[nombre_process == p]) for p in unique_processes]

    Sp = T1 / np.array(Tp)

    print("T1 (temps médian avec 1 processus) :", T1)
    print("Nombre de processus uniques :", unique_processes)
    print("Temps médians correspondants :", Tp)
    print("Speedup calculé :", Sp)


    return Sp, unique_processes

def plot_speedup(speedup_data, nombre_process_data):
    plt.figure(figsize=(10, 6))

    plt.plot(nombre_process_data, speedup_data, marker='o', label='Speedup')

    plt.title('Speedup en fonction du nombre de processus')
    plt.xlabel('Nombre de processus')
    plt.ylabel('Speedup')
    plt.axhline(1, color='red', linestyle='--', label='Speedup = 1')

    max_process = max(nombre_process_data)
    plt.plot([1, max_process], [1, max_process], color='blue', linestyle='--', label='Scalabilité idéale')

    plt.legend()
    plt.grid()

    plt.xlim(left=0)
    plt.ylim(bottom=0)
    plt.gca().set_aspect('equal', adjustable='box')

    x_ticks = np.arange(0, max_process + 1, 1)
    y_ticks = np.arange(0, max(speedup_data) + 1, 1)
    plt.xticks(x_ticks)
    plt.yticks(y_ticks)

    plt.savefig('./speedup.png')

    plt.show()

if __name__ == "__main__":
    file_path = './results.csv'
    data = read_data(file_path)

    speedup_data, nombre_process_data = calculate_speedup(data)

    plot_speedup(speedup_data, nombre_process_data)