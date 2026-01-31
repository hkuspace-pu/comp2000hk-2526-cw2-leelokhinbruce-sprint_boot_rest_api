package com.example.SpringBootRestAPI.controller;

import com.example.SpringBootRestAPI.dto.MenuItemRequest;
import com.example.SpringBootRestAPI.model.MenuItem;
import com.example.SpringBootRestAPI.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuItemController {
    @Autowired
    private ItemService itemService;

//    ---- Guest and Staff can get all for sync ----
    @PreAuthorize("hasAnyRole('GUEST', 'STAFF')")
    @GetMapping
    public ResponseEntity<?> getAllMenuItems() {
        try {
            return ResponseEntity.ok(itemService.findAllMenuItems());
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to get all menu items: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //  ---- Guest only for browsing ---- (Fail)
    @PreAuthorize("hasRole('GUEST')")
    @GetMapping("/{mealTime}")
    public ResponseEntity<?> getMenuItemsWithRelatedMealTime(String mealTime) {
        try {
            return ResponseEntity.ok(itemService.findMenuItemWithRelatedMealTime(mealTime));
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to get menu items: " +
                    e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    ---- Staff only ----
    // Add menu item
    @PreAuthorize("hasRole('STAFF')")  // Spring auto prefixes "ROLE_"
    @PostMapping
    public ResponseEntity<?> createMenuItem(@RequestBody MenuItemRequest request) {
        try {
            // New a menu item
            MenuItem menuItem = new MenuItem();
            menuItem.setFoodName(request.getFoodName());
            menuItem.setCategory(request.getCategory());
            menuItem.setMealTime(request.getMealTime());
            menuItem.setPrice(request.getPrice());
            menuItem.setAvailable(request.isAvailable());
            menuItem.setPromotion(request.isPromotion());
            menuItem.setUpdatedAt(new Date());

            // Save menu item
            itemService.saveMenuItem(menuItem);
            return ResponseEntity.ok(menuItem);  // Return the menu item
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add menu item: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Edit menu item
    @PreAuthorize("hasRole('STAFF')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMenuItem(@PathVariable Long id, @RequestBody MenuItemRequest request) {
        return itemService.findMenuItemById(id).map(item -> {
            item.setFoodName(request.getFoodName());
            item.setCategory(request.getCategory());
            item.setMealTime(request.getMealTime());
            item.setPrice(request.getPrice());
            item.setAvailable(request.isAvailable());
            item.setPromotion(request.isPromotion());
            item.setUpdatedAt(new Date());

            // Update item details
            itemService.saveMenuItem(item);
            return ResponseEntity.ok(item);
        }).orElse(ResponseEntity.notFound().build());
    }

    // Delete menu item
    @PreAuthorize("hasRole('STAFF')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMenuItem(@PathVariable Long id) {
        if (!itemService.existMenuItemById(id))
            return new ResponseEntity<>("Menu item not found", HttpStatus.BAD_REQUEST);

        // Delete the item by its id
        itemService.deleteMenuItemById(id);
        return new ResponseEntity<>("Delete successfully", HttpStatus.OK);
    }
}
