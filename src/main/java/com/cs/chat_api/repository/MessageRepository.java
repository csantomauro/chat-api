package com.cs.chat_api.repository;

import com.cs.chat_api.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByRoomIdOrderBySentAtAsc(Long roomId);
}
