/*
 * package com.MonitoringTool.Controllers;
 * 
 * import org.springframework.http.ResponseEntity; import
 * org.springframework.security.crypto.password.PasswordEncoder; import
 * org.springframework.web.bind.annotation.PostMapping; import
 * org.springframework.web.bind.annotation.RequestBody; import
 * org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.web.bind.annotation.RestController;
 * 
 * import com.MonitoringTool.Entities.Role; import
 * com.MonitoringTool.Entities.Status; import
 * com.MonitoringTool.Entities.UserEntity; import
 * com.MonitoringTool.Repositories.UserRepository; import
 * com.MonitoringTool.dto.RegisterRequest;
 * 
 * import lombok.RequiredArgsConstructor;
 * 
 * @RestController
 * 
 * @RequestMapping("/api/auth")
 * 
 * @RequiredArgsConstructor public class AuthController {
 * 
 * private final UserRepository userRepository; private final PasswordEncoder
 * passwordEncoder;
 * 
 * @PostMapping("/api/auth/register") public ResponseEntity<String> register(
 * 
 * @RequestBody RegisterRequest request) {
 * 
 * System.out.println("✅ Register API HIT"); System.out.println("Email: " +
 * request.getEmail());
 * 
 * if (userRepository.findByEmail(request.getEmail()).isPresent()) { return
 * ResponseEntity.badRequest() .body("Email already exists"); }
 * 
 * UserEntity user = new UserEntity(); user.setName(request.getName());
 * user.setEmail(request.getEmail()); user.setPassword(
 * passwordEncoder.encode(request.getPassword()) ); user.setRole(Role.USER);
 * user.setStatus(Status.ACTIVE); // or whatever default you use
 * 
 * userRepository.save(user);
 * 
 * System.out.println("✅ User saved in DB");
 * 
 * return ResponseEntity.ok("User registered successfully"); } }
 * 
 */