import subprocess

# Definiujemy polecenie grpcurl
command = [
    "grpcurl",
    "-plaintext",
    "-d", '{"arg1": 10, "arg2": 20}',
    "-import-path", "/home/marti/STUDIA/SEMESTR VI/rozproszone/gRPC",
    "-proto", "calculator.proto",
    "localhost:50051",
    "calculator.Calculator/Add"
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
