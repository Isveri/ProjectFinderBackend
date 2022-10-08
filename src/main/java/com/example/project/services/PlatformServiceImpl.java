package com.example.project.services;

import com.example.project.domain.Platform;
import com.example.project.domain.User;
import com.example.project.exceptions.UserNotFoundException;
import com.example.project.mappers.PlatformMapper;
import com.example.project.model.PlatformDTO;
import com.example.project.repositories.PlatformRepository;
import com.example.project.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.project.utils.UserDetailsHelper.getCurrentUser;

@RequiredArgsConstructor
@Service
public class PlatformServiceImpl implements PlatformService {

    private final UserRepository userRepository;
    private final PlatformRepository platformRepository;
    private final PlatformMapper platformMapper;

    @Override
    public PlatformDTO connectDC(String accessToken, String tokenType) {

        try {
            URL url = new URL("https://discord.com/api/users/@me");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", tokenType+" "+accessToken);
            int status = con.getResponseCode();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();

            return manageDCData(content);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private PlatformDTO manageDCData(StringBuffer content) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree(content.toString());
        String username = node.get("username").asText();
        String avatar = node.get("avatar").asText();
        String discriminator = node.get("discriminator").asText();

        Long id = getCurrentUser().getId();
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("User not found id:"+id));

        Platform platform = Platform.builder()
                .user(user)
                .discriminator(discriminator)
                .platformType(Platform.PlatformType.DISCORD)
                .username(username)
                .avatar(avatar)
                .build();

        platformRepository.save(platform);
        user.getPlatforms().add(platform);
        userRepository.save(user);

        return platformMapper.mapPlatformToPlatformDTO(platform);
    }
}
