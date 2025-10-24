package com.sc2006.g5.edufinder.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignupRequest {
    
    private String username;
    private String password;

}
