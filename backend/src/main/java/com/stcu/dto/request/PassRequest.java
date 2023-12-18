package com.stcu.dto.request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PassRequest {

    @NotBlank
    private String actualPass;
    @NotBlank
    private String newPass;
}
