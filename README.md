# **Customer-Support-Service**

I've recently learned and refined many fascinating concepts in the software engineering field, such as **gRPC**, **Unit/Mock/Integration Testing**, **MVC**, **REST**, various **design patterns**, **SpringBoot**, proper **Git workflows**, **build systems**, and **Continuous Integration**. Now, I'm eager to apply this knowledge, along with my experience from hackathons and other projects, to build something substantial.

Whether it's a personal project or a corporate initiative, I'm excited to get started. My next endeavor will be developing a **customer support platform** with a built-in customizable **GPT model** and other features commonly found in such services.

I'm ready to put this knowledge into practice and create something impactful!

---

## **Summary of Application’s Functionality**

The application is a customer support service with the following key functionalities:

1. **Corporation Registration and Login**:
   - Corporations can register and log in to the application via a web interface.
   - Upon logging in, each corporation accesses a dedicated dashboard.

2. **FAQ Management**:
   - Corporations can create, update, and manage a list of frequently asked questions (FAQs) through their dashboard.
   - The FAQ list is fed into a ChatGPT model to create an automated customer service representative that interacts with clients.

3. **Client Interaction and Conversation Handling**:
   - Clients (end users) initiate conversations with the ChatGPT model through the corporation’s support interface.
   - Each client must provide their email address before starting a conversation. The email serves as a unique identifier and is stored in the database.
   - Clients have the option to provide their name, but it is not mandatory.

4. **Conversation Management**:
   - Each conversation session is stored in the database with a unique identifier (ID).
   - Conversations are associated with client emails and the respective corporation.
   - If the model cannot provide an adequate answer, the conversation will be flagged and forwarded to a shared corporate support email.

5. **Data Storage Requirements**:
   - **Corporation Data**: Includes registration credentials, FAQ lists, and associated client information (stored in PostgreSQL).
   - **Client Data**: Includes unique emails, conversation IDs associated with them, and optional names (stored in PostgreSQL).
   - **Conversation Data**: Includes the full conversation history with GPT, along with a reference to the corporation and client, specifically the conversation IDs, which are also stored in PostgreSQL (stored in MongoDB).

---

## **TODOs for Implementing the Application**

### **1. Database Connections**

- **1.1. Set up PostgreSQL Connection**: *Done*

- **1.2. Set up MongoDB Connection**: *Done*

### **2. Model Connection (ChatGPT Integration)**

- **2.1. Set up API Connection to ChatGPT**: *In Progress*

### **3. Authentication and Session Logic**

- **3.1. Implement User Authentication for Corporations**: *To Do*

- **3.2. Manage Client Sessions**: *To Do*

### **4. Business Logic Implementation**

- **4.1. FAQ Management Logic**: *To Do*

- **4.2. Conversation Handling and Escalation Logic**: *To Do*

### **5. Frontend Integration**: *Backlog*
