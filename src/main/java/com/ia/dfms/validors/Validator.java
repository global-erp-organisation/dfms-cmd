package com.ia.dfms.validors;

import lombok.Builder;
import lombok.Data;

public interface Validator<E, V> {
    Result<E, V> validate();

    @Data
    @Builder
    static class Result<E, V> {
        private Boolean isValid;
        private E errors;
        private V validated;
    }

    default Result<E,V> build(E errors, V validated, Boolean isValid) {
        return Result.<E,V>builder().errors(errors).validated(validated).isValid(isValid).build();
    }
}
