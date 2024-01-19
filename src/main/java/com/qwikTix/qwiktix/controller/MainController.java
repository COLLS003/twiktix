package com.qwikTix.qwiktix.controller;
import org.springframework.data.redis.core.StringRedisTemplate;




import com.qwikTix.qwiktix.bookings.Bookings;
import com.qwikTix.qwiktix.users.UserRepository;
import com.qwikTix.qwiktix.users.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    UserRepository userRepository;
    @GetMapping("/")
    public String index() {
        return "home";
    }


    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/rent")
    public String rent() {
        return "rent";
    }

    @GetMapping("/events")
    public String events() {
        return "events";
    }

    @GetMapping("/tickets")
    public String tickets() {
        return "tickets";
    }

    @GetMapping("/successRegistration")
    public String successRegistration() {
        return "successRegistration";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password) {
        // Check credentials against the database
        Users user = userRepository.findByEmailAndPassword(email, password);

        if (user != null) {
            // Store user information in Redis
            redisTemplate.opsForValue().set("loggedInEmail:" + user.getId(), email);
            redisTemplate.opsForValue().set("loggedInPassword:" + user.getId(), password);

            // Authentication successful, redirect to a success page
            return "redirect:/";
        } else {
            // Authentication failed, redirect to login page with an error message
            return "redirect:/login?error";
        }
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("register_object", new Users());
        return "register";
    }

    @PostMapping("/register")
    public String processRegistrationForm(@ModelAttribute("register_object") Users user) {
        // Process the registration form data
        // You can access user.getName(), user.getEmail(), user.getPassword(), etc.
        // Encrypt the password using BCryptPasswordEncoder
//        String encodedPassword = asswordEncoder.encode(user.getPassword());
//        user.setPassword(encodedPassword);

        // Print the details to the console for demonstration purposes
        System.out.println("Name: " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Password: " + user.getPassword());
        userRepository.save(user);

        // Redirect to a success page or return a success message
        return "redirect:/successRegistration";
    }
    @GetMapping("/ticketDetails")
    public String ticketDetail(Model model) {
        model.addAttribute("booking_object", new Bookings());

        return "ticketDetails";
    }
    //process the bookings
    @PostMapping("/add_booking")
    public String add_booking(@ModelAttribute Bookings booking) {
        // Check if email and password are set in the session
        String loggedInEmail = (String) redisTemplate.opsForValue().get("loggedInEmail");
        String loggedInPassword = (String) redisTemplate.opsForValue().get("loggedInPassword");

        if (loggedInEmail != null && loggedInPassword != null) {
            // Email and password are set in the session, proceed with the booking
            System.out.println("Good to go");
            System.out.println("Payment Status: " + booking.getPaymentStatus());
            System.out.println("Event ID: " + booking.getEventID());
            System.out.println("User ID: " + booking.getUserID());
            // Unset email and password in the Redis session
            redisTemplate.opsForValue().set("loggedInEmail", null);
            redisTemplate.opsForValue().set("loggedInPassword", null);
            return "success";
        } else {
            // Email or password is not set in the session, redirect to another page
            return "redirect:/login?error=session";
        }
    }


}
