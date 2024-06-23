package root.app.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import root.app.dto.*;
import root.app.entity.*;

@Component
public class CustomMapper {

    @Autowired
    private ModelMapper modelMapper;

    public ProductDto convertEntityToDto(Product product) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper.map(product, ProductDto.class);
    }

    public ProductDetailDto convertProductDetailToDto(ProductDetail productDetail) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper.map(productDetail, ProductDetailDto.class);
    }

    public UserDto convertUserToDto(User user) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper.map(user, UserDto.class);
    }

    public CartDetailDto convertCartDetailToDto(CartDetail cartDetail) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper.map(cartDetail, CartDetailDto.class);
    }

    public AddressDto convertAddressToDto(Address address) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper.map(address, AddressDto.class);
    }
}
