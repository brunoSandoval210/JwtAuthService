package com.apirest.JwtAuthService.util;

import java.util.List;

public interface CrudService<A, B, C, D, E, F>{
    List<A> getAll();
    A getBy(B b);
    A save(C c, D d);
    A update(E e, F f, D d);
    A delete(E e, D d);
}
