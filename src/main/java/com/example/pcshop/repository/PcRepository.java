package com.example.pcshop.repository;

import com.example.pcshop.model.Pc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PcRepository extends JpaRepository<Pc, Long> {}
