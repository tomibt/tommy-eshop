package com.example.demo.siteInfo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteInfoRepository extends JpaRepository<SiteInfo, Long>{
	
	Optional<SiteInfo> findById(Long id);
	
	boolean existsById(Long id);

}
