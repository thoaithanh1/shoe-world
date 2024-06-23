package root.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import root.app.entity.Address;
import root.app.service.AddressService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/{addressId}")
    public ResponseEntity<?> getAddressById(@PathVariable("addressId") Integer addressId) {
        Address address = addressService.getAddressById(addressId);
        return ResponseEntity.ok(address);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAllAddressByUser(@PathVariable("userId") Long userId) {
        List<Address> addresses = addressService.getAllAddressByUser(userId);
        return ResponseEntity.ok(addresses);
    }

    @PostMapping
    public ResponseEntity<?> saveAddressByUser(@RequestBody Address address) {
        Address saveAddress = addressService.saveAddress(address);
        return ResponseEntity.ok(saveAddress);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<?> deleteAddress(@PathVariable("addressId") Integer addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.ok("OK");
    }
}
