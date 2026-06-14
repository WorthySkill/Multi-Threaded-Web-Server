# Multi-Threaded Web Server

A concurrent, high-performance HTTP web server built from scratch in Java, Socket Programming, ExecutorService, Linux to demonstrate low-level operating system resource scheduling, multi-threading, and systems programming concepts in a Linux environment.

## 🚀 Features
* **Thread Pool Architecture:** Efficiently manages up to 500 simulated concurrent client connections, drastically minimizing thread creation and destruction overhead under heavy transactional workloads.
* **Thread Synchronization:** Utilizes POSIX threads (`pthreads`), mutex locks, and condition variables to manage shared resource queues safely, completely eliminating race conditions and critical section deadlocks.
* **HTTP Request Handling:** Parses incoming basic HTTP requests and serves static web resources back to the client.

## 🛠️ Tech Stack & Concepts
* **Languages:** Java, Linux
* **APIs/Libraries:** Java Concurrency API (ExecutorService), Java Network Sockets API (ServerSocket)
* **OS Environment:** Linux / Bash
* **Core Concepts:** Concurrency, Resource Scheduling, Thread Synchronization, Memory Management, Network Sockets

## 📁 Project Structure
* `WebServer.java` - Main dispatcher loop that initializes the server socket and coordinates the fixed thread pool.
* `ClientHandler.java` - Worker thread implementation handling HTTP request parsing, MIME-type mapping, and path-traversal security verification.
* `ServerLogger.java` - Synchronized, thread-safe resource manager logging real-time server access data and error faults.

## 🔧 How to Build and Run
1. Clone the repository:
   ```bash
   git clone https://github.com/WorthySkill/Multi-Threaded-Web-Server.git
   cd Multi-Threaded-Web-Server
2. Compile the server source files:
   ```bash
   javac *.java
3. Run the main server module:
   ```bash
   java WebServer
