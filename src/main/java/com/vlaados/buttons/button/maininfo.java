package com.vlaados.buttons.button;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.vlaados.buttons.ButtonHandler;
import com.vlaados.local.LocalManager;

public class maininfo implements ButtonHandler {

    private final TelegramBot bot;

    private String language = "ru"; //TODO «¿Ã≈Õ»“‹ Õ¿ «¿œ–Œ— — ¡ƒ


    public maininfo(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public boolean canHandle(String buttonData) {
        return "maininfo".equalsIgnoreCase(buttonData);
    }

    @Override
    public void handle(Update update) {
        String chatId = String.valueOf(update.callbackQuery().message().chat().id());

        LocalManager localManager = new LocalManager(language);

        String serviceinfo = localManager.getMessage("serviceinfo");
        bot.execute(new SendMessage(chatId, serviceinfo));
    }
}