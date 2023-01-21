package org.kolesnyk.controller;

import org.kolesnyk.facade.BookingFacade;
import org.kolesnyk.model.User;
import org.kolesnyk.model.impl.UserImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private BookingFacade bookingFacade;

    @Autowired
    public UserController(BookingFacade bookingFacade) {
        this.bookingFacade = bookingFacade;
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createUser(@RequestParam(value = "name") String name,
                                           @RequestParam(value = "email") String email) {

        User user = new UserImpl();
        user.setName(name);
        user.setEmail(email);
        bookingFacade.createUser(user);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @RequestParam(value = "name") String name,
                                           @RequestParam(value = "email") String email) {
        User user = new UserImpl();
        user.setName(name);
        user.setEmail(email);
        user.setId(id);
        bookingFacade.updateUser(user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long userId) {
        bookingFacade.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{id}")
    public ModelAndView getUserById(@PathVariable("id") Long userId, Model model) {
        Map<String, Object> params = new HashMap<>();
        params.put("user", bookingFacade.getUserById(userId));
        return new ModelAndView("user", params);
    }

    @GetMapping("find/email")
    public ModelAndView getUserByEmail(@RequestParam(value = "email") String email) {
        Map<String, Object> params = new HashMap<>();
        params.put("user", bookingFacade.getUserByEmail(email));
        return new ModelAndView("user", params);
    }

    @GetMapping("find")
    public ModelAndView getUsersByName(@RequestParam(value = "name") String name,
                                       @RequestParam(value = "size") int pageSize,
                                       @RequestParam(value = "num") int pageNum) {
        Map<String, Object> params = new HashMap<>();
        params.put("users", bookingFacade.getUsersByName(name, pageSize, pageNum));
        return new ModelAndView("users", params);
    }

}
