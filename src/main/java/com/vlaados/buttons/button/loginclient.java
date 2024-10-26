package com.vlaados.buttons.button;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.vlaados.UserSessionManager;
import com.vlaados.buttons.ButtonHandler;
import com.vlaados.local.LocalManager;

import java.util.HashMap;
import java.util.Map;

public class loginclient implements ButtonHandler {

    private final TelegramBot bot;
    private final Map<Long, Boolean> awaitingInputMap = new HashMap<>();
    private final String language = "ru";

    public loginclient(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public boolean canHandle(String buttonData) {
        return "loginclient".equalsIgnoreCase(buttonData);
    }

    @Override
    public void handle(Update update) {
        long chatId = update.callbackQuery().message().chat().id();

        awaitingInputMap.put(chatId, true);

        LocalManager localManager = new LocalManager(language);
        String getContract = localManager.getMessage("getcontract");

        bot.execute(new SendMessage(chatId, getContract));
    }

    public void handleMessage(Update update) {
        long chatId = update.message().chat().id();
        String messageText = update.message().text();

        if (awaitingInputMap.getOrDefault(chatId, false)) {
            bot.execute(new SendMessage(chatId, "You entered: " + messageText));
            //TODO тут запрашиваем у сервера есть ли такой договор если есть то пишем юзеру введите /menu он зарегался
            awaitingInputMap.put(chatId, false);
        }
    }
}