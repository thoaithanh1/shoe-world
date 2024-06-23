package root.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterDto {

    @NotBlank(message = "Tên không được trống")
    private String firstName;

    @NotBlank(message = "Họ không được trống")
    private String lastName;

    @NotBlank(message = "Email không được trống")
    @Email(message = "Email không đúng định dạng", regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    private String email;

    @NotBlank(message = "Mật khẩu không được trống")
    private String password;
}
