package com.vlaados.buttons.button;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.vlaados.buttons.ButtonHandler;
import com.vlaados.local.LocalManager;

import java.util.HashMap;
import java.util.Map;

public class changePhone implements ButtonHandler {

    private final TelegramBot bot;
    private final Map<Long, Boolean> awaitingPhoneInput = new HashMap<>();
    private String language = "ru"; // TODO: заменить на запрос из БД

    public changePhone(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public boolean canHandle(String buttonData) {
        return "changePhone".equalsIgnoreCase(buttonData);
    }

    @Override
    public void handle(Update update) {
        long chatId = update.callbackQuery().message().chat().id();

        awaitingPhoneInput.put(chatId, true);

        LocalManager localManager = new LocalManager(language);
        String changePhoneText = localManager.getMessage("changePhoneText");

        bot.execute(new SendMessage(chatId, changePhoneText));
    }

    public void handleMessage(Update update) {
        long chatId = update.message().chat().id();
        String messageText = update.message().text();

        if (awaitingPhoneInput.getOrDefault(chatId, false)) {
            if (isValidPhoneNumber(messageText)) {

                System.out.println("User " + chatId + " phone to: " + messageText);

                LocalManager localManager = new LocalManager(language);
                String changePhoneSuccess = localManager.getMessage("changePhoneSuccess");

                bot.execute(new SendMessage(chatId, changePhoneSuccess));

                awaitingPhoneInput.put(chatId, false);
            } else {
                LocalManager localManager = new LocalManager(language);
                String invalidPhoneMessage = localManager.getMessage("curnumber");
                bot.execute(new SendMessage(chatId, invalidPhoneMessage));
            }
        }
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.startsWith("+7") && phoneNumber.length() == 12 && phoneNumber.substring(2).matches("\\d+");
    }
}