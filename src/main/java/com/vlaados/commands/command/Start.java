package com.vlaados.commands.command;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.vlaados.commands.CommandHandler;
import com.vlaados.local.LocalManager;

import java.util.Objects;

public class Start implements CommandHandler {

    private final TelegramBot bot;

    private String language = "ru"; //TODO ЗАМЕНИТЬ НА ЗАПРОС С БД

    private boolean aut = true; // TODO ИЗ БД должны достать есть ли к этой телеги привязанный договор

    public Start(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public boolean canHandle(String command) {
        return "/start".equalsIgnoreCase(command);
    }

    @Override
    public void handle(Update update) {
        String chatId = String.valueOf(update.message().chat().id());

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("\uD83C\uDDF7\uD83C\uDDFA Русский").callbackData("LANGUAGE_RUSSIAN"),
                        new InlineKeyboardButton("\uD83C\uDDEC\uD83C\uDDE7 English").callbackData("LANGUAGE_ENGLISH")
                }
        );

        SendMessage message = new SendMessage(chatId, "\uD83D\uDC4B Добро пожаловать в бота! / Welcome to the bot!\n" +
                "\n" +
                "Пожалуйста, выберите язык, который вам удобен.\n" +
                "Please, select your preferred language.\n" +
                "\n" +
                "\uD83C\uDDF7\uD83C\uDDFA Русский – Нажмите, чтобы продолжить на русском языке.\n" +
                "\uD83C\uDDEC\uD83C\uDDE7 English – Click here to continue in English.")
                .replyMarkup(keyboard);

        if(Objects.equals(language, "null")) {
            bot.execute(message);
        } else {

            // тут мы должны проверить привязан ли айди телеги к номеру договова если нет то вызываем кнопки регестрации

            if (aut == true) {

                LocalManager localManager = new LocalManager(language);

                String maininfo = localManager.getMessage("maininfo");
                String mainapp = localManager.getMessage("mainapp");
                String mainprofil = localManager.getMessage("mainprofil");
                String maintp = localManager.getMessage("maintp");

                InlineKeyboardMarkup keyboards = new InlineKeyboardMarkup(
                        new InlineKeyboardButton[]{
                                new InlineKeyboardButton(maininfo).callbackData("maininfo"),
                                new InlineKeyboardButton(mainapp).callbackData("mainapp"),
                                new InlineKeyboardButton(mainprofil).callbackData("mainprofil"),
                                new InlineKeyboardButton(maintp).callbackData("maintp"),
                        }
                );

                String test = localManager.getMessage("mainmess");

                SendMessage messages = new SendMessage(chatId, test).replyMarkup(keyboards);

                bot.execute(messages);

            } else {

                LocalManager localManager = new LocalManager(language);

                String login = localManager.getMessage("loginclient");
                String create = localManager.getMessage("createcontract");

                InlineKeyboardMarkup keyboards = new InlineKeyboardMarkup(
                        new InlineKeyboardButton[]{
                                new InlineKeyboardButton(login).callbackData("loginclient"),
                                new InlineKeyboardButton(create).callbackData("createcontract")
                        }
                );

                String test = localManager.getMessage("authorization");

                SendMessage messages = new SendMessage(chatId, test).replyMarkup(keyboards);

                bot.execute(messages);
            }
        }
    }
}