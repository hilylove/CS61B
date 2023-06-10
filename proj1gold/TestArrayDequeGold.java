import static org.junit.Assert.*;

import org.junit.Test;

public class TestArrayDequeGold {
    @Test
    public void testStudentArrayDeque() {
        StudentArrayDeque<Integer> testArray = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> sltArray = new ArrayDequeSolution<>();
        String log = "";

        for (int i = 0; i < 1000; i++) {
            if (sltArray.size() == 0) {
                int addNumber = StdRandom.uniform(1000);
                int headOrBack = StdRandom.uniform(2);
                if (headOrBack == 0) {
                    log = log + "addFirst(" + addNumber + ")\n";
                    testArray.addFirst(addNumber);
                    sltArray.addFirst(addNumber);
                } else {
                    log = log + "addLast(" + addNumber + ")\n";
                    testArray.addLast(addNumber);
                    sltArray.addLast(addNumber);
                }
            } else {
                int chooseMethod = StdRandom.uniform(4);
                int addNumber = StdRandom.uniform(1000);
                Integer testRemoveNumber = 1;
                Integer sltRemoveNumber = 1;

                switch (chooseMethod) {
                    case 0:
                        log = log + "addFirst(" + addNumber + ")\n";
                        testArray.addFirst(addNumber);
                        sltArray.addFirst(addNumber);
                        break;
                    case 1:
                        log = log + "addLast(" + addNumber + ")\n";
                        testArray.addLast(addNumber);
                        sltArray.addLast(addNumber);
                        break;
                    case 2:
                        log = log + "removeFirst()\n";
                        testRemoveNumber = testArray.removeFirst();
                        sltRemoveNumber = sltArray.removeFirst();
                        break;
                    case 3:
                        log = log + "removeLast()\n";
                        testRemoveNumber = testArray.removeLast();
                        sltRemoveNumber = sltArray.removeLast();
                        break;
                    default:
                }
                assertEquals(log, testRemoveNumber, sltRemoveNumber);
            }
        }
    }
}
