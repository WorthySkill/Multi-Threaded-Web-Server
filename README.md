# Multi-Threaded Web Server

A concurrent, high-performance HTTP web server built from scratch in C/C++ to demonstrate low-level operating system resource scheduling, multi-threading, and systems programming concepts in a Linux environment.

## 🚀 Features
* **Thread Pool Architecture:** Efficiently manages up to 500 simulated concurrent client connections, drastically minimizing thread creation and destruction overhead under heavy transactional workloads.
* **Thread Synchronization:** Utilizes POSIX threads (`pthreads`), mutex locks, and condition variables to manage shared resource queues safely, completely eliminating race conditions and critical section deadlocks.
* **HTTP Request Handling:** Parses incoming basic HTTP requests and serves static web resources back to the client.

## 🛠️ Tech Stack & Concepts
* **Languages:** C / C++
* **APIs/Libraries:** POSIX Threads (`pthreads`), Linux Sockets API
* **OS Environment:** Linux / Bash
* **Core Concepts:** Concurrency, Resource Scheduling, Thread Synchronization, Memory Management, Network Sockets

## 📁 Project Structure
* `src/main.c` - Server initialization, socket binding, and main execution loop.
* `src/thread_pool.c` - Thread pool logic, worker queue management, and mutex synchronization.
* `src/http_handler.c` - HTTP protocol parsing and network request handling.

## 🔧 How to Build and Run
1. Clone the repository:
   ```bash
   git clone https://github.com/WorthySkill/Multi-Threaded-Web-Server.git
   cd Multi-Threaded-Web-Server
