package com.codezap.client;

import com.fasterxml.jackson.databind.JsonNode;

@FunctionalInterface
public interface MakeResponse<T> {

    T make(JsonNode jsonResponse);
}
