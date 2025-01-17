package be.kdg.sa.clients.repositories;

import be.kdg.sa.clients.controller.dto.OrderDto;
import be.kdg.sa.clients.domain.Account;
import be.kdg.sa.clients.domain.Order;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    Optional<Order> findOrderByOrderId(UUID orderId);

    @Query("SELECT  o FROM Order o WHERE o.account.accountId = :accountId")
    List<Order> findAllByAccountId(UUID accountId);

    @Transactional
    @Modifying
    @Query("UPDATE Order o SET o.account.accountId = null WHERE o.account.accountId = :accountId")
    void nullifyAccountId(@Param("accountId") UUID accountId);

    @Query("SELECT o FROM Order o WHERE o.creationDateTime = :orderDate")
    List<Order> findAllByCreationDateTime(LocalDateTime orderDate);

}
