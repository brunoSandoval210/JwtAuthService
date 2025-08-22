package com.apirest.JwtAuthService.util;

import org.springframework.data.domain.Pageable;


public interface CrudService<A, B, C, D, E, F>{
    PageResponse<A> getAll(Pageable pageable);
    A getBy(B b);
    A getById(Long id);
    A save(C c, D d);
    A update(E e, F f, D d);
    A delete(E e, D d);
}
