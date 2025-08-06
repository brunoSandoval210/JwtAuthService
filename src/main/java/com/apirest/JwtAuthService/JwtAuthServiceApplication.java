package com.apirest.JwtAuthService;

import com.apirest.JwtAuthService.persistence.entity.Permission;
import com.apirest.JwtAuthService.persistence.entity.Role;
import com.apirest.JwtAuthService.persistence.entity.User;
import com.apirest.JwtAuthService.persistence.enums.RoleEnum;
import com.apirest.JwtAuthService.persistence.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class JwtAuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtAuthServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository) {
		return args -> {
			//crear permisos
			Permission createPermission= Permission.builder()
					.permissionName("CREATE")
					.build();

			Permission readPermission= Permission.builder()
					.permissionName("READ")
					.build();

			Permission updatePermission= Permission.builder()
					.permissionName("UPDATE")
					.build();

			Permission deletePermission= Permission.builder()
					.permissionName("DELETE")
					.build();

			//Crear roles
			Role roleAdmin = Role.builder()
					.roleEnum(RoleEnum.ADMIN)
					.permissions(Set.of(createPermission,readPermission,updatePermission,deletePermission))
					.build();

			Role roleUser= Role.builder()
					.roleEnum(RoleEnum.USER)
					.permissions(Set.of(readPermission))
					.build();

			//Crear usuarios
			User user1 = User.builder()
					.username("BrunoUser")
					.password("$2a$10$FD3d6MJm/qFFMwZap6N2Q.KwHYjQpASB5V7SNB5L5fPibfCDS.noi")
					.isEnabled(true)//El usuario está habilitado
					.accountNoExpired(true) // La cuenta no ha expirado
					.accountNonLocked(true) // La cuenta no está bloqueada
					.credentialsNonExpired(true) // Las credenciales no han expirado
					.roles(Set.of(roleUser))
					.build();

			User user2 = User.builder()
					.username("BrunoAdmin")
					.password("$2a$10$FD3d6MJm/qFFMwZap6N2Q.KwHYjQpASB5V7SNB5L5fPibfCDS.noi")
					.isEnabled(true)//El usuario está habilitado
					.accountNoExpired(true) // La cuenta no ha expirado
					.accountNonLocked(true) // La cuenta no está bloqueada
					.credentialsNonExpired(true) // Las credenciales no han expirado
					.roles(Set.of(roleAdmin))
					.build();



			userRepository.saveAll(List.of(user1, user2));
		};
	}
}
