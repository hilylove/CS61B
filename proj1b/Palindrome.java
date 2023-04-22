public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> result = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i++) {
            result.addLast(word.charAt(i));
        }
        return result;
    }

    public boolean isPalindrome(String word) {
        if (word == null || word.length() <= 1) {
            return true;
        }
        Deque<Character> deque = wordToDeque(word);

        while (deque.size() > 1) {
            // compare the characters at the front and the rear of the deque
            char front = deque.removeFirst();
            char rear = deque.removeLast();
            if (front != rear) {
                return false; // not a palindrome
            }
        }

        return true; // all characters match, so it's a palindrome
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        if (word == null || word.length() <= 1) {
            return true;
        }
        Deque<Character> deque = wordToDeque(word);
        while (deque.size() > 1) {
            char front = deque.removeFirst();
            char rear = deque.removeLast();
            if (!cc.equalChars(front, rear)) {
                return false;
            }
        }
        return true;
    }

}
