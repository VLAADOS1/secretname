package com.vlaados.buttons.button;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.vlaados.buttons.ButtonHandler;

public class EnglishLanguageButton implements ButtonHandler {

    private final TelegramBot bot;

    public EnglishLanguageButton(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public boolean canHandle(String buttonData) {
        return "LANGUAGE_ENGLISH".equalsIgnoreCase(buttonData);
    }

    @Override
    public void handle(Update update) {
        String chatId = String.valueOf(update.callbackQuery().message().chat().id());
        //TODO отправят запрс на смену языка юзера
        bot.execute(new SendMessage(chatId, "\uD83C\uDDFA\uD83C\uDDF2You selected English! Welcome to the bot!\uD83C\uDF89"));
    }
}