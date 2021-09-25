package com.ap.greenpole.transactioncomponent.repository;

import com.ap.greenpole.transactioncomponent.entity.ClientCompany;
import com.ap.greenpole.transactioncomponent.entity.ShareHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 12:56 PM
 */
public interface ClientCompanyRepository extends PagingAndSortingRepository<ClientCompany, Long> {

    @Query(value = "SELECT * FROM client_company WHERE status = ?1", nativeQuery = true)
    Page<ClientCompany> findByStatusOrderByApprovedAtDesc(String status, Pageable pageable);

    @Query(value = "SELECT * FROM client_company WHERE client_company_id = ?1 and status = ?2", nativeQuery = true)
    ClientCompany findByIdAndStatus(Long id, String status);
}
