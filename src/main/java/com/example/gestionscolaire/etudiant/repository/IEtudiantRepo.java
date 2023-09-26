package com.example.gestionscolaire.etudiant.repository;

import com.example.gestionscolaire.etudiant.model.Etudiants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IEtudiantRepo extends JpaRepository<Etudiants, Long> {
    Optional<Etudiants> findByMatricule(String matricule);
}
