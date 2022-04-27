package br.edu.ifpb.foodstore.repository;

import br.edu.ifpb.foodstore.domain.Product;
import br.edu.ifpb.foodstore.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
}
