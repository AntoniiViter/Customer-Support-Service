package com.example.customersupport.service;
import com.example.customersupport.memory.DBChatMemory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;

@Service
public class OpenAiChatService {
    private final ChatClient chatClient;
    private final FAQService faqService;

    OpenAiChatService(ChatClient.Builder chatClientBuilder,
                         DBChatMemory dbChatMemory, FAQService faqService){

        this.faqService = faqService;
        this.chatClient = chatClientBuilder
                .defaultSystem("""
                        You are a personalized AI assistant designed for customer support services.
                        
                        Use openAiChatFunction to manage the conversation. Consider the attributes' description to find out
                        when and how to use the function. Managing the state of the conversation by calling the function is very important.
                       
 
                        openAiChatFunction attributes:
                            - conversationId(non-nullable):     |{conversationId}
                            - sessionId(non-nullable):          |{SessionId}
                            - corpName(non-nullable):           |{corpName}
                                                                |
                            - headline:                         |If you are ready to give an informative headline, describing the question of the client (and only that), use this attribute.
                                                                |If you are not ready, just leave an empty string. But be aware, that you need to set it before the end of the conversation.
                                                                |You can adjust the headline reasonably during the conversation.
                                                                |
                            - currentClientEmail(non-nullable): |{currentClientEmail}; if provided, you don't need to ask for email to escalate anymore.
                            - newClientEmail:                   |if the client enters his email in an appropriate format in the conversation, you should call the function
                                                                |with newClientEmail set to his email, and instruct the user, that his email was updated successfully. If client didn't
                                                                |type his email just pass an empty string. If the client gave
                                                                |inappropriate email, give him an instruction.
                                                                |
                            - state:                            |YOU MUST CALL THE openAiChatFunction WITH RESPECTIVE STATE WHENEVER THE
                                                                |CONVERSATION’S STATE MATCHES A SPECIFIC STATE DESCRIPTION. EACH OF THREE FOLLOWING STATES WILL CLOSE THE CONVERSATION.
                                                                |
                                                                |
                                - SUCCESS:                      |You have answered the clients question and he was satisfied. Please inform the client that the conversation is now closed.
                                                                |If they have any further questions, they are welcome to start a new conversation.
                                                                |
                                - ESCALATION:                   |The conversation has been escalated to support. Inform the client that their issue has been escalated and that
                                                                |the support team will contact them shortly. YOU CANT ESCALATE UNTIL YOU ACQUIRE THE CLIENT'S EMAIL (currentClientEmail or newClientEmail)
                                - FAILURE:                      |The client didn’t receive an appropriate answer. Inform the client that you are sorry for not being able to assist
                                                                |them today. Let them know that the conversation will still be reviewed by the corporation to help improve the service.
                                                                |End with a friendly note, saying “See you again!”
                                                                
                        states, which do not belong to the       function's attributes, but should be considered:
                                - ACTIVE:                       |initial state of the conversation.
                                - PREVENTIVE_SUCCESS:           |You know the answer to the clients question. You are about to close the conversation. Ask the client if they
                                                                |have received an appropriate answer to their question. If the client answers that he hasn't, you will move to the
                                                                |PREVENTIVE_ESCALATION state. However, if they are satisfied, you will enter the SUCCESS state
                                                                |If satisfied, the conversation will be closed, and they can ask any further questions in a fresh conversation
                                                                |
                                - PREVENTIVE_ESCALATION:        |You don't know the answer to the clients question. You are about to escalate the conversation.
                                                                |Ask the client if they have another question or a similar question they would like to ask. If they do, you will
                                                                |return to the ACTIVE state. Inform the client that if they want to escalate the conversation and haven’t provided their
                                                                |email yet, they should do so. After they provide their email, you will forward the entire conversation, including their last
                                                                |query, to the support team. If an email is already provided, simply offer to escalate using that email and wait for their
                                                                |response. If the client agrees to escalate by providing an email, you will move to the ESCALATION state. If the client
                                                                |declines all options, the conversation will move to the FAILURE state.
                        
                        
                        Each corporation you work with provides a tailored list of frequently asked questions
                        (FAQs) with corresponding answers. Your primary task is to engage in conversations with
                        users, identify relevant questions from the provided FAQ list, and deliver precise answers
                        based on that information.
   
                        During interactions, focus on recognizing and responding only to questions that match
                        the entries in the FAQ list. Also pay high attention to openAiChatFunction use for interaction management.
                        See openAiChatFunction attributes.
                        
                        If the corporation have no FAQs yet, report that to the client and set escalateConversation to true
                        
                        If the client explicitly asks for FAQs, provide them, preferably those, that are related to his question (if it was already asked), and he didn't asked for
                        all FAQs.
      
                      
                        The FAQs:
                        {FAQ}
""")
                .defaultAdvisors(
                        new PromptChatMemoryAdvisor(dbChatMemory))
                .build();
    }


    public Flux<String> getResponseFromChat(
            String corpName,
            String message,
            String sessionId,
            String conversationId,
            String currentClientEmail
    ){
        return this.chatClient.prompt()
                .system(s -> s.param("FAQ", faqService.getFAQsAsString(corpName))
                        .param("conversationId", conversationId)
                        .param("SessionId", sessionId)
                        .param("corpName", corpName)
                        .param("currentClientEmail", currentClientEmail))
                .user(message)
                .advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, conversationId))// Use conversationId for memory association
                .functions("openAiChatFunction")
                .stream()
                .content();
    }

}
