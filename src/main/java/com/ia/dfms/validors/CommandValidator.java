package com.ia.dfms.validors;

import java.util.Optional;

import com.ia.dfms.util.AggregateUtil;

import lombok.Builder;
import lombok.Data;

public abstract class CommandValidator<E, V> {

    protected Result<E, V> validate() {
        return build(null, Optional.<V>empty(), true);
    }

    protected Result<E, V> validate(AggregateUtil util) {
        return validate();
    }

    protected Result<E, V> build(E errors, Optional<V> validated, Boolean isValid) {
        return Result.<E, V>builder().errors(errors).validated(validated).isValid(isValid).build();
    }

    @Data
    @Builder
    public static class Result<E, V> {
        private Boolean isValid;
        private E errors;
        private Optional<V> validated;
    }
}
