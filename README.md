# **Customer-Support-Service**

I've recently learned and refined many fascinating concepts in the software engineering field, such as **gRPC**, **Unit/Mock/Integration Testing**, **MVC**, **REST**, various **design patterns**, **SpringBoot**, proper **Git workflows**, **build systems**, and **Continuous Integration**. Now, I've reached the point where the backend part of the application is already fully implemented, including session management, conversation handling with GPT, and CI/CD processes.

Whether it's a personal project or a corporate initiative, I'm excited to continue developing and eventually integrate a frontend interface.

---

## **How to Start the Application**

Follow these steps to clone the repository, build, and start the application using Docker:

### **1. Clone the Repository**

First, you'll need to clone the repository from GitHub to your local machine. Open a terminal and run the following command:

```bash
git clone https://github.com/AntoniiViter/Customer-Support-Service.git
```

Navigate into the project directory:

```bash
cd Customer-Support-Service
```

You can add the following instructions to the README file for creating and editing the `.env` file in the `resources` folder:

### **1. Create the `.env` File and specify OPENAI_API_KEY**

Before starting the application, you need to create a `.env` file in the `resources` folder and set the `OPENAI_API_KEY`. Here’s how you can do it:

1. Navigate to the `resources` folder in your project directory:

   ```bash
   cd ./backend/src/main/resources
   ```

2. Create and open the `.env` file using a text editor. You can use `nano`, `vim`, or any other editor of your choice.

   **Using `nano`:**

   ```bash
   nano .env
   ```

   **Using `vim`:**

   ```bash
   vim .env
   ```

   **Using `Visual Studio Code` (if you have it installed):**

   ```bash
   code .env
   ```

3. Add the following line to the `.env` file:

   ```bash
   OPENAI_API_KEY=your_openai_api_key_here
   ```

4. Save and close the file.

Make sure to replace `your_openai_api_key_here` with your actual OpenAI API key. This key will be used by the application to interact with the GPT model.

### **3. Start the Application Using Docker**

The application is fully containerized with Docker, including the databases (MongoDB, PostgreSQL, and Redis). To start the application:

#### **Initial Run**

For the first time, you'll need to build the necessary Docker images. Use the `--build` flag to ensure everything is set up correctly:

```bash
docker-compose up --build
```

This command will:
- Build the Docker images for the application and its dependencies.
- Start the containers for the application, MongoDB, PostgreSQL, and Redis.
- Set up the necessary environment and dependencies.

#### **Subsequent Runs**

After the initial build, you can start the application without rebuilding the images:

```bash
docker-compose up
```

### **4. Access the Application**

Once the containers are up and running, you can access the application at:

```
http://localhost:8080
```

To try out the support chatbot, you can send a message to it by navigating to:

```
http://localhost:8080/admin/support?message=your%20message
```

Replace `your%20message` with the text you want to send to the chatbot. This URL allows you to interact with the GPT-powered chatbot through the application's support interface.

This setup enables you to quickly start and test the backend functionality of the application without manual configuration.

---

## **Summary of Application’s Functionality**

The application is a customer support service with the following key functionalities:

1. **Corporation Registration and Login**:
   - Corporations can register and log in via API endpoints.
   - Upon successful registration and login, corporations can manage their client interactions, FAQs, and conversation history.

2. **FAQ Management**:
   - Corporations can create, update, and manage a list of frequently asked questions (FAQs) through API endpoints.
   - These FAQs are utilized by the integrated GPT model to provide automated responses to client inquiries.

3. **Anonymous Client Interaction and Conversation Handling**:
   - Sessions for unauthenticated users are managed using Redis, providing efficient session handling.
   - Clients can start conversations anonymously without providing an email address. If they wish to escalate the conversation, they can provide their email to the assistant, which will then automatically escalate the issue using the provided email.

4. **Conversation Management and Storage**:
   - The GPT-powered chatbot handles client interactions using business logic that includes:
     - Email recognition.
     - Conversation state management.
     - Workflow management.
   - Conversations are streamed in real-time, enhancing user experience by eliminating the wait for full response generation.
   - All conversations are automatically saved in both MongoDB and PostgreSQL, ensuring data consistency and availability.
   - If the GPT model cannot adequately respond to a query, the conversation can be flagged and forwarded to a shared corporate support email for human intervention.

5. **Session Management**:
   - Integrated Redis is used for session storage and management, ensuring efficient handling of client sessions, including those of unauthenticated users.
   - The application keeps track of the ongoing conversation and the associated corporation, preventing users from initiating new conversations with a different corporation until the current one is ended.

6. **Data Storage Requirements**:
   - **Corporation Data**: Includes registration credentials, FAQ lists, and associated client information (stored in PostgreSQL).
   - **Client Data**: Includes unique emails, conversation IDs associated with them, and optional names (stored in PostgreSQL).
   - **Conversation Data**: Includes the full conversation history with GPT, along with a reference to the corporation and client, specifically the conversation IDs (stored in MongoDB).

7. **Scheduled Events**:
   - **Database Synchronization**: Periodically cleans and synchronizes both MongoDB and PostgreSQL databases to maintain data integrity.
   - **Conversation Cleanup**: Automatically removes potentially failed or closed conversations to ensure that both databases are up-to-date and free from redundant data.
   - **Performance Optimization**: These scheduled tasks help optimize performance by reducing the storage of unnecessary data, ensuring a more efficient and streamlined operation of the system.

