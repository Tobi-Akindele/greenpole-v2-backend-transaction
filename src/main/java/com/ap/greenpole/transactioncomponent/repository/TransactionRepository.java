package com.ap.greenpole.transactioncomponent.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ap.greenpole.transactioncomponent.entity.Transaction;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 11:46 AM
 */
public interface TransactionRepository  extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByTransactionRequestId(long transactionRequestId);

    List<Transaction> findByTransactionId(String transactionId);

	@Query(value = "SELECT * FROM cscs_transaction WHERE chn = ?1 AND cscs_transaction_request_id = ?2 AND sell_or_buy = ?3", nativeQuery = true)
	List<Transaction> findByCHNAndTransactionRequestAndSellOrBuy(String chn, Long transactionRequestId, String sellOrBuy);

	@Query(value = "SELECT * FROM cscs_transaction WHERE sell_or_buy = ?2 AND transaction_id = ?1", nativeQuery = true)
	Transaction findByTransactionIdAndSellOrBuy(String transactionId, String sellOrBuy);


    @Query(value = "SELECT  distinct on (trans_req.id) trans_req.id, trans_req.transaction_type as transaction_type, trans_req.client_company as client_company,\n" +
			"        trans_req.transaction_date as transaction_date, pro_trans.transaction_id as cscs_transaction_id,\n" +
			"        (SELECT trans.chn WHERE trans.sell_or_buy = '-') as seller_chn, pro_trans.buyer_chn as buyer_chn,\n" +
			"        pro_trans.seller_unit_before as seller_unit_before, pro_trans.seller_unit_after as seller_unit_after,\n" +
			"        pro_trans.buyer_unit_before as buyer_unit_before, pro_trans.buyer_unit_after as buyer_unit_after,\n" +
			"        pro_trans.processed_on as processed_on, pro_trans.unit_transacted as unit_transacted,\n" +
			"        pro_trans.is_cancelled as is_cancelled, pro_trans.is_processed as is_processed\n" +
			"FROM processed_transaction pro_trans\n" +
			"        INNER JOIN cscs_transaction trans  ON pro_trans.cscs_transaction_table_id = trans.id\n" +
			"        INNER JOIN cscs_transaction_request trans_req ON trans.cscs_transaction_request_id = trans_req.id\n" +
			"        INNER JOIN cscs_master trans_master  ON trans.chn = trans_master.chn --AND pro_trans.cscs_transaction_table_id != trans.id\n" +
			"        INNER JOIN shareholder share_holder ON trans_master.chn = share_holder.clearing_housing_number\n" +
			"        INNER JOIN client_company client_company ON share_holder.client_company = client_company.client_company_id\n" +
			"WHERE\n" +
			"              (trans.sell_or_buy = '-' AND trans.chn LIKE ?5)\n" +
			"              OR (pro_trans.buyer_chn LIKE ?6)\n" +
			"              OR (trans.sell_or_buy = '-' AND trans.transaction_id LIKE ?8)\n" +
			"              OR (pro_trans.transaction_id LIKE ?7)\n" +
			"              OR (trans.sell_or_buy = '-' AND share_holder.shareholder_id = ?9)\n" +
			"              OR (pro_trans.buyer_account_number = ?10)\n" +
			"              OR (trans_req.transaction_date = ?11 )\n" +
			"              OR (trans_req.transaction_date = ?13 )\n" +
			"              OR (trans_req.transaction_date < ?12 )\n" +
			"              OR (trans_req.transaction_date > ?14 )\n" +
			"              OR (client_company.register_name LIKE ?1 )\n" +
			"              OR (client_company.register_code LIKE ?2 )\n" +
			"              OR (trans_master.names LIKE ?3 )\n" +
			"              OR (pro_trans.buyer_name LIKE ?4 )", nativeQuery = true)
    List<Object[]> findFilteredTransaction( String clientCompanyName, String clientCompanyCode, String fromShareHolderName,
                                           String toShareHolderName, String fromShareHolderChn, String toShareHolderChn,
                                           String buyerCSCSTransactionId, String sellerCSCSTransactionId, Long fromShareHolderCompanyAccount,
                                           Long toShareHolderCompanyAccount, Date dateOn, Date dateBefore, Date uploadDate, Date uploadAfter, Pageable pageable);

	List<Transaction> findAllTransactionByCHN(String CHN);

    @Query(value = "SELECT   trans.transaction_id as cscs_transaction_id, trans_req.upload_date as date, trans_req.transaction_date as transDate,\n" +
            "        client_company.register_name as clientcompany, share_holder.share_unit, proc_trans.suspension_reason as reason,\n" +
            "        (SELECT '[' ||trans.chn || ']' || ' ' || '(' || share_holder.shareholder_id || ')' || ' ' || trans_master.names  WHERE trans.sell_or_buy = '-') as seller,\n" +
            "        (SELECT '[' ||proc_trans.buyer_chn|| ']' || ' ' || '(' || proc_trans.buyer_account_number || ')' || ' ' || proc_trans.buyer_name) as buyer,\n" +
            "        proc_trans.is_cancelled as cancel\n" +
            "FROM cscs_master trans_master\n" +
            "  INNER JOIN cscs_transaction trans  ON trans.chn = trans_master.chn\n" +
            "  LEFT JOIN processed_transaction proc_trans ON trans.id = proc_trans.cscs_transaction_table_id\n" +
            "  LEFT JOIN cscs_transaction_request trans_req ON trans_master.cscs_transaction_request_id = trans_req.id\n" +
            "  LEFT JOIN shareholder share_holder ON trans_master.chn = share_holder.clearing_housing_number\n" +
            "  LEFT JOIN client_company client_company ON share_holder.client_company = client_company.client_company_id\n" +
            "WHERE\n" +
            "  (proc_trans.is_suspended = TRUE) AND (proc_trans.is_cancelled = FALSE ) AND\n" +
            "  (\n" +
            "    (trans.sell_or_buy = '+' AND trans.chn LIKE ?6)\n" +
            "    OR (trans.sell_or_buy = '-' AND trans.chn LIKE ?5)\n" +
            "    OR (trans.sell_or_buy = '+' AND trans_master.names LIKE ?4)\n" +
            "    OR (trans.sell_or_buy = '-' AND trans_master.names LIKE ?3)\n" +
            "    OR (trans.sell_or_buy = '+' AND share_holder.client_company = ?10)\n" +
            "    OR (trans.sell_or_buy = '-' AND share_holder.client_company = ?9)\n" +
            "    OR (trans.sell_or_buy = '+' AND trans.transaction_id LIKE ?7)\n" +
            "    OR (trans.sell_or_buy = '-' AND trans.transaction_id LIKE ?8)\n" +
            "    OR (client_company.register_name LIKE ?1)\n" +
            "    OR (client_company.register_code LIKE ?2)\n" +
            "  )", nativeQuery = true)
    List<Object[]> findSupendedTransactions(String clientCompanyName, String clientCompanyCode, String fromShareHolderName,
                                                              String toShareHolderName, String fromShareHolderChn, String toShareHolderChn,
                                                              String buyerCSCSTransactionId, String sellerCSCSTransactionId, Long fromShareHolderCompanyAccount,
                                                              Long toShareHolderCompanyAccount);

	@Query(value = "SELECT   trans.transaction_id as cscs_transaction_id, trans_req.transaction_type as transaction_type, trans_req.transaction_date as trans_date,\n" +
			"        client_company.register_name as clientcompany, share_holder.share_unit, proc_trans.suspension_reason as reason,\n" +
			"        (SELECT '[' ||trans.chn || ']' || ' ' || '(' || share_holder.shareholder_id || ')' || ' ' || trans_master.names  WHERE trans.sell_or_buy = '-') as seller,\n" +
			"        (SELECT '[' ||proc_trans.buyer_chn|| ']' || ' ' || '(' || proc_trans.buyer_account_number || ')' || ' ' || proc_trans.buyer_name) as buyer,\n" +
			"        proc_trans.is_cancelled as cancel, proc_trans.suspended_on as suspension_date\n" +
			"FROM cscs_master trans_master\n" +
			"  INNER JOIN cscs_transaction trans  ON trans.chn = trans_master.chn\n" +
			"  LEFT JOIN processed_transaction proc_trans ON trans.id = proc_trans.cscs_transaction_table_id\n" +
			"  LEFT JOIN cscs_transaction_request trans_req ON trans_master.cscs_transaction_request_id = trans_req.id\n" +
			"  LEFT JOIN shareholder share_holder ON trans_master.chn = share_holder.clearing_housing_number\n" +
			"  LEFT JOIN client_company client_company ON share_holder.client_company = client_company.client_company_id\n" +
			"WHERE\n" +
			"  (proc_trans.is_processed = TRUE) AND (proc_trans.is_suspended = TRUE) AND (proc_trans.is_cancelled = TRUE ) AND\n" +
			"  (\n" 
//			"    (trans_req.transaction_date = ?1 )\n" +
//			"    OR "
			+ "(share_holder.client_company = ?1)\n" +
			"  )", nativeQuery = true)
//	List<Object[]> findCancelledSuspendedTransactions(Date transDate, long clientCompany);
	List<Object[]> findCancelledSuspendedTransactions(long clientCompany);
}
