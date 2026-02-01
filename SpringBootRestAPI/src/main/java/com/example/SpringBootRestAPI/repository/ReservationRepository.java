package com.example.SpringBootRestAPI.repository;

import com.example.SpringBootRestAPI.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, String> {
}
