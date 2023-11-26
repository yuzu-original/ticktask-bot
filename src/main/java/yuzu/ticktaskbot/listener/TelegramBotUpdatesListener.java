package yuzu.ticktaskbot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import okhttp3.internal.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import yuzu.ticktaskbot.models.NotificationTask;
import yuzu.ticktaskbot.service.NotificationTaskService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final Pattern pattern = Pattern.compile("([0-9.:\\s]{16})(\\s)(.+)");

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final NotificationTaskService service;

    private final TelegramBot bot;

    public TelegramBotUpdatesListener(NotificationTaskService service, TelegramBot bot) {
        this.service = service;
        this.bot = bot;
    }

    @PostConstruct
    public void init() {
        bot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);

            String text = update.message().text();
            if (text.equals("/start")) {
                processStart(update);
            } else if (text.equals("/all")) {
                processGetAll(update);
            } else {
                processMessage(update);
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void processStart(Update update) {
        logger.info("Processing /start command");
        SendMessage request = new SendMessage(update.message().chat().id(), "Hi!");
        bot.execute(request);
    }

    private void processGetAll(Update update) {
        logger.info("Processing /all command");
        Long chatId = update.message().chat().id();
        List<NotificationTask> tasks = service.findByChatId(chatId);
        String text = tasks.stream()
                .map(t -> t.getDateTime().format(dateTimeFormatter) + " " + t.getText())
                .collect(Collectors.joining("\n"));
        SendMessage request = new SendMessage(chatId, text);
        bot.execute(request);
    }

    private void processMessage(Update update) {
        String messageText = update.message().text();
        Long chatId = update.message().chat().id();

        Matcher matcher = pattern.matcher(messageText);
        if (matcher.matches()) {
            logger.info("Processing command to add a new task");
            LocalDateTime dateTime = LocalDateTime.parse(matcher.group(1), dateTimeFormatter);
            String text = matcher.group(3);
            service.add(chatId, text, dateTime);

            SendMessage request = new SendMessage(chatId, "Added task: " + text);
            bot.execute(request);
            return;
        }

        logger.info("Processing unknown command");
        SendMessage request = new SendMessage(chatId, ":v");
        bot.execute(request);
    }

}