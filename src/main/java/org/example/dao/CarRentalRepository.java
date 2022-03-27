package org.example.dao;

import org.example.domain.CarRental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public interface CarRentalRepository extends JpaRepository<CarRental, BigInteger> {

    @Query(value = "from CarRental where dueDate < :date")
    List<CarRental> findOverDueRentCars(@Param("date") Date date);

    @Query(value = "from CarRental where dueDate >= :date")
    List<CarRental> findRentCars(@Param("date") Date date);
}
