# 🚦 Multithreaded Queue Simulation System (Java)

A concurrent, event-based simulation built in Java to model a multi-server queuing system, such as a set of checkout counters or bank tellers. The system dynamically dispatches tasks (clients) to servers (queues) using customizable scheduling strategies to analyze system performance and efficiency.

---

## ✨ Key Features

This application showcases several core concepts of concurrent programming and system modeling:

### **Concurrent Servers (Multithreading)**

* **Server Implementation:** Each queue/server is implemented as a separate thread (`Server.java`), allowing tasks to be processed concurrently.
* **Thread-Safe Queues:** Utilizes `java.util.concurrent.BlockingQueue` to manage client tasks, ensuring thread-safe additions and removals between the main simulation thread and the server threads.
* **Atomic Operations:** Uses `AtomicInteger` (`waitingPeriod`) to safely track the total waiting time of tasks in a queue, enabling accurate scheduling decisions without race conditions.

### **Scheduling Strategies (Strategy Pattern)**

The system implements the Strategy design pattern, allowing the simulation manager to switch dispatch policies at runtime:

1.  **Shortest Queue (SQ):** Clients are dispatched to the server with the fewest number of waiting tasks. (`ShortestQueueStrategy.java`)
2.  **Shortest Time (ST):** Clients are dispatched to the server with the minimum total estimated waiting time (the sum of all service times of tasks currently in the queue). (`TimeStrategy.java`)

### **Simulation & Analysis**

* **Dynamic Task Generation:** Tasks (clients) are generated with randomly distributed **arrival times** and **service times**, simulating real-world traffic.
* **Performance Metrics:** Automatically calculates key statistics:
    * **Peak Hour:** The time step with the maximum number of clients (waiting and being served).
    * **Average Waiting Time:** Calculates the average time clients spent waiting in all queues.
    * **Average Service Time:** Calculates the average time required to complete all tasks.
* **Detailed Logging:** Logs the state of all queues and clients at every time step to the GUI and a dedicated log file (`LogWriter.java`).

### **User Interface**

* **Java Swing GUI:** An interactive desktop application (`SimulationFrame.java`) for easy configuration and visualization.
* **Parameter Input:** Users can define all simulation parameters (number of clients, number of queues, time limits, min/max times) before starting the simulation.
* **Real-time Output:** Displays the simulation log in real-time.

---

## 🛠️ Technologies Used

* **Language:** Java
* **GUI Library:** Java Swing
* **Design Patterns:** Strategy Pattern
* **Concurrency:** `java.lang.Thread`, `java.util.concurrent.BlockingQueue`, `java.util.concurrent.atomic.AtomicInteger`.

---

## 🚀 Getting Started

### Prerequisites

* Java Development Kit (JDK) 8 or newer installed.
* An Integrated Development Environment (IDE) like IntelliJ IDEA or Eclipse.

* **Lidia Tomus**
    * Student at the Technical University of Cluj-Napoca (UTCN)
