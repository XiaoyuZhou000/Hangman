import java.util.*;

public class HangmanManager {

    private Set<String> wordsInLength;
    private int wrongGuessesLeft;
    private Set<Character> charGuesses;
    private String patternString;

    public HangmanManager(Collection<String> dictionary, int length, int max) {
        if (length < 1 || max < 0) {
            throw new IllegalArgumentException();
        }
        wrongGuessesLeft = max;
        wordsInLength = new TreeSet<String>();
        charGuesses = new TreeSet<Character>();
        patternString = "";
        
        Iterator iter = dictionary.iterator();
        while (iter.hasNext()) {
            String word = iter.next().toString();
            if (word.length() == length) {
                wordsInLength.add(word);
            }
        }

        for (int index = 0; index < length - 1; index++) {
            patternString += "- ";
        }
        patternString += "-";
    }

    public Set<String> words() {
        return wordsInLength;
    }

    public int guessesLeft() {
        return wrongGuessesLeft;
    }

    public Set<Character> guesses() {
        return charGuesses;
    }

    public String pattern() {
        if (wordsInLength.isEmpty()) {
            throw new IllegalStateException();
        }
        return patternString;

    }

    public int record(char guess) {
        
        if (wrongGuessesLeft < 0 || wordsInLength.isEmpty()) {
            throw new IllegalStateException();
        }
        if (charGuesses.contains(guess)) {
            throw new IllegalArgumentException();
        }

        Map<String, TreeSet<String>> patternFamily = new TreeMap<String, TreeSet<String>>();
        Map<String, Integer> patternFamilySize = new TreeMap<String, Integer>();
        // String currentPattern = "";
        for (String word: wordsInLength) {
            String currentPattern = "";
            for (int index = 0; index < word.length(); index++) {
                if (word.charAt(index) == guess) {
                    currentPattern += guess + " ";
                    

                } else {
                    currentPattern += "- ";
                }
            }
            currentPattern = currentPattern.substring(0, currentPattern.length() - 1);
            if (!patternFamily.containsKey(currentPattern)) {
                TreeSet<String> wordSet = new TreeSet<String>();
                wordSet.add(word);
                patternFamily.put(currentPattern, wordSet);
                patternFamilySize.put(currentPattern, wordSet.size());
            } else {
                patternFamily.get(currentPattern).add(word);
                patternFamilySize.put(currentPattern, patternFamily.get(currentPattern).size());
            }
        }

        int maxSize = -1;
        String storageMax = "";
        for (String patternCount : patternFamily.keySet()) {
            if (patternFamilySize.get(patternCount) > maxSize) {
                maxSize = patternFamilySize.get(patternCount);
                storageMax = patternCount;
            }
        }

        boolean correctOrNot = true;
        int occurrences = 0;
        if (patternString.equals(storageMax)) {
            correctOrNot = false;
        } 
        
        for (int index = 0; index < storageMax.length(); index++) {
            if (storageMax.charAt(index) != patternString.charAt(index)) {
                occurrences++;
            }
        }


        patternString = storageMax;
        if (!correctOrNot) {
            wrongGuessesLeft--;
        }
        charGuesses.add(guess);
        

        wordsInLength = patternFamily.get(storageMax);


        return occurrences;
    }
}