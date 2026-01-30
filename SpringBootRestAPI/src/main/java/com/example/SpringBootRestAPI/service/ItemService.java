package com.example.SpringBootRestAPI.service;

import com.example.SpringBootRestAPI.model.MenuItem;
import com.example.SpringBootRestAPI.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;import java.util.Optional;

@Service
public class ItemService {
    @Autowired
    private MenuItemRepository menuItemRepository;

    public MenuItem findAllMenuItems() {
        return (MenuItem) menuItemRepository.findAll();
    }

    // Query menu items with the specific category
    public MenuItem findMenuItemWithRelatedCategory(String mealTiem) {
        return menuItemRepository.findMenuItemByMealTime(mealTiem);
    }

    // Find a menu item with id
    public Optional<MenuItem> findMenuItemById(Long id) {
        return menuItemRepository.findById(id);
    }

    public Boolean existMenuItemById(Long id) {
        return menuItemRepository.existsById(id);
    }

    public void deleteMenuItemById(Long id) {
        menuItemRepository.deleteById(id);
    }

    // Save
    public void saveMenuItem(MenuItem menuItem) {
        menuItemRepository.save(menuItem);
    }
}
