package fr.epita.eventure.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = { "id" })
public interface CredentialRepresentationMixin {
    // This interface is empty, it only serves to apply the annotation
}