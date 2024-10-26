package com.vlaados.buttons.button;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.vlaados.buttons.ButtonHandler;
import com.vlaados.local.LocalManager;

public class mainprofil implements ButtonHandler {

    private final TelegramBot bot;

    private String language = "ru"; //TODO «¿Ã≈Õ»“‹ Õ¿ «¿œ–Œ— — ¡ƒ

    public mainprofil(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public boolean canHandle(String buttonData) {
        return "mainprofil".equalsIgnoreCase(buttonData);
    }

    @Override
    public void handle(Update update) {
        String chatId = String.valueOf(update.callbackQuery().message().chat().id());

        LocalManager localManager = new LocalManager(language);

        String changeName = localManager.getMessage("changeName");
        String changePhone = localManager.getMessage("changePhone");
        String changeAdress = localManager.getMessage("changeAdress");

        InlineKeyboardMarkup keyboards = new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton(changeName).callbackData("changeName"),
                        new InlineKeyboardButton(changePhone).callbackData("changePhone"),
                        new InlineKeyboardButton(changeAdress).callbackData("changeAdress"),

                }
        );

        String myprofilMain = localManager.getMessage("myprofilMain");
        String profileName = localManager.getMessage("myprofilName");
        String profileContract = localManager.getMessage("myprofilContract");
        String profilePhone = localManager.getMessage("myprofilPhone");
        String profileAddress = localManager.getMessage("myprofilAdress");

        String profileMessage =
                myprofilMain + "\n" + "\n" +
                profileName + "\n" +
                profileContract + "\n" +
                profilePhone + "\n" +
                profileAddress;

        bot.execute(new SendMessage(chatId, profileMessage).parseMode(ParseMode.valueOf("HTML")).replyMarkup(keyboards));
    }
}