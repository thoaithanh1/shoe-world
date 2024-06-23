package root.app.service;

import root.app.entity.Address;

import java.util.List;

public interface AddressService {

    List<Address> getListAddress();

    List<Address> getAllAddressByUser(Long userId);

    Address getAddressById(Integer addressId);

    Address saveAddress(Address address);

    void deleteAddress(Integer addressId);
}
