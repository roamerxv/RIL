package com.alcor.ril.web.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created with admin.
 * User: duduba - 邓良玉
 * Date: 2017/12/6
 * Time: 12:03
 */
@Data
public class UserDTO {
    @NotNull
    @Size(min = 1)
    private String username;

    @Size(min = 1)
    private String name;

    @NotNull
    @Size(min = 6)
    private String password;

    @NotNull
    @Size(min = 1)
    private String matchingPassword;

    @NotNull
    @Size(min = 1)
    private String email;
}
