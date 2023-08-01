package DEV120_2_2_Tekiev;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Script {

    public void read(File file) throws IOException {

        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String s;
        ArrayList<String> list = new ArrayList<>();
        while ((s = br.readLine()) != null) {
            if (!s.isEmpty() && (s.trim()).charAt(0) != '#') {
                list.add(s);
            }
        }
        br.close();
        fr.close();
        String[] words;
        String[] wordsMath;
        Map<String, Integer> map = new HashMap<>();
        for (String str : list) {
            String print = "print";
            String set = "set";
            if (str.trim().startsWith(set)) {
                String tmp = str.replaceFirst(set, "").trim();
                if (!tmp.isEmpty()) {
                    if (tmp.charAt(0) == '$') {
                        words = (tmp.trim()).split("=");
                        if (words.length == 2) {
                            if (words[1].trim().chars().allMatch(Character::isDigit)) {
                                for (int i = 0; i < words.length - 1; i++) {
                                    Matcher matcher = Pattern.compile("[0-9a-zA-Z_]").matcher(words[i].trim());
                                    if (matcher.find()) {
                                        map.put(words[i].trim(), Integer.parseInt(words[i + 1].trim()));
                                    } else {
                                        System.out.println("Имя переменной задано некорректно");
                                    }
                                }
                            } else {
                                wordsMath = (words[1].trim()).split("[-+]");
                                String tmpString1 = words[1];
                                String tmpString2, tmpString3 = "";
                                for (String value : wordsMath) {
                                    if (!(value.trim().chars().allMatch(Character::isDigit))) {
                                        if (map.get(value.trim()) != null) {
                                            tmpString2 = tmpString1.trim().replace(value, " " + map.get(value.trim()).toString() + " ");
                                            tmpString1 = tmpString2;
                                            if (tmpString2.contains("-")) {
                                                tmpString3 = tmpString2.trim().replace("-", " - ");
                                                tmpString2 = tmpString3;
                                            }
                                            if (tmpString2.contains("+")) {
                                                tmpString3 = tmpString2.trim().replace("+", " + ");
                                            }
                                        } else {
                                            System.out.println("Переменная " + value.trim() + " не инициализирована");
                                            return;
                                        }
                                    }
                                }
                                String[] math = (tmpString3.trim()).split(" ");
                                int sum = 0;
                                int count = 0;
                                for (int i = 0, j = 0; i < math.length; i++, j++) {
                                    if (!math[i].isEmpty()) {
                                        count++;
                                    }
                                }
                                String[] math2 = new String[count];
                                int j = 0;
                                for (int i = 0; i < math.length; i++) {
                                    if (!math[i].isEmpty()) {
                                        math2[j] = math[i];
                                        j++;
                                    }
                                }
                                for (int i = 0; i < math2.length; i++) {
                                    if (!math2[0].contains("+")) {
                                        if (!math2[0].contains("-")) {
                                            if (math2[i].equals("+")) {
                                                sum = Integer.parseInt(math2[i - 1]) + Integer.parseInt(math2[i + 1]);
                                                math2[i + 1] = Integer.toString(sum);
                                            }
                                            if (math2[i].equals("-")) {
                                                sum = Integer.parseInt(math2[i - 1]) - Integer.parseInt(math2[i + 1]);
                                                math2[i + 1] = Integer.toString(sum);
                                            }
                                        } else {
                                            System.out.println("Методом set задано некорректное выражение. Выражение не должно начинаться со знака -");
                                            return;
                                        }
                                    } else {
                                        System.out.println("Методом set задано некорректное выражение. Выражение не должно начинаться со знака +");
                                        return;
                                    }
                                }
                                for (int i = 0; i < words.length - 1; i++) {
                                    map.put(words[i].trim(), sum);
                                }
                            }

                        } else {
                            System.out.println("Оператор set составлен некорректно");
                            return;
                        }

                    } else {
                        System.out.println("Имя переменной должно начинаться с $");
                        return;
                    }
                } else {
                    System.out.println("Не задан текст сприпта после оператора set");
                    return;
                }
            }
            else if (str.trim().startsWith(print)) {
                String tmp = str.replaceFirst(print, "").trim();
                if (!tmp.isEmpty()) {
                    if (tmp.charAt(0) == '"' && tmp.charAt(tmp.length() - 1) == '"') {
                        tmp = tmp.substring(1, tmp.length() - 1);
                        System.out.print(tmp);
                    } else {
                        words = (tmp.trim()).split(",");
                        for (String word : words) {
                            word = word.trim();
                            if (word.charAt(0) == '"' && word.charAt(word.length() - 1) == '"') {
                                System.out.print(word.substring(1, word.length() - 1));
                            } else {
                                if (map.get(word) != null) {
                                    System.out.print(map.get(word));
                                } else {
                                    System.out.println("Оператор print составлен некорректно");
                                    return;
                                }
                            }
                        }
                    }
                    System.out.println();
                } else {
                    System.out.println("Не задан текст сприпта после оператора print");
                    return;
                }
            }
            else {
                System.out.println("Задан несуществующий скриптовый оператор");
                return;
            }
        }
    }

}
