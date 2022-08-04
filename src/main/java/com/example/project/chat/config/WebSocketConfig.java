package com.example.project.chat.config;

import com.example.project.domain.User;
import com.example.project.repositories.UserRepository;
import com.example.project.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.security.Principal;
import java.util.List;


@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtTokenUtil jwtTokenUtil;

    private final UserRepository userRepository;
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:4200")
                .withSockJS()
                .setWebSocketEnabled(false)
                .setSessionCookieNeeded(false);;
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {

            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                Principal principal = SecurityContextHolder.getContext().getAuthentication();
                System.out.println("PRINCIPAL PRE-SEND START:" + principal); // shows annonymous user

                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
                List<String> tokenList = accessor.getNativeHeader("Authorization");
                String jwt = null;
                if (tokenList == null || tokenList.size() < 1) {
                    return message;
                } else {
                    jwt = tokenList.get(0).substring(7);
                    if (jwt == null) {
                        return message;
                    }
                }
                String username = jwtTokenUtil.getUsername(jwt);

                User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
                if (jwtTokenUtil.validate(jwt)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            user, null, user.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    principal = SecurityContextHolder.getContext().getAuthentication();
                    User usr =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    accessor.setUser(authentication);
                    System.out.println("PRINCIPAL PRE-SEND END:" + principal + usr.getUsername());

                }
                return message;
            }
        });
    }
}
