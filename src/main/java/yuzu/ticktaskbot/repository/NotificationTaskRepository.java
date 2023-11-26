package yuzu.ticktaskbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yuzu.ticktaskbot.models.NotificationTask;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Long> {
    List<NotificationTask> findAllByChatIdOrderByDateTime(Long chatId);

    List<NotificationTask> findAllByDateTime(LocalDateTime dateTime);
}
