
package ru.ucoz.karte.lirathebot;

// https://github.com/rubenlagus/TelegramBots/wiki
// https://jsoup.org/

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


public class MainClass extends TelegramLongPollingBot {
    
        private static final String BotUsername = "@LiraTheBot";
        private static final String BotToken = "...";
        String operation_flag = "";
        
    public static void main(String[] arg){
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new MainClass());
        } catch (TelegramApiException ex) {
            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    @Override
    public void onUpdateReceived(Update update){
        if (update.hasCallbackQuery()){
            handleCallback(update.getCallbackQuery());
        }
        if (update.hasMessage()) {
            Message message = update.getMessage();
            String cID = message.getChatId().toString();
            if (message.hasText()){
            try {
                if (!message.getText().startsWith("/")&&operation_flag==""){
                execute(SendMessage.builder()
                    .chatId(cID)
                    .text(" ? "+message.getText())
                    .build());
                }
                if ((!message.getText().startsWith("/"))&&operation_flag=="endoflife"){
                    operation_flag = "";
                    String str = message.getText().replace(".", "0");
                    int res_1 = 0;
                    for(int x = 0; x<str.length(); x = x + 1)
                        res_1=res_1+Character.getNumericValue(str.charAt(x));
                    int res_2 = 0;
                    for(int x = 0; x<String.valueOf(res_1).length(); x = x + 1)
                        res_2=res_2+Character.getNumericValue(String.valueOf(res_1).charAt(x));
                    int res_3 = 0;
                    for(int x = 0; x<String.valueOf(res_2).length(); x = x + 1)
                        res_3=res_3+Character.getNumericValue(String.valueOf(res_2).charAt(x));
                execute(SendMessage.builder()
                    .chatId(cID)
                    .text(String.valueOf(CalculateLife(res_3)))
                    .build());
                }
                if ((!message.getText().startsWith("/"))&&operation_flag=="bomber"){
                    operation_flag = "";
                    String str = message.getText();
                    String words[];
                    words = str.split(":");
                    String bcID = words[0];
                    int bcounter = Integer.valueOf(words[1]);
                    String bmsg = words[2];
                    for (int i = 0; i< bcounter; i++)
                        execute(SendMessage.builder()
                        .chatId(bcID)
                        .text(bmsg)
                        .build());
                }
                if (message.getText().equals("/stopltb")&&operation_flag==""){
                    execute(SendMessage.builder()
                    .chatId(cID)
                    .text("LTB terminated !")
                    .build());
                    System.exit(0);
                }
                if (message.getText().equals("/menu")&&operation_flag==""){
                    String header = "Menu:";
                    String button_name[] = {"Цитаты",
                                            "Остроумные",
                                            "Жизненные советы", 
                                            "Черный юмор", 
                                            "Афоризмы",
                                            "Факты из жизни",
                                            "Позитивное фото",
                                            "Грустное фото",
                                            "Сколько осталось жить",
                                            "Сегодня в истории",
                                            "Новости: Мир",
                                            "Мой ID",
                                            "Бомбер"};
                    String button_respond[] = {"/phrase",
                                            "/anekdote",
                                            "/advice",
                                            "/blackhumor",
                                            "/aphorisms",
                                            "/facts",
                                            "/positivepic",
                                            "/sadpic",
                                            "/endoflife",
                                            "/todayinhistory",
                                            "/newsworld",
                                            "/mychatid",
                                            "/bomber"};
                    Boolean row_enabled = false;
                    MakeKeyboard(cID, header, button_name, button_respond, row_enabled);
                }                       
            } catch (TelegramApiException ex) {
                Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
        }
    }
    
    private void handleCallback(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        String cID = message.getChatId().toString();
        String data = callbackQuery.getData();
        //System.out.println(data);   
        try {
            if (data.equals("/phrase"))
                execute(SendMessage.builder()
                    .chatId(cID)
                    .text(phrase())
                    .build());
            if (data.equals("/anekdote"))
                execute(SendMessage.builder()
                    .chatId(cID)
                    .text(anekdote())
                    .build());
            if (data.equals("/advice"))
                execute(SendMessage.builder()
                    .chatId(cID)
                    .text(advice())
                    .build());
            if (data.equals("/blackhumor"))
                execute(SendMessage.builder()
                    .chatId(cID)
                    .text(blackHumor())
                    .build());
            if (data.equals("/aphorisms"))
                execute(SendMessage.builder()
                    .chatId(cID)
                    .text(aphorisms())
                    .build());
            if (data.equals("/facts"))
                execute(SendMessage.builder()
                    .chatId(cID)
                    .text(facts())
                    .build());
            if (data.equals("/positivepic"))
                sendImageFromUrl(PositivePic(), cID);
            if (data.equals("/sadpic"))
                sendImageFromUrl(SadPic(), cID);
            if (data.equals("/endoflife")){
                operation_flag = "endoflife";
                execute(SendMessage.builder()
                    .chatId(cID)
                    .text("Введите дату рождения. Например: 22.09.1985")
                    .build());
            }
            if (data.equals("/todayinhistory"))
                execute(SendMessage.builder()
                    .chatId(cID)
                    .text(TodayInHistory())
                    .build());
            if (data.equals("/newsworld"))
                execute(SendMessage.builder()
                    .chatId(cID)
                    .text(NewsWorld())
                    .build());
            if (data.equals("/mychatid"))
                execute(SendMessage.builder()
                    .chatId(cID)
                    .text(cID)
                    .build());
            if (data.equals("/bomber")){
                operation_flag = "bomber";
                execute(SendMessage.builder()
                    .chatId(cID)
                    .text("Введите ID пользователья, число сообщений и текст сообщения через знак ':'."
                            + "Пример: 111222333:10:Привет ")
                    .build());
            }
        } catch (TelegramApiException ex) {
            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    @Override
    public String getBotUsername(){
        return BotUsername;
    }
    
    @Override
    public String getBotToken(){
        return BotToken;
    }

    private void MakeKeyboard(String cID,
                            String header,
                            String button_name[], 
                            String button_respond[],
                            Boolean row_enabled){
        int button_num = button_name.length;
        if (row_enabled){
            List<InlineKeyboardButton> buttons = new ArrayList<>();
            for (int t = 0; t < button_num; t++)
                buttons.add(
                    InlineKeyboardButton.builder()
                    .text(button_name[t])
                    .callbackData(button_respond[t])
                    .build());
            try {
                execute(SendMessage.builder()
                        .chatId(cID)
                        .text(header)
                        .replyMarkup(InlineKeyboardMarkup.builder().keyboardRow(buttons).build())
                        .build());
            } catch (TelegramApiException ex) {
                Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
            for (int t = 0; t < button_num; t++)
                buttons.add(Arrays.asList(
                    InlineKeyboardButton.builder()
                    .text(button_name[t])
                    .callbackData(button_respond[t])
                    .build()));
            try {
                execute(SendMessage.builder()
                        .chatId(cID)
                        .text(header)
                        .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                        .build());
            } catch (TelegramApiException ex) {
                Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
            }   
        }
    }
    
    private String phrase() {
        String str[]= new String[100];
        Random rand = new Random();
        int i = 0;
        String url = "https://readrate.com/rus/news/tsitaty-velikikh-lyudey";
        Document document;
            try {
                document = Jsoup.connect(url).get();
                Elements els = document.getElementsByTag("li");
                for (Element el : els) {
                    if (el.text().startsWith("«")){
                        str[i] = el.text();
                        i++;
                    }        
                }
                } catch (IOException ex) {
                    Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
                }
        return(str[rand.nextInt(i-1)]);
    }
    
    private String anekdote() {
        String str[]= new String[100];
        Random rand = new Random();
        int i = 0;
        String rnd = String.valueOf(rand.nextInt(32)+1);
        String url = "https://anekdotbar.ru/korotkie/page/"+rnd+"/";
        Document document;
            try {
                document = Jsoup.connect(url).get();
                Elements els = document.getElementsByClass("tecst");
                for (Element el : els) {
                    str[i] = el.text();
                    i++;
                }
                } catch (IOException ex) {
                    Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
                }
        return(str[rand.nextInt(i-1)]);
    }
    
     private String advice() {
        String str[]= new String[100];
        Random rand = new Random();
        int i = 0;
        String url = "https://takprosto.cc/100-pravil-schastlivyh-lyudey/";
        Document document;
            try {
                document = Jsoup.connect(url).get();
                Elements els = document.getElementsByClass("instruction");
                for (Element el : els) {
                    str[i] = el.text();
                    i++;     
                }
                } catch (IOException ex) {
                    Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
                }
        return(str[rand.nextInt(i-1)]);
    }

    private String blackHumor() {
        String str[]= new String[100];
        Random rand = new Random();
        int i = 0;
        String rnd = String.valueOf(rand.nextInt(4)+1);
        String url = "https://4tob.ru/anekdots/tag/black/page"+rnd;
        Document document;
            try {
                document = Jsoup.connect(url).get();
                Elements els = document.getElementsByClass("text");
                for (Element el : els) {
                    str[i] = el.text();
                    i++;
                }
                } catch (IOException ex) {
                    Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
                }
        return(str[rand.nextInt(i-1)]);
    }

    private String aphorisms() {
        String str[]= new String[100];
        Random rand = new Random();
        int i = 0;
        String rnd = String.valueOf(rand.nextInt(18)+1);
        String url = "https://www.potehechas.ru/yumor/aforizm_"+rnd+".shtml";
        Document document;
            try {
                document = Jsoup.connect(url).get();
                Elements els1 = document.getElementsByClass("text_aforizm");
                for (Element el : els1) {
                    str[i] = el.text();
                    i++;
                }
                Elements els2 = document.getElementsByClass("text_aforizm_red");
                for (Element el : els2) {
                    str[i] = el.text();
                    i++;
                }
                } catch (IOException ex) {
                    Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
                }
        return(str[rand.nextInt(i-1)]);
    }
    
    private String facts() {
        String str[]= new String[100];
        Random rand = new Random();
        int i = 0;
        String rnd = String.valueOf(rand.nextInt(14)+1);
        String url = "https://www.potehechas.ru/club/fact_"+rnd+".shtml";
        Document document;
            try {
                document = Jsoup.connect(url).get();
                Elements els = document.getElementsByClass("text_aforizm");
                for (Element el : els) {
                    str[i] = el.text();
                    i++;
                }
                } catch (IOException ex) {
                    Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
                }
        return(str[rand.nextInt(i-1)]);
    }
    
    private String PositivePic() {
        String str[]= new String[100];
        Random rand = new Random();
        int i = 0;
        String rnd = String.valueOf(rand.nextInt(82)+1);
        String URL = "https://trinixy.ru/demotivators/page/"+rnd+"/";
        String startKeyWord = "https://cdn.trinixy.ru/uploads/posts";
        Document jsDoc = null;
        try {
            jsDoc = Jsoup.connect(URL).get();
            Elements imgs = jsDoc.select("[src]");
            for (Element img : imgs) {
                if (img.normalName().equals("img") && img.attr("src").startsWith(startKeyWord)){
                    str[i] = img.attr("src");
                    i++;
                }
            }
        } catch (IOException e) {
        e.printStackTrace();
        }
    return(str[rand.nextInt(i-1)]);
    }
    
    private String SadPic(){
        String str[]= new String[100];
        Random rand = new Random();
        int i = 0;
        String rnd = String.valueOf(rand.nextInt(7)+1);
        String URL = "https://otkritkis.com/grustnye-kartinki/page/"+rnd+"/";      
        String startKeyWord = "https://otkritkis.com/wp-content/uploads";
        Document jsDoc = null;
        try {
        jsDoc = Jsoup.connect(URL).get();
        Elements imgs = jsDoc.select("[src]");
        for (Element img : imgs) {
            if (img.normalName().equals("img") && 
                img.attr("src").startsWith(startKeyWord) &&
                img.attr("src").indexOf("jpg") !=-1){
	//System.out.println(img.attr("src"));
                str[i] = img.attr("src");
                i++;
            }
        }
        } catch (IOException e) {
        e.printStackTrace();
    }
    return(str[rand.nextInt(i-1)]);
    }

    private void sendImageFromUrl(String url, String chatId) {
        // Create send method
        SendPhoto sendPhotoRequest = new SendPhoto();
        // Set destination chat id
        sendPhotoRequest.setChatId(chatId);
        // Set the photo url as a simple photo
        sendPhotoRequest.setPhoto(new InputFile(url));
        try {
            // Execute the method
            execute(sendPhotoRequest);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String CalculateLife(int sum){
        String str[]= new String[100];
        int i = 0;
        String url = "https://ritual.by/articles/data-smerti-i-data-rozhdeniya-est-li-svyaz/";
            try {
                Connection connection = Jsoup.connect(url);
                Document document = connection.get();
                Elements els = document.getElementsByTag("li");
                for (Element el : els) {
                    if (el.text().indexOf(" — ")!=-1){
                        str[i] = el.text().substring(4, el.text().length());
                        i++;
                    }
                }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }    
        return str[sum-1];
    }

    private String TodayInHistory(){
        String str_event[]= new String[100];
        String str_year[]= new String[100];
        int i = 0;
        String url = "https://www.calend.ru/events/";
        Document document;
            try {
                document = Jsoup.connect(url).get();
                //Elements els = document.getElementsByClass("year ");
                Elements els = document.getElementsByClass("title");
                for (Element el : els) {
                    str_event[i] = el.text();
                    i++;
                }
                i = 0;
                els = document.getElementsByClass("caption");
                for (Element el : els) {
                    str_year[i] = el.text().substring(0, el.text().indexOf(" "));
                    i++;
                }
                } catch (IOException ex) {
                    Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
                }
        String res="";
        for (i=10; i<Arrays.asList(str_event).indexOf("Эл. почта"); i++)
            res = res + str_year[i-10]+" г. : "+str_event[i]+"\n";
        //System.out.println(res);
        return res;
    }
    
    private String NewsWorld() {
        String url = "https://gordonua.com/news/worldnews.html";
        String res = "";
        Document document;
            try {
                document = Jsoup.connect(url).get();
                Elements els = document.getElementsByClass("carousel_news_list slide for_main sd5");
                String words[];
                    words = els.text().split("Сегодня");
                for (int i=0; i<words.length-1; i++) {
                     res = res + words[i] +"\n";
                }
                } catch (IOException ex) {
                    Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
                }
       return res;
    }

}
