package ru.ermolaayyyyyyy.leschats.dataaccesslayer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ermolaayyyyyyy.leschats.dataaccesslayer.entities.Owner;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Integer> {
}