8. **Testing and CI Integration**:
   - The project includes a comprehensive testing setup:
     - One mock/unit test to verify individual components.
     - One integration test that runs in a separate test container to ensure that different parts of the application work together as expected.
   - Continuous Integration (CI) is configured using GitHub Actions to run these tests automatically on every push and pull request, ensuring code quality and reliability.
   - Test results and build status are visible through the GitHub Actions widget: ![CI Build](https://github.com/AntoniiViter/Customer-Support-Service/actions/workflows/gradle-test.yml/badge.svg)
   
9. **Dockerization**:
   - The entire application, including databases (MongoDB, PostgreSQL, and Redis), is fully containerized.
   - The application can be started using `docker-compose up --build` for the initial setup to build the necessary Docker images. Subsequent runs can use `docker-compose up`.
   - The application is accessible on port 8080.

---

## **TODOs for Implementing the Application**

### **1. Database Connections**

- **1.1. Set up PostgreSQL Connection**: *Done*

- **1.2. Set up MongoDB Connection**: *Done*

### **2. Model Connection (ChatGPT Integration)**

- **2.1. Set up API Connection to ChatGPT**: *Done*

### **3. Authentication and Session Logic**

- **3.1. Implement User Authentication for Corporations**: *Done*

- **3.2. Manage Client Sessions with Redis**: *Done*

### **4. Business Logic Implementation**

- **4.1. FAQ Management Logic**: *Done*

- **4.2. Conversation Handling and Escalation Logic**: *Done*

### **5. Testing and CI Integration**

- **5.1. Implement Unit and Integration Tests**: *Done*
- **5.2. Set up GitHub Actions for CI**: *Done*

### **6. Frontend Integration**: *Backlog*

---

### **Potential Improvement: Integrating Vector Databases for Enhanced Performance**

While the current integration with OpenAI's ChatGPT and Spring AI provides a solid foundation for handling customer support inquiries, there is a potential improvement that could further optimize performance: using Vector Databases (VectorDBs) in conjunction with Retrieval-Augmented Generation (RAG) techniques.

#### **Benefits of Using Vector Databases:**

1. **Faster Response Times**:
   - VectorDBs can significantly speed up response times by enabling faster retrieval of relevant FAQ answers. Instead of generating responses from scratch, the system could quickly search through pre-embedded FAQs to find the closest match, reducing latency and improving user experience.

2. **Improved Accuracy**:
   - By leveraging embeddings, the AI could better understand the semantic similarities between user queries and stored FAQs. This could lead to more accurate and contextually appropriate responses, enhancing the quality of customer interactions.

#### **Challenges with Implementing VectorDBs Now:**

1. **Complexity of Embedding Management**:
   - Each corporation provides its own unique set of FAQs, requiring a tailored approach for generating and managing embeddings. Embedding generation is computationally intensive, and maintaining up-to-date embeddings for all corporations could become cumbersome.

2. **Infrastructure Requirements**:
   - Implementing VectorDBs requires additional infrastructure and storage capabilities, especially for handling large numbers of vectors efficiently. The current setup is designed for simplicity and ease of use, without the need for specialized vector storage.

#### **Future Considerations:**

While the integration of VectorDBs and RAG techniques could offer significant improvements in performance and accuracy, the complexities and infrastructure demands currently outweigh the benefits. As the project evolves, and if there's a need for more sophisticated handling of FAQs across multiple corporations, this approach could be revisited to enhance scalability and responsiveness.

For now, the focus remains on providing a reliable, easy-to-manage solution using OpenAI ChatGPT to address common customer support needs effectively.

---

### Session Tokens vs JWT Tokens

In this project, **session-based conversation logic** was chosen to manage user interactions with the chatbot, particularly for clients of corporations. This decision shaped the choice not to use **JWT (JSON Web Tokens)** for authentication and authorization.

#### Why Session Tokens Were Chosen:

- **Stateful Approach Fits the Use Case**: Since the conversation with the chatbot is tied to a specific session, it made sense to use Spring Boot's built-in session management. This keeps the application stateful, ensuring that the session is persistent across requests within a session context.

- **Avoiding Complexity**: Using JWT tokens would require turning off Spring Boot's default session management, effectively making the application **stateless**. This would mean re-implementing session management manually, which introduces unnecessary complexity for the current scope of the application.

- **Overkill for Now**: For this particular use case, JWT tokens would be overkill. Given that the chatbot logic is session-based and doesn't require stateless behavior for clients, the benefits of using JWT, such as decentralization and statelessness, are not essential at this point.

#### Future Considerations for JWT:

- **JWT for Further Improvements**: While session tokens are sufficient for now, **JWT tokens** could be considered for future improvements. JWT offers several advantages, such as scalability and more flexible authentication across different services or platforms. However, adopting JWT would mean reworking the current architecture to handle token-based authentication.

- **Hybrid Approach**: It's also possible to adopt a **hybrid approach** in the future, where some endpoints remain stateful using session tokens (such as conversation management), while others (e.g., APIs used by corporations) can be stateless and use JWT tokens for authentication.

- **Conversation Management Flexibility**: The session-based conversation logic could also evolve in the future to handle conversations differently, perhaps by decoupling them from session tokens or adopting token-based systems, depending on how the app grows.

In summary, the choice of session tokens was made to keep things simple and aligned with the stateful nature of chatbot conversations. As the app evolves, however, using JWT tokens for specific stateless endpoints and improving overall scalability remains an option for future consideration.
