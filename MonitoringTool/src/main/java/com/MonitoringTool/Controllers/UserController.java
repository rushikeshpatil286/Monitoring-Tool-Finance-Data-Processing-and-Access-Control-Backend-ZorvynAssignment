package com.MonitoringTool.Controllers;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

import org.springframework.security.core.Authentication;

import com.MonitoringTool.Entities.Role;
import com.MonitoringTool.Entities.Status;
import com.MonitoringTool.Entities.UserEntity;
import com.MonitoringTool.Services.FirebaseUserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final FirebaseUserService firebaseUserService;

    @PostMapping("/accept")
    public String acceptData(@ModelAttribute UserEntity user) throws Exception {

        Optional<UserEntity> existing =
                firebaseUserService.findByEmail(user.getEmail());

        if (existing.isPresent()) {
            return "redirect:/userform/register.html?error=email";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == null)
            user.setRole(Role.USER);

        if (user.getStatus() == null)
            user.setStatus(Status.ACTIVE);

        firebaseUserService.save(user);

        System.out.println("✅ USER REGISTERED IN FIRESTORE: " + user.getEmail());

        return "redirect:/login/login.html?success";
    }


    // ✅ ROLE BASED DASHBOARD REDIRECT
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return "redirect:/admin/dashboard.html";
        }

        return "redirect:/userpanel/userpanel.html";
    }
}
