package org.ai.appointmentbackend.configuration;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Value("${frontend.user.url}")
    private String frontendUserUrl;

    @Value("${frontend.admin.url}")
    private String frontendAdminUrl;

    private final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize

                        // Public endpoints
                        .requestMatchers(
                                "/api/login",
                                "/api/auth/patient",
                                "/api/forgotpassword/send-otp",
                                "/api/forgotpassword/verify-otp",
                                "/api/forgotpassword/reset-password",
                                "/api/doctors",
                                "/api/doctors/getDoctor/{doctorId}",

                                "/api/patients/allPatients",
                                "/api/admin/register/admin"

                        ).permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/admin/doctors/{doctorId}/availability").permitAll()




                        //admin endpoints
                        .requestMatchers(HttpMethod.POST,"/api/admin/doctors/{doctorId}/availability").hasRole("ADMIN")
                        .requestMatchers("/api/admin/register/doctor","/api/admin/dashboard",
                                "/api/admin/allAdmins").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/appointments").hasRole("ADMIN")




                        //patient end points
                        .requestMatchers("/api/pay","/api/verify","/api/appointments/{id}/reschedule",
                                "/api/patients/appointments"

                                ).hasRole("PATIENT")
                        .requestMatchers(HttpMethod.PUT,"/api/patients/{id}").hasAnyRole("PATIENT","ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/appointments").hasRole("PATIENT")



                        //doctor end points
                        .requestMatchers("/api/doctor/appointments").hasRole("DOCTOR")
                        .requestMatchers(HttpMethod.PUT, "/api/doctors/doctor/update-profile").hasAnyRole("DOCTOR","ADMIN")
                        .requestMatchers("/api/doctors/doctor/get-user-profile").hasRole("DOCTOR")
                        .requestMatchers("/api/doctors/doctor/dashboard").hasRole("DOCTOR")


                        .requestMatchers(HttpMethod.GET,"/api/doctor/appointments").hasAnyRole("DOCTOR", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/doctors/{id}").hasAnyRole("DOCTOR", "ADMIN")
                        .requestMatchers("/api/doctors/patientappointments/**").hasRole("PATIENT")


                        // All other requests need authentication
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
     return new CorsConfigurationSource() {
         @Override
         public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
             CorsConfiguration config = new CorsConfiguration();
             config.setAllowCredentials(true);
             config.setAllowedOrigins(Arrays.asList(frontendUserUrl,frontendAdminUrl));
             config.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
             config.setMaxAge(3600L);
             config.setAllowedHeaders(Collections.singletonList("*"));
             config.setExposedHeaders(Collections.singletonList("Authorization"));
             return config;
         }

     };
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
     return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
     return configuration.getAuthenticationManager();
    }


}
