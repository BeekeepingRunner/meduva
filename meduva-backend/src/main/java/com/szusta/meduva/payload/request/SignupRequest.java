package com.szusta.meduva.payload.request;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
public class SignupRequest {

    @Email(message = "Email must be in its correct format")
    private String email;

    @Size(min = 5, max = 20, message = "Login must contain between 5 and 20 characters")
    @Pattern(regexp = "^[^-\\s]+$", message = "Login must not contain forbidden characters")
    private String login;

    @Size(min = 8, max = 20, message = "Password must contain between 8 and 20 characters")
    @Pattern(regexp = "^[^-\\t\\r\\n\\v\\f]+$", message = "Password must not contain forbidden characters")
    private String password;

    @Size(min=1, max=30, message = "Firstname must contain between 1 and 30 letters")
    @Pattern(regexp = "^[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð ,.'-]+$", message = "Only unicode letters are allowed")
    private String name;

    @Size(min=1, max=30, message = "Surname must contain between 1 and 30 letters")
    @Pattern(regexp = "^[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð ,.'-]+$", message = "Only unicode letters are allowed")
    private String surname;

    @Pattern(regexp = "^(\\+[0-9]{1,4})?[0-9]{6,12}$", message = "The number must contain between 6 and 12 digits (optionally with an area code at the beginning)")
    private String phoneNumber;

    private Set<String> roles;
}
