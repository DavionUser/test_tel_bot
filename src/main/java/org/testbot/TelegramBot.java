package org.testbot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TelegramBot extends TelegramLongPollingBot {

    private final CurrencyService currencyService = CurrencyService.getInstance();


    public TelegramBot() {
        ChatBot.initDreams();
        ChatBot.initProfessions();
    }

//    @Override
//    public void onUpdateReceived(Update update) {
//        if (update.hasMessage() && update.getMessage().hasText()) {
//            String message = update.getMessage().getText();
//            String response = ChatBot.process(message);
//
//            sendText(update.getMessage().getChatId(), response);
//        }
//    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            try {
                handleCallback(update.getCallbackQuery());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        } else if (update.hasMessage()) {
            try {
                handleMessage(update.getMessage());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void handleCallback(CallbackQuery callbackQuery) throws TelegramApiException {
        Message message = callbackQuery.getMessage();
        String action = callbackQuery.getData();
        System.out.println(action);

        switch (action) {
            case "Інфо":
                execute(SendMessage.builder()
                        .text("ПриватБанк UAH/USD\n30\n32")
                        .chatId(message.getChatId().toString())
                        .build());
                break;
            case "Налаштування":
                List<List<InlineKeyboardButton>> settingsButtons = new ArrayList<>();
                for (Settings settings : Settings.values()) {
                    settingsButtons.add(Arrays.asList(InlineKeyboardButton.builder()
                            .text(settings.name())
                            .callbackData(settings.name())
                            .build()));
                }
                execute(SendMessage.builder()
                        .text("Налаштування")
                        .replyMarkup(InlineKeyboardMarkup.builder().keyboard(settingsButtons).build())
                        .chatId(message.getChatId().toString())
                        .build());
                break;
            case "DIGITS":
                List<List<InlineKeyboardButton>> digitsButtons = new ArrayList<>();

                digitsButtons.add(Arrays.asList(InlineKeyboardButton.builder()
                        .text("2")
                        .callbackData("2")
                        .build()));
                digitsButtons.add(Arrays.asList(InlineKeyboardButton.builder()
                        .text("3")
                        .callbackData("3")
                        .build()));
                digitsButtons.add(Arrays.asList(InlineKeyboardButton.builder()
                        .text("4")
                        .callbackData("4")
                        .build()));

                execute(SendMessage.builder()
                        .text("Оберіть кількість знаків після коми")
                        .chatId(message.getChatId().toString())
                        .replyMarkup(InlineKeyboardMarkup.builder().keyboard(digitsButtons).build())
                        .build());
                break;
            case "BANK":
                List<List<InlineKeyboardButton>> bankButtons = new ArrayList<>();

                bankButtons.add(Arrays.asList(InlineKeyboardButton.builder()
                        .text("НБУ")
                        .callbackData("НБУ")
                        .build()));
                bankButtons.add(Arrays.asList(InlineKeyboardButton.builder()
                        .text("ПриватБанк")
                        .callbackData("ПриватБанк")
                        .build()));
                bankButtons.add(Arrays.asList(InlineKeyboardButton.builder()
                        .text("Монобанк")
                        .callbackData("Монобанк")
                        .build()));

                execute(SendMessage.builder()
                        .text("Оберіть банк")
                        .chatId(message.getChatId().toString())
                        .replyMarkup(InlineKeyboardMarkup.builder().keyboard(bankButtons).build())
                        .build());
                break;
            case "TIME":
                break;
            case "CURRENCIES":
                List<List<InlineKeyboardButton>> currenciesButtons = new ArrayList<>();
                Currency currentCurrency = currencyService.getCurrency(message.getChatId());
                for (Currency currency : Currency.values()) {
                    currenciesButtons.add(Arrays.asList(InlineKeyboardButton.builder()
                            .text(getCurrencyButton(currentCurrency, currency))
                            .callbackData(currency.name())
                            .build()));
                }
                execute(SendMessage.builder()
                        .text("Оберіть валюту")
                        .replyMarkup(InlineKeyboardMarkup.builder().keyboard(currenciesButtons).build())
                        .chatId(message.getChatId().toString())
                        .build());
                break;
        }
    }

    private void handleMessage(Message message) throws TelegramApiException {

        if (message.hasText()) {
            if (message.getText().equals("/start")) {
                List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

                buttons.add(Arrays.asList(InlineKeyboardButton.builder()
                        .text("Інфо")
                        .callbackData("Інфо")
                        .build()));
                buttons.add(Arrays.asList(InlineKeyboardButton.builder()
                        .text("Налаштування")
                        .callbackData("Налаштування")
                        .build()));

                execute(SendMessage.builder()
                        .text("Вітаю! Цей бот допоможе Вам дізнатися актуальний курс валют")
                        .chatId(message.getChatId().toString())
                        .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                        .build());

            } else {
                    execute(SendMessage.builder()
                            .text("Будь ласка, оберіть фунцкію зі списку")
                            .chatId(message.getChatId().toString())
                            .build());
            }
        }
    }

//                    KeyboardButton infoButton = new KeyboardButton();
//                    infoButton.setText("Інфо");
//
//                    KeyboardButton settingsButton = new KeyboardButton();
//                    settingsButton.setText("Налаштування");
//                    List<List<KeyboardButton>> buttons = new ArrayList<>();
//                    buttons.add(Arrays.asList(
//                            infoButton,
//                            settingsButton)
//                    );


//                case "Налаштування":
//                    List<List<InlineKeyboardButton>> settingsButtons = new ArrayList<>();
//                    for (Settings settings : Settings.values()) {
//                        settingsButtons.add(Arrays.asList(InlineKeyboardButton.builder()
//                                .text(settings.name())
//                                .callbackData("Ви обрали " + settings)
//                                .build()));
//                    }
//                    execute(SendMessage.builder()
//                            .text("Оберіть що ви хочете змінити")
//                            .chatId(getChatId)
//                            .build());
//                    break;
//                default:
//                    execute(SendMessage.builder()
//                            .text("Оберіть фунцкію зі списку")
//                            .chatId(getChatId)
//                            .build());
//                    return;
//}


    private String getCurrencyButton(Currency saved, Currency current) {
        return saved == current ? "✅ " + current : current.name();
    }

    private String getBankButton(Bank saved, Bank current) {
        return saved == current ? "✅ " + current : current.name();
    }

    @Override
    public String getBotUsername() {
        return "chapie123bot";
    }

    @Override
    public String getBotToken() {
        return "2089034220:AAESzzRPty8zhwRrFbjHwH4SzU30UyQw0Io";
    }
}
