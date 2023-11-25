package yuzu.ticktaskbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yuzu.ticktaskbot.models.NotificationTask;

public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Long> {
}
