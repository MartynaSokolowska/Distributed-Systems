import subprocess
import json

# Definiujemy pełną ścieżkę do pliku calculator.proto
proto_file_path = "/home/marti/STUDIA/SEMESTR VI/rozproszone/gRPC"

# Tworzymy dane wejściowe zgodne z definicją struktury ComplexArithmeticOpArguments
args = {
    "optype": "AVG",  # Enum OperationType
    "args": [1.0, 2.5, 3.7]  # Lista wartości double
}

# Konwertujemy dane wejściowe do formatu JSON
data = json.dumps(args)

# Definiujemy polecenie grpcurl
command = [
    "grpcurl",
    "-plaintext",
    "-d", data,
    "-import-path", proto_file_path,
    "-proto", "calculator.proto",
    "localhost:50051",
    "calculator.AdvancedCalculator/ComplexOperation"
]

# Wywołujemy polecenie przy użyciu subprocess
result = subprocess.run(command, capture_output=True, text=True)

# Sprawdzamy, czy wystąpiły jakieś błędy
if result.returncode == 0:
    print("Wynik:")
    print(result.stdout)
else:
    print("Błąd:")
    print(result.stderr)
