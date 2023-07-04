package com.agebra.boonbaebackend.repository;

import com.agebra.boonbaebackend.domain.RecycleConfirm;
import com.agebra.boonbaebackend.domain.RecycleConfirmStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecycleConfirmRepository extends JpaRepository<RecycleConfirm, Long> {

  Page<RecycleConfirm> findByStatus(RecycleConfirmStatus status, Pageable pageable);
}
