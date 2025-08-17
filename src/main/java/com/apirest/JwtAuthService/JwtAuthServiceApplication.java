package com.apirest.JwtAuthService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JwtAuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtAuthServiceApplication.class, args);
	}

//	@Bean
//	CommandLineRunner init(UserRepository userRepository) {
//		return args -> {
//			//crear permisos
//			Permission createPermission= Permission.builder()
//					.permissionName("CREATE")
//					.createdAt(LocalDateTime.now())
//					.build();
//
//			Permission readPermission= Permission.builder()
//					.permissionName("READ")
//					.createdAt(LocalDateTime.now())
//					.build();
//
//			Permission updatePermission= Permission.builder()
//					.permissionName("UPDATE")
//					.createdAt(LocalDateTime.now())
//					.build();
//
//			Permission deletePermission= Permission.builder()
//					.permissionName("DELETE")
//					.createdAt(LocalDateTime.now())
//					.build();
//
//			//Crear roles
//			Role roleAdmin = Role.builder()
//					.role("ADMIN")
//					.permissions(Set.of(createPermission,readPermission,updatePermission,deletePermission))
//					.createdAt(LocalDateTime.now())
//					.build();
//
//			Role roleUser= Role.builder()
//					.role("USER")
//					.permissions(Set.of(readPermission))
//					.createdAt(LocalDateTime.now())
//					.build();
//
//			//Crear usuarios
//			User user1 = User.builder()
//					.username("BrunoUser")
//					.password("$2a$10$FD3d6MJm/qFFMwZap6N2Q.KwHYjQpASB5V7SNB5L5fPibfCDS.noi")
//					.isEnabled(true)//El usuario está habilitado
//					.accountNoExpired(true) // La cuenta no ha expirado
//					.accountNonLocked(true) // La cuenta no está bloqueada
//					.credentialsNonExpired(true) // Las credenciales no han expirado
//					.roles(Set.of(roleUser))
//					.createdAt(LocalDateTime.now())
//					.build();
//
//			User user2 = User.builder()
//					.username("BrunoAdmin")
//					.password("$2a$10$FD3d6MJm/qFFMwZap6N2Q.KwHYjQpASB5V7SNB5L5fPibfCDS.noi")
//					.isEnabled(true)//El usuario está habilitado
//					.accountNoExpired(true) // La cuenta no ha expirado
//					.accountNonLocked(true) // La cuenta no está bloqueada
//					.credentialsNonExpired(true) // Las credenciales no han expirado
//					.roles(Set.of(roleAdmin))
//					.createdAt(LocalDateTime.now())
//					.build();
//
//
//
//			userRepository.saveAll(List.of(user1, user2));
//		};
//	}
}
