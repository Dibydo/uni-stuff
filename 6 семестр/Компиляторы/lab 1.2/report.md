% Лабораторная работа № 1.2. «Лексический анализатор
  на основе регулярных выражений»
% 10 апреля 2023 г.
% Ярослав Жовтяк, ИУ9-61Б

# Цель работы
Целью данной работы является приобретение навыка разработки простейших лексических анализаторов,
работающих на основе поиска в тексте по образцу, заданному регулярным выражением.

# Индивидуальный вариант
Строковые литералы: ограничены двойными кавычками, не могут пересекать границы текста,
содержат escape-последовательности «\\n», «\\"», «\\t» и «\\\».
Числовые литералы: последовательности десятичных знаков и знаков «_»,
начинающиеся с цифры (прочерк не влияет на значение числа).

# Реализация

```java
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Position {
    private int line, pos;

    Position(int line,int pos) {
        this.line = line;
        this.pos = pos;
    }

    void next_line() {
        this.line += 1;
        this.pos = 1;
    }

    void next_pos(int pos) {
        this.pos += pos;
    }

    public String toString() {
        return "(" + this.line + "," + this.pos + ")";
    }
}

public class Main {

    private static void test_match(String line,Position pos) {

        String str_literal = "\"(?:[^\"\\\\\r\n]|\\\\[n\"t\\\\]|\r?\n)*\"";
        String num_literal = "\\d[_\\d]*";
        
        String pattern = "(^" + str_literal + ")|(^" + num_literal + ")";

        Pattern p = Pattern.compile(pattern);
        Matcher m;

        while (!line.equals("")) {
            m = p.matcher(line);
            if (m.find()) {
                if (m.group(1) != null) {
                    String item = m.group(1);
                    System.out.println("STR LITERAL " + pos.toString() + ": " + item);
                    pos.next_pos(item.length());
                    line = line.substring(line.indexOf(item) + item.length());
                } else {
                    String item= m.group(2);
                    System.out.println("NUM LITERAL " + pos.toString() + ": " + item);
                    pos.next_pos(item.length());
                    line = line.substring(line.indexOf(item) + item.length());
                }
            }
            else {
                if (Character.isWhitespace(line.charAt(0))) {
                    while (Character.isWhitespace(line.charAt(0))) {
                        line = line.substring(1);
                        pos.next_pos(1);
                    }
                }
                else {
                    System.out.println("syntax error " + pos.toString());
                    while (!m.find() && !line.equals("")) {
                        line = line.substring(1);
                        pos.next_pos(1);
                        m = p.matcher(line);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: java <Name> -encoding utf8 <file>");
            System.exit(1);
        }
        else {
            Position pos = new Position(1,1);
            List<String> lines;
            String file_name = args[2];
            try {
                lines = Files.readAllLines(Paths.get(file_name), StandardCharsets.UTF_8);
                for (String line : lines) {
                    test_match(line,pos);
                    pos.next_line();
                }
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
```

# Тестирование

Входные данные

```
123_123 "123"
3333
"абабаб\"аба"  "abc\qdef"
```

Вывод на `stdout` (если необходимо)

```
NUM LITERAL (1,1): 123_123
STR LITERAL (1,9): "123"
NUM LITERAL (2,1): 3333
STR LITERAL (3,1): "абабаб\"аба"
syntax error (3,16)
```

# Вывод
В ходе данной лабораторной работы ознакомился с разработкой простейших лексических анализаторов,
работающих на основе поиска в тексте по образцу, заданному регулярным выражением,
получил опыт написания точных регулярных выражений и выполнил поставленную задачу.