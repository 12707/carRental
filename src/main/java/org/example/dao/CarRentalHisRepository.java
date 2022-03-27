package org.example.dao;

import org.example.domain.CarRentalHis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface CarRentalHisRepository extends JpaRepository<CarRentalHis, BigInteger> {
}
