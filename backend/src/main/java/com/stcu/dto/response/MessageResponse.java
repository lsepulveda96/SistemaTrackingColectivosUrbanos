package com.stcu.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MessageResponse {
    
    private String message;

    public MessageResponse( String m ) {
        message = m;
    }

}
