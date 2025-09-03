package com.apirest.JwtAuthService.persistence;

import com.apirest.JwtAuthService.persistence.entity.Resource;
import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

//Esta annotación se utiliza para pruebas de JPA que solo requieren componentes relacionados con la persistenciade datos.
@DataJpaTest
public class ResourceRepositoryTest {

    @Autowired
    private ResourceRepository resourceRepository;

    @DisplayName("Test para listar todos los recursos")
    @Test
    void findAllTest(){

        Resource resource1 = Resource.builder()
                .name("test1")
                .description("resource test")
                .createdAt(LocalDateTime.now())
                .usuReg("test")
                .build();

        Resource resource2 = Resource.builder()
                .name("test2")
                .description("resource test")
                .createdAt(LocalDateTime.now())
                .usuReg("test")
                .build();

        Resource resource3 = Resource.builder()
                .name("test3")
                .description("resource test")
                .createdAt(LocalDateTime.now())
                .usuReg("test")
                .build();

        resourceRepository.saveAll(List.of(resource1, resource2, resource3));

        Pageable pageable = PageRequest.of(0, 10);
        //When
        Page<Resource> resources = resourceRepository.findAll(pageable);

        // Then
        assertNotNull(resources, "La lista de recursos no debe ser nula");
        assertFalse(resources.isEmpty(), "La lista de recursos no debe estar vacía");
        assertEquals(3, resources.getTotalElements(), "La lista de recursos debe contener 3 elementos");
    }

    @DisplayName("Test para encontrar un recurso por su ID")
    @Test
    void findByIdTest() {
        // Given
        Resource resourceToSave = Resource.builder()
                .name("test1")
                .description("resource test")
                .createdAt(LocalDateTime.now())
                .usuReg("test")
                .build();

        Resource savedResource = resourceRepository.save(resourceToSave);

        // When
        Optional<Resource> foundResource = resourceRepository.findById(savedResource.getResourceId());

        // Then
        assertTrue(foundResource.isPresent(), "El recurso debe ser encontrado");
        assertEquals(savedResource.getResourceId(), foundResource.get().getResourceId(), "El ID del recurso debe coincidir");
    }

    @DisplayName("Test para encontrar un recurso por su nombre")
    @Test
    void findByNameTest(){
        // Given
        Resource resourceToSave = Resource.builder()
                .name("test1")
                .description("resource test")
                .createdAt(LocalDateTime.now())
                .usuReg("test")
                .build();

        Resource savedResource = resourceRepository.save(resourceToSave);

        // When
        Optional<Resource> foundResource = resourceRepository.findByName(savedResource.getName());

        // Then
        assertTrue(foundResource.isPresent(), "El recurso debe ser encontrado");
        assertEquals(savedResource.getName(), foundResource.get().getName(), "El nombre del recurso debe coincidir");
    }

    @DisplayName("Test para verificar la existencia de un recurso por nombre")
    @Test
    void existsByNameTest(){
        // Given
        Resource resource = Resource.builder()
                .name("test1")
                .description("resource test")
                .createdAt(LocalDateTime.now())
                .usuReg("test")
                .build();

        resourceRepository.save(resource);

        // When
        boolean exists = resourceRepository.existsByName(resource.getName());

        // Then
        assertTrue(exists, "El recurso debe existir por nombre");
    }

    @DisplayName("Test para guardar un recurso")
    @Test
    void saveTest() {
        //Given
        Resource resource = Resource.builder()
                .name("test2")
                .description("resource test2")
                .createdAt(LocalDateTime.now())
                .usuReg("test")
                .build();

        //When
        Resource savedResource = resourceRepository.save(resource);

        //Then

        assertAll(()->{
            assertNotNull(savedResource, "El recurso guardado no debe ser nulo");
            assertTrue(savedResource.getResourceId() > 0, "El ID del recurso guardado debe ser mayor que 0");
        });

    }

    @DisplayName("Test para actualizar un recurso")
    @Test
    void updateTest() {
        // Given
        Resource resourceToSave = Resource.builder()
                .name("test")
                .description("resource test")
                .createdAt(LocalDateTime.now())
                .usuReg("test")
                .build();

        Resource savedResource = resourceRepository.save(resourceToSave);

        // When
        savedResource.setName("test updated");
        savedResource.setUpdatedAt(LocalDateTime.now());
        savedResource.setUsuMod("test mod");

        Resource updatedResource = resourceRepository.save(savedResource);

        // Then
        assertAll(()->{
            assertNotNull(updatedResource, "El recurso actualizado no debe ser nulo");
            assertEquals("test updated", updatedResource.getName(), "El nombre del recurso debe ser 'test updated'");
        });
    }

    @DisplayName("Test para eliminar un recurso por ID")
    @Test
    void deleteTest() {
        // Given
        Resource resource = Resource.builder()
                .name("test_delete")
                .description("resource test")
                .createdAt(LocalDateTime.now())
                .usuReg("test")
                .build();

        resourceRepository.save(resource);

        // When
        resourceRepository.deleteById(resource.getResourceId());
        Optional<Resource> foundResource = resourceRepository.findById(resource.getResourceId());

        // Then
        assertTrue(foundResource.isEmpty(), "El recurso debe haber sido eliminado");
    }

}
