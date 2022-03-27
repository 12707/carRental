package org.example.dao;

import org.example.domain.CarInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

public interface CarInventoryRepository extends JpaRepository<CarInventory, BigInteger> {

    @Query(value = "from CarInventory where lockId is null")
    List<CarInventory> findAllRentAvailableCars();

    @Query(value = "from CarInventory where lockId = :lockId")
    CarInventory findByLockId(@Param("lockId") String lockId);

    @Modifying
    @Query(value = "update T_CAR_INVENTORY set LOCK_ID = :lockId, UPDATE_TIME = current_timestamp(6), UPDATED_BY = 'admin' where ID = (select ID from T_CAR_INVENTORY where NAME = :carModel and LOCK_ID is null LIMIT 1)", nativeQuery = true)
    int lockCarForBooking(@Param("lockId") String lockId, @Param("carModel") String carModel);

    @Modifying
    @Query(value = "update CarInventory set lockId = null where id = :id")
    int unlockSingleCarToRent(@Param("id") BigInteger id);

    @Modifying
    @Query(value = "update T_CAR_INVENTORY set lockId = null where ID in (select ID from T_CAR_RENTAL where DUE_DATE < current_timestamp(6))", nativeQuery = true)
    int restoreCarsToRent();

    @Modifying
    @Query(value = "update CarInventory set lockId = null, updateTime = current_date, updatedBy = 'admin' where id In :ids")
    int restoreCarsToRent(@Param("ids") List<BigInteger> ids);

    @Query(value = "select name, count(name) as count from CarInventory where lockId is null group by name")
    List<?> findInStockCount();
}
