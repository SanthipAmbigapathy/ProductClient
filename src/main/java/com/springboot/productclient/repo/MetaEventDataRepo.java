package com.springboot.productclient.repo;

import com.springboot.productclient.data.MetaDataEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetaEventDataRepo extends JpaRepository<MetaDataEvent,String> {
}
