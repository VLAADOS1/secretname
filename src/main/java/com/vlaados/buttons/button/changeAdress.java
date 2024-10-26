package com.vlaados.buttons.button;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.vlaados.buttons.ButtonHandler;
import com.vlaados.local.LocalManager;

import java.util.HashMap;
import java.util.Map;

public class changeAdress implements ButtonHandler {

    private final TelegramBot bot;
    private final Map<Long, Boolean> awaitingAddressInput = new HashMap<>();
    private String language = "ru";

    public changeAdress(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public boolean canHandle(String buttonData) {
        return "changeAdress".equalsIgnoreCase(buttonData);
    }

    @Override
    public void handle(Update update) {
        long chatId = update.callbackQuery().message().chat().id();

        awaitingAddressInput.put(chatId, true);

        LocalManager localManager = new LocalManager(language);
        String changeAddressText = localManager.getMessage("changeAdressText");

        bot.execute(new SendMessage(chatId, changeAddressText));
    }

    public void handleMessage(Update update) {
        long chatId = update.message().chat().id();
        String messageText = update.message().text();

        if (awaitingAddressInput.getOrDefault(chatId, false)) {
            System.out.println("User " + chatId + " changed their address to: " + messageText);

            LocalManager localManager = new LocalManager(language);
            String changeAddressSuccess = localManager.getMessage("changeAdressSuccess");

            bot.execute(new SendMessage(chatId, changeAddressSuccess));

            awaitingAddressInput.put(chatId, false);
        }
    }
}