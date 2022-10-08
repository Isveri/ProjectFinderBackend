package com.example.project.services;

import com.example.project.model.PlatformDTO;

public interface PlatformService {

    PlatformDTO connectDC(String accessToken, String tokenType);
}
