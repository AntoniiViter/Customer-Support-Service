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

- **2.1. Set up API Connection to ChatGPT**: *Done*

### **3. Authentication and Session Logic**

- **3.1. Implement User Authentication for Corporations**: *Done*

- **3.2. Manage Client Sessions**: *Done*

### **4. Business Logic Implementation**

- **4.1. FAQ Management Logic**: *In Progress*

- **4.2. Conversation Handling and Escalation Logic**: *In Progress*

### **5. Frontend Integration**: *Backlog*

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
