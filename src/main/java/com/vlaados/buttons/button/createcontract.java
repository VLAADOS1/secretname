package com.vlaados.buttons.button;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Contact;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import com.vlaados.buttons.ButtonHandler;
import com.vlaados.local.LocalManager;

import java.util.HashMap;
import java.util.Map;

import com.pengrad.telegrambot.request.SendMessage;

public class createcontract implements ButtonHandler {
    private final TelegramBot bot;
    private final Map<Long, String> awaitingInputMap = new HashMap<>();
    private final String language = "ru";

    public createcontract(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public boolean canHandle(String buttonData) {
        return "createcontract".equalsIgnoreCase(buttonData);
    }

    @Override
    public void handle(Update update) {
        long chatId = update.callbackQuery().message().chat().id();

        awaitingInputMap.put(chatId, "awaitingPhone");

        LocalManager localManager = new LocalManager(language);
        String getPhoneMessage = localManager.getMessage("getphone");

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(
                new KeyboardButton(localManager.getMessage("sharephone")).requestContact(true)
        ).oneTimeKeyboard(true).resizeKeyboard(true);

        bot.execute(new SendMessage(chatId, getPhoneMessage).replyMarkup(keyboard));
    }

    public void handleMessage(Update update) {
        long chatId = update.message().chat().id();
        String currentState = awaitingInputMap.getOrDefault(chatId, "");

        LocalManager localManager = new LocalManager(language);

        if ("awaitingPhone".equals(currentState)) {
            if (update.message().contact() != null) {
                String phoneNumber = update.message().contact().phoneNumber();
                System.out.println("User " + chatId + " shared phone number: " + phoneNumber);

                bot.execute(new SendMessage(chatId, localManager.getMessage("getadres"))
                        .replyMarkup(new ReplyKeyboardRemove()));

                awaitingInputMap.put(chatId, "awaitingAddress");

            } else if (update.message().text() != null) {
                String messageText = update.message().text();
                if (isValidPhoneNumber(messageText)) {
                    System.out.println("User " + chatId + " entered phone number: " + messageText);

                    bot.execute(new SendMessage(chatId, localManager.getMessage("getadres"))
                            .replyMarkup(new ReplyKeyboardRemove()));

                    awaitingInputMap.put(chatId, "awaitingAddress");
                } else {
                    String curnumber = localManager.getMessage("curnumber");
                    bot.execute(new SendMessage(chatId, curnumber));
                }
            }
        } else if ("awaitingAddress".equals(currentState) && update.message().text() != null) {
            String messageText = update.message().text();
            System.out.println("User " + chatId + " entered address: " + messageText);

            String succesreg = localManager.getMessage("succesreg");
            bot.execute(new SendMessage(chatId, succesreg));

            awaitingInputMap.remove(chatId);
        }
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.startsWith("+7") && phoneNumber.length() == 12 && phoneNumber.substring(2).matches("\\d+");
    }
}