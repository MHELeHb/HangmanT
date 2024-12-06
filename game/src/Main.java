import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        startNewGame();
    }
    public static void startNewGame() {
        Scanner in = new Scanner(System.in);
        System.out.println("Виселица. Выберите режим: Лёгкий, Средний, Тяжёлый.");
        System.out.println("Введите ваш выбор (или введите EXIT для выхода):");
        Mode mode = new Mode();
        while (true) {
            String input = in.nextLine();
            if (input.equalsIgnoreCase("EXIT")) {
                System.out.println("Игра завершена. До встречи!");
                System.exit(0);
            }
            if (mode.setMode(input)) {
                break;
            }
            System.out.println("Неверный ввод. Повторите: Лёгкий, Средний, Тяжёлый (или введите EXIT для выхода).");
        }
        WordGenerator generator = new WordGenerator(mode);
        Session session = new Session(generator);
        session.startGame();
    }
}

class Mode {
    private String mode;

    public boolean setMode(String input) {
        if (input.equalsIgnoreCase("Лёгкий") || input.equalsIgnoreCase("Средний") || input.equalsIgnoreCase("Тяжёлый")) {
            this.mode = input.toLowerCase();
            return true;
        }
        return false;
    }

    public String getMode() {
        return mode;
    }
}

class WordGenerator {
    private final String[] words;
    public WordGenerator(Mode mode) {
        String[] easyWords = {"ночь", "день", "добро", "злость", "слово", "тест", "атлет", "спорт", "охрана", "ров", "кот", "панк", "дань", "повтор", "цикл", "секрет", "слон", "ёж", "пёс", "собака", "сериал", "фильм", "еда", "яйцо", "мёд", "тигр", "лев", "носок", "нос", "глаз", "тело", "физика", "игра", "геймер", "сердце", "мозг", "разум", "шанс", "грусть", "тильт", "печаль", "покер", "дурак", "шашки", "шок", "клуб", "лицей", "король", "шут", "лесник", "кукла", "колдун", "фурри", "шпион", "стрим"};
        String[] mediumWords = {"шоколад", "интерес", "позитив", "оптимизм", "оптимист", "пессимист", "пессимизм", "весельчак", "депрессия", "градация", "критерий", "дружелюбие", "культура", "студент", "кровосос", "артефакт", "волшебник", "сокровище", "учебник", "университет", "институт", "общество", "угадайка", "виселица", "воротник", "ненависть", "красота", "картина", "квадробер", "славянофил", "либерал", "федерал", "капитан", "генерал", "бездарь", "дарование", "вареник", "пельмень", "любитель", "гуманист", "стример"};
        String[] hardWords = {"неуязвимость", "непоколебимость", "дружелюбность", "враждебность", "полиморфизм", "инкапсуляция", "наследование", "празднование", "какоррафиофобия", "программирование", "неотвратимость", "путешественник", "путешествие", "испорченность", "подготовленность", "огнестойкость", "окрашенность", "водостойкость", "правильность", "образованность", "противопоставление", "коррумпированность", "гиппопотомомонстросесквиппедалиофобия", "тетрогидрокарбонат", "параллелограмм", "десятиугольник", "беспощадность"};
        switch (mode.getMode()) {
            case "лёгкий":
                words = easyWords;
                break;
            case "средний":
                words = mediumWords;
                break;
            case "тяжёлый":
                words = hardWords;
                break;
            default:
                throw new IllegalArgumentException("Неверный режим игры.");
        }
    }

    public String generateWord() {
        Random random = new Random();
        return words[random.nextInt(words.length)];
    }
}

class Session {
    private final Scanner in = new Scanner(System.in);
    private final WordGenerator generator;
    private int totalMistakes = 0, totalWords = 0;

    public Session(WordGenerator generator) {
        this.generator = generator;
    }

    public void startGame() {
        String word = generator.generateWord();
        System.out.println("Начинаем игру! Угадайте слово:");
        char[] guessed = new char[word.length()];
        Arrays.fill(guessed, '_');
        System.out.println("Для выхода пропишите EXIT");
        int attempts = 5;
        while (attempts > 0 && new String(guessed).contains("_")) {
            System.out.println("Слово: " + formatWord(word, guessed));
            System.out.println("Прав на ошибку осталось: " + attempts);
            System.out.println("Введите букву или слово:");
            String input = in.nextLine();
            if (input.equalsIgnoreCase("EXIT")) {
                endGame();
                return;
            } else if (input.length() == 1) {
                char letter = input.charAt(0);
                if (word.contains(String.valueOf(letter))) {
                    for (int i = 0; i < word.length(); i++) {
                        if (word.charAt(i) == letter) {
                            guessed[i] = letter;
                        }
                    }
                } else {
                    attempts--;
                    totalMistakes++;
                    System.out.println("Неправильно! Прав на ошибку осталось: " + attempts);
                }
            } else if (input.equalsIgnoreCase(word)) {
                guessed = word.toCharArray();
                break;
            } else {
                attempts--;
                totalMistakes++;
                System.out.println("Неверное слово! Попыток осталось: " + attempts);
            }
        }
        if (new String(guessed).equals(word)) {
            System.out.println("Поздравляем! Вы угадали слово: " + word);
            totalWords++;
        } else {
            System.out.println("Вы проиграли. Слово было: " + word);
        }
        startGamePrompt();
    }

    private String formatWord(String word, char[] guessed) {
        StringBuilder display = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            display.append(guessed[i] != '_' ? guessed[i] : '_');
        }
        return display.toString();
    }

    private void startGamePrompt() {
        System.out.println("Хотите сыграть ещё раз? (да/нет)");
        if (in.nextLine().equalsIgnoreCase("да")) {
            Main.startNewGame();
        } else {
            endGame();
        }
    }

    private void endGame() {
        System.out.println("Игра окончена!");
        System.out.println("Всего ошибок: " + totalMistakes);
        System.out.println("Отгадано слов: " + totalWords);
    }
}
