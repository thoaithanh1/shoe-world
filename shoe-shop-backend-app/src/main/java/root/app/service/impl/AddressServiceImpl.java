package root.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import root.app.entity.Address;
import root.app.exception.ObjectNotFoundException;
import root.app.repository.AddressRepository;
import root.app.service.AddressService;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public List<Address> getListAddress() {
        return addressRepository.findAll();
    }

    @Override
    public List<Address> getAllAddressByUser(Long userId) {
        return addressRepository.findAllAddressByUser(userId);
    }

    @Override
    public Address getAddressById(Integer addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new ObjectNotFoundException("Không tồn tại địa chỉ có id " + addressId));
    }

    @Override
    public Address saveAddress(Address address) {
        boolean isUpdateAddress = address.getId() != null;
        if(isUpdateAddress) {
            Address addressDB = addressRepository.findById(address.getId()).get();
            if(addressDB.getIsDefault() != address.getIsDefault()) {
                Address addressDefault =
                        addressRepository.findAddressByIsDefaultAndUser(true, addressDB.getUser());
                addressDefault.setIsDefault(false);
                addressRepository.save(addressDefault);
            } else {
                address.setUser(addressDB.getUser());
            }
        } else {
            if(address.getIsDefault()) {
                Address addressDefault =
                        addressRepository.findAddressByIsDefaultAndUser(true, address.getUser());
                addressDefault.setIsDefault(false);
                addressRepository.save(addressDefault);
            }
        }
        address.setCountry("Việt Nam");
        return addressRepository.save(address);
    }

    @Override
    public void deleteAddress(Integer addressId) {
        addressRepository.deleteById(addressId);
    }
}
