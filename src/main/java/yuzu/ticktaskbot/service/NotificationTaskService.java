package yuzu.ticktaskbot.service;

import org.springframework.stereotype.Service;
import yuzu.ticktaskbot.models.NotificationTask;
import yuzu.ticktaskbot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationTaskService {
    private final NotificationTaskRepository repository;

    public NotificationTaskService(NotificationTaskRepository repository) {
        this.repository = repository;
    }

    public NotificationTask add(NotificationTask task) {
        task.setId(null);
        return repository.save(task);
    }

    public NotificationTask add(Long chatId, String text, LocalDateTime dateTime) {
        NotificationTask task = new NotificationTask(null, chatId, text, dateTime);
        return repository.save(task);
    }

    public List<NotificationTask> findByChatId(Long chatId) {
        return repository.findAllByChatIdOrderByDateTime(chatId);
    }
}
