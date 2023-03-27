package ch.bbw.m183.vulnerapp.repository;

import java.util.UUID;

import ch.bbw.m183.vulnerapp.datamodel.BlogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<BlogEntity, UUID> {

}
