# Distributed Systems Projects

This repository contains various projects that demonstrate the use of distributed systems. Each project is implemented using different technologies to showcase various aspects of distributed computing.

## Projects Overview

### 1. ClientServer-chat

The `ClientServer-chat` project creates a chat application where clients send messages through TCP, and the messages are delivered to every other client.

- **Directory**: `ClinetServer-chat/chat`
- **Key Files**:
  - `ClientHandler.java`
  - `JavaClient.java`
  - `JavaServer.java`
  - `MsgListener.java`

### 2. ZooKeeper Watcher

The `ZooKeeper` project monitors ZooKeeper servers and opens an external graphic application that displays the number of children in the `/a` znode.

- **Directory**: `Zookeeper`
- **Key Files**:
  - `ZooKeeperWatcherApp.java`

### 3. Ray

The `RayAgents` project demonstrates basic tasks in Ray, focusing on concurrency and parallelism, including sorting and calculating Pi.

- **Directory**: `Ray`
- **Key Files**:
  - `ray.ipynb` 

### 4. RabbitMQ

The `RabbitMQ` project enables message exchange between doctors and technicians using RabbitMQ. It also includes an administrator component that reads logs.

- **Directory**: `RabbitMQ/Rabbit`
- **Key Files**:
  - `Administrator.java`
  - `Config.java`
  - `Doctor.java`
  - `Main.java`
  - `Technician.java`

## Getting Started

### Prerequisites

- Java Development Kit (JDK) for Java projects
- Python for Ray project
- RabbitMQ server for RabbitMQ project
- ZooKeeper server for ZooKeeper project

### Installation

1. **Clone the repository**:

    ```bash
    git clone https://github.com/your-username/Distributed-Systems.git
    cd Distributed-Systems
    ```

2. **Navigate to the project directory** you want to run and follow the specific instructions for each project.

### Running the Projects

#### ClientServer-chat

1. Navigate to the `ClinetServer-chat/chat` directory.
2. Compile the Java files:

    ```bash
    javac *.java
    ```

3. Start the server:

    ```bash
    java JavaServer
    ```

4. Start the clients:

    ```bash
    java JavaClient
    ```

#### ZooKeeper Watcher

1. Ensure ZooKeeper is running.
2. Navigate to the `Zookeeper` directory.
3. Compile and run the application:

    ```bash
    javac ZooKeeperWatcherApp.java
    java ZooKeeperWatcherApp
    ```

#### Ray

1. Ensure you have Ray installed:

    ```bash
    pip install ray
    ```

2. Navigate to the `Ray` directory.
3. Run the scripts:

    ```bash
    python sort.py
    python calculate_pi.py
    ```

#### RabbitMQ

1. Ensure RabbitMQ is running.
2. Navigate to the `RabbitMQ/Rabbit` directory.
3. Compile the Java files:

    ```bash
    javac *.java
    ```

4. Run the main application:

    ```bash
    java Main
    ```

## Contributing

Contributions are welcome! Please fork this repository and submit a pull request for any enhancements or bug fixes.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Special thanks to the creators and maintainers of the various libraries and tools used in this project.

