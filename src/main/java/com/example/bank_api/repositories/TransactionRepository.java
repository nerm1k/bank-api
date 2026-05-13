package com.example.bank_api.repositories;

import com.example.bank_api.models.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {


    //Не учтен TRANSFER, если деньги придут на счет.
    List<TransactionEntity> findAllTransactionsByAccountFromIdOrAccountToId(Long accountFromId, Long accountToId);
    //и передавать одинаковые айдишники )).
    // Но это некрасиво, лучше так:
    @Query("SELECT t FROM TransactionEntity t " +
            "WHERE t.accountFrom.id = :accountId OR t.accountTo.id = :accountId")
    List<TransactionEntity> findAllByAccountId(@Param("accountId") Long accountId);


    List<TransactionEntity> findAllTransactionsByAccountFromId(Long accountId);
}
