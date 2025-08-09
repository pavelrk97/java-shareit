package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    @Query("select distinct ir from ItemRequest ir left join fetch ir.items where ir.requester.id = :userId")
    List<ItemRequest> findAllByRequesterIdWithItems(@Param("userId") Long userId);
}