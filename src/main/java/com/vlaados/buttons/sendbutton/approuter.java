package com.vlaados.buttons.sendbutton;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.vlaados.buttons.ButtonHandler;
import com.vlaados.local.LocalManager;

public class approuter implements ButtonHandler {

    private final TelegramBot bot;

    private String language = "ru"; //TODO �������� �� ������ � ��

    public approuter(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public boolean canHandle(String buttonData) {
        return "approuter".equalsIgnoreCase(buttonData);
    }

    @Override
    public void handle(Update update) {
        String chatId = String.valueOf(update.callbackQuery().message().chat().id());

        LocalManager localManager = new LocalManager(language);

        String request = localManager.getMessage("request");

        bot.execute(new SendMessage(chatId, request));
    }
}