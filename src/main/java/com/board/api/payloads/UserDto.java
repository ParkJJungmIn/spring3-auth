package com.board.api.payloads;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    private int id;

    @NotEmpty
    @Size(min = 4, message = "4글자 이상")
    private String name;
    private String email;
    @NotEmpty
    @Size(min=5, message = "비밀번호 5글자 이상")
    private String password;
    private String about;
}
