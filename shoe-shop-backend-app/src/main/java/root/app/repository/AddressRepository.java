package root.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import root.app.entity.Address;
import root.app.entity.User;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Integer> {

    @Query("select a from Address a where a.user.id = :userId order by a.isDefault desc")
    List<Address> findAllAddressByUser(@Param("userId") Long userId);

    Address findAddressByIsDefaultAndUser(Boolean isDefault, User user);
}
