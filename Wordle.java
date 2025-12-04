public class Wordle {

    // Reads all words from dictionary filename into a String array.
    public static String[] readDictionary(String filename) {
        In in = new In(filename);
        int count = 0;
        while (!in.isEmpty()) {
            in.readLine();
            count++;
        }

        String[] words = new String[count];

        In in2 = new In(filename);
        for (int i = 0; i < count; i++) {
            words[i] = in2.readLine();
        }
        return words;
    }

    // Choose a random secret word from the dictionary.
    public static String chooseSecretWord(String[] dict) {
        int random = (int)(Math.random() * dict.length);
        return dict[random];
    }

    // Simple helper: check if letter c appears anywhere in secret.
    public static boolean containsChar(String secret, char c) {
        for (int i = 0; i < secret.length(); i++) {
            if (secret.charAt(i) == c)
                return true;
        }
        return false;
    }

    // Compute feedback for a single guess into resultRow.
    // G = exact match, Y = letter appears somewhere else, _ = no match.
    public static void computeFeedback(String secret, String guess, char[] resultRow) {
        for (int i = 0; i < guess.length(); i++) {
            if (guess.charAt(i) == secret.charAt(i)) {
                resultRow[i] = 'G';
            } else if (containsChar(secret, guess.charAt(i))) {
                resultRow[i] = 'Y';
            } else {
                resultRow[i] = '_';
            }
        }
    }

    // Store a guess into the 2D guesses array.
    public static void storeGuess(String guess, char[][] guesses, int row) {
        for (int i = 0; i < guesses[row].length; i++) {
            guesses[row][i] = guess.charAt(i);
        }
    }

    // Prints the game board up to currentRow (inclusive).
    public static void printBoard(char[][] guesses, char[][] results, int currentRow) {
        System.out.println("Current board:");
        for (int row = 0; row <= currentRow; row++) {
            System.out.print("Guess " + (row + 1) + ": ");
            for (int col = 0; col < guesses[row].length; col++) {
                System.out.print(guesses[row][col]);
            }
            System.out.print("   Result: ");
            for (int col = 0; col < results[row].length; col++) {
                System.out.print(results[row][col]);
            }
            System.out.println();
        }
        System.out.println();
    }

    // Returns true if all entries in resultRow are 'G'.
    public static boolean isAllGreen(char[] resultRow) {
        for (int i = 0; i < resultRow.length; i++) {
            if (resultRow[i] != 'G')
                return false;
        }
        return true;
    }

    public static void main(String[] args) {

        int WORD_LENGTH = 5;
        int MAX_ATTEMPTS = 6;

        // Read dictionary
        String[] dict = readDictionary("dictionary.txt");

        // Choose secret word
        String secret = chooseSecretWord(dict);

        // Prepare 2D arrays for guesses and results
        char[][] guesses = new char[MAX_ATTEMPTS][WORD_LENGTH];
        char[][] results = new char[MAX_ATTEMPTS][WORD_LENGTH];

        // Read input from standard input
        In inp = new In();

        int attempt = 0;
        boolean won = false;

        while (attempt < MAX_ATTEMPTS && !won) {

            String guess = "";
            boolean valid = false;

            // Loop until a valid guess is give
            while (!valid) {
                System.out.print("Enter your guess (5-letter word): ");
                guess = inp.readString();

                if (guess.length() != 5) {
                    System.out.println("Invalid word. Please try again.");
                } else {
                    valid = true;
                }
            }

            storeGuess(guess, guesses, attempt);
            computeFeedback(secret, guess, results[attempt]);
            printBoard(guesses, results, attempt);

            if (isAllGreen(results[attempt])) {
                System.out.println("Congratulations! You guessed the word in " + (attempt + 1) + " attempts.");
                won = true;
            }

            attempt++;
        }

        if (!won) {
            System.out.println("Sorry, you did not guess the word.");
            System.out.println("The secret word was: " + secret);
        }

        inp.close();
    }
}
