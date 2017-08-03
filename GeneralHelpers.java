package uk.co.savant.barry;

import java.text.BreakIterator;
import java.util.ArrayList;

/**
 * Created by martins on 08/09/2016.
 */
public class GeneralHelpers {

    public static ArrayList<String> extractWordsFromString(String text) {
        ArrayList<String> words = new ArrayList<String>();
        BreakIterator breakIterator = BreakIterator.getWordInstance();
        breakIterator.setText(text);
        int lastIndex = breakIterator.first();

        while (BreakIterator.DONE != lastIndex) {
            int firstIndex = lastIndex;
            lastIndex = breakIterator.next();

            if (lastIndex != BreakIterator.DONE && Character.isLetterOrDigit(text.charAt(firstIndex))) {
                String word = text.substring(firstIndex, lastIndex);
                if (word.length() >= 5) {
                    words.add(word);
                }
            }
        }
        return words;
    }

}
