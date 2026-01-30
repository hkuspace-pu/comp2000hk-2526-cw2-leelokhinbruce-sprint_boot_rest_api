package com.example.SpringBootRestAPI.repository;

import com.example.SpringBootRestAPI.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;import org.springframework.data.jpa.repository.Query;import org.springframework.data.repository.query.Param;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    // Filter by category to fetch related menu items
    @Query("SELECT m FROM MenuItem m WHERE m.mealTime = :mealTime")
    MenuItem findMenuItemByMealTime(@Param("mealTime") String mealTime);
}
