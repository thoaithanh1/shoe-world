package root.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddressDto {

    private Integer id;

    private String country;

    private String city;

    private String district;

    private String wards;

    private String location;

    private Boolean isDefault;
}
