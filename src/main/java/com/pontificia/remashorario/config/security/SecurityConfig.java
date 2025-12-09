//package com.pontificia.remashorario.config.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import com.pontificia.remashorario.modules.user.UserService;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import java.util.List;
//
//@Configuration
//@EnableMethodSecurity
//public class SecurityConfig {
//
//    private final JwtAuthenticationFilter jwtAuthenticationFilter;
//    private final UserService userService;
//
//    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, UserService userService) {
//        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
//        this.userService = userService;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//                .csrf(csrf -> csrf.disable())
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(auth -> auth
//
//                        // ==================== AUTENTICACIÓN ====================
//                        .requestMatchers("/api/auth/**").permitAll()
//
//                        // ==================== PERFIL DE USUARIO ====================
//                        .requestMatchers("/api/protected/me/**")
//                        .hasAnyRole("TEACHER", "ASSISTANT", "COORDINATOR", "ACCOUNTANT")
//
//                        // ==================== PERÍODOS ACADÉMICOS ====================
//                        .requestMatchers(HttpMethod.GET, "/api/protected/periods/active")
//                        .hasAnyRole("TEACHER", "ASSISTANT", "COORDINATOR")
//
//                        // ==================== ENDPOINTS PARA DOCENTES ====================
//
//                        // ✅ Docentes pueden ver sus propias clases asignadas
//                        .requestMatchers(HttpMethod.GET, "/api/protected/class-sessions/teacher/**")
//                        .hasAnyRole("TEACHER", "ASSISTANT", "COORDINATOR")
//
//                        // Asistencias - Contador puede ver y gestionar
//                        .requestMatchers(HttpMethod.GET, "/api/protected/teacher-attendances/**")
//                       .hasAnyRole("ASSISTANT", "COORDINATOR", "ACCOUNTANT")
//
//                        // Aprobar/modificar asistencias
//                       .requestMatchers(HttpMethod.PATCH, "/api/protected/teacher-attendances/*/approve")
//                       .hasAnyRole("ASSISTANT", "COORDINATOR", "ACCOUNTANT")
//
//                       // Acceso a nómina
//                      .requestMatchers("/api/protected/payroll-periods/**")
//                      .hasAnyRole("ASSISTANT", "COORDINATOR", "ACCOUNTANT")
//
//                      .requestMatchers("/api/protected/payroll-lines/**")
//                      .hasAnyRole("ASSISTANT", "COORDINATOR", "ACCOUNTANT")
//
//                     // Gestión de tarifas (solo lectura para ACCOUNTANT)
//                    .requestMatchers(HttpMethod.GET, "/api/protected/teacher-rates/**")
//                     .hasAnyRole("ASSISTANT", "COORDINATOR", "ACCOUNTANT")
//
//
//                     // Modificar tarifas (solo COORDINATOR y ACCOUNTANT)
//                     .requestMatchers(HttpMethod.POST, "/api/protected/teacher-rates/**")
//                     .hasAnyRole("COORDINATOR", "ACCOUNTANT")
//
//                        // ✅ Docentes pueden gestionar su propia asistencia
//                        .requestMatchers(HttpMethod.GET, "/api/protected/teacher-attendances/teacher/**")
//                        .hasAnyRole("TEACHER", "ASSISTANT", "COORDINATOR")
//                        .requestMatchers(HttpMethod.POST, "/api/protected/teacher-attendances/check-in")
//                        .hasAnyRole("TEACHER", "ASSISTANT", "COORDINATOR")
//                        .requestMatchers(HttpMethod.POST, "/api/protected/teacher-attendances/check-in-with-schedule")
//                        .hasAnyRole("TEACHER", "ASSISTANT", "COORDINATOR")
//
//                        // ✅✅ CORREGIDO: Usar ** en lugar de * para el UUID
//                        .requestMatchers(HttpMethod.PATCH, "/api/protected/teacher-attendances/*/check-out")
//                        .hasAnyRole("TEACHER", "ASSISTANT", "COORDINATOR")
//
//                        // ✅ Docentes pueden consultar feriados
//                        .requestMatchers(HttpMethod.GET, "/api/protected/calendar-exceptions/is-holiday/**")
//                        .hasAnyRole("TEACHER", "ASSISTANT", "COORDINATOR")
//                        .requestMatchers(HttpMethod.GET, "/api/protected/calendar-exceptions/date/**")
//                        .hasAnyRole("TEACHER", "ASSISTANT", "COORDINATOR")
//
//                        // ==================== ENDPOINTS SOLO PARA ADMINS ====================
//
//                        // ✅ Solo administradores pueden aprobar/modificar asistencias
////                        .requestMatchers(HttpMethod.PATCH, "/api/protected/teacher-attendances/**/approve")
////                        .hasAnyRole("ASSISTANT", "COORDINATOR")
////                        .requestMatchers(HttpMethod.PATCH, "/api/protected/teacher-attendances/**/override")
////                        .hasAnyRole("ASSISTANT", "COORDINATOR")
////                        .requestMatchers(HttpMethod.PATCH, "/api/protected/teacher-attendances/**/mark-holiday")
////                        .hasAnyRole("ASSISTANT", "COORDINATOR")
////                        .requestMatchers(HttpMethod.PATCH, "/api/protected/teacher-attendances/**/reject")
////                        .hasAnyRole("ASSISTANT", "COORDINATOR")
//
//                        .requestMatchers(HttpMethod.PATCH, "/api/protected/teacher-attendances/*/approve")
//                        .hasAnyRole("ASSISTANT", "COORDINATOR")
//                        .requestMatchers(HttpMethod.PATCH, "/api/protected/teacher-attendances/*/override")
//                        .hasAnyRole("ASSISTANT", "COORDINATOR")
//                        .requestMatchers(HttpMethod.PATCH, "/api/protected/teacher-attendances/*/mark-holiday")
//                        .hasAnyRole("ASSISTANT", "COORDINATOR")
//                        .requestMatchers(HttpMethod.PATCH, "/api/protected/teacher-attendances/*/reject")
//                        .hasAnyRole("ASSISTANT", "COORDINATOR")
//
//                        // ==================== DISPONIBILIDADES DE DOCENTES ====================
//                        .requestMatchers("/api/protected/teachers/{teacherUuid}/availabilities/**")
//                        .hasAnyRole("TEACHER", "ASSISTANT", "COORDINATOR")
//                        .requestMatchers("/api/protected/teachers/availabilities/**")
//                        .hasAnyRole("TEACHER", "ASSISTANT", "COORDINATOR")
//
//                        // ==================== ELIMINACIONES (SOLO COORDINATOR) ====================
//                        .requestMatchers(HttpMethod.DELETE, "/api/protected/**")
//                        .hasRole("COORDINATOR")
//
//                        // ==================== OTROS ENDPOINTS PROTEGIDOS ====================
//                        .requestMatchers("/api/protected/**")
//                        .hasAnyRole("TEACHER", "COORDINATOR", "ASSISTANT")
//
//                                // ==================== CUALQUIER OTRA PETICIÓN ====================
//                        .anyRequest().authenticated()
//                )
//                .userDetailsService(userService)
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowedOrigins(List.of("http://localhost:4200"));
//        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
//        config.setAllowedHeaders(List.of("Authorization","Content-Type"));
//        config.setAllowCredentials(true);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }
//}

package com.pontificia.remashorario.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.pontificia.remashorario.modules.user.UserService;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserService userService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, UserService userService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // ==================== AUTENTICACIÓN ====================
                        .requestMatchers("/api/auth/**").permitAll()

                        // ==================== PERFIL DE USUARIO ====================
                        .requestMatchers("/api/protected/me/**")
                        .hasAnyRole("TEACHER", "ASSISTANT", "COORDINATOR", "ACCOUNTANT")

                        // ==================== PERÍODOS ACADÉMICOS ====================
                        .requestMatchers(HttpMethod.GET, "/api/protected/periods/active")
                        .hasAnyRole("TEACHER", "ASSISTANT", "COORDINATOR")

                        // ==================== ENDPOINTS PARA DOCENTES ====================
                        // ⚠️ IMPORTANTE: Las reglas más ESPECÍFICAS van PRIMERO

                        // ✅ Docentes pueden ver sus propias clases asignadas
                        .requestMatchers(HttpMethod.GET, "/api/protected/class-sessions/teacher/**")
                        .hasAnyRole("TEACHER", "ASSISTANT", "COORDINATOR","ACCOUNTANT")

                        // ✅ Docentes pueden gestionar su propia asistencia (ESPECÍFICO)
                        .requestMatchers(HttpMethod.GET, "/api/protected/teacher-attendances/teacher/**")
                        .hasAnyRole("TEACHER", "ASSISTANT", "COORDINATOR")

                        .requestMatchers(HttpMethod.POST, "/api/protected/teacher-attendances/check-in")
                        .hasAnyRole("TEACHER", "ASSISTANT", "COORDINATOR")

                        .requestMatchers(HttpMethod.POST, "/api/protected/teacher-attendances/check-in-with-schedule")
                        .hasAnyRole("TEACHER", "ASSISTANT", "COORDINATOR")

                        .requestMatchers(HttpMethod.PATCH, "/api/protected/teacher-attendances/*/check-out")
                        .hasAnyRole("TEACHER", "ASSISTANT", "COORDINATOR")

                        // ✅ Docentes pueden consultar feriados
                        .requestMatchers(HttpMethod.GET, "/api/protected/calendar-exceptions/is-holiday/**")
                        .hasAnyRole("TEACHER", "ASSISTANT", "COORDINATOR")

                        .requestMatchers(HttpMethod.GET, "/api/protected/calendar-exceptions/date/**")
                        .hasAnyRole("TEACHER", "ASSISTANT", "COORDINATOR")

                        // ==================== ENDPOINTS PARA ADMINISTRACIÓN Y CONTABILIDAD ====================
                        // ⚠️ Estas reglas van DESPUÉS de las específicas de docentes

                        // Asistencias - Contador y admins pueden ver TODAS (GENÉRICO)
                        .requestMatchers(HttpMethod.GET, "/api/protected/teacher-attendances/**")
                        .hasAnyRole("ASSISTANT", "COORDINATOR", "ACCOUNTANT")

                        // Aprobar/modificar asistencias
                        .requestMatchers(HttpMethod.PATCH, "/api/protected/teacher-attendances/*/approve")
                        .hasAnyRole("ASSISTANT", "COORDINATOR", "ACCOUNTANT")

                        .requestMatchers(HttpMethod.PATCH, "/api/protected/teacher-attendances/*/override")
                        .hasAnyRole("ASSISTANT", "COORDINATOR")

                        .requestMatchers(HttpMethod.PATCH, "/api/protected/teacher-attendances/*/mark-holiday")
                        .hasAnyRole("ASSISTANT", "COORDINATOR")

                        .requestMatchers(HttpMethod.PATCH, "/api/protected/teacher-attendances/*/reject")
                        .hasAnyRole("ASSISTANT", "COORDINATOR")

                        // ==================== NÓMINA Y CONTABILIDAD ====================
                        // Acceso a nómina
                        .requestMatchers("/api/protected/payroll-periods/**")
                        .hasAnyRole("ASSISTANT", "COORDINATOR", "ACCOUNTANT")

                        .requestMatchers("/api/protected/payroll-lines/**")
                        .hasAnyRole("ASSISTANT", "COORDINATOR", "ACCOUNTANT")

                        // Gestión de tarifas (solo lectura para ACCOUNTANT)
                        .requestMatchers(HttpMethod.GET, "/api/protected/teacher-rates/**")
                        .hasAnyRole("ASSISTANT", "COORDINATOR", "ACCOUNTANT")

                        // Modificar tarifas (solo COORDINATOR y ACCOUNTANT)
                        .requestMatchers(HttpMethod.POST, "/api/protected/teacher-rates/**")
                        .hasAnyRole("COORDINATOR", "ACCOUNTANT")

                        .requestMatchers(HttpMethod.PUT, "/api/protected/teacher-rates/**")
                        .hasAnyRole("COORDINATOR", "ACCOUNTANT")

                        // ==================== DISPONIBILIDADES DE DOCENTES ====================
                        .requestMatchers("/api/protected/teachers/{teacherUuid}/availabilities/**")
                        .hasAnyRole("TEACHER", "ASSISTANT", "COORDINATOR")

                        .requestMatchers("/api/protected/teachers/availabilities/**")
                        .hasAnyRole("TEACHER", "ASSISTANT", "COORDINATOR")

                        // ==================== ELIMINACIONES (SOLO COORDINATOR) ====================
                        .requestMatchers(HttpMethod.DELETE, "/api/protected/**")
                        .hasRole("COORDINATOR")

                        // ==================== OTROS ENDPOINTS PROTEGIDOS ====================
                        .requestMatchers("/api/protected/**")
                        .hasAnyRole("TEACHER", "COORDINATOR", "ASSISTANT", "ACCOUNTANT")

                        // ==================== CUALQUIER OTRA PETICIÓN ====================
                        .anyRequest().authenticated()
                )
                .userDetailsService(userService)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}