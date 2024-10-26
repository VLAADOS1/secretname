package com.vlaados.buttons.button;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.vlaados.buttons.ButtonHandler;
import com.vlaados.local.LocalManager;

import java.util.HashMap;
import java.util.Map;

public class changeName implements ButtonHandler {

    private final TelegramBot bot;
    private final Map<Long, Boolean> awaitingNameInput = new HashMap<>();
    private String language = "ru"; // TODO: заменить на запрос из БД

    public changeName(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public boolean canHandle(String buttonData) {
        return "changeName".equalsIgnoreCase(buttonData);
    }

    @Override
    public void handle(Update update) {
        long chatId = update.callbackQuery().message().chat().id();

        awaitingNameInput.put(chatId, true);

        LocalManager localManager = new LocalManager(language);
        String changeNameText = localManager.getMessage("changeNameText");

        bot.execute(new SendMessage(chatId, changeNameText));
    }

    public void handleMessage(Update update) {
        long chatId = update.message().chat().id();
        String messageText = update.message().text();

        if (awaitingNameInput.getOrDefault(chatId, false)) {
            System.out.println("User " + chatId + " name to: " + messageText);

            LocalManager localManager = new LocalManager(language);
            String changeNameSuccess = localManager.getMessage("changeNameSuccess");

            bot.execute(new SendMessage(chatId, changeNameSuccess));

            awaitingNameInput.put(chatId, false);
        }
    }
}